package com.github.fabioper.newsletter.domain.common;

public class NoteNotFoundException extends IllegalArgumentException {
    public NoteNotFoundException() {
        super("Note is not assigned to this edition");
    }
}
