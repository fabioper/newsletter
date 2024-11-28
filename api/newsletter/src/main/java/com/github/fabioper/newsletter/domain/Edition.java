package com.github.fabioper.newsletter.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Edition {
    private final UUID id;
    private Editor editor;
    private Status status;
    private Category category;
    private final List<Note> notes = new ArrayList<>();
    private LocalDateTime publicationDate;

    public Edition(Editor editor, Category category) {
        if (editor == null)
            throw new IllegalArgumentException("editor should not be null");

        if (category == null)
            throw new IllegalArgumentException("category should not be null");

        this.id = UUID.randomUUID();
        this.editor = editor;
        this.category = category;
        this.status = Status.DRAFT;
    }

    public UUID getId() {
        return id;
    }

    public Editor getEditor() {
        return editor;
    }

    public Status getStatus() {
        return status;
    }

    public Category getCategory() {
        return category;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void assignNote(Note note) {
        if (this.isPublished()) {
            throw new IllegalStateException("Cannot add new notes to a published edition");
        }

        this.notes.add(note);
    }

    public boolean isPublished() {
        return this.status == Status.PUBLISHED;
    }

    public void publish() {
        if (this.isPublished()) {
            throw new IllegalStateException("Cannot publish an edition with published status");
        }

        this.status = Status.PUBLISHED;
        this.publicationDate = LocalDateTime.now();
    }
}
