package com.gdscewha.withmate.domain.week.repository;

import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.week.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {
    Optional<Week> findById(Long id);
    Optional<Week> findByJourneyAndWeekNum(Journey journey, Long weekNum);
    List<Week> findAllByJourney(Journey journey);
}