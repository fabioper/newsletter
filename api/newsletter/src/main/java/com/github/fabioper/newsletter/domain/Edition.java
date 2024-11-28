package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.specifications.ExceedsReadingTimeLimitSpecification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.fabioper.newsletter.domain.specifications.ExceedsReadingTimeLimitSpecification.READING_TIME_LIMIT_IN_MINUTES;

public class Edition {
    public static final ExceedsReadingTimeLimitSpecification exceedsReadingTimeLimitSpec =
        new ExceedsReadingTimeLimitSpecification();

    private final UUID id;
    private final Editor editor;
    private Status status;
    private Category category;
    private final List<Note> notes = new ArrayList<>();
    private LocalDateTime publicationDate;

    public Edition(Editor editor, Category category) {
        if (editor == null) {
            throw new IllegalArgumentException("editor should not be null");
        }

        if (category == null) {
            throw new IllegalArgumentException("category should not be null");
        }

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

    public void updateNotes(List<Note> notes) {
        this.notes.clear();
        notes.forEach(this::assignNote);
    }

    public void assignNote(Note note) {
        if (isPublished()) {
            throw new IllegalStateException("Cannot add new notes to a published edition");
        }

        if (this.notes.contains(note)) {
            throw new IllegalArgumentException("Note is alread assigned to this edition");
        }

        this.notes.add(note);
    }

    public void unassignNote(Note note) {
        if (!notes.contains(note)) {
            throw new IllegalArgumentException("Note is not assigned to this edition");
        }

        this.notes.remove(note);
    }

    public void publish() {
        if (isPublished()) {
            throw new IllegalStateException("Edition has already been published");
        }

        if (notes.isEmpty()) {
            throw new IllegalStateException("Edition has no notes");
        }

        if (exceedsReadingTimeLimitSpec.isSatisfiedBy(this)) {
            throw new IllegalStateException(
                "Total reading time exceeds limit of %d minutes".formatted(
                    READING_TIME_LIMIT_IN_MINUTES
                )
            );
        }

        this.status = Status.PUBLISHED;
        this.publicationDate = LocalDateTime.now();
    }

    public boolean isPublished() {
        return this.status == Status.PUBLISHED;
    }
}
