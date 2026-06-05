package cl.orioneta.notifications.infrastructure.out.persistence;

import cl.orioneta.notifications.domain.model.Notification;
import cl.orioneta.notifications.domain.repository.NotificationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationPersistenceAdapter implements NotificationRepositoryPort {

    private final JpaNotificationRepository jpaNotificationRepository;

    public NotificationPersistenceAdapter(JpaNotificationRepository jpaNotificationRepository) {
        this.jpaNotificationRepository = jpaNotificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return toDomain(jpaNotificationRepository.save(toEntity(notification)));
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaNotificationRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Notification> findByUserId(UUID userId) {
        return jpaNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private NotificationEntity toEntity(Notification notification) {
        return new NotificationEntity(
                notification.getId(),
                notification.getUserId(),
                notification.getType(),
                notification.getTitle(),
                notification.getBody(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }

    private Notification toDomain(NotificationEntity entity) {
        return Notification.rehydrate(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getTitle(),
                entity.getBody(),
                entity.isRead(),
                entity.getCreatedAt(),
                entity.getReadAt()
        );
    }
}
