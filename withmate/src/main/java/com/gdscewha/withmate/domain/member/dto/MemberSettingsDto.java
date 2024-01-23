package com.gdscewha.withmate.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSettingsDto {
    private String userName;
    private String nickname;
    private String email;
    private String birth;
    private String nationality;
}
