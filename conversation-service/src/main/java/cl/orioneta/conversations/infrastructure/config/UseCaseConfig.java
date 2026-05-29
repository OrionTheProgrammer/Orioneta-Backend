package cl.orioneta.conversations.infrastructure.config;

import cl.orioneta.conversations.application.usecase.*;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import cl.orioneta.conversations.domain.service.ConversationDomainService;
import cl.orioneta.conversations.domain.service.ConversationEventPublisherPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ConversationDomainService conversationDomainService(
            ConversationRepositoryPort conversationRepository) {
        return new ConversationDomainService(conversationRepository);
    }

    @Bean
    public CreatePrivateConversationUseCase createPrivateConversationUseCase(
            ConversationRepositoryPort conversationRepository,
            ConversationDomainService conversationDomainService,
            ConversationEventPublisherPort eventPublisher) {
        return new CreatePrivateConversationUseCase(
                conversationRepository, conversationDomainService, eventPublisher);
    }

    @Bean
    public CreateGroupConversationUseCase createGroupConversationUseCase(
            ConversationRepositoryPort conversationRepository) {
        return new CreateGroupConversationUseCase(conversationRepository);
    }

    @Bean
    public AddParticipantUseCase addParticipantUseCase(
            ConversationRepositoryPort conversationRepository,
            ConversationDomainService conversationDomainService,
            ConversationEventPublisherPort eventPublisher) {
        return new AddParticipantUseCase(
                conversationRepository, conversationDomainService, eventPublisher);
    }

    @Bean
    public FindUserConversationsUseCase findUserConversationsUseCase(
            ConversationRepositoryPort conversationRepository) {
        return new FindUserConversationsUseCase(conversationRepository);
    }
}