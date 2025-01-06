package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.application.dto.CreateEditionRequest;
import com.github.fabioper.newsletter.application.dto.EditionResponse;
import com.github.fabioper.newsletter.application.dto.request.AssignNoteRequest;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.EditionId;
import com.github.fabioper.newsletter.domain.edition.EditionRepository;
import com.github.fabioper.newsletter.domain.edition.EditorId;
import com.github.fabioper.newsletter.domain.note.NoteId;
import com.github.fabioper.newsletter.domain.note.NoteRepository;
import com.github.fabioper.newsletter.domain.review.ReviewApprovedEvent;
import com.github.fabioper.newsletter.domain.review.ReviewDeniedEvent;
import com.github.fabioper.newsletter.domain.review.ReviewRepository;
import com.github.fabioper.newsletter.domain.review.ReviewStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditionService {
    private final EditionRepository editionRepository;
    private final NoteRepository noteRepository;
    private final ReviewRepository reviewRepository;

    public EditionService(
        EditionRepository editionRepository, NoteRepository noteRepository,
        ReviewRepository reviewRepository
    ) {
        this.editionRepository = editionRepository;
        this.noteRepository = noteRepository;
        this.reviewRepository = reviewRepository;
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
        return new EditionResponse(
            entity.getId().getValue(),
            entity.getTitle(),
            entity.getStatus().name(),
            entity.getEditorId().getValue()
        );
    }

    public EditionResponse assignNote(String editionId, AssignNoteRequest request) {
        var edition = this.editionRepository.findById(new EditionId(editionId))
            .orElseThrow();

        var noteToBeAssigned = this.noteRepository.findById(new NoteId(request.noteId()))
            .orElseThrow();

        edition.assign(noteToBeAssigned);

        return mapToResponse(this.editionRepository.save(edition));
    }

    public void unassignNote(String editionId, String noteId) {
        var edition = this.editionRepository.findById(new EditionId(editionId))
            .orElseThrow();

        var noteToBeUnassigned = this.noteRepository.findById(new NoteId(noteId))
            .orElseThrow();

        edition.unassign(noteToBeUnassigned);

        this.editionRepository.save(edition);
    }

    public void submitEditionToReview(String editionId) {
        var edition = this.editionRepository.findById(new EditionId(editionId))
            .orElseThrow();

        edition.submitToReview();

        this.editionRepository.save(edition);
    }

    @EventListener
    public void handleReviewStartedEvent(ReviewStartedEvent reviewStartedEvent) {
        var review = this.reviewRepository.findById(reviewStartedEvent.getReviewId())
            .orElseThrow();

        var edition = this.editionRepository.findById(review.getEditionId())
            .orElseThrow();

        edition.putUnderReview();

        this.editionRepository.save(edition);
    }

    @EventListener
    public void handleReviewApprovalEvent(ReviewApprovedEvent reviewApprovedEvent) {
        var review = this.reviewRepository.findById(reviewApprovedEvent.getReviewId())
            .orElseThrow();

        var edition = this.editionRepository.findById(review.getEditionId())
            .orElseThrow();

        edition.makeAvailableForPublication();

        this.editionRepository.save(edition);
    }

    @EventListener
    public void handleReviewDenyEvent(ReviewDeniedEvent reviewDeniedEvent) {
        var review = this.reviewRepository.findById(reviewDeniedEvent.getReviewId())
            .orElseThrow();

        var edition = this.editionRepository.findById(review.getEditionId())
            .orElseThrow();

        edition.reopen();

        this.editionRepository.save(edition);
    }
}
