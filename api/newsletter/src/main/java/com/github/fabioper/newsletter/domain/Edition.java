package com.github.fabioper.newsletter.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Edition {
    public static final int MAX_TOTAL_READING_TIME_IN_MINUTES = 8;

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
        if (this.isPublished()) {
            throw new IllegalStateException("Cannot add new notes to a published edition");
        }

        this.notes.add(note);
    }

    public boolean isPublished() {
        return this.status == Status.PUBLISHED;
    }

    public void publish() {
        if (isPublished()) {
            throw new IllegalStateException("Edition has already been published");
        }

        if (totalReadingTimeExceedsLimit()) {
            throw new IllegalStateException(
                "Total reading time exceeds limit of %d minutes".formatted(MAX_TOTAL_READING_TIME_IN_MINUTES)
            );
        }

        this.status = Status.PUBLISHED;
        this.publicationDate = LocalDateTime.now();
    }

    private boolean totalReadingTimeExceedsLimit() {
        return getTotalReadingTime() > MAX_TOTAL_READING_TIME_IN_MINUTES;
    }

    private int getTotalReadingTime() {
        return notes.stream().mapToInt(note -> note.getReadingTime().minutes()).sum();
    }
}
