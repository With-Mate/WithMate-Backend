package com.gdscewha.withmate.domain.matching.repository;

import com.gdscewha.withmate.domain.enums.Category;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findById(Long id);
    Optional<Matching> findByMember(Member member); // 특정 Member가 Matching 중인지 확인 위해
    List<Matching> findAllByCategory(Category category); // 특정 Category의 유저 찾기 위해
}
