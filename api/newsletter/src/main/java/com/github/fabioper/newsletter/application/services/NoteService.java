package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.application.dto.CreateNoteRequest;
import com.github.fabioper.newsletter.application.dto.NoteResponse;
import com.github.fabioper.newsletter.domain.note.AuthorId;
import com.github.fabioper.newsletter.domain.note.Note;
import com.github.fabioper.newsletter.domain.note.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteResponse> getAllNotes() {
        var notes = noteRepository.findAll();
        return notes.stream().map(NoteService::mapToResponse).toList();
    }

    private static NoteResponse mapToResponse(Note note) {
        return new NoteResponse(
            note.getId().getValue(),
            note.getTitle(),
            note.getContent(),
            note.getStatus().name()
        );
    }

    public NoteResponse createNote(CreateNoteRequest dto, String userId) {
        var newNote = new Note(dto.title(), dto.content(), new AuthorId(userId));
        return mapToResponse(noteRepository.save(newNote));
    }
}
