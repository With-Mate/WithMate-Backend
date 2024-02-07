package com.gdscewha.withmate.domain.matching.controller;

import com.gdscewha.withmate.domain.matching.dto.MatchedResultDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingReqDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingResDto;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.service.MatchingService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MatchingController {

    private final MatchingService matchingService;
    private final MemberService memberService;

    // 내 매칭 여부 확인, 매칭 대기 중인 경우 매칭 중인 정보 반환
    @GetMapping("/match")
    public ResponseEntity<?> getMatchingInfo() {
        Member member = memberService.getCurrentMember();
        // match 불가:
        // 메이트 관계가 있는 경우 리다이렉트
        if (member.getIsRelationed())
            return ResponseEntity.ok().header("Location", "/api/home").build();
        // match 가능:
        MatchingResDto resDto = matchingService.getCurrentMatchingDto();
        // 아직 매칭 X
        if (resDto == null)
            return ResponseEntity.noContent().build();
        // 매칭 중: 매칭 중인 상태에 대한 정보를 전송
        return ResponseEntity.ok().body(resDto);
    }

    // 매핑 가능한 사람 보기 (카테고리 순서대로 반환됨)
    @GetMapping("/match/people")
    public ResponseEntity<?> getPeopleMatching() {
        List<Matching> matchingList = matchingService.getPeopleMatching();
        if (matchingList.isEmpty())
            return ResponseEntity.ok().body("매칭 가능한 상대방이 없습니다"); // 사람이 없음; 매칭 대기 띄워야 함
        return ResponseEntity.ok().body(matchingList);
    }

    // 매칭하기
    @PostMapping("/match/relate")
    public ResponseEntity<?> postNewMatchingRelation(@RequestBody MatchingReqDto reqDto) {
        // 입력이 잘못 들어옴
        if (!matchingService.checkMatchingAvailability(reqDto))
            return ResponseEntity.badRequest().body("목표 혹은 카테고리가 비어있습니다.");
        // 정상적으로 매칭 맺기 가능
        MatchedResultDto resultDto = matchingService.getMatchedResult(reqDto);
        return ResponseEntity.ok().body(resultDto);
    }

    // 매칭 대기 하기: 내 매칭 데이터 생성 혹은 업데이트
    @PostMapping("/match/post")
    public ResponseEntity<?> updateMatchingInfo(@RequestBody MatchingReqDto reqDto) {
        // 입력이 잘못 들어옴
        if (!matchingService.checkMatchingAvailability(reqDto))
            return ResponseEntity.badRequest().body("목표 혹은 카테고리가 비어있습니다.");
        // 정상적으로 매칭 생성/업데이트 가능
        Matching matching = matchingService.createOrUpdateMatching(reqDto);
        return ResponseEntity.ok().body(matching);
    }

    // 매칭 대기 취소: 내 매칭 삭제
    @DeleteMapping("/match/cancel")
    public ResponseEntity<?> cancelMatching() {
        Matching matching = matchingService.deleteMatching();
        if (matching == null)
            return ResponseEntity.ok().body("취소할 Matching이 없습니다.");
        return ResponseEntity.ok().body("기존 matching을 취소했습니다.");
    }
}
