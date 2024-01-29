package com.gdscewha.withmate.domain.sticker.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.StickerException;
import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.sticker.dto.StickerCreateDTO;
import com.gdscewha.withmate.domain.sticker.dto.StickerResDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerPreviewDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerUpdateDTO;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.repository.StickerRepository;
import com.gdscewha.withmate.domain.week.entity.Week;
import com.gdscewha.withmate.domain.week.service.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StickerService {
    private final ValidationService validationService;
    private final StickerRepository stickerRepository;
    private final MemberService memberService;
    private final WeekService weekService;

    // 새로운 스티커 CREATE 메소드(제목, 메모)
    public Sticker createSticker(StickerCreateDTO stickerCreateDTO) {
        Member member = memberService.getCurrentMember();
        Week currentWeek = weekService.updateStickerCountOfCurrentWeek(1L);
        Sticker sticker = Sticker.builder()
                .stickerNum(currentWeek.getStickerCount() + 1)
                .title(stickerCreateDTO.getTitle())
                .content(stickerCreateDTO.getContent())
                .creationTime(LocalDate.now())
                .week(currentWeek)
                .member(member)
                .build();
        return stickerRepository.save(sticker);
    }


    // 스티커 UPDATE 메소드 (제목, 메모, 느낀점)
    public Sticker updateSticker(StickerUpdateDTO stickerUpdateDTO) {
        Member currentMember = memberService.getCurrentMember();
        // 스티커 id로 스티커를 찾음
        Sticker sticker = validationService.valSticker(stickerUpdateDTO.getId());
        if (sticker.getMember() != currentMember)
            throw new StickerException(ErrorCode.UNAUTHORIZED_TO_UPDATE_OR_DELETE_STICKER);
        // 변경 내용 업데이트
        sticker.setTitle(stickerUpdateDTO.getTitle());
        sticker.setContent(stickerUpdateDTO.getContent());
        // 느낀점 추가되었는지 확인
        String impression = stickerUpdateDTO.getImpression();
        if (impression != null && !impression.equals("")) {
            sticker.setImpression(stickerUpdateDTO.getImpression());
            sticker.setImpressionTime(LocalDate.now());
        }
        return stickerRepository.save(sticker);
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

    // 단일 스티커 조회 메소드 - 내 것인지 상대 것인지 확인함
    public StickerResDto getSticker(Long stickerId){
        Sticker sticker = validationService.valSticker(stickerId);
        Member currentMember = memberService.getCurrentMember();
        StickerResDto stickerMyResDto = StickerResDto.builder()
                .id(stickerId)
                .title(sticker.getTitle())
                .content(sticker.getContent())
                .impression(sticker.getImpression())
                .impressionTime(sticker.getImpressionTime().toString())
                .isMine(null)
                .build();
        if (sticker.getMember() == currentMember){
            stickerMyResDto.setIsMine(true);
        } else {
            stickerMyResDto.setIsMine(false);
        }
        return stickerMyResDto;
    }

    // 이번 주 스티커 미리보기로 조회 메소드
    public List<StickerPreviewDto> getStickersForThisWeek() {
        Week week = weekService.getCurrentWeek();
        List<Sticker> stickerList = stickerRepository.findByWeek(week);
        List<StickerPreviewDto> stickerPreviewDtos = null;
        for(Sticker sticker : stickerList) {
            String impression = sticker.getImpression();
            if (impression == null || impression.equals("")) {
                stickerPreviewDtos.add(new StickerPreviewDto(sticker.getId(), sticker.getTitle(), false));
            } else {
                stickerPreviewDtos.add(new StickerPreviewDto(sticker.getId(), sticker.getTitle(), true));
            }
        }
        return stickerPreviewDtos;
    }

}
