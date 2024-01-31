package com.gdscewha.withmate.domain.member.service;

import com.gdscewha.withmate.auth.login.DefaultLoginService;
import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.MemberException;
import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.dto.MemberSettingsDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.repository.MemberRelationRepository;
import com.gdscewha.withmate.domain.memberrelation.service.MemberRelationService;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final ValidationService validationService;
    private final MemberRepository memberRepository;
    private final MemberRelationRepository mRRepository;
    private final StickerRepository stickerRepository;
    private final MemberRelationService mRService;
    // 임시 코드
    private final DefaultLoginService defaultLoginService;

    // 내 프로필 정보 조회 - getCurrentMember()에서 id를 받아서
    public MemberProfileDto getMyProfile() {
        Member member = getCurrentMember();
        return getMemberProfile(member.getId());
    }
    // 메이트의 프로필 정보 조회
    public MemberProfileDto getMateProfile() {
        MemberRelation myMR = mRService.findLastMROfMember(getCurrentMember());
        if (myMR == null)
            return null; // 반환
        MemberRelation mateMR = mRService.findMROfMateByRelation(myMR, myMR.getRelation());
        if (mateMR == null)
            return null; // 반환
        return getMemberProfile(mateMR.getMember().getId());
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
    public MemberSettingsDto getSettingsInfo() {
        Member member = getCurrentMember();
        return MemberSettingsDto.builder()
                .userName(member.getUserName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birth(member.getBirth())
                .country(member.getCountry())
                .build();
    }
    // 설정에서 내 별명 업데이트
    public MemberSettingsDto updateMemberNickname(String nickname) {
        if (nickname == null) // nickname 변경이 불가능한 경우
            return null;
        Member member = getCurrentMember();
        member.setNickname(nickname);
        memberRepository.save(member);
        return getSettingsInfo();
    }

    // 현재 사용자 로그인 정보로 Member 반환
    public Member getCurrentMember() {
        // 나중에 수정해야 하는 부분...
        Long memberId = defaultLoginService.getCurrentMemberId();
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isPresent())
            return member.get();
        else
            throw new MemberException(ErrorCode.MEMBER_NOT_FOUND);

        // TODO: 임시 코드. 추후 시큐리티 코드 추가해 받아올 것임.
        /*Member member = Member.builder()
                .userName("TestUserName")
                .nickname("TestNickname")
                .passwd("TestPasswd")
                .email("testuser@example.com")
                .birth("2000-01-01")
                .country("Korea")
                .build()
                ;*/
        //memberRepository.save(member);
    }
    
    // 멤버 저장(임시) - 테스트용
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
    
    // 멤버 삭제
    public void deleteMember(Member member){
        List<Sticker> stickerList = stickerRepository.findAllByMember(member);
        stickerRepository.deleteAll(stickerList);
        List<MemberRelation> memberRelationList = mRRepository.findAllByMember(member);
        mRRepository.deleteAll(memberRelationList);
        memberRepository.delete(member);
    }

}
