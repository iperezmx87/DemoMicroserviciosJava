package org.isra.microservicios.recepciontransferencias.dto;

import java.util.UUID;

import org.bson.types.Decimal128;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EventoRecibidoDto {
    @JsonProperty("EventId")
    private UUID eventId;

    @JsonProperty("Monto")
    private Decimal128 monto;

    @JsonProperty("TipoEvento")
    private String tipoEvento;

    @JsonProperty("AggregateId")
    private UUID aggregateId;

    @JsonProperty("Version")
    private int version;

    @JsonProperty("OcurridoEn")
    private String ocurridoEn;

    @JsonProperty("CuentaDestinoId")
    private UUID cuentaDestinoId;
}
