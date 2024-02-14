package com.gdscewha.withmate.domain.sticker.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerUpdateReqDTO {
    private Long id;
    private String title;
    private String content;
    private String impression;
    // 스티커 색상
    private String stickerColor;
}