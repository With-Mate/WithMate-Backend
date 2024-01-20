package com.gdscewha.withmate.domain.matching.entity;

import com.gdscewha.withmate.domain.category.Category;
import com.gdscewha.withmate.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchingId")
    private Long id;

    @Column(nullable = false, name = "goal")
    private String goal;

    @Column(nullable = false, name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
