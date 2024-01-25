package com.gdscewha.withmate.domain.relation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationHomeDto {
    // from MemberRelation Entity
    // 내 정보
    private String MyName;
    private String MyMessage;
    private String MyGoal;
    // 상대방 정보
    private String MateName;
    private String MateMessage;
    private String MateGoal;
}
