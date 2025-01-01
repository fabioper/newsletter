package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.note.Note;
import com.github.fabioper.newsletter.domain.shared.Guard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Edition {
    private EditionId id;
    private String title;
    private EditionStatus status;
    private List<Note> notes;

    public Edition() {
    }

    public Edition(String title) {
        Guard.againstNullOrEmpty(title, "Title cannot be empty");

        this.title = title;
        this.status = EditionStatus.OPEN;
        this.notes = new ArrayList<>();
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

        this.status = EditionStatus.CLOSED;
    }

    public void submitToReview() {
        ensureIsClosed();
        this.status = EditionStatus.AVAILABLE_FOR_REVIEW;
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
