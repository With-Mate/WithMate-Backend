package com.gdscewha.withmate.domain.matching.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.matching.dto.MatchingDTO;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.repository.MatchingRepository;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final ValidationService validationService;

    // 매칭한 적 있는지 확인(Matching이 존재하는지)
    public Matching hasMatched(Member member, MatchingDTO matchingDTO) {
        Optional<Matching> existingMatching = matchingRepository.findById(member.getId());
        if (existingMatching.isPresent()) {
            // 매칭한 적 있을 때 업데이트
            return updateMatching(member, matchingDTO);
        } else {
            // 매칭한 적 없을 때 생성
            return createMatching(member, matchingDTO);
        }
    }

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
    public Matching updateMatching(Member member, MatchingDTO matchingDTO){
        Matching matching = validationService.valMatching(matchingDTO.getId());

        // 목표가 기존과 다른 경우 업데이트
        if (!matching.getGoal().equals(matchingDTO.getGoal())) {
            matching.setGoal(matchingDTO.getGoal());
        }

        // 카테고리가 기존과 다른 경우 업데이트
        if (!matching.getCategory().equals(matchingDTO.getCategory())) {
            matching.setCategory(matchingDTO.getCategory());
        }

        return  matchingRepository.save(matching);
    }
}
