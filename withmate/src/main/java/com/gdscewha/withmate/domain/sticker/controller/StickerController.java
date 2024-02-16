package com.gdscewha.withmate.domain.sticker.controller;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.sticker.dto.*;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StickerController {

    private final MemberService memberService;
    private final StickerService stickerService;

    // 내 이름, 목표 와 메이트 이름과 목표
    @GetMapping("/sticker/relation")
    public ResponseEntity<?> getMeAndMateInfo() {
        Member member = memberService.getCurrentMember();
        if (!member.getIsRelationed())
            return ResponseEntity.badRequest().body("현재 메이트를 맺은 상태가 아닙니다.");
        StickerRelationDto stickerRelationDto = stickerService.getStickerRelationInfo();
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
        WeekStickersDto weekStickersDto = stickerService.getStickersForAWeek(member);
        return ResponseEntity.ok().body(weekStickersDto);
    }

    // 모달 스티커 작성
    @PostMapping("/sticker/create")
    public ResponseEntity<?> createSticker(@RequestBody StickerCreateDTO stickerCreateDTO){
        StickerPreviewResDto stickerResDto = stickerService.createSticker(stickerCreateDTO);
        if (stickerResDto == null)
            return ResponseEntity.badRequest().body("내용은 공백일 수 없습니다.");
        return ResponseEntity.ok().body(stickerResDto);
    }

    // 편집할 스티커 불러오기
    @GetMapping("/sticker/edit")
    public ResponseEntity<?> editSticker(@RequestParam Long id){
        StickerDetailResDto resDto = stickerService.getSticker(id);
        if (resDto == null)
            return ResponseEntity.badRequest().body("해당 id의 스티커가 존재하지 않습니다.");
        return ResponseEntity.ok().build();
    }

    // 스티커 편집하기
    @PatchMapping("/sticker/select")
    public ResponseEntity<?> selectedSticker(@RequestBody StickerUpdateReqDTO stickerUpdateDTO){
        Sticker editedSticker = stickerService.updateSticker(stickerUpdateDTO);
        if (editedSticker == null)
            return ResponseEntity.badRequest().body("해당 id의 스티커가 존재하지 않습니다.");
        return ResponseEntity.ok().build();
    }

    // 스티커 삭제
    @DeleteMapping("/sticker/delete")
    public ResponseEntity<?> deleteSticker(@RequestParam Long id){
        stickerService.deleteSticker(id);
        return ResponseEntity.ok().body("스티커를 삭제했습니다.");
    }
}