package cl.orioneta.netamarket.infrastructure.in.web;

import cl.orioneta.netamarket.application.dto.NetaTemplateRequestDTO;
import cl.orioneta.netamarket.application.dto.NetaTemplateResponseDTO;
import cl.orioneta.netamarket.application.mapper.NetaTemplateMapper;
import cl.orioneta.netamarket.application.usecase.DownloadTemplateUseCase;
import cl.orioneta.netamarket.application.usecase.PublishTemplateUseCase;
import cl.orioneta.netamarket.application.usecase.SearchTemplatesUseCase;
import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/neta-market")
public class NetaMarketController {

    private final PublishTemplateUseCase publishTemplateUseCase;
    private final SearchTemplatesUseCase searchTemplatesUseCase;
    private final DownloadTemplateUseCase downloadTemplateUseCase;
    private final NetaTemplateMapper mapper;

    public NetaMarketController(PublishTemplateUseCase publishTemplateUseCase, SearchTemplatesUseCase searchTemplatesUseCase, DownloadTemplateUseCase downloadTemplateUseCase, NetaTemplateMapper mapper) {
        this.publishTemplateUseCase = publishTemplateUseCase;
        this.searchTemplatesUseCase = searchTemplatesUseCase;
        this.downloadTemplateUseCase = downloadTemplateUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/templates")
    @ResponseStatus(HttpStatus.CREATED)
    public NetaTemplateResponseDTO publish(@Valid @RequestBody NetaTemplateRequestDTO request) {
        return mapper.toResponse(publishTemplateUseCase.execute(request));
    }

    @GetMapping("/templates")
    public List<NetaTemplateResponseDTO> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) NetaTemplateType type,
            @RequestParam(required = false) NetaTemplateStatus status
    ) {
        return searchTemplatesUseCase.execute(q, type, status)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/templates/featured")
    public List<NetaTemplateResponseDTO> featured() {
        return searchTemplatesUseCase.featured()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping("/templates/{id}/download")
    public NetaTemplateResponseDTO download(@PathVariable UUID id) {
        return mapper.toResponse(downloadTemplateUseCase.execute(id));
    }
}
