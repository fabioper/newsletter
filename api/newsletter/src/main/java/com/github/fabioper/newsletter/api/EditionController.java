package com.github.fabioper.newsletter.api;

import com.github.fabioper.newsletter.application.dto.CreateEditionRequest;
import com.github.fabioper.newsletter.application.dto.EditionResponse;
import com.github.fabioper.newsletter.application.services.EditionService;
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

    @GetMapping
    public List<EditionResponse> listEditions() {
        return this.editionService.getAllEditions();
    }

    @PostMapping
    public EditionResponse createEdition(@RequestBody CreateEditionRequest dto, Jwt jwt) {
        var loggedUserId = jwt.getSubject();
        return this.editionService.createEdition(dto, loggedUserId);
    }
}
