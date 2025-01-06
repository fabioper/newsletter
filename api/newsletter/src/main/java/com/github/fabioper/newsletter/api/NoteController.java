package com.github.fabioper.newsletter.api;

import com.github.fabioper.newsletter.application.dto.CreateNoteRequest;
import com.github.fabioper.newsletter.application.dto.NoteResponse;
import com.github.fabioper.newsletter.application.services.NoteService;
import com.github.fabioper.newsletter.infra.security.annotations.IsAuthor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @IsAuthor
    @GetMapping
    public List<NoteResponse> listNotes() {
        return this.noteService.getAllNotes();
    }

    @IsAuthor
    @PostMapping
    public NoteResponse createNote(@RequestBody CreateNoteRequest dto, @AuthenticationPrincipal Jwt jwt) {
        return this.noteService.createNote(dto, jwt.getSubject());
    }
}
