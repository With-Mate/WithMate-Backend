package com.gdscewha.withmate.domain.relation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationProfileDto {
    // 내 정보
    private String nickname1;
    private String goal1;
    // 상대방 정보
    private String nickname2;
    private String goal2;
}
