package com.gdscewha.withmate.domain.sticker.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerResDto {
    private Long id;
    private String title;
    private String content;
    private String creationTime;
    private String impression;
    private String impressionTime;
    private Boolean isMine;
}