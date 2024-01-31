package com.gdscewha.withmate.auth.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpReqDto {
    private String userName;
    private String nickname;
    private String email;
    private String passwd;
    private String birth;
    private String country;
}
