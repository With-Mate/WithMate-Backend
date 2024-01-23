package com.gdscewha.withmate.domain.member.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.dto.MemberSettingsDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final ValidationService validationService;
    private final MemberRepository memberRepository;

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
                .country(member.getCountry())
                .regDate(member.getRegDate().toString())
                .loginDate(member.getLoginDate().toString())
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
                .country(member.getCountry())
                .build();
    }

    // 현재 사용자 로그인 정보로 Member 반환
    public Member getCurrentMember() {
        // TODO: 임시 코드. 추후 시큐리티 코드 추가해 받아올 것임.
        Member member = Member.builder()
                .userName("TestUserName")
                .nickname("TestNickname")
                .passwd("Test-CurrentMember-Passwd")
                .email("testuser@example.com")
                .birth("2000-01-01")
                .country("Korea")
                .regDate(LocalDate.now())
                .loginDate(LocalDate.now())
                .isRelationed(false)
                .build()
                ;
        return member;
    }
    
    // 멤버 저장 - 테스트용
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    // TODO: (후순위) 회원가입, 로그인, 로그아웃
    public void signUp () { }
    public void logIn () { }
    public void logOut() { }

}
