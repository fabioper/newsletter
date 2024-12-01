package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.domain.category.CategoriesRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }
}
