package com.gdscewha.withmate.domain;


import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.dto.MemberSettingsDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private MemberService memberService;

    @Test
    void testGetMyProfile() {
        // 가짜 Member 객체 생성
        Member fakeMember = new Member();
        fakeMember.setId(1L);
        fakeMember.setNickname("TestUser");
        fakeMember.setNationality("Korean");
        fakeMember.setRegDate(LocalDate.now().);
        fakeMember.setLoginDate(LocalDate.now());

        // ValidationService의 valMember 메서드가 호출될 때 가짜 Member 객체 반환하도록 설정
        when(validationService.valMember(1L)).thenReturn(fakeMember);

        // 테스트 대상 메서드 호출
        MemberProfileDto result = memberService.getMyProfile();

        // 예상 결과와 실제 결과 비교
        assertEquals(fakeMember.getNickname(), result.getNickname());
        assertEquals(fakeMember.getNationality(), result.getNationality());
        assertEquals(fakeMember.getRegDate(), result.getRegDate());
        assertEquals(fakeMember.getLoginDate(), result.getLoginDate());
    }

    // getMemberProfile, getMySettingsInfo에 대한 테스트도 유사하게 작성 가능

    // TODO: getCurrentMember, signUp, logIn, logOut에 대한 테스트도 작성해야 함
}
