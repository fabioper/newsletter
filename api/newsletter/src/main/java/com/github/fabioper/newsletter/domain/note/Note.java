package com.github.fabioper.newsletter.domain.note;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.events.NoteContentUpdatedEvent;
import com.github.fabioper.newsletter.domain.note.events.NoteCreatedEvent;
import com.github.fabioper.newsletter.domain.note.events.NoteEditorialUpdatedEvent;
import com.github.fabioper.newsletter.domain.note.events.NoteTitleUpdatedEvent;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.UUID;

public class Note extends BaseEntity {
    private final UUID id;
    private String title;
    private String content;
    private final UUID authorId;
    private Editorial editorial;
    private Edition edition;

    public Note(String title, String content, UUID authorId, Editorial editorial) {
        if (title == null) {
            throw new IllegalArgumentException("title should not be null");
        }

        if (content == null) {
            throw new IllegalArgumentException("content should not be null");
        }

        if (editorial == null) {
            throw new IllegalArgumentException("editorial should not be null");
        }

        if (authorId == null) {
            throw new IllegalArgumentException("author should not be null");
        }

        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.editorial = editorial;

        raiseDomainEvent(new NoteCreatedEvent(this.id));
    }

    public String getTitle() {
        return title;
    }

    public UUID getId() {
        return id;
    }

    public void updateTitle(String title) {
        if (edition != null && edition.isPublished()) {
            throw new IllegalStateException("Cannot update note assigned to a published edition");
        }

        var oldTitle = this.title;

        this.title = title;

        raiseDomainEvent(new NoteTitleUpdatedEvent(this.id, oldTitle, title));
    }

    public void updateContent(String content) {
        if (edition != null && edition.isPublished()) {
            throw new IllegalStateException("Cannot update note assigned to a published edition");
        }

        var oldContent = this.content;

        this.content = content;

        raiseDomainEvent(new NoteContentUpdatedEvent(this.id, oldContent, content));
    }

    public void updateEditorial(Editorial editorial) {
        if (edition != null && edition.isPublished()) {
            throw new IllegalStateException("Cannot update note assigned to a published edition");
        }

        var oldEditorial = this.editorial;
        this.editorial = editorial;

        raiseDomainEvent(new NoteEditorialUpdatedEvent(this.id, oldEditorial.getId(), editorial.getId()));
    }

    public String getContent() {
        return content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public Edition getEdition() {
        return edition;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public ReadingTime getReadingTime() {
        return ReadingTime.from(content);
    }

    public void updateEdition(Edition edition) {
        this.edition = edition;
    }
}
