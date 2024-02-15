package com.gdscewha.withmate.domain.matching.dto;

import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.member.entity.Member;
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

    public MatchingResDto(Matching matching, Member member) {
        this.nickname = member.getNickname();
        this.goal = matching.getGoal();
        this.category = matching.getCategory();
        this.country = member.getCountry();
    }
}
