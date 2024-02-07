package com.gdscewha.withmate.domain.journey.repository;

import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    Optional<Journey> findById(Long id);
    List<Journey> findAllByRelation(Relation relation);
    Optional<Journey> findByRelationAndJourneyNum(Relation relation, Long JourneyNum);
}