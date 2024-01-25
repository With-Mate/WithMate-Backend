package com.gdscewha.withmate.domain.journey.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.JourneyException;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.journey.repository.JourneyRepository;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.service.RelationMateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JourneyService {
    private final JourneyRepository journeyRepository;
    private final RelationMateService relationMateService;

    // 새로운 Journey 생성 및 저장
    public Journey createJourney() {
        Relation relation = relationMateService.getCurrentRelation();
        if (relation == null)
            return null;
        List<Journey> existingJourneyList = journeyRepository.findAllByRelation(relation);
        Integer journeyNum = existingJourneyList.size();
        Journey journey = Journey.builder()
                .journeyNum(journeyNum.longValue()) // 처음에 1L
                .weekCount(0L) // 처음에 0L
                .relation(relation)
                .build();
        return journeyRepository.save(journey);
    }

    // 해당 Journey의 WeekCount 업데이트
    public Journey updateWeekCountOfJourney(Journey journey){
        Long newWeekCount = journey.getWeekCount() + 1;
        journey.setWeekCount(newWeekCount);
        return journeyRepository.save(journey);
    }

    // 단일 Journey 조회: Relation과 index로
    public Journey getJourneyByRelationAndIndex(Relation relation, Long index) {
        Optional<Journey> journeyOptional = journeyRepository.findByRelationAndJourneyNum(relation, index);
        if (journeyOptional.isPresent())
            return journeyOptional.get();
        throw new JourneyException(ErrorCode.JOURNEY_NOT_FOUND);
    }

    // 현재 Journey 조회
    public Journey getCurrentJourney() {
        Relation relation = relationMateService.getCurrentRelation();
        if (relation == null)
            return null;
        List<Journey> existingJourneyList = journeyRepository.findAllByRelation(relation);
        if (existingJourneyList == null || existingJourneyList.isEmpty())
            return null;
        Integer index = existingJourneyList.size() - 1;
        return existingJourneyList.get(index);
    }
}
