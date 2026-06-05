package cl.orioneta.netamarket.application.usecase;

import cl.orioneta.netamarket.domain.exception.NetaTemplateNotFoundException;
import cl.orioneta.netamarket.domain.model.NetaTemplate;
import cl.orioneta.netamarket.domain.repository.NetaTemplateRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DownloadTemplateUseCase {

    private final NetaTemplateRepositoryPort repository;

    public DownloadTemplateUseCase(NetaTemplateRepositoryPort repository) {
        this.repository = repository;
    }

    public NetaTemplate execute(UUID id) {
        NetaTemplate template = repository.findById(id)
                .orElseThrow(() -> new NetaTemplateNotFoundException("Template no encontrado"));

        template.registerDownload();
        return repository.save(template);
    }
}
