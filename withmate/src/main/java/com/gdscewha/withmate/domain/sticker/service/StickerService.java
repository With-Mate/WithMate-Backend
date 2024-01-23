package com.gdscewha.withmate.domain.sticker.service;

import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.repository.StickerRepository;
import com.gdscewha.withmate.domain.week.entity.Week;
import com.gdscewha.withmate.domain.week.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StickerService {
    private final ValidationService validationService;
    private final StickerRepository stickerRepository;
    private final WeekRepository weekRepository;

    // 나와 메이트의 스티커 내용 조회




    //스티커의 week를 조회하는 메소드
    private Week getCurrentWeek() {
        //  Member로 jouney찾고 weeknum 찾는 메소드 중간
        // 그럼 그거로 뭔가 week를 반환
        // return weekRepository.findById(week.getId())
        //        .orElseThrow(() -> new RuntimeException("현재 주 정보를 찾을 수 없습니다. Week ID: " + week.getId()));
    }

    // 새로운 스티커 생성 메소드(제목, 메모)
    public Sticker createSticker(Member member, String title, String content, Long stickerNum){
        Week currentWeek = getCurrentWeek();
        LocalDateTime creationTime = LocalDateTime.now(); // creationTime을 내 서버에서 현재 시간으로 초기화. 국적은 나중에..

        Sticker sticker = Sticker.builder()
                .member(member)
                .creationTime(LocalDate.from(creationTime))
                .title(title)
                .content(content)
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
    public Sticker updateSticker(Long stickerId, String title, String content, String impression) {
        // 스티커 id로 스티커를 찾음
        Sticker existingSticker = validationService.valSticker(stickerId);
        // 변경 내용을 set으로 업데이트
        existingSticker.setTitle(title);
        existingSticker.setContent(content);
        existingSticker.setImpression(impression);

        stickerRepository.save(existingSticker);

        // 업데이트된 스티커 반환
        return existingSticker;
    }


















}
