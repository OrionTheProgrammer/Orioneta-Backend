package cl.orioneta.notifications.infrastructure.in.web;

import cl.orioneta.notifications.application.dto.NotificationRequestDTO;
import cl.orioneta.notifications.application.dto.NotificationResponseDTO;
import cl.orioneta.notifications.application.mapper.NotificationMapper;
import cl.orioneta.notifications.application.usecase.CreateNotificationUseCase;
import cl.orioneta.notifications.application.usecase.FindNotificationsUseCase;
import cl.orioneta.notifications.application.usecase.MarkNotificationAsReadUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final FindNotificationsUseCase findNotificationsUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
    private final NotificationMapper notificationMapper;

    public NotificationController(
            CreateNotificationUseCase createNotificationUseCase,
            FindNotificationsUseCase findNotificationsUseCase,
            MarkNotificationAsReadUseCase markNotificationAsReadUseCase,
            NotificationMapper notificationMapper
    ) {
        this.createNotificationUseCase = createNotificationUseCase;
        this.findNotificationsUseCase = findNotificationsUseCase;
        this.markNotificationAsReadUseCase = markNotificationAsReadUseCase;
        this.notificationMapper = notificationMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponseDTO create(@Valid @RequestBody NotificationRequestDTO request) {
        return notificationMapper.toResponse(createNotificationUseCase.execute(request));
    }

    @GetMapping("/users/{userId}")
    public List<NotificationResponseDTO> findByUser(@PathVariable UUID userId) {
        return findNotificationsUseCase.execute(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{id}/read")
    public NotificationResponseDTO markAsRead(@PathVariable UUID id) {
        return notificationMapper.toResponse(markNotificationAsReadUseCase.execute(id));
    }
}
