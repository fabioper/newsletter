package com.github.fabioper.newsletter.domain.author;

import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.UUID;

public class Author extends BaseEntity {
    private final UUID id;

    public Author(UUID id) {
        this.id = id;
    }

    public Note createNote(String title, String content, Editorial editorial) {
        return new Note(title, content, this, editorial, null);
    }

    public UUID getId() {
        return id;
    }
}
