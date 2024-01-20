package com.gdscewha.withmate.domain.sticker.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.week.entity.Week;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekId")
    private Long id;

    @Column(nullable = false, name = "stickerNum")
    private Long stickerNum; // Week 내에서 순서대로 부여하는 법 찾아봐야

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "creationTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd'T'HH:mm:ss")
    private LocalDate creationTime;

    @Column(name = "impression")
    private String impression;

    @Column(name = "impressionTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd'T'HH:mm:ss")
    private LocalDate impressionTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "weekId")
    private Week week;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private Member member;
}
