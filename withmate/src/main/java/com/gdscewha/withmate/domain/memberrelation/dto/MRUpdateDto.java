package com.gdscewha.withmate.domain.memberrelation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MRUpdateDto {
    private String nickname;
    private String message;
    private String goal;
}