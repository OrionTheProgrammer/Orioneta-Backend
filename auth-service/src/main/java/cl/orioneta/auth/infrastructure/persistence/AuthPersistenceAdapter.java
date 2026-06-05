package cl.orioneta.auth.infrastructure.persistence;

import cl.orioneta.auth.app.repository.AuthUserRepository;
import cl.orioneta.auth.app.repository.RefreshTokenRepository;
import cl.orioneta.auth.domain.model.AuthProvider;
import cl.orioneta.auth.domain.model.AuthUser;
import cl.orioneta.auth.domain.model.RefreshToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Adaptador JPA para los puertos de auth-service.
 */
@Repository
public class AuthPersistenceAdapter implements AuthUserRepository, RefreshTokenRepository {

    private final JpaAuthUserRepository authUserRepository;
    private final JpaRefreshTokenRepository refreshTokenRepository;

    public AuthPersistenceAdapter(
            JpaAuthUserRepository authUserRepository,
            JpaRefreshTokenRepository refreshTokenRepository
    ) {
        this.authUserRepository = authUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AuthUser save(AuthUser user) {
        return toDomain(authUserRepository.save(toEntity(user)));
    }

    @Override
    public Optional<AuthUser> findById(UUID id) {
        return authUserRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return authUserRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<AuthUser> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId) {
        return authUserRepository.findByProviderAndProviderUserId(provider, providerUserId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return authUserRepository.existsByEmail(email);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return toDomain(refreshTokenRepository.save(toEntity(refreshToken)));
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenRepository.findByTokenHash(tokenHash)
                .map(this::toDomain);
    }

    private AuthUserEntity toEntity(AuthUser user) {
        return new AuthUserEntity(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getDisplayName(),
                user.getAvatarUrl(),
                user.getProvider(),
                user.getProviderUserId(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private AuthUser toDomain(AuthUserEntity entity) {
        return AuthUser.rehydrate(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getDisplayName(),
                entity.getAvatarUrl(),
                entity.getProvider(),
                entity.getProviderUserId(),
                entity.getRole(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        return new RefreshTokenEntity(
                refreshToken.getId(),
                refreshToken.getAuthUserId(),
                refreshToken.getTokenHash(),
                refreshToken.getCreatedAt(),
                refreshToken.getExpiresAt(),
                refreshToken.getRevokedAt()
        );
    }

    private RefreshToken toDomain(RefreshTokenEntity entity) {
        return RefreshToken.rehydrate(
                entity.getId(),
                entity.getAuthUserId(),
                entity.getTokenHash(),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getRevokedAt()
        );
    }
}
