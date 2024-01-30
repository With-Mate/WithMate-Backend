package com.gdscewha.withmate.domain.matching.controller;

import com.gdscewha.withmate.domain.matching.dto.MatchedResultDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingReqDto;
import com.gdscewha.withmate.domain.matching.dto.MatchingResDto;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.service.MatchingService;
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

    // 내 매칭 데이터 매핑하기 위해 받아오기 (기존 데이터 받아오기)
    @GetMapping("/match")
    public ResponseEntity<?> getMatchingInfo() {
        // Member member = memberService.getCurrentMember(); // 로그인 따로 확인해야
        // 멤버가 로그인 x
        // if (member == null)
        //    return ResponseEntity.ok().header("Location", "/intro").build();
        // 멤버가 relation이 있는 경우 /api/home으로 리다이렉트해야함
        //if (member.getIsRelationed())
        //    return ResponseEntity.ok().header("Location", "/api/home").build();

        // match 가능
        MatchingResDto resDto = matchingService.getMyMatching();
        if (resDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(resDto); //필요한 정보들 전송
    }

    // 내 매칭 데이터 생성 혹은 업데이트
    @PostMapping("/match/post")
    public ResponseEntity<?> updateMatchingInfo(@RequestBody MatchingReqDto reqDto) {
        // goal이 비어있음
        if (reqDto.getGoal().isBlank() || reqDto.getGoal() == null)
            return ResponseEntity.badRequest().build();
        // category가 비어있음
        if (reqDto.getCategory() == null)
            return ResponseEntity.badRequest().build();
        // 정상적으로 매칭 생성/업데이트 가능
        MatchingResDto resDto = matchingService.createOrUpdateMatching(reqDto);
        if (resDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(resDto);
    }

    // 매칭 취소
    @DeleteMapping("/match/cancel")
    public ResponseEntity<?> cancelMatching() {
        Matching matching = matchingService.deleteMatching();
        if (matching == null)
            return ResponseEntity.ok().body("취소할 Matching이 없습니다.");
        return ResponseEntity.ok().body("기존 matching을 취소했습니다.");
    }

    // 매핑 가능한 사람들 보기 (카테고리 순서대로 반환됨)
    @GetMapping("/match/people")
    public ResponseEntity<?> getPeopleMatching() {
        List<Matching> matchingList = matchingService.getPeopleMatching();
        return ResponseEntity.ok().body(matchingList);
    }

    // 매칭하기
    @PostMapping("/match/relate")
    public ResponseEntity<?> postNewMatchingRelation(@RequestBody Category category) { // 나중에 바꿔야 할수도
        MatchedResultDto resultDto = matchingService.getMatchedResult(category);
        if (resultDto == null)
            return ResponseEntity.ok().body("매칭 가능한 상대방이 없습니다");
        return ResponseEntity.ok().body("매칭 성공\n" + resultDto);
    }
}
