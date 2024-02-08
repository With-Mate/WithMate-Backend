package com.gdscewha.withmate.auth.login;

import com.gdscewha.withmate.auth.dto.LoginReqDto;
import com.gdscewha.withmate.auth.dto.SignUpReqDto;
import com.gdscewha.withmate.auth.jwt.JwtTokenProvider;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.model.Role;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public String signUpMember(SignUpReqDto signUpReqDto) { //회원가입
        // id 글자수 조건 등 여기에
        if (memberRepository.existsByUserName(signUpReqDto.getUserName())) {
            return null;
        }
        if (memberRepository.existsByEmail(signUpReqDto.getEmail())) {
            return null;
        }
        // 새로운 회원 엔티티 생성
        Member member = Member.builder()
                .userName(signUpReqDto.getUserName())
                .nickname(signUpReqDto.getNickname())
                .passwd(bCryptPasswordEncoder.encode(signUpReqDto.getPasswd()))
                .email(signUpReqDto.getEmail())
                .birth(signUpReqDto.getBirth())
                .country(signUpReqDto.getCountry())
                .role(Role.USER)
                .build();
        memberRepository.save(member); // DB에 저장
        return member.getNickname();
    }

    @Transactional
    public String loginMember(LoginReqDto loginRequestDto) { // 기본 로그인
        // 사용자가 입력한 아이디, 비밀번호
        log.info("아이디 비번 get");
        String userName = loginRequestDto.getUserName();
        String password = loginRequestDto.getPasswd();
        log.info("토큰");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        // 사용자 인증
        log.info("사용자 인증 진행");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 사용자 인증이 완료된 경우
        if (authentication.isAuthenticated()) {
            log.info("사용자 인증 완료");
            /* 사용자가 인증되면 AuthDetails 객체가 생성되어 Authentication 객체에 포함되고,
             * 이 AuthDetails 객체를 통해서 인증된 사용자의 정보를 확인 가능 */
            AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
            Member member = authDetails.getMember();
            Long authenticatedId  = member.getId();
            String authenticatedUserName = member.getUserName();
            // 로그인 시간 업데이트
            memberRepository.save(member.updateLoginDate());
            // JWT 토큰 반환
            log.info("JWT 토큰 반환");
            return jwtTokenProvider.generateJwtToken(authenticatedId, authenticatedUserName);
        }
        return null;
    }

    public String logoutMember() {
        // 현재 사용자의 인증 정보를 제거하여 로그아웃 수행
        SecurityContextHolder.clearContext();
        return "로그아웃";
    }
}
