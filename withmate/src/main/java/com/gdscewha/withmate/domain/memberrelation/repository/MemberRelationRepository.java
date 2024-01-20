package com.gdscewha.withmate.domain.memberrelation.repository;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRelationRepository extends JpaRepository<MemberRelation, Long> {
    Optional<MemberRelation> findById(Long id);
    List<MemberRelation> findAllByMember(Member member);
    List<MemberRelation> findAllByRelation(Relation relation);
}
