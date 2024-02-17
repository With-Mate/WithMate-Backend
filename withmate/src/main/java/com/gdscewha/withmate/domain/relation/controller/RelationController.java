package com.gdscewha.withmate.domain.relation.controller;

import com.gdscewha.withmate.common.response.exception.ErrorCode;
import com.gdscewha.withmate.common.response.exception.MemberRelationException;
import com.gdscewha.withmate.domain.relation.dto.RelationHomeDto;
import com.gdscewha.withmate.domain.relation.dto.RelationManageDto;
import com.gdscewha.withmate.domain.relation.dto.RelationReportDto;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.service.RelationMateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
            return ResponseEntity.badRequest().header("Location", "/api/match").build();
        return ResponseEntity.ok().body(relationManageDto);
    }

    // 메이트 끊기
    @PatchMapping("/mate/unrelate")
    public ResponseEntity<?> unrelateMate(){ //@RequestBody RelationReportDto relationReportDto
            Relation relation = relationMateService.endCurrentRelation();
            if (relation == null)
                return ResponseEntity.badRequest().body("관계를 끊을 메이트가 없습니다.");
            return ResponseEntity.ok().body("메이트와의 관계를 끊었습니다.\n"
                    + relation.getStartDate() + "부터 " + relation.getEndDate() + "까지 함께했습니다.");
    }

}
