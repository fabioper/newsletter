package com.github.fabioper.newsletter.api;

import com.github.fabioper.newsletter.application.dto.CreateEditionRequest;
import com.github.fabioper.newsletter.application.dto.EditionResponse;
import com.github.fabioper.newsletter.application.services.EditionService;
import com.github.fabioper.newsletter.infra.security.annotations.EditorOnly;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editions")
public class EditionController {
    private final EditionService editionService;

    public EditionController(EditionService editionService) {
        this.editionService = editionService;
    }

    @EditorOnly
    @GetMapping
    public List<EditionResponse> listEditions() {
        return this.editionService.getAllEditions();
    }

    @EditorOnly
    @PostMapping
    public EditionResponse createEdition(@RequestBody CreateEditionRequest dto, @AuthenticationPrincipal Jwt jwt) {
        return this.editionService.createEdition(dto, jwt.getSubject());
    }
}
