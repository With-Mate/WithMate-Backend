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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class JourneyController {
    private final MemberService memberService;
    private final JourneyService journeyService;
    private final JourneyRepository journeyRepository;
    private final RelationMateService relationMateService;


    // 내 현재 여정 조회
    @GetMapping("/self/journey")
    public ResponseEntity<?> getMyJourney() {
        Member currentMember = memberService.getCurrentMember();
        Journey currentJourney = journeyService.getCurrentJourney(currentMember);
        return ResponseEntity.ok().body(currentJourney);
    }

    // 메이트의 현재 여정 조회
    @GetMapping("/mate/journey")
    public ResponseEntity<?> getMateJourney() {
        Member currentMember = memberService.getCurrentMember();
        Journey currentJourney = journeyService.getCurrentJourney(currentMember);
        return ResponseEntity.ok().body(currentJourney);
    }


    //TODO:
    // 내 n번째 여정 조회
    // 메이트의 n번째 여정 조회
    // 참고 : 밑의 함수는 서비스 코드, 밑 함수 사용하면 될 듯
    // 단일 Journey 조회: Relation과 index로
//    public Journey getJourneyByRelationAndIndex(Relation relation, Long index) {
  //      Optional<Journey> journeyOptional = journeyRepository.findByRelationAndJourneyNum(relation, index);
    //    if (journeyOptional.isPresent())
      //      return journeyOptional.get();
        //throw new JourneyException(ErrorCode.JOURNEY_NOT_FOUND);
    //}
