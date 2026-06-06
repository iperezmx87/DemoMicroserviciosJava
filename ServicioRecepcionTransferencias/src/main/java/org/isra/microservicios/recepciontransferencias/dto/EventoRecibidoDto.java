package org.isra.microservicios.recepciontransferencias.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EventoRecibidoDto {
    @JsonProperty("EventId")
    private UUID eventId;

    @JsonProperty("Monto")
    private BigDecimal monto;

    @JsonProperty("TipoEvento")
    private String tipoEvento;

    @JsonProperty("AggregateId")
    private UUID aggregateId;

    @JsonProperty("Version")
    private int version;

    @JsonProperty("OcurridoEn")
    private String ocurridoEn;
}
