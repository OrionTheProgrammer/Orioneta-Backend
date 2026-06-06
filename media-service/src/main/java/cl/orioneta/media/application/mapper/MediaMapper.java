package cl.orioneta.media.application.mapper;

import cl.orioneta.media.application.dto.MediaResponseDTO;
import cl.orioneta.media.domain.model.MediaFile;
import org.springframework.stereotype.Component;

@Component
public class MediaMapper {

    public MediaResponseDTO toResponse(MediaFile mediaFile) {
        return new MediaResponseDTO(
                mediaFile.getId(),
                mediaFile.getOwnerUserId(),
                mediaFile.getFileName(),
                mediaFile.getContentType(),
                mediaFile.getSize(),
                mediaFile.getUrl(),
                mediaFile.getStorageKey(),
                mediaFile.getPurpose(),
                mediaFile.getCreatedAt(),
                mediaFile.getDeletedAt()
        );
    }
}
