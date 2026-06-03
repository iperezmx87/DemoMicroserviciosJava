package org.isra.microservicios.cuentasmovimientos.modelo;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "cuentas_movimientos")
public class EventoBase {
    @Id
    private UUID eventId;

    @Field("AggregateId")
    private UUID aggregateId;

    @Field("Version")
    private Integer version;

    @Field("OcurridoEn")
    private Date ocurridoEn;

    @Field("TipoEvento")
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
