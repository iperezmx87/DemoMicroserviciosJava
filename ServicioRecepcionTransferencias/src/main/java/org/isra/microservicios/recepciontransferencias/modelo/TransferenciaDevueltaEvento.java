package org.isra.microservicios.recepciontransferencias.modelo;

import java.util.UUID;

import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class TransferenciaDevueltaEvento extends EventoBase {
    @Field("Monto")
    private Decimal128 monto;

    private String _t = "TransferenciaDevueltaEvento";

    @Field("CuentaOrigenId")
    private UUID cuentaOrigenId;

    @Field("IdTransferenciaOrigen")
    private UUID idTransferenciaOrigen;

    @Field("MotivoDevolucion")
    private String motivoDevolucion;

    public TransferenciaDevueltaEvento(UUID id, UUID idTransferencia, Decimal128 monto, UUID cuentaOrigenId,
            int version) {
        super();
        this.setEventId(UUID.randomUUID());
        this.monto = monto;
        this.setIdTransferenciaOrigen(idTransferenciaOrigen);
        this.setCuentaOrigenId(cuentaOrigenId);
        this.setAggregateId(id);
        this.setVersion(version);
        this._t = "TransferenciaDevueltaEvento";
    }

    public TransferenciaDevueltaEvento() {
    }
}
