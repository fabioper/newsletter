package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.application.dto.CreateEditionRequest;
import com.github.fabioper.newsletter.application.dto.EditionResponse;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.EditionRepository;
import com.github.fabioper.newsletter.domain.edition.EditorId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditionService {
    private final EditionRepository editionRepository;

    public EditionService(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public List<EditionResponse> getAllEditions() {
        return this.editionRepository.findAll().stream()
            .map(EditionService::mapToResponse)
            .toList();
    }

    public EditionResponse createEdition(CreateEditionRequest dto, String userId) {
        var newEdition = new Edition(dto.title(), new EditorId(userId));
        return mapToResponse(editionRepository.save(newEdition));
    }

    private static EditionResponse mapToResponse(Edition entity) {
        return new EditionResponse(entity.getId().getValue(), entity.getTitle());
    }
}
