package org.isra.microservicios.cuentasmovimientos.config;

import java.util.UUID;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.isra.microservicios.cuentasmovimientos.modelo.DineroDepositadoEvento;
import org.isra.microservicios.cuentasmovimientos.modelo.DineroRetiradoEvento;
import org.isra.microservicios.cuentasmovimientos.modelo.EventoBase;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class EventoBdConverter implements Converter<Document, EventoBase> {

    @Override
    public EventoBase convert(Document source) {
        String tipoEvento = source.getString("TipoEvento");

        if ("DineroDepositadoEvento".equals(tipoEvento)) {
            DineroDepositadoEvento evento = new DineroDepositadoEvento(
                    (UUID) source.get("AggregateId"),
                    ((Decimal128) source.get("Monto")),
                    (Integer) source.get("Version"));

            evento.setEventId((UUID) source.get("EventId"));
            evento.setOcurridoEn((java.util.Date)source.get("OcurridoEn"));
            evento.setTipoEvento(tipoEvento);
            
            return evento;
        } else if ("DineroRetiradoEvento".equals(tipoEvento)) {
            DineroRetiradoEvento evento = new DineroRetiradoEvento(
                    (UUID) source.get("AggregateId"),
                    ((Decimal128) source.get("Monto")),
                    (Integer) source.get("Version"));
            evento.setEventId((UUID) source.get("EventId"));
            evento.setOcurridoEn((java.util.Date)source.get("OcurridoEn"));
            evento.setTipoEvento(tipoEvento);
            return evento;
        }

        return null;
        // throw new IllegalArgumentException("Tipo de evento desconocido: " +
        // tipoEvento);
    }
}
