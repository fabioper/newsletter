package com.github.fabioper.newsletter.infra.services.identity;

import com.github.fabioper.newsletter.domain.author.Author;
import com.github.fabioper.newsletter.domain.collaborator.CollaboratorService;
import com.github.fabioper.newsletter.domain.editor.Editor;
import org.springframework.stereotype.Service;

@Service
public class UserRoleToCollaboratorService implements CollaboratorService {
    private static final String AUTHOR_ROLE = "author";
    private static final String EDITOR_ROLE = "editor";

    private final IdentityService auth;

    public UserRoleToCollaboratorService(IdentityService identityService) {
        this.auth = identityService;
    }

    @Override
    public Author getAuthorFromActiveUser() {
        var currentUser = auth.getActiveUser();

        if (!currentUser.hasRole(AUTHOR_ROLE)) {
            throw new IllegalStateException("User " + currentUser.id() + " is not an author");
        }

        return new Author(currentUser.id());
    }

    @Override
    public Editor getEditorFromActiveUser() {
        var currentUser = auth.getActiveUser();

        if (!currentUser.hasRole(EDITOR_ROLE)) {
            throw new IllegalStateException("User " + currentUser.id() + " is not an editor");
        }

        return new Editor(currentUser.id());
    }
}
