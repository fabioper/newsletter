package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.note.Note;
import com.github.fabioper.newsletter.domain.shared.Guard;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Edition {
    @EmbeddedId
    private EditionId id;

    @Column(nullable = false, length = 150)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EditionStatus status;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "edition_id")
    private List<Note> notes;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "editor_id", nullable = false))
    private EditorId editorId;

    private static final int TOTAL_READING_TIME_LIMIT = 8;

    public Edition() {
    }

    public Edition(String title, EditorId editorId) {
        Guard.againstNullOrEmpty(title, "Title cannot be empty");
        Guard.againstNullOrEmpty(editorId, "EditorId cannot be null");

        this.id = new EditionId();
        this.title = title;
        this.status = EditionStatus.OPEN;
        this.notes = new ArrayList<>();
        this.editorId = editorId;
    }

    public EditionId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public EditionStatus getStatus() {
        return status;
    }

    public EditorId getEditorId() {
        return editorId;
    }

    public void updateTitle(String title) {
        Guard.againstNullOrEmpty(title, "Title cannot be empty");

        this.title = title;
    }

    public void assign(Note note) {
        ensureIsOpen();
        this.notes.add(note);
    }

    public void close() {
        ensureIsOpen();
        ensureIsNotEmpty();
        ensureAllNotesAreClosed();
        ensureTotalReadingTimeDoesNotExceedLimit();

        this.status = EditionStatus.CLOSED;
    }

    private int getTotalReadingTime() {
        return notes.stream().mapToInt(note -> note.getReadingTime().getMinutes()).sum();
    }

    private void ensureTotalReadingTimeDoesNotExceedLimit() {
        if (getTotalReadingTime() > TOTAL_READING_TIME_LIMIT) {
            throw new IllegalStateException("Total reading time exceeds limit");
        }
    }

    public void submitToReview() {
        ensureIsClosed();
        this.status = EditionStatus.AVAILABLE_FOR_REVIEW;
    }

    public boolean isAvailableForReview() {
        return status.isAvailableForReview();
    }

    public void putUnderReview() {
        ensureIsAvailableForReview();
        this.status = EditionStatus.UNDER_REVIEW;
    }

    public void makeAvailableForPublication() {
        ensureIsUnderReview();
        this.status = EditionStatus.AVAILABLE_FOR_PUBLICATION;
    }

    private void ensureAllNotesAreClosed() {
        if (!notes.stream().filter(note -> !note.isClosed()).toList().isEmpty()) {
            throw new IllegalStateException("There are open notes");
        }
    }

    private void ensureIsUnderReview() {
        if (!status.isUnderReview()) {
            throw new IllegalStateException("Edition is not under preview");
        }
    }

    private void ensureIsAvailableForReview() {
        if (!status.isAvailableForReview()) {
            throw new IllegalStateException("Edition is not available for review");
        }
    }

    private void ensureIsClosed() {
        if (!status.isClosed()) {
            throw new IllegalStateException("Edition is not closed");
        }
    }

    private void ensureIsOpen() {
        if (!status.isOpen()) {
            throw new IllegalStateException("Edition is not open");
        }
    }

    private void ensureIsNotEmpty() {
        if (notes.isEmpty()) {
            throw new IllegalStateException("Edition cannot be closed");
        }
    }
}
