package cl.orioneta.netamarket.application.usecase;

import cl.orioneta.netamarket.domain.model.NetaTemplate;
import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import cl.orioneta.netamarket.domain.repository.NetaTemplateRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchTemplatesUseCase {

    private final NetaTemplateRepositoryPort repository;

    public SearchTemplatesUseCase(NetaTemplateRepositoryPort repository) {
        this.repository = repository;
    }

    public List<NetaTemplate> execute(String text, NetaTemplateType type, NetaTemplateStatus status) {
        return repository.search(text, type, status);
    }

    public List<NetaTemplate> featured() {
        return repository.findFeatured();
    }
}
