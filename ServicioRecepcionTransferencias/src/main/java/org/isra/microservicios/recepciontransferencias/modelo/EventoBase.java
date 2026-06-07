package org.isra.microservicios.recepciontransferencias.modelo;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "cuentas_movimientos")
public class EventoBase {
 @Id
    @JsonProperty("EventId")
    private UUID eventId;

    @Field("AggregateId")
    @JsonProperty("AggregateId")
    private UUID aggregateId;

    @Field("Version")
    @JsonProperty("Version")
    private Integer version;

    @Field("OcurridoEn")
    @JsonProperty("OcurridoEn")
    private Date ocurridoEn;

    @Field("TipoEvento")
    @JsonProperty("TipoEvento")
    private String tipoEvento;
    
    public EventoBase() {
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getOcurridoEn() {
        return ocurridoEn;
    }

    public void setOcurridoEn(Date ocurridoEn) {
        this.ocurridoEn = ocurridoEn;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
}
