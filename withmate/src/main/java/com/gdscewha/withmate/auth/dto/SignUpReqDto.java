package com.gdscewha.withmate.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReqDto {
    private String userName;
    private String nickname;
    private String email;
    private String passwd;
    private String birth;
    private String country;
}
