package com.gdscewha.withmate.auth.googlelogin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/code/{registrationId}")
    public ResponseEntity<?> googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        UserResource userResource = loginService.socialLogin(code, registrationId);
        return ResponseEntity.ok().body(userResource);
    }
    // 닉네임 수정 필요

    @PostMapping("/login")
    public ResponseEntity<String> memberLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        TokenDTO tokenDTO = memberService.login(loginRequestDTO);
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", tokenDTO.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")     //??
                .maxAge(tokenDTO.getDuration())
                .path("/")
                .build();
        return ResponseEntity.ok().header("Set-Cookie", responseCookie.toString()).body(tokenDTO.getAccessToken());
    }
}