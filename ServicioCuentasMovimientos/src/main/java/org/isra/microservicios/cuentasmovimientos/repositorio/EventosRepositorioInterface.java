package org.isra.microservicios.cuentasmovimientos.repositorio;

import java.util.List;
import java.util.UUID;

import org.isra.microservicios.cuentasmovimientos.modelo.EventoBase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventosRepositorioInterface extends MongoRepository<EventoBase, UUID> {
    List<EventoBase> findByAggregateIdOrderByVersionAsc(UUID aggregateId);
}
