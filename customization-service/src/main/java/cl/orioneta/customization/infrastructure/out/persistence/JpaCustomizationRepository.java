package cl.orioneta.customization.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCustomizationRepository extends JpaRepository<UserCustomizationEntity, UUID> {

    Optional<UserCustomizationEntity> findByUserId(UUID userId);
}
