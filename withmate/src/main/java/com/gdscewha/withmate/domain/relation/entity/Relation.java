package com.gdscewha.withmate.domain.relation.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationId")
    private Long id;

    @Column(nullable = false, name = "startDate")
    private Long startDate;

    @Column(name = "endDate")
    private Long endDate;

    @Column(nullable = false, name = "isProceed")
    private Boolean isProceed;

}
