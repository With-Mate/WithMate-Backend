package com.gdscewha.withmate.domain.sticker.dto;

import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.week.entity.Week;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class WeekStickersDto {
    private Long weekNum;
    private List<StickerPreviewResDto> stickerBoard;

    public WeekStickersDto(Week week, List<Sticker> stickerList) {
        this.weekNum = week.getWeekNum();
        this.stickerBoard = stickerList
                .stream()
                .map(sticker -> StickerPreviewResDto.builder()
                        .id(sticker.getId())
                        .title(sticker.getTitle())
                        .stickerColor(sticker.getStickerColor())
                        .stickerShape(sticker.getStickerShape())
                        .stickerTop(sticker.getStickerTop())
                        .stickerLeft(sticker.getStickerLeft())
                        .build())
                .collect(Collectors.toList());
    }
}
