package cl.orioneta.netamarket.infrastructure.in.web;

import cl.orioneta.netamarket.application.dto.NetaTemplateRequestDTO;
import cl.orioneta.netamarket.application.dto.NetaTemplateResponseDTO;
import cl.orioneta.netamarket.application.mapper.NetaTemplateMapper;
import cl.orioneta.netamarket.application.usecase.DownloadTemplateUseCase;
import cl.orioneta.netamarket.application.usecase.PublishTemplateUseCase;
import cl.orioneta.netamarket.application.usecase.SearchTemplatesUseCase;
import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import cl.orioneta.netamarket.infrastructure.out.client.MediaClient;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/neta-market")
public class NetaMarketController {

    private final PublishTemplateUseCase publishTemplateUseCase;
    private final SearchTemplatesUseCase searchTemplatesUseCase;
    private final DownloadTemplateUseCase downloadTemplateUseCase;
    private final NetaTemplateMapper mapper;
    private final MediaClient mediaClient;

    public NetaMarketController(
            PublishTemplateUseCase publishTemplateUseCase,
            SearchTemplatesUseCase searchTemplatesUseCase,
            DownloadTemplateUseCase downloadTemplateUseCase,
            NetaTemplateMapper mapper,
            MediaClient mediaClient
    ) {
        this.publishTemplateUseCase = publishTemplateUseCase;
        this.searchTemplatesUseCase = searchTemplatesUseCase;
        this.downloadTemplateUseCase = downloadTemplateUseCase;
        this.mapper = mapper;
        this.mediaClient = mediaClient;
    }

    @PostMapping(value = "/templates", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public NetaTemplateResponseDTO publish(@Valid @RequestBody NetaTemplateRequestDTO request) {
        return mapper.toResponse(publishTemplateUseCase.execute(request));
    }

    /**
     * Publica un template subiendo los archivos reales a media-service/MinIO.
     *
     * <p>Este endpoint es el flujo que usa el frontend cuando el usuario crea
     * un tema desde el studio: Neta Market recibe metadata y archivos,
     * media-service guarda los binarios, y finalmente se registra el template
     * con las URLs publicas devueltas por media-service.</p>
     */
    @PostMapping(value = "/templates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public NetaTemplateResponseDTO publishWithFiles(
            @RequestParam UUID authorUserId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam NetaTemplateType type,
            @RequestParam(required = false) String version,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "preview", required = false) MultipartFile preview
    ) throws IOException {
        requireFile(file, "El archivo del template es obligatorio");

        MediaClient.UploadedMediaResponse uploadedFile = mediaClient.uploadTemplateFile(authorUserId, file, type);
        String previewImageUrl = "";

        if (preview != null && !preview.isEmpty()) {
            MediaClient.UploadedMediaResponse uploadedPreview = mediaClient.uploadTemplatePreview(authorUserId, preview);
            previewImageUrl = uploadedPreview.url();
        }

        NetaTemplateRequestDTO request = new NetaTemplateRequestDTO(
                authorUserId,
                name,
                description,
                type,
                previewImageUrl,
                requireUrl(uploadedFile),
                version
        );

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

    private void requireFile(MultipartFile file, String message) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private String requireUrl(MediaClient.UploadedMediaResponse uploadedFile) {
        if (uploadedFile == null || uploadedFile.url() == null || uploadedFile.url().isBlank()) {
            throw new IllegalStateException("media-service no devolvio una URL valida para el template");
        }

        return uploadedFile.url();
    }
}
