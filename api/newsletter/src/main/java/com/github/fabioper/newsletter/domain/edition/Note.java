package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.common.Guard;
import com.github.fabioper.newsletter.domain.edition.events.NoteContentUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteCreatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteEditorialUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteTitleUpdatedEvent;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

public class Note extends BaseEntity {
    private final NoteId id;
    private String title;
    private String content;
    private final AuthorId authorId;
    private Editorial editorial;

    Note(String title, String content, AuthorId authorId, Editorial editorial) {
        Guard.againstNull(title, "title should not be null");
        Guard.againstNull(content, "content should not be null");
        Guard.againstNull(editorial, "editorialId should not be null");
        Guard.againstNull(authorId, "authorId should not be null");

        this.id = new NoteId();
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.editorial = editorial;

        raiseEvent(new NoteCreatedEvent(this.id.value()));
    }

    //region Getters
    public NoteId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public AuthorId getAuthorId() {
        return authorId;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public ReadingTime getReadingTime() {
        return ReadingTime.from(content);
    }
    //endregion

    public void updateTitle(String title) {
        var oldTitle = this.title;

        if (oldTitle.equals(title)) return;

        this.title = title;

        raiseEvent(new NoteTitleUpdatedEvent(this.id.value(), oldTitle, title));
    }

    public void updateContent(String content) {
        var oldContent = this.content;
        if (oldContent.equals(content)) return;

        this.content = content;

        raiseEvent(new NoteContentUpdatedEvent(this.id.value(), oldContent, content));
    }

    public void updateEditorial(Editorial editorial) {
        var oldEditorial = this.editorial;
        if (oldEditorial.equals(editorial)) return;

        this.editorial = editorial;

        raiseEvent(new NoteEditorialUpdatedEvent(
            this.id.value(),
            oldEditorial.getId().value(),
            editorial.getId().value()
        ));
    }
}
