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
public class MatchingResDto {
    private String nickname;
    private String goal;
    private Category category;
    private String country;

    // Matching을 받는 생성자
    public MatchingResDto(Matching matching) {
        this.nickname = matching.getMember().getNickname();
        this.goal = matching.getGoal();
        this.category = matching.getCategory();
        this.country = matching.getMember().getCountry();
    }
}
