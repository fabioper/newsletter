package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.edition.events.*;
import com.github.fabioper.newsletter.domain.shared.Guard;
import com.github.fabioper.newsletter.domain.shared.exceptions.NoteNotFoundException;
import com.github.fabioper.newsletter.domain.shared.exceptions.TotalReadingTimeExceededException;
import com.github.fabioper.newsletterapi.abstractions.AggregateRoot;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Collections.unmodifiableList;

@Entity
@Table(name = "editions")
public class Edition extends AggregateRoot {
    private static final int READING_TIME_LIMIT_IN_MINUTES = 8;

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private UUID editorId;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private UUID categoryId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "editionId", nullable = false)
    private List<Note> notes;

    @Column(nullable = false)
    private LocalDateTime publicationDate;

    public Edition() {
    }

    public Edition(String title, UUID editorId, UUID categoryId) {
        Guard.againstNull(title, "title should not be null");
        Guard.againstNull(editorId, "editorId should not be null");
        Guard.againstNull(categoryId, "category should not be null");

        this.id = UUID.randomUUID();
        this.title = title;
        this.editorId = editorId;
        this.categoryId = categoryId;
        this.status = Status.DRAFT;
        this.notes = new ArrayList<>();

        raiseEvent(new EditionCreatedEvent(this.id));
    }

    //region Getters
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

    public UUID getCategoryId() {
        return categoryId;
    }

    public List<Note> getNotes() {
        return unmodifiableList(notes);
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }
    //endregion

    public UUID addNote(
        String title,
        String content,
        UUID authorId,
        UUID editorialId
    ) {
        ensureEditionCanBeUpdated();

        var note = new Note(title, content, authorId, editorialId);
        notes.add(note);

        raiseEvent(new NoteAddedToEdition(note.getId(), this.id));

        return note.getId();
    }

    public void removeNote(UUID noteId) {
        ensureEditionCanBeUpdated();

        var note = notes.stream()
            .filter(n -> n.getId().equals(noteId)).findFirst()
            .orElseThrow(NoteNotFoundException::new);

        this.notes.remove(note);
    }

    public void updateNote(
        UUID noteId,
        String title,
        String content,
        UUID editorialId
    ) {
        ensureEditionCanBeUpdated();

        var note = notes.stream().filter(n -> n.getId().equals(noteId)).findFirst()
            .orElseThrow(NoteNotFoundException::new);

        updateNoteTitle(note, title);
        updateNoteContent(note, content);
        updateNoteEditorial(note, editorialId);
    }

    private void updateNoteTitle(Note note, String title) {
        var oldTitle = note.getTitle();

        if (!oldTitle.equals(title)) {
            note.updateTitle(title);
            raiseEvent(new NoteTitleUpdatedEvent(note.getId(), oldTitle, title));
        }
    }

    private void updateNoteContent(Note note, String content) {
        var oldContent = note.getContent();

        if (!oldContent.equals(content)) {
            note.updateContent(content);
            raiseEvent(new NoteContentUpdatedEvent(note.getId(), oldContent, content));
        }
    }

    private void updateNoteEditorial(Note note, UUID editorialId) {
        var oldEditorialId = note.getEditorialId();

        if (!oldEditorialId.equals(editorialId)) {
            note.updateEditorial(editorialId);
            raiseEvent(new NoteEditorialUpdatedEvent(note.getId(), oldEditorialId, editorialId));
        }
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

        raiseEvent(new EditionClosedEvent(this.id));
    }

    public void updateTitle(String title) {
        if (!this.status.isDraft() && !this.status.isPendingAdjustments()) {
            throw new IllegalStateException("Edition cannot not be updated");
        }

        var oldTitle = this.title;
        this.title = title;

        raiseEvent(new EditionTitleUpdated(this.id, oldTitle, this.title));
    }

    public void updateCategory(UUID categoryId) {
        ensureEditionCanBeUpdated();

        var oldCategoryId = this.categoryId;
        this.categoryId = categoryId;

        raiseEvent(new EditionCategoryUpdated(this.id, oldCategoryId, this.categoryId));
    }

    public void submitToReview() {
        if (!this.status.isClosed()) {
            throw new IllegalStateException("Edition is not closed");
        }

        var oldStatus = this.status;
        this.status = Status.AVAILABLE_FOR_REVIEW;

        raiseEvent(new EditionStatusChanges(this.id, oldStatus, this.status));
    }

    public void approve() {
        if (!this.status.isUnderReview()) {
            throw new IllegalStateException("Edition must be under review to be approved");
        }

        this.status = Status.APPROVED;
    }

    public void putAsPendingAdjustments() {
        if (!this.status.isUnderReview()) {
            throw new IllegalStateException("Edition must be under review to be rejected");
        }

        this.status = Status.PENDING_ADJUSTMENTS;
    }

    public void putUnderReview() {
        if (!this.status.isAvailableForReview()) {
            throw new IllegalStateException("Edition must be available for review in order to put it under review");
        }

        this.status = Status.UNDER_REVIEW;
    }

    private int getTotalReadingTime() {
        return notes.stream().mapToInt(note -> note.getReadingTime().minutes()).sum();
    }

    private void ensureEditionCanBeUpdated() {
        if (!this.status.isDraft() && !this.status.isPendingAdjustments()) {
            throw new IllegalStateException("Edition cannot be updated");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Edition edition = (Edition) object;
        return Objects.equals(getId(), edition.getId());
    }
}
