package com.gdscewha.withmate.domain.matching.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.MatchingException;
import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.matching.dto.MatchingDTO;
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
    private final ValidationService validationService;

    // 매칭한 적 있는지 확인(Matching이 존재하는지)
    public Matching hasMatched(MatchingDTO matchingDTO) {
        Member member = memberService.getCurrentMember();
        Optional<Matching> existingMatching = matchingRepository.findByMember(member);
        if (existingMatching.isPresent()) {
            // 매칭한 적 있을 때 업데이트
            return updateMatching(existingMatching.get(), matchingDTO); //existingMatching.get()-> get이 optional에서 matching 객체 꺼내오는 함수
        } else {
            // 매칭한 적 없을 때 생성
            return createMatching(member, matchingDTO);
        }
    }

    // 매칭 객체 새로 만들기 (매칭 전에 안했던 사람만)
    public Matching createMatching(Member member, MatchingDTO matchingDTO) {
            Matching matching = Matching.builder()
                    .member(member)
                    .goal(matchingDTO.getGoal())
                    .category(Category.ART)
                    .build();

            return matchingRepository.save(matching);
    }

    // 매칭 객체 업데이트하기 (저번에 매칭 했던 사람) - 목표와 카테고리 업데이트
    public Matching updateMatching(Matching matching, MatchingDTO matchingDTO){
        // 목표가 기존과 다른 경우 업데이트
        if (!matching.getGoal().equals(matchingDTO.getGoal())) {
            matching.setGoal(matchingDTO.getGoal());
        }
        // 카테고리가 기존과 다른 경우 업데이트
        if (!matching.getCategory().equals(matchingDTO.getCategory())) {
            matching.setCategory(matchingDTO.getCategory());
        }
        return matchingRepository.save(matching);
    }


    // category에 대해 다른 matching 사람이 존재하는지 (Find by category) - 일단 나부터 1개
    public List<Matching> tryMatching(Category category) {
        // 같은 카테고리의 Matching 리스트
        List<Matching> matchingList = matchingRepository.findAllByCategory(category);
        
        // 혼자면 (matchings에 1개) 기다려야 한다고 응답 (service에서는 null을 리턴)
        if(matchingList.size() == 1){
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

            // isRelationed를 true로 업데이트
            mate1.getMember().setIsRelationed(true);
            mate2.getMember().setIsRelationed(true);

            // Member 저장
            memberRepository.save(mate1.getMember());
            memberRepository.save(mate2.getMember());

            // 수정된 Matching 리스트 반환
            return mateList;
        }else {
            throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
        }
    }
}
