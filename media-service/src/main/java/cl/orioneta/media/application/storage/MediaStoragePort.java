package cl.orioneta.media.application.storage;

import cl.orioneta.media.domain.model.MediaFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * Puerto para guardar y recuperar archivos binarios.
 */
public interface MediaStoragePort {

    String buildPublicUrl(UUID mediaId);

    void store(UUID mediaId, String fileName, String contentType, long size, InputStream inputStream);

    MediaContent load(MediaFile mediaFile);
}
