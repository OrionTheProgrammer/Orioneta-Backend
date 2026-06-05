package cl.orioneta.netamarket.application.usecase;

import cl.orioneta.netamarket.application.dto.NetaTemplateRequestDTO;
import cl.orioneta.netamarket.domain.model.NetaTemplate;
import cl.orioneta.netamarket.domain.repository.NetaTemplateRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class PublishTemplateUseCase {

    private final NetaTemplateRepositoryPort repository;

    public PublishTemplateUseCase(NetaTemplateRepositoryPort repository) {
        this.repository = repository;
    }

    public NetaTemplate execute(NetaTemplateRequestDTO request) {
        return repository.save(NetaTemplate.publish(
                request.authorUserId(),
                request.name(),
                request.description(),
                request.type(),
                request.previewImageUrl(),
                request.fileUrl(),
                request.version()
        ));
    }
}
