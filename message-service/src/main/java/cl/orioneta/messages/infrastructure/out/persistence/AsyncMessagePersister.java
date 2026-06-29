package cl.orioneta.messages.infrastructure.out.persistence;

import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncMessagePersister {

    private static final Logger log = LoggerFactory.getLogger(AsyncMessagePersister.class);

    private final MessageRepositoryPort messageRepositoryPort;

    public AsyncMessagePersister(MessageRepositoryPort messageRepositoryPort) {
        this.messageRepositoryPort = messageRepositoryPort;
    }

    @Async("messagePersistenceExecutor")
    public void persistAsync(Message message) {
        messageRepositoryPort.save(message);
        log.debug("Message {} persisted asynchronously", message.getId());
    }
}
