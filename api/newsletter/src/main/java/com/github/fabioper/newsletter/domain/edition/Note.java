package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.edition.events.NoteContentUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteCreatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteEditorialUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteTitleUpdatedEvent;
import com.github.fabioper.newsletter.domain.shared.Guard;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.Objects;
import java.util.UUID;

public class Note extends BaseEntity {
    private final UUID id;
    private String title;
    private String content;
    private UUID authorId;
    private UUID editorialId;

    Note(String title, String content, UUID authorId, UUID editorialId) {
        Guard.againstNull(title, "title should not be null");
        Guard.againstNull(content, "content should not be null");
        Guard.againstNull(editorialId, "editorialId should not be null");
        Guard.againstNull(authorId, "authorId should not be null");

        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.editorialId = editorialId;

        raiseEvent(new NoteCreatedEvent(this.id));
    }

    //region Getters
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getEditorialId() {
        return editorialId;
    }

    public ReadingTime getReadingTime() {
        return ReadingTime.from(content);
    }
    //endregion

    public void updateTitle(String title) {
        var oldTitle = this.title;

        if (oldTitle.equals(title)) return;

        this.title = title;

        raiseEvent(new NoteTitleUpdatedEvent(this.id, oldTitle, title));
    }

    public void updateContent(String content) {
        var oldContent = this.content;
        if (oldContent.equals(content)) return;

        this.content = content;

        raiseEvent(new NoteContentUpdatedEvent(this.id, oldContent, content));
    }

    public void updateEditorial(UUID editorialId) {
        var oldEditorial = this.editorialId;
        if (oldEditorial.equals(editorialId)) return;

        this.editorialId = editorialId;

        raiseEvent(new NoteEditorialUpdatedEvent(this.id, oldEditorial, editorialId));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Note note = (Note) object;
        return Objects.equals(getId(), note.getId());
    }
}
