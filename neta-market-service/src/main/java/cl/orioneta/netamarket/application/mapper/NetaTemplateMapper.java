package cl.orioneta.netamarket.application.mapper;

import cl.orioneta.netamarket.application.dto.NetaTemplateResponseDTO;
import cl.orioneta.netamarket.domain.model.NetaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NetaTemplateMapper {

    public NetaTemplateResponseDTO toResponse(NetaTemplate template) {
        return new NetaTemplateResponseDTO(
                template.getId(),
                template.getAuthorUserId(),
                template.getName(),
                template.getDescription(),
                template.getType(),
                template.getStatus(),
                template.getPreviewImageUrl(),
                template.getFileUrl(),
                template.getVersion(),
                template.getDownloads(),
                template.getRatingAverage(),
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }
}
