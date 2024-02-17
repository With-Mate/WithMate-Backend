package com.gdscewha.withmate.domain.sticker.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.MemberRelationException;
import com.gdscewha.withmate.common.response.exception.StickerException;
import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.journey.dto.JourneyStickersDto;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.journey.service.JourneyService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.repository.MemberRelationRepository;
import com.gdscewha.withmate.domain.relation.dto.RelationHomeDto;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.service.RelationMateService;
import com.gdscewha.withmate.domain.sticker.dto.*;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.repository.StickerRepository;
import com.gdscewha.withmate.domain.week.dto.WeekStickersDto;
import com.gdscewha.withmate.domain.week.entity.Week;
import com.gdscewha.withmate.domain.week.service.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StickerService {
    private final ValidationService validationService;
    private final MemberService memberService;
    private final RelationMateService relationMateService;
    private final JourneyService journeyService;
    private final WeekService weekService;
    private final MemberRelationRepository mRRepository;
    private final StickerRepository stickerRepository;

    // 목표 화면에서 나-메이트 정보 가져오기
    public StickerRelationDto getStickerRelationInfo() {
        RelationHomeDto relationHomeDto = relationMateService.getHomeInfo();
        return StickerRelationDto.builder()
                .myName(relationHomeDto.getMyName())
                .myGoal(relationHomeDto.getMyGoal())
                .mateName(relationHomeDto.getMateName())
                .mateGoal(relationHomeDto.getMateGoal())
                .build();
    }

    // 이번 주 스티커 미리보기로 조회 메소드
    public WeekStickersDto getStickersForCurrentWeek(Member member) {
        Week week = weekService.getCurrentWeek(member);
        List<Sticker> stickerList = stickerRepository.findAllByWeek(week);
        return new WeekStickersDto(week, member, stickerList);
    }

    // 한 주 스티커 미리보기로 조회 메소드
    public WeekStickersDto getStickersForAWeek(Member member, Week week) {
        List<Sticker> stickerList = stickerRepository.findAllByWeek(week);
        return new WeekStickersDto(week, member, stickerList);
    }

    // 프로필 화면 - index번째 여정의 스티커들을 가져오기
    public JourneyStickersDto getStickersForAJourney(Member member, Long index) {
        List<MemberRelation> mRList = mRRepository.findAllByMember(member);
        int relationIndex; // 볼 relation의 index
        if (mRList == null || mRList.isEmpty())
            return null;
        if (index == null) // 가장 최신을 보는 경우
            relationIndex = mRList.size();
        else if (index > mRList.size())
            throw new MemberRelationException(ErrorCode.MEMBERRELATION_NOT_FOUND);
        else
            relationIndex = index.intValue();
        Relation relation = mRList.get(relationIndex - 1).getRelation();
        Journey journey = journeyService.getJourneyByRelation(relation);
        List<Week> weekList = weekService.getAllWeeksByJourney(journey);
        List<WeekStickersDto> weekStickersDtos = new ArrayList<>();
        for (Week w : weekList) {
            weekStickersDtos.add(getStickersForAWeek(member, w));
        }
        StickerRelationDto relationDto = getStickerRelationInfo();
        return new JourneyStickersDto(relationIndex, weekList.size(), relationDto, weekStickersDtos);
    }

    // 새로운 스티커 CREATE 메소드(제목, 스티커 색깔, 모양, 위치 등)
    public StickerPreviewResDto createSticker(StickerCreateDTO stickerCreateDTO) {
        Member member = memberService.getCurrentMember();
        Week currentWeek = weekService.updateStickerCountOfCurrentWeek(1L);
        Sticker sticker = Sticker.builder()
                .stickerNum(currentWeek.getStickerCount()) // 처음에 1L
                .title(stickerCreateDTO.getTitle())
                .content("")
                .impression("")
                .creationTime(LocalDate.now())
                .stickerColor(stickerCreateDTO.getStickerColor())
                .stickerShape(stickerCreateDTO.getStickerShape())
                .stickerTop(stickerCreateDTO.getStickerTop())
                .stickerLeft(stickerCreateDTO.getStickerLeft())
                .week(currentWeek)
                .member(member)
                .build();
        stickerRepository.save(sticker);
        return StickerPreviewResDto.toDto(sticker);
    }

    // 단일 스티커 조회 메소드 - 내 것인지 상대 것인지 확인함
    public StickerDetailResDto getSticker(Long stickerId){
        Sticker sticker = validationService.valSticker(stickerId);
        Member currentMember = memberService.getCurrentMember();
        return StickerDetailResDto.toDto(sticker, sticker.getMember() == currentMember);
    }

    // 단일 스티커 UPDATE 메소드 (제목, 메모, 느낀점)
    public StickerDetailResDto updateSticker(StickerUpdateReqDTO stickerUpdateDTO) {
        Member currentMember = memberService.getCurrentMember();
        // 스티커 id로 스티커를 찾음
        Sticker sticker = validationService.valSticker(stickerUpdateDTO.getId());
        if (sticker.getMember() != currentMember)
            throw new StickerException(ErrorCode.UNAUTHORIZED_TO_UPDATE_OR_DELETE_STICKER);
        // 변경 내용 업데이트
        sticker.setTitle(stickerUpdateDTO.getTitle());
        sticker.setContent(stickerUpdateDTO.getContent());
        sticker.setStickerColor(stickerUpdateDTO.getStickerColor());
        // 느낀 점 추가되었는지 확인
        String impression = stickerUpdateDTO.getImpression();
        if (impression != null && !impression.equals("")) {
            sticker.setImpression(stickerUpdateDTO.getImpression());
            sticker.setImpressionTime(LocalDate.now());
        }
        stickerRepository.save(sticker);
        return StickerDetailResDto.toDto(sticker, sticker.getMember() == currentMember);
    }

    // 스티커 DELETE 메소드
    public void deleteSticker(Long stickerId) {
        Member currentMember = memberService.getCurrentMember();
        // 스티커 id로 스티커를 찾음
        Sticker sticker = validationService.valSticker(stickerId);
        if (sticker.getMember() != currentMember)
            throw new StickerException(ErrorCode.UNAUTHORIZED_TO_UPDATE_OR_DELETE_STICKER);
        weekService.updateStickerCountOfCurrentWeek(-1L);
        stickerRepository.delete(sticker);
    }
}