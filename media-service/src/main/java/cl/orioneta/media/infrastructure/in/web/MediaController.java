package cl.orioneta.media.infrastructure.in.web;

import cl.orioneta.media.application.dto.MediaResponseDTO;
import cl.orioneta.media.application.dto.MediaUploadRequestDTO;
import cl.orioneta.media.application.mapper.MediaMapper;
import cl.orioneta.media.application.usecase.FindMediaUseCase;
import cl.orioneta.media.application.usecase.UploadMediaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final UploadMediaUseCase uploadMediaUseCase;
    private final FindMediaUseCase findMediaUseCase;
    private final MediaMapper mediaMapper;

    public MediaController(UploadMediaUseCase uploadMediaUseCase, FindMediaUseCase findMediaUseCase, MediaMapper mediaMapper) {
        this.uploadMediaUseCase = uploadMediaUseCase;
        this.findMediaUseCase = findMediaUseCase;
        this.mediaMapper = mediaMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MediaResponseDTO register(@Valid @RequestBody MediaUploadRequestDTO request) {
        return mediaMapper.toResponse(uploadMediaUseCase.execute(request));
    }

    @GetMapping("/{id}")
    public MediaResponseDTO findById(@PathVariable UUID id) {
        return mediaMapper.toResponse(findMediaUseCase.findById(id));
    }

    @GetMapping("/owners/{ownerUserId}")
    public List<MediaResponseDTO> findByOwner(@PathVariable UUID ownerUserId) {
        return findMediaUseCase.findByOwner(ownerUserId)
                .stream()
                .map(mediaMapper::toResponse)
                .toList();
    }
}
