package com.gdscewha.withmate.domain.memberrelation.service;

import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.repository.MemberRelationRepository;
import com.gdscewha.withmate.domain.model.Category;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberRelationService {

    private final MemberRelationRepository mRRepository;

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
    /*public void createMemberRelationPair(Relation relation){
        // 나랑 메이트의 Matching 정보를 불러오는 로직이 필요함
        Matching myMatching = new Matching();
        Matching mateMatching = new Matching();

        MemberRelation myMR = MemberRelation.builder()
                .goal("")
                .category(Category.ART) //임시 ENUM
                // message is nullable
                .member(myMatching.getMember())
                .relation(relation)
                .build();
        mRRepository.save(myMR);
        MemberRelation mateMR = MemberRelation.builder()
                .goal("")
                .category(Category.ART) //임시 ENUM
                // message is nullable
                .member(mateMatching.getMember())
                .relation(relation)
                .build();
        mRRepository.save(mateMR);
    }*/
}
