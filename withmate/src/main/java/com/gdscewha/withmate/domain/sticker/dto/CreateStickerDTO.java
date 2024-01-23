package com.gdscewha.withmate.domain.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStickerDTO {
    private String title;
    private String content;
}
