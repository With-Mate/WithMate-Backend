package com.gdscewha.withmate.domain.relation.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationReportDto {
    // 상대방 정보
    private String MateName;
    private String MateCategory;
    private String reason; // ""로 전달 후 내용 받을 예정
}
