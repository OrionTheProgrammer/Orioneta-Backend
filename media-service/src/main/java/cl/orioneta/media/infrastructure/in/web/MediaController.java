package cl.orioneta.media.infrastructure.in.web;

import cl.orioneta.media.application.dto.MediaResponseDTO;
import cl.orioneta.media.application.dto.MediaUploadRequestDTO;
import cl.orioneta.media.application.mapper.MediaMapper;
import cl.orioneta.media.application.storage.StoreMediaFileCommand;
import cl.orioneta.media.application.storage.StoredMediaContent;
import cl.orioneta.media.application.usecase.DownloadMediaUseCase;
import cl.orioneta.media.application.usecase.FindMediaUseCase;
import cl.orioneta.media.application.usecase.UploadMediaFileUseCase;
import cl.orioneta.media.application.usecase.UploadMediaUseCase;
import cl.orioneta.media.domain.exception.MediaStorageException;
import cl.orioneta.media.domain.model.MediaPurpose;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final UploadMediaUseCase uploadMediaUseCase;
    private final UploadMediaFileUseCase uploadMediaFileUseCase;
    private final DownloadMediaUseCase downloadMediaUseCase;
    private final FindMediaUseCase findMediaUseCase;
    private final MediaMapper mediaMapper;

    public MediaController(
            UploadMediaUseCase uploadMediaUseCase,
            UploadMediaFileUseCase uploadMediaFileUseCase,
            DownloadMediaUseCase downloadMediaUseCase,
            FindMediaUseCase findMediaUseCase,
            MediaMapper mediaMapper
    ) {
        this.uploadMediaUseCase = uploadMediaUseCase;
        this.uploadMediaFileUseCase = uploadMediaFileUseCase;
        this.downloadMediaUseCase = downloadMediaUseCase;
        this.findMediaUseCase = findMediaUseCase;
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
            @RequestPart("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo es obligatorio");
        }

        UUID mediaId = UUID.randomUUID();

        try (InputStream content = file.getInputStream()) {
            StoreMediaFileCommand command = new StoreMediaFileCommand(
                    mediaId,
                    ownerUserId,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    purpose,
                    content
            );

            return mediaMapper.toResponse(uploadMediaFileUseCase.execute(command));
        } catch (IOException exception) {
            throw new MediaStorageException("No se pudo leer el archivo recibido", exception);
        }
    }

    @GetMapping("/{id}")
    public MediaResponseDTO findById(@PathVariable UUID id) {
        return mediaMapper.toResponse(findMediaUseCase.findById(id));
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<InputStreamResource> download(@PathVariable UUID id) {
        StoredMediaContent content = downloadMediaUseCase.execute(id);
        ResponseEntity.BodyBuilder response = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(content.contentType()));

        if (content.size() >= 0) {
            response.contentLength(content.size());
        }

        return response.body(new InputStreamResource(content.content()));
    }

    @GetMapping("/owners/{ownerUserId}")
    public List<MediaResponseDTO> findByOwner(@PathVariable UUID ownerUserId) {
        return findMediaUseCase.findByOwner(ownerUserId)
                .stream()
                .map(mediaMapper::toResponse)
                .toList();
    }
}
