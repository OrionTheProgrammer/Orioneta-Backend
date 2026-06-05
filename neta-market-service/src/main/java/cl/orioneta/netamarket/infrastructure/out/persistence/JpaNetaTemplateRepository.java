package cl.orioneta.netamarket.infrastructure.out.persistence;

import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaNetaTemplateRepository extends JpaRepository<NetaTemplateEntity, UUID> {

    List<NetaTemplateEntity> findTop20ByStatusOrderByDownloadsDesc(NetaTemplateStatus status);

    List<NetaTemplateEntity> findByStatusAndTypeOrderByUpdatedAtDesc(NetaTemplateStatus status, NetaTemplateType type);

    List<NetaTemplateEntity> findByStatusAndNameContainingIgnoreCaseOrderByUpdatedAtDesc(NetaTemplateStatus status, String name);

    List<NetaTemplateEntity> findByStatusOrderByUpdatedAtDesc(NetaTemplateStatus status);
}
