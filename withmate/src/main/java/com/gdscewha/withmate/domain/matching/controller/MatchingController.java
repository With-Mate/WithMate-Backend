package com.gdscewha.withmate.domain.matching.controller;

import com.gdscewha.withmate.domain.matching.service.MatchingService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MatchingController {

    private final MatchingService matchingService;
    private final MemberService memberService; //임시

    @GetMapping("/match")
    public ResponseEntity<?> getMatchingInfo() {
        Member member = memberService.getCurrentMember();
        if (member == null)
            return ResponseEntity.ok().header("Location", "/intro").build();
        else if (member.getIsRelationed())
            return ResponseEntity.ok().header("Location", "/api/home").build();
        else
            return ResponseEntity.ok().body("can match"); //필요한 정보들 전송
    }
}
