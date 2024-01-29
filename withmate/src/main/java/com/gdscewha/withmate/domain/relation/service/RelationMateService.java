package com.gdscewha.withmate.domain.relation.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.service.MemberRelationService;
import com.gdscewha.withmate.domain.relation.dto.RelationHomeDto;
import com.gdscewha.withmate.domain.relation.dto.RelationManageDto;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@RequiredArgsConstructor
@Service
public class RelationMateService {
    private final RelationRepository relationRepository;
    private final MemberService memberService;
    private final MemberRelationService mRService;

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

    // 현재 Relation 조회
    public Relation getCurrentRelation(){
        Member member = memberService.getCurrentMember();
        MemberRelation memberRelation = mRService.findLastMROfMember(member);
        return memberRelation.getRelation();
    }

    // 메이트 신고하기
    // 추후 구글 소셜 로그인 구현 후 GMAIL API 사용해 추가

    // 현재 Relation 끝내기
    public Relation endCurrentRelation(){
        Relation relation = getCurrentRelation();
        if (relation == null)
            return null;
        relation.setEndDate(LocalDate.now());
        relation.setIsProceed(false);
        relationRepository.save(relation);
        return relation;
    }

    // 홈 화면에 정보 매핑
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
            return null; // 반환
        return RelationHomeDto.builder()
                .myName(myMR.getMember().getNickname())
                .myMessage(myMR.getMessage())
                .myGoal(myMR.getGoal())
                .mateName(mateMR.getMember().getNickname())
                .mateMessage(mateMR.getMessage())
                .mateGoal(mateMR.getGoal())
                .build();
    }

    // 메이트 관리 화면에 정보 매핑
    public RelationManageDto getRelationManageInfo() {
        Member member = memberService.getCurrentMember();
        MemberRelation myMR = mRService.findLastMROfMember(member);
        if (myMR == null)
            return null; // 반환
        Relation relation = myMR.getRelation();
        if (relation == null)
            return null; // 반환
        MemberRelation mateMR = mRService.findMROfMateByRelation(myMR, relation);
        if (mateMR == null)
            return null; // 반환
        // 두 날짜 간의 차이 계산
        long daysDifference = ChronoUnit.DAYS.between(relation.getStartDate(), LocalDate.now());

        return RelationManageDto.builder()
                .startDate(relation.getStartDate().toString())
                .proceedingTime(daysDifference)
                .myMessage(myMR.getMessage())
                .mateName(mateMR.getMember().getNickname())
                .mateCategory(mateMR.getCategory().toString())
                .mateMessage(mateMR.getMessage())
                .build();
    }
}
