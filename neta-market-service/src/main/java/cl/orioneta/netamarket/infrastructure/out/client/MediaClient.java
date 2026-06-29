package cl.orioneta.netamarket.infrastructure.out.client;

import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cliente interno hacia media-service.
 *
 * <p>Neta Market no guarda binarios. Solo registra metadata del template y
 * delega el archivo real a media-service, que a su vez lo persiste en MinIO.
 * Esto mantiene separadas las responsabilidades: mercado visual por un lado,
 * almacenamiento por otro.</p>
 */
@Component
public class MediaClient {

    private final RestClient restClient;

    public MediaClient(
            RestClient.Builder restClientBuilder,
            @Value("${orioneta.services.media:${ORIONETA_MEDIA_URL:http://localhost:8089}}") String mediaServiceUrl
    ) {
        this.restClient = restClientBuilder.baseUrl(mediaServiceUrl).build();
    }

    /**
     * Sube el archivo instalable del template.
     *
     * @param ownerUserId usuario que publica el template.
     * @param file archivo del tema, fondo, fuente o paquete visual.
     * @param type tipo de template que se publica.
     * @return metadata publica devuelta por media-service.
     * @throws IOException si no se puede leer el archivo recibido.
     */
    public UploadedMediaResponse uploadTemplateFile(UUID ownerUserId, MultipartFile file, NetaTemplateType type) throws IOException {
        return upload(ownerUserId, resolveTemplatePurpose(type), file);
    }

    /**
     * Sube una imagen opcional para previsualizar el template en Neta Market.
     *
     * @param ownerUserId usuario que publica el template.
     * @param preview imagen de preview.
     * @return metadata publica devuelta por media-service.
     * @throws IOException si no se puede leer la imagen recibida.
     */
    public UploadedMediaResponse uploadTemplatePreview(UUID ownerUserId, MultipartFile preview) throws IOException {
        return upload(ownerUserId, "TEMPLATE_PREVIEW", preview);
    }

    private UploadedMediaResponse upload(UUID ownerUserId, String purpose, MultipartFile file) throws IOException {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("ownerUserId", ownerUserId.toString());
        formData.add("purpose", purpose);
        formData.add("file", new MultipartFileResource(file));

        return restClient.post()
                .uri("/api/media/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(formData)
                .retrieve()
                .body(UploadedMediaResponse.class);
    }

    private String resolveTemplatePurpose(NetaTemplateType type) {
        return switch (type) {
            case BACKGROUND -> "BACKGROUND";
            case SOUND_PACK -> "SOUND";
            case FONT -> "FONT";
            default -> "TEMPLATE_FILE";
        };
    }

    public record UploadedMediaResponse(
            UUID id,
            UUID ownerUserId,
            String fileName,
            String contentType,
            long size,
            String url,
            String purpose,
            LocalDateTime createdAt,
            LocalDateTime deletedAt
    ) {
    }

    private static final class MultipartFileResource extends ByteArrayResource {

        private final String filename;

        private MultipartFileResource(MultipartFile file) throws IOException {
            super(file.getBytes());
            this.filename = resolveFilename(file);
        }

        @Override
        public String getFilename() {
            return filename;
        }

        private static String resolveFilename(MultipartFile file) {
            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null || originalFilename.isBlank()) {
                return "template-file";
            }

            return originalFilename;
        }
    }
}
