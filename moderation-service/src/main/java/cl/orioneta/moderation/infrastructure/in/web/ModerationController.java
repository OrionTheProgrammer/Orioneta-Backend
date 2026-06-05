package cl.orioneta.moderation.infrastructure.in.web;

import cl.orioneta.moderation.application.dto.ModerationReviewRequestDTO;
import cl.orioneta.moderation.application.dto.ModerationReviewResponseDTO;
import cl.orioneta.moderation.application.mapper.ModerationMapper;
import cl.orioneta.moderation.application.usecase.ReportContentUseCase;
import cl.orioneta.moderation.application.usecase.ReviewTemplateUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {

    private final ReportContentUseCase reportContentUseCase;
    private final ReviewTemplateUseCase reviewTemplateUseCase;
    private final ModerationMapper mapper;

    public ModerationController(ReportContentUseCase reportContentUseCase, ReviewTemplateUseCase reviewTemplateUseCase, ModerationMapper mapper) {
        this.reportContentUseCase = reportContentUseCase;
        this.reviewTemplateUseCase = reviewTemplateUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ModerationReviewResponseDTO create(@Valid @RequestBody ModerationReviewRequestDTO request) {
        return mapper.toResponse(reportContentUseCase.execute(request));
    }

    @GetMapping("/reviews/pending")
    public List<ModerationReviewResponseDTO> pending() {
        return reviewTemplateUseCase.findPending().stream().map(mapper::toResponse).toList();
    }

    @PatchMapping("/reviews/{id}")
    public ModerationReviewResponseDTO resolve(@PathVariable UUID id, @Valid @RequestBody ModerationReviewRequestDTO request) {
        return mapper.toResponse(reviewTemplateUseCase.resolve(id, request));
    }
}
