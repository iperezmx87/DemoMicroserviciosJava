package org.isra.microservicios.saldo.modelo;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EventoRecibidoDto {
    @JsonProperty("Monto")
    private BigDecimal monto;

    @JsonProperty("TipoEvento")
    private String tipoEvento;

    @JsonProperty("AggregateId")
    private UUID aggregateId;

    @JsonProperty("Version")
    private int version;
}
