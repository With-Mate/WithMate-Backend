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
    private String member1Name;
    private String member1Goal;
    // 상대방 정보
    private String member2Name;
    private String member2Goal;
}
