package com.gdscewha.withmate.domain.week.controller;

import com.gdscewha.withmate.domain.journey.service.JourneyService;
import com.gdscewha.withmate.domain.sticker.service.StickerService;
import com.gdscewha.withmate.domain.week.service.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WeekController {

    private static JourneyService journeyService;
    private static WeekService weekService;
    private static StickerService stickerService;
}
