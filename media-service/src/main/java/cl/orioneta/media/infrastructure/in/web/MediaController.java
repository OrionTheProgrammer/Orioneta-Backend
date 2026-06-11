package cl.orioneta.media.infrastructure.in.web;

import cl.orioneta.media.application.dto.MediaResponseDTO;
import cl.orioneta.media.application.dto.MediaUploadRequestDTO;
import cl.orioneta.media.application.dto.UploadMediaFileCommand;
import cl.orioneta.media.application.mapper.MediaMapper;
import cl.orioneta.media.application.storage.MediaContent;
import cl.orioneta.media.application.usecase.FindMediaUseCase;
import cl.orioneta.media.application.usecase.LoadMediaContentUseCase;
import cl.orioneta.media.application.usecase.UploadMediaUseCase;
import cl.orioneta.media.domain.model.MediaPurpose;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final UploadMediaUseCase uploadMediaUseCase;
    private final FindMediaUseCase findMediaUseCase;
    private final LoadMediaContentUseCase loadMediaContentUseCase;
    private final MediaMapper mediaMapper;

    public MediaController(
            UploadMediaUseCase uploadMediaUseCase,
            FindMediaUseCase findMediaUseCase,
            LoadMediaContentUseCase loadMediaContentUseCase,
            MediaMapper mediaMapper
    ) {
        this.uploadMediaUseCase = uploadMediaUseCase;
        this.findMediaUseCase = findMediaUseCase;
        this.loadMediaContentUseCase = loadMediaContentUseCase;
        this.mediaMapper = mediaMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MediaResponseDTO register(@Valid @RequestBody MediaUploadRequestDTO request) {
        return mediaMapper.toResponse(uploadMediaUseCase.execute(request));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MediaResponseDTO upload(
            @RequestParam UUID ownerUserId,
            @RequestParam MediaPurpose purpose,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return mediaMapper.toResponse(uploadMediaUseCase.upload(new UploadMediaFileCommand(
                ownerUserId,
                resolveFileName(file),
                file.getContentType(),
                file.getSize(),
                purpose,
                file.getInputStream()
        )));
    }

    @GetMapping("/{id}")
    public MediaResponseDTO findById(@PathVariable UUID id) {
        return mediaMapper.toResponse(findMediaUseCase.findById(id));
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<InputStreamResource> downloadContent(@PathVariable UUID id) {
        MediaContent content = loadMediaContentUseCase.execute(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(content.contentType()))
                .contentLength(content.size())
                .cacheControl(CacheControl.maxAge(Duration.ofDays(7)).cachePublic())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(new InputStreamResource(content.inputStream()));
    }

    @GetMapping("/owners/{ownerUserId}")
    public List<MediaResponseDTO> findByOwner(@PathVariable UUID ownerUserId) {
        return findMediaUseCase.findByOwner(ownerUserId)
                .stream()
                .map(mediaMapper::toResponse)
                .toList();
    }

    private String resolveFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            return "archivo";
        }

        return originalFilename;
    }
}
