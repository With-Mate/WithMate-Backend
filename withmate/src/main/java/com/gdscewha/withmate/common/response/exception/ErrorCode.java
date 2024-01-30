package com.gdscewha.withmate.common.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 도메인
    // 타입(상태 코드, "메시지");

    // Token

    // Default
    ERROR(400, "요청 처리에 실패했습니다."),

    // Journey
    JOURNEY_NOT_FOUND(404, "여정이 존재하지 않습니다."),

    // Matching
    MATCHING_NOT_FOUND(404, "매칭 중 오류가 발생했습니다."),

    // Member
    MEMBER_NOT_FOUND(404, "존재하지 않는 사용자입니다."),

    // MemberRelation
    MEMBERRELATION_NOT_FOUND(404, "멤버-메이트관계가 정상적으로 존재하지 않습니다."),

    // Relation
    RELATION_NOT_FOUND(404, "메이트관계가 존재하지 않습니다."),

    // Sticker
    STICKER_NOT_FOUND(404, "스티커가 존재하지 않습니다."),
    UNAUTHORIZED_TO_UPDATE_OR_DELETE_STICKER(401, "스티커를 수정/삭제할 권한이 없습니다."),

    // Week
    WEEK_NOT_FOUND(404, "Week가 존재하지 않습니다."),

    // Category
    CATEGORY_NOT_FOUND(404, "카테고리가 존재하지 않습니다."),

    ;

    private final int statusCode;
    private final String message;
}

