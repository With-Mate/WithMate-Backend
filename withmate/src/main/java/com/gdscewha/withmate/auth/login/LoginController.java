package com.gdscewha.withmate.auth.login;

import com.gdscewha.withmate.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class LoginController {

    private final SocialLoginService socialloginService;
    private final DefaultLoginService defaultLoginService;

    @GetMapping("/code/{registrationId}")
    public ResponseEntity<?> googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        Member member = socialloginService.socialLogin(code, registrationId);
        return ResponseEntity.ok().body("소셜 로그인 성공\n" + member);
    }
    // 닉네임 수정 필요

    @PatchMapping("/api/login")
    public ResponseEntity<?> defaultLogin(@RequestBody LoginRequestDto loginRequestDto) {
        Member member = defaultLoginService.defaultLogin(loginRequestDto);
        return ResponseEntity.ok().body("기본 로그인 성공\n" + member);
        /*ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", tokenDTO.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")     //??
                .maxAge(tokenDTO.getDuration())
                .path("/")
                .build();
        return ResponseEntity.ok().header("Set-Cookie", responseCookie.toString()).body(tokenDTO.getAccessToken());*/
    }

    @PostMapping("/api/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpReqDto signUpReqDto) {
        Member member = defaultLoginService.signUp(signUpReqDto);
        return ResponseEntity.ok().body("회원가입 성공\n" + member);
    }
}