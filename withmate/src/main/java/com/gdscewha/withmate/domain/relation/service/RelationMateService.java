package com.gdscewha.withmate.domain.relation.service;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.service.MemberRelationService;
import com.gdscewha.withmate.domain.relation.dto.RelationHomeDto;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@RequiredArgsConstructor
@Service
public class RelationMateService {

    private final RelationRepository relationRepository;
    private final MemberService memberService;
    private final MemberRelationService mRService;

    // Relation 생성
    public void newMateMatched() {
        Relation relation = createRelation();
        mRService.createMemberRelationPair(relation);
        // cMRP에 Matching 관련 로직 필요함
    }

    // Relation 생성
    public Relation createRelation() {
        LocalDate today = LocalDate.now();
        Relation relation = Relation.builder()
                .startDate(today)
                .endDate(today.plusDays(28)) //28일 후의 날짜를 저장
                .isProceed(true)
                .build();
        relationRepository.save(relation);
        return relation;
    }

    // 홈 화면에 매핑되는 정보
    public RelationHomeDto getHomeInfo() {
        Member member = memberService.getCurrentMember();
        MemberRelation myMR = mRService.findLastMROfMember(member);
        if (myMR == null)
            return null; // 반환
        Relation relation = myMR.getRelation();
        if (relation == null)
            return null; // 반환
        MemberRelation mateMR = mRService.findMROfMateByRelation(myMR, relation);
        if (mateMR == null)
            return null; // controller로 반환
        return RelationHomeDto.builder()
                .MyName(myMR.getMember().getNickname())
                .MyMessage(myMR.getMessage())
                .MyGoal(myMR.getGoal())
                .MateName(mateMR.getMember().getNickname())
                .MateMessage(mateMR.getMessage())
                .MateGoal(mateMR.getGoal())
                .build();
    }
}
