package cl.orioneta.media.application.storage;

/**
 * Puerto de salida para almacenamiento de objetos.
 *
 * <p>La aplicacion conoce esta interfaz, pero no sabe si por debajo se usa
 * MinIO, S3 u otro proveedor. Esa decision queda en infraestructura.</p>
 */
public interface MediaStoragePort {

    StoredMediaObject store(StoreMediaFileCommand command);

    StoredMediaContent load(String storageKey);
}
