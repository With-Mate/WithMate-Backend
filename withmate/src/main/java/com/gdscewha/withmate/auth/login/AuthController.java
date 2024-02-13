package com.gdscewha.withmate.auth.login;

import com.gdscewha.withmate.auth.dto.LoginReqDto;
import com.gdscewha.withmate.auth.dto.SignUpReqDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final MemberRepository memberRepository;

    private final AuthService authService;

    // 기본 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(@RequestBody SignUpReqDto signUpReqDto) {
        log.info("회원가입 진행");
        String memberName = authService.memberSignUp(signUpReqDto);
        if (memberName == null)
            return ResponseEntity.badRequest().body("이미 가입된 아이디 혹은 이메일입니다.");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body("회원가입 성공: " + memberName);
    }

    // 기본 로그인
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginReqDto loginReqDto) {
        System.out.println("로그인 시작");
        log.info("로그인 시작");
        String message = authService.memberLogin(loginReqDto);
        if (message.isBlank())
            return ResponseEntity.badRequest().body("로그인에 실패했습니다. 이메일 또는 비밀번호가 일치하는지 확인해주세요.");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body("로그인 성공\n토큰: "+message);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(HttpServletRequest request) {
        String logoutMessage = authService.memberLogout(request);
        if (logoutMessage == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(logoutMessage);
    }

    // 탈퇴
    @DeleteMapping("/signout")
    public ResponseEntity<?> userSignOut(HttpServletRequest request) {
        String signOutMessage = authService.signOutMember(request);
        if (signOutMessage == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(signOutMessage);
    }

    /*@GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<?> googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        log.info("소셜 로그인을 시도합니다.");
        Member member = socialloginService.socialLogin(code, registrationId);
        log.info("소셜 로그인 완료");
        return ResponseEntity.ok().body("소셜 로그인 성공\n" + member);
    }*/

    // 임시
    @GetMapping("/test/user/info")
    @Operation(summary = "맴버 엔티티 반환 API", description = "JWT 토큰을 바탕으로 맴버 엔티티를 반환하는 테스트용 API 입니다.")
    public ResponseEntity<Member> getMemberData(Authentication authentication) {
        // 인증된 사용자의 정보를 인증 객체(authentication)를 통해 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();

        // 사용자 ID를 이용하여 데이터베이스에서 사용자 정보 조회
        Member member = memberRepository.findMemberByUserName(userName);

        return new ResponseEntity<>(member, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/")
    public ResponseEntity<?> intro() {
        return ResponseEntity.ok().body("시작 화면입니다.");
    }
}