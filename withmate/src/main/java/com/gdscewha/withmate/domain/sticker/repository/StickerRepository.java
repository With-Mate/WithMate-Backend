package com.gdscewha.withmate.domain.sticker.repository;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.week.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
    Optional<Sticker> findById(Long id);
    List<Sticker> findAllByWeek(Week week);
    List<Sticker> findAllByMember(Member member);
}
