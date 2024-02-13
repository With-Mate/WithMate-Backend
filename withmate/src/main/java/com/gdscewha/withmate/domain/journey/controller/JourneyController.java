package com.gdscewha.withmate.domain.journey.controller;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.JourneyException;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.journey.repository.JourneyRepository;
import com.gdscewha.withmate.domain.journey.service.JourneyService;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.service.RelationMateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class JourneyController {
    private final MemberService memberService;
    private final JourneyService journeyService;


    // 내 n번째 여정 조회
    @GetMapping("/self/journey")
    public ResponseEntity<?> getMyNthJourney(@RequestParam(required = false) Long index) {
        Member currentMember = memberService.getCurrentMember();
        Journey journey = journeyService.getNthJourneyInProfile(currentMember, index);
        return ResponseEntity.ok().body(journey);
    }
    // 메이트의 n번째 여정 조회
    @GetMapping("/mate/journey")
    public ResponseEntity<?> getMateNthJourney(@RequestParam(required = false) Long index) {
        Member mate = memberService.getCurrentMate();
        Journey journey = journeyService.getNthJourneyInProfile(mate, index);
        return ResponseEntity.ok().body(journey);
    }
}


