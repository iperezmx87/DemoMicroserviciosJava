package org.isra.microservicios.recepciontransferencias.repositorio;

import java.util.List;
import java.util.UUID;

import org.isra.microservicios.recepciontransferencias.modelo.EventoBase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventosRepositorioInterface extends MongoRepository<EventoBase, UUID> {
    List<EventoBase> findByAggregateIdOrderByVersionAsc(UUID aggregateId);
}
