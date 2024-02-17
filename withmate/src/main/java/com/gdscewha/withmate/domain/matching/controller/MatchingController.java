package com.gdscewha.withmate.domain.matching.controller;

import com.gdscewha.withmate.domain.matching.dto.MatchedResultDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingInputDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingResDto;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.service.MatchingService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.model.Category;
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
        MatchingInputDto resDto = matchingService.getCurrentMatchingDto();
        // 아직 매칭 X
        if (resDto == null)
            return ResponseEntity.noContent().build();
        // 매칭 중: 매칭 중인 상태에 대한 정보를 전송
        return ResponseEntity.ok().body(resDto);
    }

    // 특정 카테고리의 매칭 가능한 사람 1명 보기
    @GetMapping("/match/person")
    public ResponseEntity<?> getPersonMatching(@RequestParam Category category) {
        MatchingResDto matchingResDto = matchingService.getMatchingByCategory(category);
        if (matchingResDto == null)
            return ResponseEntity.ok().body("매칭 가능한 상대방이 없습니다"); // 사람이 없음; 매칭 대기 띄워야 함
        return ResponseEntity.ok().body(matchingResDto);
    }

    // 매칭 가능한 사람들 모두 보기 (카테고리 순서대로 반환됨)
    @GetMapping("/match/people")
    public ResponseEntity<?> getPeopleMatching() {
        List<MatchingResDto> matchingList = matchingService.getCurrentMatchingList();
        if (matchingList.isEmpty())
            return ResponseEntity.ok().body("매칭 가능한 상대방이 없습니다"); // 사람이 없음; 매칭 대기 띄워야 함
        return ResponseEntity.ok().body(matchingList);
    }

    // 매칭하기
    @PostMapping("/match/relate")
    public ResponseEntity<?> postNewMatchingRelation(@RequestBody MatchingInputDto reqDto) {
        // 입력이 잘못 들어옴
        if (!matchingService.checkMatchingAvailability(reqDto))
            return ResponseEntity.badRequest().body("목표 혹은 카테고리가 비어있습니다.");
        // 정상적으로 매칭 맺기 가능
        MatchedResultDto resultDto = matchingService.getMatchedResult(reqDto);
        if (resultDto == null)
            return ResponseEntity.badRequest().body("매칭할 메이트가 없거나, 내가 매칭 불가능한 상태입니다.");
        return ResponseEntity.ok().body(resultDto);
    }

    // 매칭 대기 등록: 내 매칭 데이터 생성 혹은 업데이트
    @PostMapping("/match/register")
    public ResponseEntity<?> updateMatchingInfo(@RequestBody MatchingInputDto reqDto) {
        // 입력이 잘못 들어옴
        if (!matchingService.checkMatchingAvailability(reqDto))
            return ResponseEntity.badRequest().body("목표 혹은 카테고리가 비어있습니다.");
        // 정상적으로 매칭 생성/업데이트 가능
        Matching matching = matchingService.createOrUpdateMatching(reqDto);
        if (matching == null)
            return ResponseEntity.badRequest().body("이미 메이트가 있습니다.");
        return ResponseEntity.ok().body(matching);
    }

    // 매칭 대기 취소: 내 매칭 삭제
    @DeleteMapping("/match/cancel")
    public ResponseEntity<?> cancelMatching() {
        String cancelMessage = matchingService.deleteMyMatching();
        return ResponseEntity.ok().body(cancelMessage);
    }
}
