package com.gdscewha.withmate.domain.sticker.dto;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.week.entity.Week;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WeekStickersDto {
    private Long weekNum;
    private Long myStickerCount = 0L;
    private List<StickerPreviewResDto> stickerBoard;

    public WeekStickersDto(Week week, Member member, List<Sticker> stickerList) {
        this.weekNum = week.getWeekNum();
        this.myStickerCount = stickerList.stream()
                .filter(sticker -> sticker.getMember().equals(member))
                .peek(sticker -> myStickerCount++)
                .count();
        this.stickerBoard = stickerList.stream()
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
