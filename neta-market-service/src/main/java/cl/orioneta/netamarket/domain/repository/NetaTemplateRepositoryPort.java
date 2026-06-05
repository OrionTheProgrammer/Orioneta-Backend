package cl.orioneta.netamarket.domain.repository;

import cl.orioneta.netamarket.domain.model.NetaTemplate;
import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NetaTemplateRepositoryPort {

    NetaTemplate save(NetaTemplate template);

    Optional<NetaTemplate> findById(UUID id);

    List<NetaTemplate> search(String text, NetaTemplateType type, NetaTemplateStatus status);

    List<NetaTemplate> findFeatured();
}
