package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.application.factories.UserRoleFactory;
import com.github.fabioper.newsletter.domain.category.CategoriesRepository;
import com.github.fabioper.newsletter.infra.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final AuthService auth;

    public CategoriesService(
        CategoriesRepository categoriesRepository,
        AuthService authService
    ) {
        this.categoriesRepository = categoriesRepository;
        this.auth = authService;
    }

    public String sayHelloToUser() {
        var author = UserRoleFactory.editorFrom(auth.currentUser());
        return "Hello, user <" + author.getId() + ">";
    }
}
