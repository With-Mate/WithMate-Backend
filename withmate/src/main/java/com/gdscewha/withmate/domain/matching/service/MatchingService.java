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

    private final MemberService memberService;
    private final MemberRelationService memberRelationService;
    private final RelationMateService relationMateService;
    private final MatchingRepository matchingRepository;
    private final MemberRepository memberRepository;

    // 내 매칭 받아오기 (존재하지 않으면 null 반환)
    public MatchingResDto getMyMatching() {
        Member member = memberService.getCurrentMember();
        Matching matching = matchingRepository.findByMember(member).get();
        if (matching == null)
            return null;
        return new MatchingResDto(matching);
    }

    // 내 매칭을 생성 혹은 기존 매칭 업데이트
    public MatchingResDto createOrUpdateMatching(MatchingReqDto reqDto) {
        Member member = memberService.getCurrentMember();
        Optional<Matching> matchingOptional = matchingRepository.findByMember(member);
        if (matchingOptional.isPresent()) {
            // 매칭한 적 있을 때 업데이트
            return new MatchingResDto(updateMatching(matchingOptional.get(), reqDto)); //existingMatching.get() -> get이 optional에서 matching 객체 꺼내오는 함수
        } else {
            // 매칭한 적 없을 때 생성
            return new MatchingResDto(createMatching(member, reqDto));
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

    }

    // tryMatching로부터 생성되는 매칭 결과를 컨트롤러에 반환
    public MatchedResultDto getMatchedResult(Category category) {
        if (category == null)
            throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND);
        List<Matching> matchingList = tryMatching(category);
        // 매칭 실패 (해당 카테고리에 1명)
        if (matchingList == null)
            return null;
        // 매칭 성공
        return new MatchedResultDto(matchingList);
    }

    // category에 대해 다른 matching 사람이 존재하는지 확인 후 매칭 (Find by category) - 일단 나부터 1개
    public List<Matching> tryMatching(Category category) {
        // 같은 카테고리의 Matching 리스트
        List<Matching> matchingList = matchingRepository.findAllByCategory(category);
        
        // 혼자면 (matchings에 1개) 기다려야 함
        if (matchingList.size() == 1){
            return null;
        }
        // Matching 상태가 2명이면 매칭 시작
        else if (matchingList.size() == 2){
            Matching mate1 = matchingList.get(0);
            Matching mate2 = matchingList.get(1);

            List<Matching> mateList = new ArrayList<>(); // 메이트 리스트
            mateList.add(mate1);
            mateList.add(mate2);

            // Relation 생성, MemberRelationPair 생성
            Relation pairRelation = relationMateService.createRelation();
            memberRelationService.createMemberRelationPair(mateList, pairRelation);

            // Matching 삭제
            matchingRepository.deleteAll(matchingList);

            // Member들의 isRelationed를 true로 업데이트 후 저장
            mate1.getMember().setIsRelationed(true);
            mate2.getMember().setIsRelationed(true);
            memberRepository.save(mate1.getMember());
            memberRepository.save(mate2.getMember());

            // 수정된 Matching 리스트 반환
            return mateList;
        } else {
            throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
        }
    }

}
