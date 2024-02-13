package com.gdscewha.withmate.domain.sticker.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerCreateDTO {
    private String title;
    // 스티커 색상, 모양
    private String stickerColor;
    private String stickerShape;
    // 스티커 좌표
    private Long stickerTop;
    private Long stickerLeft;
}