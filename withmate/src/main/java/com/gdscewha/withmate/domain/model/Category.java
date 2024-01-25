package com.gdscewha.withmate.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    HEALTH,
    STUDY,
    ART,
    ADVENTURE,
    HOBBY;

    // ex. JSON 데이터 "ART"라는 문자열 -> Category.ART로 변환
    @JsonCreator
    public static Category from(String s) {
        return Category.valueOf(s.toUpperCase());
    }
}
