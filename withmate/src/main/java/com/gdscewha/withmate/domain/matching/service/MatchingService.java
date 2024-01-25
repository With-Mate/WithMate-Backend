package com.gdscewha.withmate.domain.matching.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.matching.dto.MatchingDTO;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.repository.MatchingRepository;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.model.Category;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final ValidationService validationService;


    // 매칭한 적 있는지 확인


    // 매칭 객체 새로 만들기 (매칭 전에 안했던 사람만)
    public Matching createMatching(Member member, MatchingDTO matchingDTO) {
        Matching matching = Matching.builder()
                .id(matchingDTO.getId())
                .member(member)
                .goal(matchingDTO.getGoal())
                .category(Category.ART)
                .build();

        return matchingRepository.save(matching);
    }


    // 매칭 객체 업데이트하기 (저번에 매칭 했던 사람) - 목표와 카테고리 업데이트
    public Matching updateGoalOfMatching(MatchingDTO matchingDTO){
        Matching matching = validationService.valMatching(matchingDTO.getId());
        // 찾은 Matching 객체에 새로운 Goal 설정
        matching.setGoal(matching.getGoal());
        matching.setCategory(matchingDTO.getCategory());
        matchingRepository.save(matching);

        return matching;
    }

}
