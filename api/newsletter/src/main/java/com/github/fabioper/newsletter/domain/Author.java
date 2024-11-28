package com.github.fabioper.newsletter.domain;

import java.util.UUID;

public class Author {
    private UUID id;

    public Note createNote(String title, String content, Editorial editorial) {
        return new Note(title, content, this, editorial);
    }
}
