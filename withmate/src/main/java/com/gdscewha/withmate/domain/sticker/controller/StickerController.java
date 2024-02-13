package com.gdscewha.withmate.domain.sticker.controller;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.service.MemberService;
import com.gdscewha.withmate.domain.sticker.dto.StickerCreateDTO;
import com.gdscewha.withmate.domain.sticker.dto.StickerDetailResDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerPreviewResDto;
import com.gdscewha.withmate.domain.sticker.dto.StickerUpdateReqDTO;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<StickerPreviewResDto>> getstickerpreview() {
        Member member = memberService.getCurrentMember();
        List<StickerPreviewResDto> stickerPreviewDto = stickerService.getStickersForAWeek(member);
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

    // 편집할 스티커 불러오기
    @GetMapping("/sticker/edit")
    public ResponseEntity<?> editSticker(@RequestParam Long id){
        StickerDetailResDto resDto = stickerService.getSticker(id);
        if (resDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    // 스티커 편집하기
    @PatchMapping("/sticker/edit")
    public ResponseEntity<?> editSticker(@RequestBody StickerUpdateReqDTO stickerUpdateDTO){
        Sticker editedSticker = stickerService.updateSticker(stickerUpdateDTO);
        if (editedSticker == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    // 스티커 삭제
    @DeleteMapping("/sticker/delete")
    public ResponseEntity<?> deleteSticker(@RequestParam Long id){
        stickerService.deleteSticker(id);
        return ResponseEntity.ok().body("스티커를 삭제했습니다.");
    }


}