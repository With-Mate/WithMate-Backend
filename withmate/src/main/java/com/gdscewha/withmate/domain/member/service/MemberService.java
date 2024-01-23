package com.gdscewha.withmate.domain.member.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.dto.MemberSettingsDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final ValidationService validationService;

    // 내 프로필 정보 조회 - getCurrentMember()에서 id를 받아서
    public MemberProfileDto getMyProfile() {
        Member member = getCurrentMember();
        return getMemberProfile(member.getId());
    }
    // 단일 유저 프로필 정보 조회 - TODO: 현재는 멤버 아이디를 LONG으로 받고 있음.
    public MemberProfileDto getMemberProfile(Long memberId) {
        Member member = validationService.valMember(memberId);
        return MemberProfileDto.builder()
                .nickname(member.getNickname())
                .nationality(member.getNationality())
                .regDate(member.getRegDate())
                .loginDate(member.getLoginDate())
                .build();
    }

    // 설정에서 내 정보 조회
    public MemberSettingsDto getMySettingsInfo() {
        Member member = getCurrentMember();
        return MemberSettingsDto.builder()
                .userName(member.getUserName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birth(member.getBirth())
                .nationality(member.getNationality())
                .build();
    }

    // 현재 사용자 로그인 정보로 Member 반환
    public Member getCurrentMember() {
        // TODO: 추후 시큐리티 코드 추가하기
        Member member = null;
        return member;
    }

    // TODO: (후순위) 회원가입, 로그인, 로그아웃
    public void signUp () { }
    public void logIn () { }
    public void logOut() { }
}
