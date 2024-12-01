package com.github.fabioper.newsletter.api.controllers;

import com.github.fabioper.newsletter.application.services.EditorialsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/editorials")
public class EditorialsController {
    private final EditorialsService editorialsService;

    public EditorialsController(EditorialsService editorialsService) {
        this.editorialsService = editorialsService;
    }
}
