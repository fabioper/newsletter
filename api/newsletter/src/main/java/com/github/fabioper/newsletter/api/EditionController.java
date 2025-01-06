package com.github.fabioper.newsletter.api;

import com.github.fabioper.newsletter.application.dto.CreateEditionRequest;
import com.github.fabioper.newsletter.application.dto.EditionResponse;
import com.github.fabioper.newsletter.application.dto.request.AssignNoteRequest;
import com.github.fabioper.newsletter.application.services.EditionService;
import com.github.fabioper.newsletter.infra.security.annotations.IsAuthor;
import com.github.fabioper.newsletter.infra.security.annotations.IsEditor;
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

    @IsEditor
    @GetMapping
    public List<EditionResponse> listEditions() {
        return editionService.getAllEditions();
    }

    @IsEditor
    @PostMapping
    public EditionResponse createEdition(
        @RequestBody CreateEditionRequest dto,
        @AuthenticationPrincipal Jwt principal
    ) {
        return editionService.createEdition(dto, principal.getSubject());
    }

    @IsAuthor
    @PostMapping("{editionId}/notes")
    public EditionResponse assignNote(@PathVariable String editionId, @RequestBody AssignNoteRequest request) {
        return editionService.assignNote(editionId, request);
    }

    @IsAuthor
    @DeleteMapping("{editionId}/notes/{noteId}")
    public void unassignNote(@PathVariable String editionId, @PathVariable String noteId) {
        this.editionService.unassignNote(editionId, noteId);
    }

    @IsEditor
    @PostMapping("{editionId}/submit-to-review")
    public void submitToReview(@PathVariable String editionId) {
        this.editionService.submitEditionToReview(editionId);
    }
}
