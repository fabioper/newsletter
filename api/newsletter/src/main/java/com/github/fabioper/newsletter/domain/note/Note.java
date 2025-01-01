package com.github.fabioper.newsletter.domain.note;

import com.github.fabioper.newsletter.domain.edition.EditionId;
import com.github.fabioper.newsletter.domain.shared.Guard;
import jakarta.persistence.*;

@Entity
public class Note {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private NoteId id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Embedded
    private EditionId editionId;

    @Enumerated(EnumType.STRING)
    private NoteStatus status;

    public Note() {
    }

    public Note(String title, String content) {
        Guard.againstNullOrEmpty(title, "Title cannot be empty");
        Guard.againstNullOrEmpty(content, "Content cannot be empty");

        this.id = new NoteId();
        this.title = title;
        this.content = content;
        this.status = NoteStatus.OPEN;
    }

    public NoteId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public EditionId getEditionId() {
        return editionId;
    }

    public NoteStatus getStatus() {
        return status;
    }

    public void updateTitle(String title) {
        Guard.againstNullOrEmpty(title, "Title cannot be empty");
        ensureIsOpen();
        this.title = title;
    }

    public void updateContent(String content) {
        Guard.againstNullOrEmpty(content, "Content cannot be empty");
        ensureIsOpen();
        this.content = content;
    }

    public void close() {
        ensureIsOpen();
        this.status = NoteStatus.CLOSED;
    }

    public void open() {
        ensureIsClosed();
        this.status = NoteStatus.OPEN;
    }

    private void ensureIsClosed() {
        if (!status.isClosed()) {
            throw new IllegalStateException("Note is not closed");
        }
    }

    private void ensureIsOpen() {
        if (!status.isOpen()) {
            throw new IllegalStateException("Note is closed");
        }
    }
}
