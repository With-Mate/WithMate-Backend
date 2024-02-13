package com.gdscewha.withmate.domain.matching.dto;

import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchingInputDto {
    private String goal;
    private Category category;

    public MatchingInputDto(Matching matching) {
        this.goal = matching.getGoal();
        this.category = matching.getCategory();
    }
}
