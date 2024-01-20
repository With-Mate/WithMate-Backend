package com.gdscewha.withmate.domain.sticker.repository;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.week.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StickerRepository extends JpaRepository<Sticker, Long> {
    Optional<Sticker> findById(Long id);
    Optional<Sticker> findByWeekAndStickerNum(Member member, Long stickerNum);
    List<Sticker> findAllByWeek(Week week);
}
