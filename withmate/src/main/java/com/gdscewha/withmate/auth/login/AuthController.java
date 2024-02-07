package com.gdscewha.withmate.auth.login;

import com.gdscewha.withmate.auth.dto.LoginReqDto;
import com.gdscewha.withmate.auth.dto.SignUpReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json")
@Slf4j
public class AuthController {

    private final AuthService authService;

    // 기본 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(@RequestBody SignUpReqDto signUpReqDto) {
        String memberName = authService.signUpMember(signUpReqDto);
        if (memberName == null)
            return ResponseEntity.badRequest().body("이미 가입된 아이디 혹은 이메일입니다.");
        return ResponseEntity.ok().body("회원가입 성공: " + memberName);
    }

    // 기본 로그인
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginReqDto loginReqDto) {
        String message = authService.defaultLogin(loginReqDto);
        if (message == null)
            return ResponseEntity.badRequest().body("로그인에 실패했습니다. 이메일 또는 비밀번호가 일치하는지 확인해주세요.");
        return ResponseEntity.ok().body(message);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> userLogout() {
        String logoutMessage = authService.logoutMember();
        return ResponseEntity.ok().body("로그아웃 성공\n" + logoutMessage);
    }

    /*@GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<?> googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        log.info("소셜 로그인을 시도합니다.");
        Member member = socialloginService.socialLogin(code, registrationId);
        log.info("소셜 로그인 완료");
        return ResponseEntity.ok().body("소셜 로그인 성공\n" + member);
    }*/
}