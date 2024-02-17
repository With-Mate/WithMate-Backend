package com.gdscewha.withmate.domain.journey.dto;

import com.gdscewha.withmate.domain.relation.dto.RelationProfileDto;
import com.gdscewha.withmate.domain.week.dto.WeekStickersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JourneyStickersDto {
    private int journeyIndex;
    private int weekCount;
    private RelationProfileDto relationInfo;
    private List<WeekStickersDto> weekBoards;
}
