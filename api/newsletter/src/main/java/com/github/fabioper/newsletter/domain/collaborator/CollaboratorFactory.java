package com.github.fabioper.newsletter.domain.collaborator;

import com.github.fabioper.newsletter.domain.author.Author;
import com.github.fabioper.newsletter.domain.editor.Editor;

import java.util.UUID;

public class CollaboratorFactory {
    public static Author authorFrom(String authorId) {
        return new Author(UUID.fromString(authorId));
    }

    public static Editor editorFrom(String editorId) {
        return new Editor(UUID.fromString(editorId));
    }
}
