package com.gdscewha.withmate.domain.member.controller;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
