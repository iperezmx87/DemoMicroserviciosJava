package org.isra.microservicios.cuentasmovimientos.modelo;

import java.util.UUID;

import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class TransferenciaRecibidaEvento extends EventoBase {
    @Field("Monto")
    private Decimal128 monto;

    private String _t = "TransferenciaRecibidaEvento";

    @Field("CuentaDestinoId")
    private UUID cuentaDestinoId;

    public TransferenciaRecibidaEvento(UUID id, Decimal128 monto, Integer version, UUID cuentaDestinoId) {
        super();
        this.setEventId(UUID.randomUUID());
        this.monto = monto;
        this.setOcurridoEn(new java.util.Date());
        this.setTipoEvento("TransferenciaRecibidaEvento");
        this.setVersion(version);
        this.setAggregateId(id);
        this.cuentaDestinoId = cuentaDestinoId;
        this._t = "TransferenciaRecibidaEvento";
    }

    public TransferenciaRecibidaEvento() {
    }

}
