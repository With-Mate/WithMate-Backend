package com.gdscewha.withmate.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtToken {
    private String jwtAccessToken;

    @Builder
    public JwtToken(String jwtAccessToken) {
        this.jwtAccessToken = jwtAccessToken;
    }
}