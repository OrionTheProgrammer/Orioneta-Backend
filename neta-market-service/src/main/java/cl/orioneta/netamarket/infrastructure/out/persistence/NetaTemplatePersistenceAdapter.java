package cl.orioneta.netamarket.infrastructure.out.persistence;

import cl.orioneta.netamarket.domain.model.NetaTemplate;
import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import cl.orioneta.netamarket.domain.repository.NetaTemplateRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NetaTemplatePersistenceAdapter implements NetaTemplateRepositoryPort {

    private final JpaNetaTemplateRepository repository;

    public NetaTemplatePersistenceAdapter(JpaNetaTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public NetaTemplate save(NetaTemplate template) {
        return toDomain(repository.save(toEntity(template)));
    }

    @Override
    public Optional<NetaTemplate> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<NetaTemplate> search(String text, NetaTemplateType type, NetaTemplateStatus status) {
        NetaTemplateStatus effectiveStatus = status == null ? NetaTemplateStatus.APPROVED : status;
        List<NetaTemplateEntity> results;
        if (type != null) {
            results = repository.findByStatusAndTypeOrderByUpdatedAtDesc(effectiveStatus, type);
        } else if (text != null && !text.isBlank()) {
            results = repository.findByStatusAndNameContainingIgnoreCaseOrderByUpdatedAtDesc(effectiveStatus, text.trim());
        } else {
            results = repository.findByStatusOrderByUpdatedAtDesc(effectiveStatus);
        }
        return results.stream().map(this::toDomain).toList();
    }

    @Override
    public List<NetaTemplate> findFeatured() {
        return repository.findTop20ByStatusOrderByDownloadsDesc(NetaTemplateStatus.APPROVED)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private NetaTemplateEntity toEntity(NetaTemplate template) {
        return new NetaTemplateEntity(template.getId(), template.getAuthorUserId(), template.getName(), template.getDescription(), template.getType(), template.getStatus(), template.getPreviewImageUrl(), template.getFileUrl(), template.getVersion(), template.getDownloads(), template.getRatingAverage(), template.getCreatedAt(), template.getUpdatedAt());
    }

    private NetaTemplate toDomain(NetaTemplateEntity entity) {
        return NetaTemplate.rehydrate(entity.getId(), entity.getAuthorUserId(), entity.getName(), entity.getDescription(), entity.getType(), entity.getStatus(), entity.getPreviewImageUrl(), entity.getFileUrl(), entity.getVersion(), entity.getDownloads(), entity.getRatingAverage(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
