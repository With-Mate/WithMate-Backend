package com.gdscewha.withmate.auth.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpReqDto {
    private String userName;
    private String nickname;
    private String email;
    private String passwd;
    private String birth;
    private String country;
}
