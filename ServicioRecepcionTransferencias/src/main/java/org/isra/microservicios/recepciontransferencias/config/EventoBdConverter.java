package org.isra.microservicios.recepciontransferencias.config;

import java.util.UUID;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.isra.microservicios.recepciontransferencias.modelo.DineroDepositadoEvento;
import org.isra.microservicios.recepciontransferencias.modelo.DineroRetiradoEvento;
import org.isra.microservicios.recepciontransferencias.modelo.EventoBase;
import org.isra.microservicios.recepciontransferencias.modelo.TransferenciaDevueltaEvento;
import org.isra.microservicios.recepciontransferencias.modelo.TransferenciaRealizadaEvento;
import org.isra.microservicios.recepciontransferencias.modelo.TransferenciaRecibidaEvento;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class EventoBdConverter implements Converter<Document, EventoBase> {

    @Override
    public EventoBase convert(Document source) {
        String tipoEvento = source.getString("TipoEvento");

        return switch (tipoEvento) {
            case "DineroDepositadoEvento" -> {
                DineroDepositadoEvento evento = new DineroDepositadoEvento(
                        (UUID) source.get("AggregateId"),
                        (Decimal128) source.get("Monto"),
                        (Integer) source.get("Version"));

                evento.setEventId((UUID) source.get("EventId"));
                evento.setOcurridoEn((java.util.Date) source.get("OcurridoEn"));
                evento.setTipoEvento(tipoEvento);

                yield evento;
            }
            case "DineroRetiradoEvento" -> {
                DineroRetiradoEvento evento = new DineroRetiradoEvento(
                        (UUID) source.get("AggregateId"),
                        (Decimal128) source.get("Monto"),
                        (Integer) source.get("Version"));
                evento.setEventId((UUID) source.get("EventId"));
                evento.setOcurridoEn((java.util.Date) source.get("OcurridoEn"));
                evento.setTipoEvento(tipoEvento);
                yield evento;
            }
            case "TransferenciaRealizadaEvento" -> {
                TransferenciaRealizadaEvento evento = new TransferenciaRealizadaEvento(
                        (UUID) source.get("AggregateId"),
                        (Decimal128) source.get("Monto"),
                        (Integer) source.get("Version"),
                        (UUID) source.get("CuentaDestinoId"));

                evento.setEventId((UUID) source.get("EventId"));
                evento.setOcurridoEn((java.util.Date) source.get("OcurridoEn"));
                evento.setTipoEvento(tipoEvento);

                yield evento;
            }

            case "TransferenciaRecibidaEvento" -> {
                TransferenciaRecibidaEvento evento = new TransferenciaRecibidaEvento(
                        (UUID) source.get("AggregateId"),
                        (Decimal128) source.get("Monto"),
                        (Integer) source.get("Version"),
                        (UUID) source.get("CuentaDestinoId"));

                evento.setEventId((UUID) source.get("EventId"));
                evento.setOcurridoEn((java.util.Date) source.get("OcurridoEn"));
                evento.setTipoEvento(tipoEvento);

                yield evento;
            }

            case "TransferenciaDevueltaEvento" -> {
                TransferenciaDevueltaEvento evento = new TransferenciaDevueltaEvento(
                        (UUID) source.get("AggregateId"),
                        (UUID) source.get("IdTransferenciaOrigen"),
                        (Decimal128) source.get("Monto"),
                        (UUID) source.get("CuentaOrigenId"),
                        (Integer) source.get("Version"));

                evento.setEventId((UUID) source.get("EventId"));
                evento.setOcurridoEn((java.util.Date) source.get("OcurridoEn"));
                evento.setTipoEvento(tipoEvento);

                yield evento;
            }

            default -> null;
        };
    }
}
