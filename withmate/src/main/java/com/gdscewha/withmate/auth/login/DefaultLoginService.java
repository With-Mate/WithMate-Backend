package com.gdscewha.withmate.auth.login;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.entity.Role;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultLoginService {

    private final MemberRepository memberRepository;
    // 임시 코드
    private Long currentMemberId;
    public Long getCurrentMemberId() { // 임시 코드
        return currentMemberId;
    }

    // 기본 로그인
    public Member defaultLogin(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByUserName(loginRequestDto.getUserName())
                .orElse(null);
        if (member == null)
            return null;
        if (!Objects.equals(member.getPasswd(), loginRequestDto.getPasswd()))
            return null;
        // LoginDate를 업데이트해 반환
        currentMemberId = member.getId();
        return member.updateLoginDate();
    }

    public Member signUp (SignUpReqDto signUpReqDto) {
        if (memberRepository.existsByUserName(signUpReqDto.getUserName())) {
            return null;
        }
        // 새로운 회원 엔티티 생성
        Member newMember = Member.builder()
                .userName(signUpReqDto.getUserName())
                .nickname(signUpReqDto.getNickname())
                .passwd(signUpReqDto.getPasswd())
                .email(signUpReqDto.getEmail())
                .birth(signUpReqDto.getBirth())
                .country(signUpReqDto.getCountry())
                .role(Role.USER)
                .build();
        // 회원 저장
        currentMemberId = newMember.getId();
        return memberRepository.save(newMember);
    }
}
