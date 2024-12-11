package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.events.*;
import com.github.fabioper.newsletter.domain.note.Note;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Edition extends BaseEntity {
    private static final int READING_TIME_LIMIT_IN_MINUTES = 8;

    private final UUID id;
    private String title;
    private final UUID editorId;
    private Status status;
    private Category category;
    private final List<Note> notes = new ArrayList<>();
    private LocalDateTime publicationDate;

    public Edition(String title, UUID editorId, Category category) {
        if (title == null) {
            throw new IllegalArgumentException("title should not be null");
        }

        if (editorId == null) {
            throw new IllegalArgumentException("editor should not be null");
        }

        if (category == null) {
            throw new IllegalArgumentException("category should not be null");
        }

        this.id = UUID.randomUUID();
        this.title = title;
        this.editorId = editorId;
        this.category = category;
        this.status = Status.DRAFT;

        raiseDomainEvent(new EditionCreatedEvent(this.id));
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public UUID getEditorId() {
        return editorId;
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

        raiseDomainEvent(new NoteAssignedToEditionEvent(note.getId(), this.id));
    }

    public void unassignNote(Note note) {
        if (isPublished()) {
            throw new IllegalArgumentException("Cannot unassign note from a published edition");
        }

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

        if (getTotalReadingTime() > READING_TIME_LIMIT_IN_MINUTES) {
            throw new IllegalStateException(
                "Total reading time exceeds limit of %d minutes".formatted(
                    READING_TIME_LIMIT_IN_MINUTES
                )
            );
        }

        this.status = Status.PUBLISHED;
        this.publicationDate = LocalDateTime.now();
        this.notes.forEach(Note::lockForChanges);

        raiseDomainEvent(new EditionPublishedEvent(this.id));
    }

    public boolean isPublished() {
        return this.status == Status.PUBLISHED;
    }

    public void updateTitle(String title) {
        if (isPublished()) {
            throw new IllegalStateException("Cannot update an edition that is already published");
        }

        var oldTitle = this.title;
        this.title = title;

        raiseDomainEvent(new EditionTitleUpdated(this.id, oldTitle, this.title));
    }

    public void updateCategory(Category category) {
        if (isPublished()) {
            throw new IllegalStateException("Cannot update an edition that is already published");
        }

        var oldCategory = this.category;
        this.category = category;

        raiseDomainEvent(new EditionCategoryUpdated(this.id, oldCategory.getId(), this.category.getId()));
    }

    private int getTotalReadingTime() {
        return notes.stream().mapToInt(note -> note.getReadingTime().minutes()).sum();
    }
}
