package cl.orioneta.auth.app.service;

import cl.orioneta.auth.app.repository.AuthUserRepository;
import cl.orioneta.auth.app.repository.PasswordResetTokenRepository;
import cl.orioneta.auth.domain.exception.AuthUserNotFoundException;
import cl.orioneta.auth.infrastructure.config.PasswordResetProperties;
import cl.orioneta.auth.domain.exception.InvalidCredentialsException;
import cl.orioneta.auth.domain.model.PasswordResetToken;
import cl.orioneta.auth.infrastructure.email.EmailService;
import cl.orioneta.auth.app.security.PasswordHasher;
import java.security.SecureRandom;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final AuthUserRepository userRepository;
    private final EmailService emailService;
    private final PasswordHasher passwordHasher;
    private final PasswordResetProperties  properties;
    private final SecureRandom random = new SecureRandom();

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            AuthUserRepository userRepository,
            EmailService emailService,
            PasswordHasher passwordHasher,
            PasswordResetProperties  properties
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository  = userRepository;
        this.emailService    = emailService;
        this.passwordHasher  = passwordHasher;
        this.properties      = properties;
    }

    @Transactional
    public void requestReset(String email) {
        if (!userRepository.existsByEmail(email.toLowerCase())) {
            throw new AuthUserNotFoundException("El correo no está registrado");
        }
        tokenRepository.deleteAllByEmail(email);

        String code = generateCode();
        Instant expiresAt = Instant.now().plus(properties.codeTtl());

        tokenRepository.save(PasswordResetToken.create(email, code, expiresAt));
        emailService.sendPasswordResetCode(email, code);
    }

    @Transactional(readOnly = true)
    public void verifyCode(String email, String code) {
        PasswordResetToken token = tokenRepository
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new InvalidCredentialsException("Código inválido o expirado"));

        if (!token.isValid()) {
            throw new InvalidCredentialsException("Código inválido o expirado");
        }
    }

    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        PasswordResetToken token = tokenRepository
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new InvalidCredentialsException("Código inválido o expirado"));

        if (!token.isValid()) {
            throw new InvalidCredentialsException("Código inválido o expirado");
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado"));

        user.changePassword(passwordHasher.hash(newPassword));
        userRepository.save(user);

        token.markUsed();
        tokenRepository.save(token);
    }

    private String generateCode() {
        return String.format("%06d", random.nextInt(1_000_000));
    }
}