package com.gdscewha.withmate.domain.member.controller;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.dto.MemberSettingsDto;
import com.gdscewha.withmate.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    // 내 프로필 조회
    @GetMapping("/self/profile")
    public ResponseEntity<?> getMyProfile() {
        MemberProfileDto memberProfileDto = memberService.getMyProfile();
        return ResponseEntity.ok().body(memberProfileDto); // TODO: 추후 ApiResponse 형식 맞춰야 함.
    }
    // 메이트 프로필 조회
    @GetMapping("/mate/profile")
    public ResponseEntity<?> getMateProfile() {
        MemberProfileDto memberProfileDto = memberService.getMateProfile();
        return ResponseEntity.ok().body(memberProfileDto);
    }
    // 설정에서 내 정보 조회
    @GetMapping("/settings")
    public ResponseEntity<?> getSettingsInfo() {
        MemberSettingsDto memberSettingsDto = memberService.getSettingsInfo();
        return ResponseEntity.ok().body(memberSettingsDto);
    }
    // 설정에서 닉네임 업데이트
    @PatchMapping("/settings/update")
    public ResponseEntity<?> updateSettingsInfo(@RequestParam String nickname) {
        MemberSettingsDto memberSettingsDto = memberService.updateMemberNickname(nickname);
        if (memberSettingsDto == null)
            return ResponseEntity.badRequest().body(ErrorCode.ERROR); // 닉네임 null 불가함
        return ResponseEntity.ok().body(memberSettingsDto);
    }
}
