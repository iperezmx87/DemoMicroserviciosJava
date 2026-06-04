package org.isra.microservicios.cuentasmovimientos.modelo;

import java.util.Date;
import java.util.UUID;

import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.Field;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class DineroDepositadoEvento extends EventoBase {
    
    @Field("Monto")
    private Decimal128 monto;

    private String _t;

    public DineroDepositadoEvento(UUID id, Decimal128 monto, Integer version) {
        super();
        this.setEventId(UUID.randomUUID());
        this.monto = monto;
        this.setOcurridoEn(new Date());
        this.setTipoEvento("DineroDepositadoEvento");
        this.setAggregateId(id);
        this.setVersion(version);
        this._t = "DineroDepositadoEvento";
    }

    public DineroDepositadoEvento() {
    }
    
    public Decimal128 getMonto() {
        return monto;
    }

    public void setMonto(Decimal128 monto) {
        this.monto = monto;
    }
}
