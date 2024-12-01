package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.domain.editorial.EditorialsRepository;
import org.springframework.stereotype.Service;

@Service
public class EditorialsService {
    private final EditorialsRepository editorialsRepository;

    public EditorialsService(EditorialsRepository editorialsRepository) {
        this.editorialsRepository = editorialsRepository;
    }
}
