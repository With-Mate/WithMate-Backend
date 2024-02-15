package com.gdscewha.withmate.domain.sticker.controller;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.relation.dto.RelationHomeDto;
import com.gdscewha.withmate.domain.relation.service.RelationMateService;
import com.gdscewha.withmate.domain.sticker.dto.StickerCreateDTO;
import com.gdscewha.withmate.domain.sticker.dto.StickerPreviewDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerUpdateDTO;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StickerController {

    private final MemberService memberService;
    private final StickerService stickerService;
    private final RelationMateService relationMateService;

    // 보드에서 스티커 미리보기로 보기
    @GetMapping("/home/board")
    public ResponseEntity<List<StickerPreviewDto>> getStickerPreview() {
        Member member = memberService.getCurrentMember();
        List<StickerPreviewDto> myStickerPreviewDto = stickerService.getStickersForAWeek(member);
        Member mate = memberService.getCurrentMate();
        List<StickerPreviewDto> mateStickerPreviewDto = stickerService.getStickersForAWeek(mate);
        // 나와 메이트의 스티커 합쳐서 반환
        List<StickerPreviewDto> combinedStickerPreviews = new ArrayList<>();
        combinedStickerPreviews.addAll(myStickerPreviewDto);
        combinedStickerPreviews.addAll(mateStickerPreviewDto);

        return ResponseEntity.ok().body(combinedStickerPreviews);
    }



    // 내 이름, 목표 와 메이트 이름과 목표 (누르면 메이트 프로필로?)
    @GetMapping("/sticker/relation")
    public ResponseEntity<?> getMeAndMateInfo() {
        RelationHomeDto relationHomeDto = relationMateService.getHomeInfo();
        if (relationHomeDto == null)
            return ResponseEntity.ok().header("Location", "/api/match").build();
        return ResponseEntity.ok().body(relationHomeDto);
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