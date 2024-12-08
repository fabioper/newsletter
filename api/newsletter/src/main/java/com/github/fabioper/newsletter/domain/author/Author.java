package com.github.fabioper.newsletter.domain.author;

import com.github.fabioper.newsletter.domain.collaborator.Collaborator;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;

import java.util.UUID;

public class Author extends Collaborator {
    public Author(UUID id) {
        super(id);
    }

    public Note createNote(String title, String content, Editorial editorial) {
        return new Note(title, content, this, editorial, null);
    }
}
