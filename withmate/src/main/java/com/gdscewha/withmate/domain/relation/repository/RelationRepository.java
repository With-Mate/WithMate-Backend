package com.gdscewha.withmate.domain.relation.repository;

import com.gdscewha.withmate.domain.relation.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    Optional<Relation> findById(Long id);
}
