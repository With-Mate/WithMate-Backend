package com.gdscewha.withmate.domain.member.controller;

import com.gdscewha.withmate.domain.member.MemberCreateForm;
import com.gdscewha.withmate.domain.member.dto.MemberCreateDto;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequiredArgsConstructor
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/self/profile") // 내 정보 조회
    public ResponseEntity<?> getMyProfile() {
        MemberProfileDto memberProfileDto = memberService.getMyProfile();
        return ResponseEntity.ok().body(memberProfileDto); // TODO: 추후 ApiResponse 형식 맞춰야 함.
    }
}
