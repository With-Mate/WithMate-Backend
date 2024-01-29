package com.gdscewha.withmate.domain.memberrelation.service;

import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.repository.MemberRelationRepository;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberRelationService {

    private final MemberRelationRepository mRRepository;
    private final MemberService memberService;

    // Current Member의 모든 MR을 반환한다
    public List<MemberRelation> findAllMROfMember(Member member) {
        List<MemberRelation> mRList = mRRepository.findAllByMember(member);
        if (mRList != null && !mRList.isEmpty()) {
            return mRList;
        }
        return null; // 찾지 못했음
    }

    // Current Member에게 가장 최신인 MR 하나를 반환한다
    public MemberRelation findLastMROfMember(Member member) {
        List<MemberRelation> mRList = findAllMROfMember(member);
        if (mRList == null) {
            return null;      //throw new RelationException(ErrorCode.RELATION_NOT_FOUND); ??
        }
        MemberRelation lastMR = mRList.get(mRList.size() - 1);
        if (lastMR.getRelation().getIsProceed() == true) { // 지속중이라면
            return lastMR;
        }
        return null; // 찾지 못했음
    }

    // Relation으로 두 MR 쌍을 찾고 그중 메이트의 MR을 반환
    public MemberRelation findMROfMateByRelation(MemberRelation myMR, Relation relation) {
        List<MemberRelation> mRPair = mRRepository.findAllByRelation(relation);
        if (mRPair.size() == 2) {
            if (mRPair.get(0) != myMR)
                return mRPair.get(0);
            else
                return mRPair.get(1);
        }
        return null; // MR 쌍을 찾지 못했음
    }

    // MR 두 개 만들고 저장
    public void createMemberRelationPair(List<Matching> matchingList, Relation relation){
        Matching matching1 = matchingList.get(0);
        Matching matching2 = matchingList.get(1);

        MemberRelation myMR = MemberRelation.builder()
                .goal(matching1.getGoal())
                .category(matching1.getCategory())
                // message is nullable
                .member(matching1.getMember())
                .relation(relation)
                .build();
        mRRepository.save(myMR);
        MemberRelation mateMR = MemberRelation.builder()
                .goal(matching2.getGoal())
                .category(matching2.getCategory())
                // message is nullable
                .member(matching2.getMember())
                .relation(relation)
                .build();
        mRRepository.save(mateMR);
    }

    // 내 Goal 업데이트
    public MemberRelation updateMRGoal(String newGoal) {
        Member member = memberService.getCurrentMember();
        MemberRelation memberRelation = findLastMROfMember(member);
        memberRelation.setGoal(newGoal);
        return mRRepository.save(memberRelation);
    }

    // 내 Message 업데이트
    public MemberRelation updateMRMessage(String newMessage) {
        Member member = memberService.getCurrentMember();
        MemberRelation memberRelation = findLastMROfMember(member);
        memberRelation.setMessage(newMessage);
        return mRRepository.save(memberRelation);
    }
}
