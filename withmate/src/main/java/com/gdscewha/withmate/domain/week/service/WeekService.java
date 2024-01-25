package com.gdscewha.withmate.domain.week.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.WeekException;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.journey.repository.JourneyRepository;
import com.gdscewha.withmate.domain.journey.service.JourneyService;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import com.gdscewha.withmate.domain.week.entity.Week;
import com.gdscewha.withmate.domain.week.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;
    private final JourneyRepository journeyRepository;
    private final StickerService stickerService;

    // 새로운 Week 생성 및 저장: Journey를 받아서
    public Week createWeek(Journey journey) {
        Long weekNum = journey.getWeekCount() + 1;
        Week week = Week.builder()
                .weekNum(weekNum) // 처음에 1L
                .weekStartDate(LocalDate.now())
                .stickerCount(0L) // 처음에 0L
                .journey(journey)
                .build();
        return weekRepository.save(week);
    }

    // Week의 Sticker 신규 생성 위치에 대한 고민이 필요함
//    public Week addNewStickerOfCurrentWeek(Journey journey){
//        Week week = getCurrentWeek(journey);
//        Sticker sticker = stickerService.createSticker(week, ...);
//        Long newStickerCount = week.getStickerCount() + 1;
//        week.setStickerCount(newStickerCount);
//        return weekRepository.save(week);
//    }

    // 단일 Week 조회: Journey와 weekNum으로
    public Week getWeekByJourneyAndWeekNum(Journey journey, Long weekNum){
         return weekRepository.findByJourneyAndWeekNum(journey, weekNum)
                .orElseThrow(() -> new WeekException(ErrorCode.WEEK_NOT_FOUND));
    }

    // 현재 Week를 조회
    private Week getCurrentWeek(Journey journey) {
        Long weekNum = journey.getWeekCount();
        return getWeekByJourneyAndWeekNum(journey, weekNum);
    }
}
