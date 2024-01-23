package com.gdscewha.withmate.domain.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStickerDTO {
    private String title;
    private String content;
    private String impression;
}
