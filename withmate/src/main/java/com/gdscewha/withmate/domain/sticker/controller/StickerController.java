package com.gdscewha.withmate.domain.sticker.controller;

import com.gdscewha.withmate.domain.journey.dto.JourneyStickersDto;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.sticker.dto.*;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import com.gdscewha.withmate.domain.week.dto.WeekStickersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StickerController {

    private final MemberService memberService;
    private final StickerService stickerService;

    // 내 이름, 목표와 메이트 이름과 목표
    @GetMapping("/sticker/relation")
    public ResponseEntity<?> getMeAndMateInfo() {
        Member member = memberService.getCurrentMember();
        if (!member.getIsRelationed())
            return ResponseEntity.badRequest().body("현재 메이트를 맺은 상태가 아닙니다.");
        StickerRelationDto stickerRelationDto = stickerService.getStickerRelationInfo(member);
        if (stickerRelationDto == null)
            return ResponseEntity.ok().header("Location", "/api/match").build();
        return ResponseEntity.ok().body(stickerRelationDto);
    }

    // 보드에서 스티커 미리보기로 보기
    @GetMapping("/sticker/board")
    public ResponseEntity<?> getStickersPreview() {
        Member member = memberService.getCurrentMember();
        if (!member.getIsRelationed())
            return ResponseEntity.badRequest().body("현재 메이트를 맺은 상태가 아닙니다.");
        WeekStickersDto weekStickersDto = stickerService.getStickersForCurrentWeek(member);
        return ResponseEntity.ok().body(weekStickersDto);
    }

    // 내 n번째 여정 조회
    @GetMapping("/self/journey")
    public ResponseEntity<?> getMyNthJourney(@RequestParam(required = false) Long index) {
        Member member = memberService.getCurrentMember();
        JourneyStickersDto journeyStickersDto = stickerService.getStickersForAJourney(member, index);
        if (journeyStickersDto == null)
            return ResponseEntity.badRequest().body("여정이 없습니다.");
        return ResponseEntity.ok().body(journeyStickersDto);
    }

    // 메이트의 n번째 여정 조회
    @GetMapping("/mate/journey")
    public ResponseEntity<?> getMateNthJourney(@RequestParam(required = false) Long index) {
        Member mate = memberService.getCurrentMate();
        JourneyStickersDto journeyStickersDto = stickerService.getStickersForAJourney(mate, index);
        if (journeyStickersDto == null)
            return ResponseEntity.badRequest().body("여정이 없습니다.");
        return ResponseEntity.ok().body(journeyStickersDto);
    }


    // 모달 스티커 작성
    @PostMapping("/sticker/create")
    public ResponseEntity<?> createSticker(@RequestBody StickerCreateDTO stickerCreateDTO){
        StickerPreviewResDto previewResDto = stickerService.createSticker(stickerCreateDTO);
        if (previewResDto == null)
            return ResponseEntity.badRequest().body("내용은 공백일 수 없습니다.");
        return ResponseEntity.ok().body(previewResDto);
    }

    // 편집할 스티커 불러오기
    @GetMapping("/sticker/select")
    public ResponseEntity<?> editSticker(@RequestParam Long id){
        StickerDetailResDto detailResDto = stickerService.getSticker(id);
        if (detailResDto == null)
            return ResponseEntity.badRequest().body("해당 id의 스티커가 존재하지 않습니다.");
        return ResponseEntity.ok().body(detailResDto);
    }

    // 스티커 편집하기
    @PatchMapping("/sticker/edit")
    public ResponseEntity<?> selectedSticker(@RequestBody StickerUpdateReqDTO stickerUpdateDTO){
        StickerDetailResDto editedSticker = stickerService.updateSticker(stickerUpdateDTO);
        if (editedSticker == null)
            return ResponseEntity.badRequest().body("해당 id의 스티커가 존재하지 않습니다.");
        return ResponseEntity.ok().body(editedSticker);
    }

    // 스티커 삭제
    @DeleteMapping("/sticker/delete")
    public ResponseEntity<?> deleteSticker(@RequestParam Long id){
        stickerService.deleteSticker(id);
        return ResponseEntity.ok().body("스티커를 삭제했습니다.");
    }
}