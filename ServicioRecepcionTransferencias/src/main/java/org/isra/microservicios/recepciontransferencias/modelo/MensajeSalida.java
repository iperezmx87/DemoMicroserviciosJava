package org.isra.microservicios.recepciontransferencias.modelo;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "cuentas_movimientos_outbox")
public class MensajeSalida {
    @Id
    private UUID id;

    @Field("Topic")
    private String topic;

    @Field("Payload")
    private String payload;

    @Field("OccurredOn")
    private Date occurredOn;

    @Field("Processed")
    private boolean processed;

    public MensajeSalida() {
        this.occurredOn = new Date();
        this.processed = false;
    }
}
