package com.gdscewha.withmate.auth.login;

import com.gdscewha.withmate.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json")
@Slf4j
public class LoginController {

    private final SocialLoginService socialloginService;
    private final DefaultLoginService defaultLoginService;

    @GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<?> googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        log.info("소셜 로그인을 시도합니다.");
        Member member = socialloginService.socialLogin(code, registrationId);
        log.info("소셜 로그인 완료");
        return ResponseEntity.ok().body("소셜 로그인 성공\n" + member);
    }

    // 로그인 화면: 임시
    @GetMapping("/login")
    public ResponseEntity<?> login(){
        log.info("로그인 화면");
        return ResponseEntity.ok().body("로그인 화면입니다");
    }

    // 기본 로그인
    @PostMapping("/login")
    public ResponseEntity<?> defaultLogin(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("일반 로그인을 시도합니다.");
        Member member = defaultLoginService.defaultLogin(loginRequestDto);
        log.info("일반 로그인 완료");
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

    // 기본 회원가입 페이지: 임시
    @GetMapping("/signup")
    public ResponseEntity<?> signUp() {
        return ResponseEntity.ok().body("회원가입 화면입니다");
    }

    // 기본 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpReqDto signUpReqDto) {
        Member member = defaultLoginService.signUp(signUpReqDto);
        return ResponseEntity.ok().body("회원가입 성공\n" + member);
    }

    // 인트로 페이지: 임시
    @GetMapping("/intro")
    public ResponseEntity<?> intro() {
        return ResponseEntity.ok().body("시작 화면입니다");
    }

    // 로그아웃
}