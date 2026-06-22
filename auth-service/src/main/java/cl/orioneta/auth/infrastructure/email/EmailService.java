package cl.orioneta.auth.infrastructure.email;

import cl.orioneta.auth.infrastructure.config.PasswordResetProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final PasswordResetProperties properties;

    public EmailService(JavaMailSender mailSender, PasswordResetProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    public void sendPasswordResetCode(String toEmail, String code) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(
                    properties.fromEmail(),
                    properties.fromName()
            );
            helper.setTo(toEmail);
            helper.setSubject("🔑 Código de recuperación - Orioneta");
            helper.setText(buildEmailHtml(code), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email de recuperación: " + e.getMessage(), e);
        }
    }

    private String buildEmailHtml(String code) {
        return """
            <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f9fafb; width: 100%%; table-layout: fixed;">
                <tr>
                    <td align="center" style="padding: 40px 20px;">
                        
                        <!-- Tarjeta Principal -->
                        <div style="max-width: 480px; width: 100%%; margin: 0 auto; background-color: #0d0e14; border-radius: 20px; overflow: hidden; box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3); border-top: 4px solid #7c3aed; text-align: left; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;">
                            
                            <!-- Header / Logo de la marca -->
                            <div style="padding: 32px 32px 16px 32px;">
                                <span style="color: #ffffff; font-size: 20px; font-weight: 800; letter-spacing: 1.5px;">ORIONETA</span>
                            </div>
                            
                            <!-- Contenido Principal -->
                            <div style="padding: 0 32px 32px 32px;">
                                <h2 style="color: #a78bfa; margin-top: 0; margin-bottom: 12px; font-size: 24px; font-weight: 700; letter-spacing: -0.5px;">Recupera tu cuenta</h2>
                                <p style="color: #9ca3af; font-size: 14px; margin-bottom: 24px; line-height: 1.6;">
                                    Has solicitado restablecer tu contraseña de Orioneta. Utiliza el siguiente código de seguridad de un solo uso para continuar:
                                </p>
                                
                                <!-- Contenedor del Código Destacado -->
                                <div style="background-color: #161722; border: 1px solid #2e2f44; border-radius: 14px; padding: 24px; text-align: center; margin: 28px 0; box-shadow: inset 0 2px 4px rgba(0,0,0,0.2);">
                                    <span style="color: #a78bfa; font-family: 'Courier New', Courier, monospace; font-size: 36px; font-weight: 800; letter-spacing: 8px; padding-left: 8px; display: inline-block;">%s</span>
                                </div>
                                
                                <!-- Alertas / Seguridad -->
                                <p style="color: #6b7280; font-size: 12px; line-height: 1.6; margin-bottom: 24px; background-color: #13141c; padding: 12px; border-radius: 8px; border-left: 2px solid #4b5563;">
                                    ⏱️ Este código <strong>expira en 15 minutos</strong>. Si tú no realizaste esta solicitud, puedes ignorar este correo de forma segura; tu cuenta sigue protegida.
                                </p>
                            </div>
                            
                            <!-- Footer del Correo -->
                            <div style="background-color: #090a0f; padding: 20px 32px; text-align: center; border-top: 1px solid #1f202e;">
                                <p style="color: #4b5563; font-size: 11px; margin: 0; letter-spacing: 0.5px;">&copy; 2026 Orioneta. Todos los derechos reservados.</p>
                            </div>
                            
                        </div>
                        
                    </td>
                </tr>
            </table>
            """.formatted(code);
    }
}