package com.gdscewha.withmate.domain.relation.controller;

import com.gdscewha.withmate.domain.relation.dto.RelationHomeDto;
import com.gdscewha.withmate.domain.relation.dto.RelationManageDto;
import com.gdscewha.withmate.domain.relation.service.RelationMateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RelationController {

    private final RelationMateService relationMateService;

    // Home 화면에서 메이트 관계 정보 조회
    @GetMapping("/home")
    public ResponseEntity<?> getHomeInfo() {
        RelationHomeDto relationHomeDto = relationMateService.getHomeInfo();
        if (relationHomeDto == null)
            return ResponseEntity.ok().header("Location", "/api/match").build();
        return ResponseEntity.ok().body(relationHomeDto);
    }

    // 메이트 관리 화면
    @GetMapping("/mate/manage")
    public ResponseEntity<?> getMateManageInfo() {
        RelationManageDto relationManageDto = relationMateService.getRelationManageInfo();
        if (relationManageDto == null)
            return ResponseEntity.ok().header("Location", "/api/match").build();
        return ResponseEntity.ok().body(relationManageDto);
    }
}
