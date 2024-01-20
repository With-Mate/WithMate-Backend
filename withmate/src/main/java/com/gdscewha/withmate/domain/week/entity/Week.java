package com.gdscewha.withmate.domain.week.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekId")
    private Long id;

    @Column(nullable = false, name = "weekNum")
    private Long weekNum;

    @Column(nullable = false, name = "weekStartDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd'T'HH:mm:ss")
    private LocalDate weekStartDate;

    @Builder.Default
    @Column(nullable = false, name = "stickerCount")
    private Long stickerCount = 0L; // 처음에 0개라서 근데 나중에 필요없을수도...

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "journeyId")
    private Journey journey;
}
