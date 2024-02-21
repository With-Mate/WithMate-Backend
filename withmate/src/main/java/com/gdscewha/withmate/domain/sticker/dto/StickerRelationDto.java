package com.gdscewha.withmate.domain.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerRelationDto {
    // 내 정보
    private String myName;
    private String myGoal;
    // 상대방 정보
    private String mateName;
    private String mateGoal;
}
