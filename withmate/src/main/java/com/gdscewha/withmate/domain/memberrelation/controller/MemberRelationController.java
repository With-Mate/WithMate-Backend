package com.gdscewha.withmate.domain.memberrelation.controller;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.memberrelation.dto.MRUpdateDto;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.service.MemberRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberRelationController {

    private final MemberRelationService mRService;
    private final MemberService memberService;

    // TODO: 응답 dto로 바꿔서 보내야 함
    // 내 목표 업데이트
    @PatchMapping("/self/goal/update")
    public ResponseEntity<?> updateGoal(@RequestParam String goal) {
        Member member = memberService.getCurrentMember();
        if (!member.getIsRelationed())
            return ResponseEntity.badRequest().build(); // 메이트가 없어서 바꿀 수 없음
        MRUpdateDto mrUpdateDto = mRService.updateMRGoal(member, goal);
        return ResponseEntity.ok().body(mrUpdateDto);
    }

    // 내 응원 메시지 업데이트
    @PatchMapping("/self/message/update")
    public ResponseEntity<?> updateMessage(@RequestParam String message) {
        Member member = memberService.getCurrentMember();
        if (!member.getIsRelationed())
            return ResponseEntity.badRequest().build(); // 메이트가 없어서 바꿀 수 없음
        MRUpdateDto mrUpdateDto = mRService.updateMRMessage(member, message);
        return ResponseEntity.ok().body(mrUpdateDto);
    }
}
