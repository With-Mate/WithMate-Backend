package com.gdscewha.withmate.domain.sticker.controller;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.StickerException;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.member.dto.MemberProfileDto;
import com.gdscewha.withmate.domain.member.dto.MemberSettingsDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.sticker.dto.StickerCreateDTO;
import com.gdscewha.withmate.domain.sticker.dto.StickerPreviewDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerUpdateDTO;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import com.gdscewha.withmate.domain.week.entity.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StickerController {

    private final MemberService memberService;
    private final StickerService stickerService;

    // 보드에서 스티커 미리보기로 보기
    // TODO: 나와 메이트 모두 조회가 되나?
    @GetMapping("/home/board")
    public ResponseEntity<List<StickerPreviewDto>> getstickerpreview() {
        Member member = memberService.getCurrentMember();
        List<StickerPreviewDto> stickerPreviewDto = stickerService.getStickersForAWeek(member);
        return ResponseEntity.ok().body(stickerPreviewDto);
    }


    // 모달 스티커 작성
    @PostMapping("/sticker/create")
    public ResponseEntity<?> createSticker(@RequestBody StickerCreateDTO stickerCreateDTO){
        Sticker createdSticker = stickerService.createSticker(stickerCreateDTO);
        StickerCreateDTO convertedDTO = stickerService.convertToCreateDTO(createdSticker);
        if (convertedDTO == null)
            return ResponseEntity.badRequest().body(ErrorCode.ERROR); // title null 불가
        return ResponseEntity.ok().body(convertedDTO);
    }

    // 모달 스티커 편집
    @PatchMapping("/sticker/edit")
    public ResponseEntity<?> editSticker(@RequestBody StickerUpdateDTO stickerUpdateDTO){
        Sticker editedSticker = stickerService.updateSticker(stickerUpdateDTO);
        StickerUpdateDTO convertedDTO = stickerService.convertToUpdateDTO(editedSticker);
        return ResponseEntity.ok().body(convertedDTO);
    }

    // 스티커 삭제
    @DeleteMapping("/sticker/delete")
    public ResponseEntity<?> deleteSticker(@RequestParam Long stickerId){
        stickerService.deleteSticker(stickerId);
        return ResponseEntity.ok().body("스티커를 삭제했습니다.");
    }


}