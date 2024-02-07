package com.gdscewha.withmate.domain.matching.service;

import com.gdscewha.withmate.common.response.exception.CategoryException;
import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.MatchingException;
import com.gdscewha.withmate.domain.matching.dto.MatchedResultDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingReqDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberRelationService memberRelationService;
    private final RelationMateService relationMateService;

    public Boolean checkMatchingAvailability(MatchingReqDto reqDto) {
        // goal이 비어있음
        if (reqDto.getGoal().isBlank() || reqDto.getGoal() == null)
            return false;
        // category가 비어있음
        if (reqDto.getCategory() == null)
            return false;
        return true;
    }

    // 내 매칭 받아오기 (MatchingResDto 반환, 존재하지 않으면 null 반환)
    public MatchingResDto getCurrentMatchingDto() {
        Member member = memberService.getCurrentMember();
        Optional<Matching> matchingOptional = matchingRepository.findByMember(member);
        return matchingOptional.map(MatchingResDto::new).orElse(null);
    }

    // 내 매칭을 생성 혹은 기존 매칭 업데이트
    public Matching createOrUpdateMatching(MatchingReqDto reqDto) {
        Member member = memberService.getCurrentMember();
        Optional<Matching> matchingOptional = matchingRepository.findByMember(member);
        if (matchingOptional.isPresent()) {
            // 매칭한 적 있을 때 업데이트
            return updateMatching(matchingOptional.get(), reqDto); //existingMatching.get() -> get이 optional에서 matching 객체 꺼내오는 함수
        } else {
            // 매칭한 적 없을 때 생성
            return createMatching(member, reqDto);
        }
    }

    // 새 매칭 생성 (매칭을 하지 않았던 사람)
    public Matching createMatching(Member member, MatchingReqDto reqDto) {
        Matching matching = Matching.builder()
                .goal(reqDto.getGoal())
                .category(reqDto.getCategory())
                .member(member)
                .build();
        return matchingRepository.save(matching);
    }

    // 매칭 객체 업데이트하기 (전에 매칭을 했던 사람) - 목표, 카테고리 업데이트
    public Matching updateMatching(Matching matching, MatchingReqDto reqDto){
        matching.setGoal(reqDto.getGoal());
        matching.setCategory(reqDto.getCategory());
        return matchingRepository.save(matching);
    }

    // 내 매칭 삭제 (매칭 취소)
    public Matching deleteMatching() {
        Member member = memberService.getCurrentMember();
        Optional<Matching> existingMatching = matchingRepository.findByMember(member);
        if (existingMatching.isPresent()) {
            matchingRepository.delete(existingMatching.get());
            return existingMatching.get();
        }
        return null;
    }

    // 모든 카테고리의 매칭중인 사람들을 조회
    public List<Matching> getPeopleMatching() {
        Category[] categories = Category.values();
        List<Matching> matchingList = new ArrayList<>();
        for(Category c : categories) {
            matchingList.addAll(matchingRepository.findAllByCategory(c));
        }
        return matchingList;
    }

    // tryMatching로부터 생성되는 매칭 결과를 컨트롤러에 반환
    public MatchedResultDto getMatchedResult(MatchingReqDto reqDto) {
        Matching myMatching = createOrUpdateMatching(reqDto);   // 내 매칭 생성
        List<Matching> matchingList = relateMatesByCategory(reqDto.getCategory(), myMatching);  // 카테고리로 Mates 관계 맺기
        return new MatchedResultDto(matchingList);
    }

    // Matching으로 Mates 관계 맺기
    public List<Matching> relateMatesByCategory(Category category, Matching myMatching) {
        if (category == null)
            throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND);
        // 같은 카테고리의 Matching 리스트
        List<Matching> matchingList = matchingRepository.findAllByCategory(category);
        // matchingList에 2개 이상이어야 매칭 가능
        if (matchingList.size() >= 2){
            Matching mateMatching = matchingList.get(0);

            List<Matching> mateList = new ArrayList<>(); // 메이트 리스트
            mateList.add(mateMatching);
            mateList.add(myMatching);

            // Relation 생성, MemberRelationPair 생성
            Relation pairRelation = relationMateService.createRelation();
            memberRelationService.createMemberRelationPair(mateList, pairRelation);
            // Matching 삭제
            matchingRepository.deleteAll(matchingList);

            // Member들의 isRelationed를 true로 업데이트 후 저장
            mateMatching.getMember().setIsRelationed(true);
            myMatching.getMember().setIsRelationed(true);
            memberRepository.save(mateMatching.getMember());
            memberRepository.save(myMatching.getMember());

            // 수정된 Matching 리스트 반환
            return mateList;
        } else {
            throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
        }
    }

}
