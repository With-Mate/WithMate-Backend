package com.gdscewha.withmate.domain.sticker.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.sticker.dto.CreateStickerDTO;
import com.gdscewha.withmate.domain.sticker.dto.UpdateStickerDTO;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.repository.StickerRepository;
import com.gdscewha.withmate.domain.week.entity.Week;
import com.gdscewha.withmate.domain.week.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StickerService {
    private final ValidationService validationService;
    private final StickerRepository stickerRepository;
    private final WeekRepository weekRepository;

    // 나와 메이트의 스티커 내용 조회 메소드
    // 멤버relation-mate-journey-week-sticker 이렇게 엔티티를 타고타고 가서 조회할 수 없을까?




    // 스티커의 week를 조회하는 메소드
    private Week getCurrentWeek() {
        //   TODO: Member로 jouney찾고 weeknum 찾는 메소드 중간
        // 그럼 그거로 뭔가 week를 반환
        // return weekRepository.findById(week.getId())
        //        .orElseThrow(() -> new RuntimeException("현재 주 정보를 찾을 수 없습니다. Week ID: " + week.getId()));
        return null; //임시....
    }

    // 새로운 스티커 생성 메소드(제목, 메모)
    // TODO : 스티커 생성 및 수정하는 멤버가 당사자인지 권한 확인 필요
    public Sticker createSticker(Member member, CreateStickerDTO createStickerDTO) {
        Week currentWeek = getCurrentWeek();
        LocalDateTime creationTime = LocalDateTime.now();

        Sticker sticker = Sticker.builder()
                .member(member)
                .creationTime(LocalDate.from(creationTime))
                .title(createStickerDTO.getTitle())
                .content(createStickerDTO.getContent())
                .week(currentWeek)
                .stickerNum(currentWeek.getStickerCount())
                .build();

        stickerRepository.save(sticker);

        // Week 엔티티의 stickerCount +1 증가
        currentWeek.setStickerCount(currentWeek.getStickerCount() + 1);
        weekRepository.save(currentWeek);

        return sticker;
    }


    // 있던 스티커 변경 메소드 (제목, 메모, 느낀점)
    // TODO : 스티커 생성 및 수정하는 멤버가 당사자인지 권한 확인 필요
    public Sticker updateSticker(Long stickerId, UpdateStickerDTO updateStickerDTO) {
        // 스티커 id로 스티커를 찾음
        Sticker existingSticker = validationService.valSticker(stickerId);

        // 변경 내용을 set으로 업데이트
        existingSticker.setTitle(updateStickerDTO.getTitle());
        existingSticker.setContent(updateStickerDTO.getContent());
        existingSticker.setImpression(updateStickerDTO.getImpression());

        stickerRepository.save(existingSticker);

        // 업데이트된 스티커 반환
        return existingSticker;
    }

















}
