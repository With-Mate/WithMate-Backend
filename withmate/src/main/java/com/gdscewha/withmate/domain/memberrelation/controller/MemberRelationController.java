package com.gdscewha.withmate.domain.memberrelation.controller;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
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

    // 내 응원 메시지 업데이트
    @PatchMapping("/self/message/update")
    public ResponseEntity<?> updateMessage(@RequestParam String message) {
        Member member = memberService.getCurrentMember(); // 나중에 다르게 변경 예정임
        if (!member.getIsRelationed())
            return ResponseEntity.badRequest().build(); // 메이트가 없어서 바꿀 수 없음
        MemberRelation memberRelation = mRService.updateMRMessage(member, message);
        return ResponseEntity.ok().body(memberRelation);
    }
    // 내 목표 업데이트: 폐기
    /*@PatchMapping("/self/goal/update")
    public ResponseEntity<?> updateGoal(@RequestParam String goal) {
        Member member = memberService.getCurrentMember(); // 나중에 다르게 변경 예정임
        if (!member.getIsRelationed())
            return ResponseEntity.badRequest().build(); // 메이트가 없어서 바꿀 수 없음
        MemberRelation memberRelation = mRService.updateMRGoal(member, goal);
        return ResponseEntity.ok().body(memberRelation);
    }*/
}
