package com.gdscewha.withmate.domain.sticker.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerUpdateDTO {
    private Long id;
    private String title;
    private String content;
    private String impression;
}