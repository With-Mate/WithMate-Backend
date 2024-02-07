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
    private Long proceedingTime; // 며칠째 지속중인지
    // 내 응원 수정
    private String myMessage;
    // 상대방 정보
    private String mateName;
    private String mateCategory;
    private String mateMessage;
}
