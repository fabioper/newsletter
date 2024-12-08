package com.github.fabioper.newsletter.domain.collaborator;

import com.github.fabioper.newsletter.domain.author.Author;
import com.github.fabioper.newsletter.domain.editor.Editor;

public interface CollaboratorService {
    Author getAuthorFromActiveUser();

    Editor getEditorFromActiveUser();
}
