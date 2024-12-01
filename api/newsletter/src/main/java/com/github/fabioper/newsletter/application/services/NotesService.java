package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.domain.note.NotesRepository;
import org.springframework.stereotype.Service;

@Service
public class NotesService {
    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }
}
