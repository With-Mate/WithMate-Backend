package com.gdscewha.withmate.domain.matching.dto;

import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchedResultDto {
    private String nickname1;
    private String goal1;
    private Category category1;

    private String nickname2;
    private String goal2;
    private Category category2;

    // List<Matching>을 받는 생성자
    public MatchedResultDto(List<Matching> matching) {
        this.nickname1 = matching.get(0).getMember().getNickname();
        this.goal1 = matching.get(0).getGoal();
        this.category1 = matching.get(0).getCategory();
        this.nickname2 = matching.get(1).getMember().getNickname();
        this.goal2 = matching.get(1).getGoal();
        this.category2 = matching.get(1).getCategory();
    }
}
