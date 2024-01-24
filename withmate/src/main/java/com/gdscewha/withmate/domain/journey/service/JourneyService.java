package com.gdscewha.withmate.domain.journey.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.JourneyException;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.journey.repository.JourneyRepository;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JourneyService {
    private final JourneyRepository journeyRepository;

    // 새로운 여정 생성: Relation을 받아서
    public void createJourney(Relation relation) {
        List<Journey> existingJourneyList = journeyRepository.findAllByRelation(relation);
        Integer journeyCount = existingJourneyList.size();
        Journey journey = Journey.builder()
                .journeyNum(journeyCount.longValue()) // 처음에 0
                .weekCount(1L) // 처음에 1L
                .relation(relation)
                .build();
        journeyRepository.save(journey);
    }

    // 여정의 주 수 업데이트
    public void updateWeekCountOfJourney(Journey journey){
        Long newWeekCount = journey.getWeekCount() + 1;
        journey.setWeekCount(newWeekCount);
    }

    // 단일 Journey 조회: Relation과 index로
    public Journey getJourneyByRelationAndIndex(Relation relation, Long index) {
        Optional<Journey> journeyOptional = journeyRepository.findByRelationAndJourneyNum(relation, index);
        if (journeyOptional.isPresent())
            return journeyOptional.get();
        throw new JourneyException(ErrorCode.JOURNEY_NOT_FOUND);
    }

    // 현재 Journey를 반환 -> DTO로 변경 필요
    public Journey getCurrentJourney(Relation relation) {
        List<Journey> existingJourneyList = journeyRepository.findAllByRelation(relation);
        if (existingJourneyList == null || existingJourneyList.isEmpty())
            return null;
        Integer index = existingJourneyList.size() - 1;
        Journey journey = existingJourneyList.get(index);
        return journey;
    }
    
    // TODO: Relation에서 Journey 호출
}
