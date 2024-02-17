package com.gdscewha.withmate.domain.week.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.WeekException;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.journey.service.JourneyService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
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
    private final MemberService memberService;

    // 새로운 Week 생성 및 저장: Journey를 받아서
    public Week createWeek() {
        Journey journey = journeyService.updateWeekCountOfCurrentJourney();
        if (journey == null)
            throw new WeekException(ErrorCode.JOURNEY_NOT_FOUND);
        Week week = Week.builder()
                .weekNum(journey.getWeekCount()) // 처음에 1L (위에서 업데이트)
                .weekStartDate(LocalDate.now())
                .stickerCount(0L) // 처음에 0L
                .journey(journey)
                .build();
        return weekRepository.save(week);
    }

    // 해당 Week의 StickerCount 업데이트
    public Week updateStickerCountOfCurrentWeek(Long diff){
        Member member = memberService.getCurrentMember();
        Week week = getCurrentWeek(member);
        if(week == null)
            throw new WeekException(ErrorCode.WEEK_NOT_FOUND);
        Long newStickerCount = week.getStickerCount() + diff;
        if (newStickerCount < 0)
            throw new WeekException(ErrorCode.STICKER_NOT_FOUND);
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
            throw new WeekException(ErrorCode.WEEK_NOT_FOUND);
        return weekList;
    }

    // 현재 Week 조회
    public Week getCurrentWeek(Member member) {
        Journey journey = journeyService.getCurrentJourney(member);
        if (journey == null)
            throw new WeekException(ErrorCode.JOURNEY_NOT_FOUND);
        Long weekNum = journey.getWeekCount();
        return getWeekByJourneyAndWeekNum(journey, weekNum);
    }
}
