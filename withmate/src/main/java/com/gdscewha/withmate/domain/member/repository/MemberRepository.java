package com.gdscewha.withmate.domain.member.repository;

import com.gdscewha.withmate.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByUserName(String userName);
    Optional<Member> findByNickname(String nickName);
    Optional<Member> findByEmail(String email);
}