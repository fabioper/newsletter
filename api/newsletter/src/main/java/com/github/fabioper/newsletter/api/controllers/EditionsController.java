package com.github.fabioper.newsletter.api.controllers;

import com.github.fabioper.newsletter.application.services.EditionsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/editions")
public class EditionsController {
    private final EditionsService editionsService;

    public EditionsController(EditionsService editionsService) {
        this.editionsService = editionsService;
    }
}
