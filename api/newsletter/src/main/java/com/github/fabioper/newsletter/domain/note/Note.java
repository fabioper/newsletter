package com.github.fabioper.newsletter.domain.note;

import com.github.fabioper.newsletter.domain.shared.Guard;
import jakarta.persistence.*;

@Entity
public class Note {
    @EmbeddedId
    private NoteId id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private NoteStatus status;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "author_id", nullable = false))
    private AuthorId authorId;

    public Note() {
    }

    public Note(String title, String content, AuthorId authorId) {
        Guard.againstNullOrEmpty(title, "Title cannot be empty");
        Guard.againstNullOrEmpty(content, "Content cannot be empty");
        Guard.againstNullOrEmpty(authorId, "AuthorId cannot be null");

        this.id = new NoteId();
        this.title = title;
        this.content = content;
        this.status = NoteStatus.OPEN;
        this.authorId = authorId;
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

    public NoteStatus getStatus() {
        return status;
    }

    public AuthorId getAuthorId() {
        return authorId;
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
