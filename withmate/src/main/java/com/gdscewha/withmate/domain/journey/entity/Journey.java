package com.gdscewha.withmate.domain.journey.entity;

import com.gdscewha.withmate.domain.relation.entity.Relation;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journeyId")
    private Long id;

    @Column(nullable = false, name = "weekCount")
    private Long weekCount;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "relationId")
    private Relation relation;
}
