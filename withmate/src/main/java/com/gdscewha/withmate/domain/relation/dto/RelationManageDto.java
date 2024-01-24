package com.gdscewha.withmate.domain.relation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationManageDto {
    private String startDate; //언제부터
    private String proceedingTime; // 며칠째 지속중인지
    // 상대방 정보
    private String MateName;
    private String MateCategory;
    private String MateMessage;
    // 내 응원 수정
    private String MyMessage;
}
