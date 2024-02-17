package com.gdscewha.withmate.domain.matching.dto;

import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

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
    public MatchedResultDto(Pair<Matching, Matching> matchedPair) {
        this.nickname1 = matchedPair.getFirst().getMember().getNickname();
        this.goal1 = matchedPair.getFirst().getGoal();
        this.category1 = matchedPair.getFirst().getCategory();
        this.nickname2 = matchedPair.getSecond().getMember().getNickname();
        this.goal2 = matchedPair.getSecond().getGoal();
        this.category2 = matchedPair.getSecond().getCategory();
    }
}
