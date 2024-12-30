package com.github.fabioper.newsletter.domain.note;

import com.github.fabioper.newsletter.domain.edition.Edition;

public class Note {
    private NoteId id;
    private String title;
    private NoteContent content;
    private Edition edition;

    public Note() {
    }

    public Note(String title, NoteContent content, Edition edition) {
        this.title = title;
        this.content = content;
        this.edition = edition;
    }

    public NoteId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public NoteContent getContent() {
        return content;
    }

    public Edition getEditionId() {
        return edition;
    }

    public void updateTitle(String newTitle) {
        title = newTitle;
    }

    public void updateContent(NoteContent newContent) {
        content = newContent;
    }

    public void assignToEdition(Edition edition) {
        this.edition = edition;
    }
}
