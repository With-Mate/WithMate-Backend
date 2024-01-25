package com.gdscewha.withmate.domain;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import com.gdscewha.withmate.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private MemberService memberService;

    @Test
    void testSaveMember() {
        // 새로운 가짜 Member 객체 생성
        Member fakeMember = Member.builder()
                .userName("TestUserName")
                .nickname("TestUser")
                .passwd("Test-Passwd")
                .email("testuser@example.com")
                .birth("2000-01-01")
                .country("Korean")
                .regDate(LocalDate.now())
                .loginDate(LocalDate.now())
                .isRelationed(false)
                .build();

        // MemberRepository의 save 메서드가 호출될 때 가짜 Member 객체 반환하도록 설정
        when(memberRepository.save(fakeMember)).thenReturn(fakeMember);

        // ValidationService의 valMember 메서드가 호출될 때 가짜 Member 객체 반환하도록 설정
        when(validationService.valMember(fakeMember.getId())).thenReturn(fakeMember);

        // MemberService의 saveMember 메서드 호출
        Member savedMember = memberService.saveMember(fakeMember);

        // 예상 결과와 실제 결과 비교
        assertEquals(fakeMember.getUserName(), savedMember.getUserName());
        assertEquals(fakeMember.getNickname(), savedMember.getNickname());
        assertEquals(fakeMember.getEmail(), savedMember.getEmail());
        assertEquals(fakeMember.getBirth(), savedMember.getBirth());
        assertEquals(fakeMember.getCountry(), savedMember.getCountry());
    }
}