package org.isra.microservicios.recepciontransferencias.repositorio;

import java.util.List;
import java.util.UUID;

import org.isra.microservicios.recepciontransferencias.modelo.MensajeSalida;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MensajeSalidaRepositorioInterface extends MongoRepository<MensajeSalida, UUID> {
    List<MensajeSalida> findByProcessedFalseOrderByOccurredOnAsc();
}
