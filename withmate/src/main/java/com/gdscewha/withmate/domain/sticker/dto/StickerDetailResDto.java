package com.gdscewha.withmate.domain.sticker.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerDetailResDto {
    private Long id;
    private String title;
    private String content;
    private String creationTime;
    private String impression;
    private String impressionTime;
    private Boolean isMine;
    // 스티커 색상, 모양
    private String stickerColor;
    private String stickerShape;
}