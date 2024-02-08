package com.gdscewha.withmate.auth.login;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public AuthDetails loadUserByUsername(String userName) {
        Member member = memberRepository.findMemberByUserName(userName);
        return new AuthDetails(member);
    }
}
