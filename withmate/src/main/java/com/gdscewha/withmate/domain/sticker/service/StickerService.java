package com.gdscewha.withmate.domain.sticker.service;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.StickerException;
import com.gdscewha.withmate.common.validation.ValidationService;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.sticker.dto.StickerCreateDTO;
import com.gdscewha.withmate.domain.sticker.dto.StickerDetailResDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerPreviewResDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerUpdateReqDTO;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.repository.StickerRepository;
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
    private final StickerRepository stickerRepository;
    private final MemberService memberService;
    private final WeekService weekService;

    // 새로운 스티커 CREATE 메소드(제목, 메모)
    public Sticker createSticker(StickerCreateDTO stickerCreateDTO) {
        Member member = memberService.getCurrentMember();
        Week currentWeek = weekService.updateStickerCountOfCurrentWeek(1L);
        Sticker sticker = Sticker.builder()
                .stickerNum(currentWeek.getStickerCount() + 1) // 처음에 1L
                .title(stickerCreateDTO.getTitle())
                .creationTime(LocalDate.now())
                .stickerColor(stickerCreateDTO.getStickerColor())
                .stickerShape(stickerCreateDTO.getStickerShape())
                .stickerTop(stickerCreateDTO.getStickerTop())
                .stickerLeft(stickerCreateDTO.getStickerLeft())
                .week(currentWeek)
                .member(member)
                .build();
        return stickerRepository.save(sticker);
    }

    // entity를 StickerCreateDTO로 변환
    public StickerCreateDTO convertToCreateDTO(Sticker sticker) {
        return StickerCreateDTO.builder()
                .title(sticker.getTitle())
                .build();
    }

    // 스티커 UPDATE 메소드 (제목, 메모, 느낀점)
    public Sticker updateSticker(StickerUpdateReqDTO stickerUpdateDTO) {
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
        return stickerRepository.save(sticker);
    }

    // entity를 StickerUpdateReqTO로 변환
    public StickerUpdateReqDTO convertToUpdateDTO(Sticker sticker) {
        return StickerUpdateReqDTO.builder()
                .title(sticker.getTitle())
                .content(sticker.getContent())
                .impression(sticker.getImpression())
                .build();
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
    public StickerDetailResDto getSticker(Long stickerId){
        Sticker sticker = validationService.valSticker(stickerId);
        Member currentMember = memberService.getCurrentMember();
        StickerDetailResDto stickerMyResDto = StickerDetailResDto.builder()
                .id(stickerId)
                .title(sticker.getTitle())
                .content(sticker.getContent())
                .impression(sticker.getImpression())
                .impressionTime(sticker.getImpressionTime().toString())
                .isMine(null)
                .build();
        stickerMyResDto.setIsMine(sticker.getMember() == currentMember);
        return stickerMyResDto;
    }

    // 이번 주 스티커 미리보기로 조회 메소드
    public List<StickerPreviewResDto> getStickersForAWeek(Member member) {
        Week week = weekService.getCurrentWeek(member);
        List<Sticker> stickerList = stickerRepository.findAllByWeek(week);
        List<StickerPreviewResDto> stickerPreviewDtos = new ArrayList<>();
        for (Sticker sticker : stickerList) {
            String impression = sticker.getImpression();
            StickerPreviewResDto previewDto = StickerPreviewResDto.builder()
                    .id(sticker.getId())
                    .title(sticker.getTitle())
                    .stickerColor(sticker.getStickerColor())
                    .stickerShape(sticker.getStickerShape())
                    .stickerTop(sticker.getStickerTop())
                    .stickerLeft(sticker.getStickerLeft())
                    .build();
            stickerPreviewDtos.add(previewDto);
        }
        return stickerPreviewDtos;
    }

    // TODO: 컨트롤러 작성하며 프로필에서 여정, 주, 스티커 조회 방식 고민 필요
}