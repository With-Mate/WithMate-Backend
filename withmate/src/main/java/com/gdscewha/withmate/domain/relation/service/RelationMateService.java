package com.gdscewha.withmate.domain.relation.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.JourneyException;
import com.gdscewha.withmate.common.response.exception.MemberRelationException;
import com.gdscewha.withmate.domain.journey.entity.Journey;
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
import java.util.List;
import java.util.Optional;


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

    // 단일 Member의 현재 Relation 조회
    public Relation getCurrentRelation(Member member){
        MemberRelation memberRelation = mRService.findLastMROfMember(member);
        if (memberRelation == null)
            return null;
        return memberRelation.getRelation();
    }

    // 메이트 신고하기
    // 추후 구글 소셜 로그인 구현 후 GMAIL API 사용해 추가

    // 현재 Relation 끝내기: 두 멤버의 isRelationed도 false로 설정
    public Relation endCurrentRelation(){
        Member member = memberService.getCurrentMember();
        Relation relation = getCurrentRelation(member);
        if (relation == null)
            throw new MemberRelationException(ErrorCode.RELATION_NOT_FOUND);
        mRService.endIsRelationedOfMembers(relation);
        relation.setEndDate(LocalDate.now());
        relation.setIsProceed(false);
        return relationRepository.save(relation);
    }

    // 홈 화면에 정보 매핑
    public RelationHomeDto getHomeInfo() {
        Member member = memberService.getCurrentMember();
        MemberRelation myMR = mRService.findLastMROfMember(member);
        if (myMR == null)
            return null; // 반환
        Relation relation = myMR.getRelation();
        if (relation == null)
            throw new MemberRelationException(ErrorCode.RELATION_NOT_FOUND);
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

    // 메이트 관리 화면에 정보 매핑-> 나중에 예외처리 고민 필요
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
                .proceedingTime(daysDifference + 1) // 차이에서 하루를 더한 날짜
                .myMessage(myMR.getMessage())
                .mateName(mateMR.getMember().getNickname())
                .mateCategory(mateMR.getCategory().toString())
                .mateMessage(mateMR.getMessage())
                .build();
    }

    // N번째 MR의 Relation을 반환한다
    /*public Relation getRelationOfNthMR(Member member, Long index) {
        MemberRelation memberRelation = mRService.findNthMROfMember(member, index);
        if (memberRelation == null)
            return null;
        return memberRelation.getRelation();
    }*/
}
