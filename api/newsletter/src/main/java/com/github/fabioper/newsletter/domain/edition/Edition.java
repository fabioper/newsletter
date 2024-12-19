package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.CategoryId;
import com.github.fabioper.newsletter.domain.common.Guard;
import com.github.fabioper.newsletter.domain.common.exceptions.NoteNotFoundException;
import com.github.fabioper.newsletter.domain.common.exceptions.TotalReadingTimeExceededException;
import com.github.fabioper.newsletter.domain.edition.events.*;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.EditorialId;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Edition extends BaseEntity {
    private static final int READING_TIME_LIMIT_IN_MINUTES = 8;

    private final EditionId id;
    private String title;
    private final EditorId editorId;
    private Status status;
    private CategoryId categoryId;
    private final List<Note> notes;
    private LocalDateTime publicationDate;

    public Edition(String title, EditorId editorId, CategoryId categoryId) {
        Guard.againstNull(title, "title should not be null");
        Guard.againstNull(editorId, "editorId should not be null");
        Guard.againstNull(categoryId, "categoryId should not be null");

        this.id = new EditionId();
        this.title = title;
        this.editorId = editorId;
        this.categoryId = categoryId;
        this.status = Status.DRAFT;
        this.notes = new ArrayList<>();

        raiseEvent(new EditionCreatedEvent(this.id.value()));
    }

    //region Getters
    public EditionId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public EditorId getEditorId() {
        return editorId;
    }

    public Status getStatus() {
        return status;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public List<Note> getNotes() {
        return unmodifiableList(notes);
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }
    //endregion

    public NoteId addNote(
        String title,
        String content,
        AuthorId authorId,
        EditorialId editorialId
    ) {
        if (!isDraft()) {
            throw new IllegalStateException("Edition can only be updated if it is in draft state");
        }

        var note = new Note(title, content, authorId, editorialId);
        notes.add(note);

        raiseEvent(new NoteAddedToEdition(note.getId().value(), this.id.value()));

        return note.getId();
    }

    public void removeNote(NoteId noteId) {
        if (!isDraft()) {
            throw new IllegalStateException("Edition can only be updated if it is in draft state");
        }

        var note = notes.stream()
            .filter(n -> n.getId().equals(noteId)).findFirst()
            .orElseThrow(NoteNotFoundException::new);

        this.notes.remove(note);
    }

    public void updateNote(
        NoteId noteId,
        String title,
        String content,
        EditorialId editorialId
    ) {
        if (!isDraft()) {
            throw new IllegalStateException("Edition can only be updated if it is in draft state");
        }

        var note = notes.stream().filter(n -> n.getId().equals(noteId)).findFirst()
            .orElseThrow(NoteNotFoundException::new);

        note.updateTitle(title);
        note.updateContent(content);
        note.updateEditorialId(editorialId);

        raiseEvent(new NoteAddedToEdition(note.getId().value(), this.id.value()));
    }

    public void closeEdition() {
        if (notes.isEmpty()) {
            throw new IllegalStateException("Edition has no notes");
        }

        if (getTotalReadingTime() > READING_TIME_LIMIT_IN_MINUTES) {
            throw new TotalReadingTimeExceededException(READING_TIME_LIMIT_IN_MINUTES);
        }

        this.status = Status.CLOSED;
        this.publicationDate = LocalDateTime.now();

        raiseEvent(new EditionClosedEvent(this.id.value()));
    }

    public void updateTitle(String title) {
        if (!isDraft()) {
            throw new IllegalStateException("Edition can only be updated if it is in draft state");
        }

        var oldTitle = this.title;
        this.title = title;

        raiseEvent(new EditionTitleUpdated(this.id.value(), oldTitle, this.title));
    }

    public void updateCategory(CategoryId categoryId) {
        if (!isDraft()) {
            throw new IllegalStateException("Edition can only be updated if it is in draft state");
        }

        var oldCategoryId = this.categoryId;
        this.categoryId = categoryId;

        raiseEvent(new EditionCategoryUpdated(
            this.id.value(), oldCategoryId.value(), this.categoryId.value())
        );
    }

    private int getTotalReadingTime() {
        return notes.stream().mapToInt(note -> note.getReadingTime().minutes()).sum();
    }

    public void submitToReview() {
        if (!isClosed()) {
            throw new IllegalStateException("Edition is not closed");
        }

        var oldStatus = this.status;
        this.status = Status.AVAILABLE_FOR_REVIEW;

        raiseEvent(new EditionStatusChanges(this.id.value(), oldStatus, this.status));
    }

    public void approve() {
        if (!isUnderReview()) {
            throw new IllegalStateException("Edition must be under review to be approved");
        }

        this.status = Status.APPROVED;
    }

    public void reject() {
        if (!isUnderReview()) {
            throw new IllegalStateException("Edition must be under review to be rejected");
        }

        this.status = Status.REJECTED;
    }

    public void putUnderReview() {
        if (!isAvailableForReview()) {
            throw new IllegalStateException("Edition must be available for review in order to put it under review");
        }

        this.status = Status.UNDER_REVIEW;
    }

    private boolean isDraft() {
        return this.status == Status.DRAFT;
    }

    private boolean isClosed() {
        return this.status == Status.CLOSED;
    }

    private boolean isUnderReview() {
        return this.status == Status.UNDER_REVIEW;
    }

    private boolean isAvailableForReview() {
        return this.status == Status.AVAILABLE_FOR_REVIEW;
    }
}
