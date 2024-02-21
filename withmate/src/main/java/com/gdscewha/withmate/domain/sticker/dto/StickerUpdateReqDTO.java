package com.gdscewha.withmate.domain.sticker.dto;

import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerUpdateReqDTO {
    private Long id;
    private String title;
    private String content;
    private String impression;
    private String stickerColor;
}