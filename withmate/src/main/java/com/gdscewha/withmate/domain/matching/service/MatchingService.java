package com.gdscewha.withmate.domain.matching.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.matching.dto.MatchingDTO;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.repository.MatchingRepository;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final ValidationService validationService;


    //매칭 객체 새로 만들기
    public Matching createMatching(Member member, MatchingDTO matchingDTO) {
        Matching matching = Matching.builder()
                .id(matchingDTO.getId())
                .member(member)
                .goal(matchingDTO.getGoal())
                .category(Category.ART)
                .build();

        return matchingRepository.save(matching);
    }


    // 목표 입력 시 저장하기
    // matchingrepository에서 matchingid로 goal 찾아서 거기에 저장 save


    // 카테고리 선택 시 matching의 category에 저장하기
    // 목표와 카테고리를 DTO로 받을까?

}
