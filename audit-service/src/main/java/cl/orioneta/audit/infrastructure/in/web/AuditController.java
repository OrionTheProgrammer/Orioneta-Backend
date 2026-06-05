package cl.orioneta.audit.infrastructure.in.web;

import cl.orioneta.audit.application.dto.AuditEventRequestDTO;
import cl.orioneta.audit.application.dto.AuditEventResponseDTO;
import cl.orioneta.audit.application.mapper.AuditMapper;
import cl.orioneta.audit.application.usecase.FindAuditEventsUseCase;
import cl.orioneta.audit.application.usecase.RegisterAuditEventUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final RegisterAuditEventUseCase registerAuditEventUseCase;
    private final FindAuditEventsUseCase findAuditEventsUseCase;
    private final AuditMapper auditMapper;

    public AuditController(RegisterAuditEventUseCase registerAuditEventUseCase, FindAuditEventsUseCase findAuditEventsUseCase, AuditMapper auditMapper) {
        this.registerAuditEventUseCase = registerAuditEventUseCase;
        this.findAuditEventsUseCase = findAuditEventsUseCase;
        this.auditMapper = auditMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuditEventResponseDTO register(@Valid @RequestBody AuditEventRequestDTO request) {
        return auditMapper.toResponse(registerAuditEventUseCase.execute(request));
    }

    @GetMapping
    public List<AuditEventResponseDTO> findRecent() {
        return findAuditEventsUseCase.findRecent()
                .stream()
                .map(auditMapper::toResponse)
                .toList();
    }

    @GetMapping("/target")
    public List<AuditEventResponseDTO> findByTarget(@RequestParam String targetType, @RequestParam UUID targetId) {
        return findAuditEventsUseCase.findByTarget(targetType, targetId)
                .stream()
                .map(auditMapper::toResponse)
                .toList();
    }
}
