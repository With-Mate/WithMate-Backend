package com.gdscewha.withmate.domain.week.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.WeekException;
import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.journey.repository.JourneyRepository;
import com.gdscewha.withmate.domain.journey.service.JourneyService;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import com.gdscewha.withmate.domain.week.entity.Week;
import com.gdscewha.withmate.domain.week.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;
    private final JourneyService journeyService;

    // 새로운 Week 생성 및 저장: Journey를 받아서
    public Week createWeek() {
        Journey journey = journeyService.getCurrentJourney();
        if (journey == null)
            return null;
        Long weekNum = journey.getWeekCount() + 1;
        Week week = Week.builder()
                .weekNum(journey.getWeekCount() + 1) // 처음에 1L
                .weekStartDate(LocalDate.now())
                .stickerCount(0L) // 처음에 0L
                .journey(journey)
                .build();
        journeyService.updateWeekCountOfJourney(journey);
        return weekRepository.save(week);
    }

    // 해당 Week의 StickerCount 업데이트
    public Week updateStickerCountOfWeek(Week week){
        Long newStickerCount = week.getStickerCount() + 1;
        week.setStickerCount(newStickerCount);
        return weekRepository.save(week);
    }

    // (단일 여정의) 단일 Week 조회: Journey와 weekNum으로
    public Week getWeekByJourneyAndWeekNum(Journey journey, Long weekNum){
         return weekRepository.findByJourneyAndWeekNum(journey, weekNum)
                .orElseThrow(() -> new WeekException(ErrorCode.WEEK_NOT_FOUND));
    }

    // (단일 여정의) 모든 Week 조회: Journey로
    public List<Week> getAllWeeksByJourney(Journey journey){
        List<Week> weekList = weekRepository.findAllByJourney(journey);
        if (weekList == null || weekList.isEmpty())
            return null;
        return weekList;
    }

    // 현재 Week 조회
    private Week getCurrentWeek() {
        Journey journey = journeyService.getCurrentJourney();
        if (journey == null)
            return null;
        Long weekNum = journey.getWeekCount();
        return getWeekByJourneyAndWeekNum(journey, weekNum);
    }
}
