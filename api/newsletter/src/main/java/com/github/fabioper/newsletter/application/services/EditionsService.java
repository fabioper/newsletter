package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.domain.edition.EditionsRepository;
import org.springframework.stereotype.Service;

@Service
public class EditionsService {
    private final EditionsRepository editionsRepository;

    public EditionsService(EditionsRepository editionsRepository) {
        this.editionsRepository = editionsRepository;
    }
}
