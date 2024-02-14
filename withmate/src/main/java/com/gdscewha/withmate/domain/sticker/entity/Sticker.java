package com.gdscewha.withmate.domain.sticker.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.week.entity.Week;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stickerId")
    private Long id;

    @Column(nullable = false, name = "stickerNum")
    private Long stickerNum; // Week 내에서 순서대로 부여하는 법 찾아봐야

    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "content")
    private String content; // 처음에 비어있음

    @Builder.Default
    @Column(nullable = false, name = "creationTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate creationTime = LocalDate.now();

    @Column(name = "impression")
    private String impression;

    @Column(name = "impressionTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate impressionTime;

    // 스티커 색깔, 모양
    @Column(nullable = false, name = "stickerColor")
    private String stickerColor;
    @Column(nullable = false, name = "stickerShape")
    private String stickerShape;

    // 스티커 좌표 (240213 추가)
    @Column(nullable = false, name = "stickerTop")
    private Long stickerTop;
    @Column(nullable = false, name = "stickerLeft")
    private Long stickerLeft;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "weekId")
    private Week week;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberId")
    private Member member;
}
