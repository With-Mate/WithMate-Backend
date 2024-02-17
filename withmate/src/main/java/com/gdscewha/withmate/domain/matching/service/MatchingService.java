package com.gdscewha.withmate.domain.matching.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.MatchingException;
import com.gdscewha.withmate.domain.journey.service.JourneyService;
import com.gdscewha.withmate.domain.matching.dto.MatchedResultDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingInputDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingResDto;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.repository.MatchingRepository;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.memberrelation.service.MemberRelationService;
import com.gdscewha.withmate.domain.model.Category;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.service.RelationMateService;
import com.gdscewha.withmate.domain.week.service.WeekService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberRelationService memberRelationService;
    private final RelationMateService relationMateService;
    // Relation 만들 때 필요한 Service들
    private final JourneyService journeyService;
    private final WeekService weekService;

    // 내 매칭 받아오기 (MatchingResDto 반환, 존재하지 않으면 null 반환)
    public MatchingInputDto getCurrentMatchingDto() {
        Member member = memberService.getCurrentMember();
        Optional<Matching> matchingOptional = matchingRepository.findByMember(member);
        return matchingOptional.map(MatchingInputDto::new).orElse(null);
    }
    // 컨트롤러의 입력을 확인
    public Boolean checkMatchingAvailability(MatchingInputDto reqDto) {
        // goal이 비어있음
        if (reqDto.getGoal().isBlank() || reqDto.getGoal() == null)
            return false;
        // category가 비어있음
        if (reqDto.getCategory() == null)
            return false;
        return true;
    }

    // 내 매칭을 생성 혹은 기존 매칭 업데이트
    public Matching createOrUpdateMatching(MatchingInputDto reqDto) {
        Member member = memberService.getCurrentMember();
        if (member.getIsRelationed())
            return null;
        if (member.getMatching() == null) {
            // 매칭한 적 없을 때 생성
            return createMatching(member, reqDto);
        } else {
            // 매칭한 적 있을 때 업데이트
            return updateMatching(member.getMatching(), reqDto); //existingMatching.get() -> get이 optional에서 matching 객체 꺼내오는 함수
        }
    }

    // 새 매칭 생성 (매칭을 하지 않았던 사람)
    public Matching createMatching(Member member, MatchingInputDto reqDto) {
        Matching matching = Matching.builder()
                .goal(reqDto.getGoal())
                .category(reqDto.getCategory())
                .member(member)
                .build();
        matchingRepository.save(matching);
        member.setMatching(matching);
        memberRepository.save(member);
        return matching;
    }

    // 매칭 객체 업데이트하기 (전에 매칭을 했던 사람) - 목표, 카테고리 업데이트
    public Matching updateMatching(Matching matching, MatchingInputDto reqDto){
        matching.setGoal(reqDto.getGoal());
        matching.setCategory(reqDto.getCategory());
        return matchingRepository.save(matching);
    }

    // 내 매칭 삭제 (매칭 취소)
    public String deleteMyMatching() {
        Member member = memberService.getCurrentMember();
        if (member.getMatching() != null) {
            matchingRepository.delete(member.getMatching());
            member.setMatching(null);
            memberRepository.save(member);
            return "기존 matching을 취소했습니다.";
        }
        return "취소할 Matching이 없습니다.";
    }

    // 특정 카테고리의 매칭중인 사람 1명을 반환
    public MatchingResDto getMatchingByCategory(Category category) {
        List<Matching> matchingList = matchingRepository.findAllByCategory(category);
        if (matchingList == null || matchingList.isEmpty())
            return null;
        Member member = memberRepository.findMemberByMatching(matchingList.get(0));
        return new MatchingResDto(matchingList.get(0), member);
    }

    // 모든 카테고리의 매칭중인 사람들을 조회
    public List<MatchingResDto> getCurrentMatchingList() {
        Category[] categories = Category.values();
        List<MatchingResDto> matchingResList = new ArrayList<>();
        for (Category c : categories) {
            List<Matching> matchingList = matchingRepository.findAllByCategory(c);
            matchingResList.addAll(convertToMatchingResDtoList(matchingList));
        }
        return matchingResList;
    }
    public List<MatchingResDto> convertToMatchingResDtoList(List<Matching> matchingList) {
        return matchingList.stream()
                .map(matching -> {
                    Member member = memberRepository.findMemberByMatching(matching);
                    return new MatchingResDto(matching, member);
                })
                .collect(Collectors.toList());
    }

    // tryMatching로부터 생성되는 매칭 결과를 컨트롤러에 반환
    public MatchedResultDto getMatchedResult(MatchingInputDto reqDto) {
        Pair<Matching, Matching> matchingPair = relateMatesByCategory(reqDto.getCategory(), reqDto); // 카테고리로 Mates 관계 맺기
        if (matchingPair == null)
            return null;
        return new MatchedResultDto(matchingPair);
    }

    // Matching으로 Mates 관계 맺기
    @Transactional
    public Pair<Matching, Matching> relateMatesByCategory(Category category, MatchingInputDto reqDto) {
        Member me = memberService.getCurrentMember();
        if (me.getIsRelationed()) {
            log.info("멤버: isRelationed");
            return null;
        }
        if (me.getMatching().equals(null)){
            log.info("멤버: matched");
            return null;
        }
        // 같은 카테고리의 Matching 리스트
        List<Matching> matchingList = matchingRepository.findAllByCategory(category);
        // matchingList에 1개 이상이어야 매칭 가능 (내 매칭은 db에 저장하지 않음)
        if (matchingList.size() >= 1){
            Matching mateMatching = matchingList.get(0);
            Member mate = memberRepository.findMemberByMatching(mateMatching);
            if (mate == null || mateMatching == null)
                throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
            Matching myMatching = Matching.builder() // 내 매칭 생성 (DB에 저장 X)
                    .goal(reqDto.getGoal())
                    .category(reqDto.getCategory())
                    .member(me)
                    .build();
            Pair<Matching, Matching> matchingPair = Pair.of(mateMatching, myMatching);
            Pair<Member, Member> memberPair = Pair.of(mate, me);

            // Relation 생성, MemberRelationPair 생성
            Relation relation = relationMateService.createRelation();
            memberRelationService.createMemberRelationPair(matchingPair, memberPair, relation);

            // 첫번째 Journey 생성
            journeyService.createJourney(relation);
            // 첫번째 Week 생성
            weekService.createWeek();
            // Member들의 isRelationed를 true로 업데이트 후 저장
            mate.setIsRelationed(true);
            me.setIsRelationed(true);
            // Mate의 매칭 삭제
            matchingRepository.delete(mateMatching);
            return matchingPair;
        } else {
            throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
        }
    }

}
