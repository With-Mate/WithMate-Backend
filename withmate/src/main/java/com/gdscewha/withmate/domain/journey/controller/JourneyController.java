package com.gdscewha.withmate.domain.journey.controller;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.JourneyException;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class JourneyController {



    // 내 n번째 여정 조회
    // 메이트 프로필 조회
    @GetMapping("/self/profile")
    public ResponseEntity<?> getMyJourney() {
        MemberProfileDto memberProfileDto = memberService.getMateProfile();
        return ResponseEntity.ok().body(memberProfileDto);
    }

    // 메이트의 n번째 여정 조회


}
    // 단일 Journey 조회: Relation과 index로
    public Journey getJourneyByRelationAndIndex(Relation relation, Long index) {
        Optional<Journey> journeyOptional = journeyRepository.findByRelationAndJourneyNum(relation, index);
        if (journeyOptional.isPresent())
            return journeyOptional.get();
        throw new JourneyException(ErrorCode.JOURNEY_NOT_FOUND);
    }

    // 현재 Journey 조회
    public Journey getCurrentJourney(Member member) {
        Relation relation = relationMateService.getCurrentRelation(member);
        if (relation == null)
            throw new JourneyException(ErrorCode.RELATION_NOT_FOUND);
        List<Journey> existingJourneyList = journeyRepository.findAllByRelation(relation);
        if (existingJourneyList == null || existingJourneyList.isEmpty())
            return null;
        Integer index = existingJourneyList.size() - 1;
        return existingJourneyList.get(index);