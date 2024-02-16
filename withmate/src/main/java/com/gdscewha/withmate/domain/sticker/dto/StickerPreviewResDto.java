package com.gdscewha.withmate.domain.sticker.dto;

import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerPreviewResDto {
    private Long id;
    private String title;
    // 스티커 색상, 모양
    private String stickerColor;
    private String stickerShape;
    // 스티커 좌표
    private Long stickerTop;
    private Long stickerLeft;

    public static StickerPreviewResDto toDto(Sticker sticker){
        return StickerPreviewResDto.builder()
                .id(sticker.getId())
                .title(sticker.getTitle())
                .stickerColor(sticker.getStickerColor())
                .stickerShape(sticker.getStickerShape())
                .stickerTop(sticker.getStickerTop())
                .stickerLeft(sticker.getStickerLeft())
                .build();
    }
}
