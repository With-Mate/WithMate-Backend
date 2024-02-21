package com.gdscewha.withmate.domain.memberrelation.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.MemberRelationException;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import com.gdscewha.withmate.domain.memberrelation.dto.MRUpdateDto;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.repository.MemberRelationRepository;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberRelationService {

    private final MemberRelationRepository mRRepository;
    private final MemberRepository memberRepository;

    // Member의 모든 MR을 반환한다
    public List<MemberRelation> findAllMROfMember(Member member) {
        List<MemberRelation> mRList = mRRepository.findAllByMember(member);
        if (mRList == null || mRList.isEmpty())
            return null;
        return mRList; // 찾지 못했음
    }

    // Member의 N번째 MR을 반환한다
    /*public MemberRelation findNthMROfMember(Member member, Long index) {
        List<MemberRelation> mRList = mRRepository.findAllByMember(member);
        if (mRList == null || mRList.isEmpty())
            return null;
        if (index == null)
            return mRList.get(mRList.size() - 1);
        if (index > mRList.size())
            return null;
        return mRList.get(index.intValue());
    }*/

    // Member에게 가장 최신인 MR 하나를 반환한다
    public MemberRelation findLastMROfMember(Member member) {
        List<MemberRelation> mRList = mRRepository.findAllByMember(member);
        if (mRList == null || mRList.isEmpty())
            return null;
        MemberRelation lastMR = mRList.get(mRList.size() - 1);
        if (!lastMR.getRelation().getIsProceed())
            return null;
        return lastMR;
    }

    // MR 두 개 만들고 저장, 두 Member의 isRelationed는 MatchingService에서 바꿔줌
    public void createMemberRelationPair(Pair<Matching, Matching> matchingPair, Pair<Member, Member> memberPair, Relation relation){
        MemberRelation myMR = MemberRelation.builder()
                .goal(matchingPair.getFirst().getGoal())
                .category(matchingPair.getFirst().getCategory())
                // message is nullable
                .member(memberPair.getFirst())
                .relation(relation)
                .build();
        MemberRelation mateMR = MemberRelation.builder()
                .goal(matchingPair.getSecond().getGoal())
                .category(matchingPair.getSecond().getCategory())
                // message is nullable
                .member(memberPair.getSecond())
                .relation(relation)
                .build();
        mRRepository.save(myMR);
        mRRepository.save(mateMR);
    }

    // Relation이 생성된 후, Relation으로 두 MR을 찾고 그중 메이트의 MR을 반환
    public MemberRelation findMROfMateByRelation(MemberRelation myMR, Relation relation) {
        List<MemberRelation> mRPair = mRRepository.findAllByRelation(relation);
        if (mRPair.size() != 2) {
            throw new MemberRelationException(ErrorCode.MEMBERRELATION_NOT_FOUND); // MR 쌍을 찾지 못했음
        }
        if (mRPair.get(0) != myMR)
            return mRPair.get(0);
        else
            return mRPair.get(1);
    }

    // Relation 삭제 시, Relation으로 두 MR을 찾고 isRelationed를 false로 변경, Matching을 null로 변경
    public void endIsRelationedOfMembers(Relation relation) {
        List<MemberRelation> mRPair = mRRepository.findAllByRelation(relation);
        if (mRPair.size() != 2) {
            throw new MemberRelationException(ErrorCode.MEMBERRELATION_NOT_FOUND);
        }
        Member member1 = mRPair.get(0).getMember();
        Member member2 = mRPair.get(1).getMember();
        member1.setIsRelationed(false);
        member2.setIsRelationed(false);
        member1.setMatching(null);
        member2.setMatching(null);
        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    // Member의 Goal 업데이트
    public MRUpdateDto updateMRGoal(Member member, String newGoal) {
        MemberRelation memberRelation = findLastMROfMember(member);
        if (memberRelation == null)
            throw new MemberRelationException(ErrorCode.MEMBERRELATION_NOT_FOUND);
        memberRelation.setGoal(newGoal);
        mRRepository.save(memberRelation);
        return MRUpdateDto.builder()
                .nickname(memberRelation.getMember().getNickname())
                .goal(newGoal)
                .message(memberRelation.getMessage())
                .build();
    }

    // Member의 Message 업데이트
    public MRUpdateDto updateMRMessage(Member member, String newMessage) {
        MemberRelation memberRelation = findLastMROfMember(member);
        if (memberRelation == null)
            throw new MemberRelationException(ErrorCode.MEMBERRELATION_NOT_FOUND);
        memberRelation.setMessage(newMessage);
        mRRepository.save(memberRelation);
        return MRUpdateDto.builder()
                .nickname(memberRelation.getMember().getNickname())
                .goal(memberRelation.getGoal())
                .message(newMessage)
                .build();
    }
}
