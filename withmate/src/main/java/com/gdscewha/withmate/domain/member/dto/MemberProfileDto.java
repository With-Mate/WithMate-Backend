package com.gdscewha.withmate.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileDto {
    private String nickname;
    private String country;
    private String regDate;
    private String loginDate;
}