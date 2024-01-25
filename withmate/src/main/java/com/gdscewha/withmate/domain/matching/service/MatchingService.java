package com.gdscewha.withmate.domain.matching.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.matching.dto.MatchingDTO;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.repository.MatchingRepository;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return matchingRepository.save(matching);
    }



    // category에 대해 다른 matching 사람이 존재하는지 (Find by category) - 일단 나부터 1개
    public List<Matching> tryMatching(Category category) {
        // 같은 카테고리의 Matching 리스트
        List<Matching> matchings = matchingRepository.findAllByCategory(category);

        //리스트가 아니라 내 아이디로? 저 리스트에서 상대 아이디 빼내오기?

        // 혼자면 (matchings에 1개) 기다려야 한다고 응답 (service에서는 null을 리턴)
        if(matchings.size() == 1){
            return null;
        }

        // 만약 2개이면 RelationService( public Relation createRelation())와 MRService()를 사용, 그 두 matching 정보를 가지고
        // 하나의 Relation과 두개의 Member Relation을 생성한 후에 Matching을 삭제.
        if(matchings.size() == 2){

        }

        // 2개면 RelationService와 MemberRelationService를 사용
        // 2 Matching으로 1 Relation, 2 Member Relation을 생성한 후 Matching 삭제
        // 그리고 Member에 isRelationed가 있는데 true로 업데이트 해줘야함
        // 그리고 Member의 isRelationed를 true로 업데이트 해줘야함
        //memberrepository.save(getIsRelation(true)); 이렇게?





    }

    public void singleNotMatchable






}
