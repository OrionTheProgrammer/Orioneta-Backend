package cl.orioneta.netamarket.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de reglas puras de templates de Neta Market.
 */
class NetaTemplateTest {

    private final Faker faker = new Faker();

    @Test
    void publishCreatesPendingTemplateWithDefaultVersion() {
        NetaTemplate template = NetaTemplate.publish(
                UUID.randomUUID(),
                "  " + faker.lorem().word() + "  ",
                faker.lorem().sentence(),
                NetaTemplateType.CHAT_THEME,
                "https://media.orioneta.cl/preview.png",
                "https://media.orioneta.cl/template.zip",
                " "
        );

        assertThat(template.getName()).isEqualTo(template.getName().trim());
        assertThat(template.getStatus()).isEqualTo(NetaTemplateStatus.PENDING_REVIEW);
        assertThat(template.getVersion()).isEqualTo("1.0.0");
        assertThat(template.getDownloads()).isZero();
    }

    @Test
    void registerDownloadIncrementsCounter() {
        NetaTemplate template = NetaTemplate.publish(
                UUID.randomUUID(),
                faker.lorem().word(),
                faker.lorem().sentence(),
                NetaTemplateType.GLOBAL_THEME,
                "",
                "https://media.orioneta.cl/theme.zip",
                "1.0.0"
        );

        template.registerDownload();

        assertThat(template.getDownloads()).isEqualTo(1);
    }

    @Test
    void publishRejectsBlankFileUrl() {
        assertThatThrownBy(() -> NetaTemplate.publish(
                UUID.randomUUID(),
                "Tema",
                "Descripcion",
                NetaTemplateType.GLOBAL_THEME,
                "",
                " ",
                "1.0.0"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La URL del archivo es obligatoria");
    }
}
