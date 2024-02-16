package com.gdscewha.withmate.domain.sticker.dto;

import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

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

    public static StickerDetailResDto toDto(Sticker sticker, Boolean isMine){
        String impressionTime = Optional.ofNullable(sticker.getImpressionTime())
                .map(Object::toString)
                .orElse("");
        return StickerDetailResDto.builder()
                .id(sticker.getId())
                .title(sticker.getTitle())
                .content(sticker.getContent())
                .creationTime(sticker.getCreationTime().toString())
                .impression(sticker.getImpression())
                .impressionTime(impressionTime)
                .isMine(isMine)
                .stickerColor(sticker.getStickerColor())
                .stickerShape(sticker.getStickerShape())
                .build();
    }
}