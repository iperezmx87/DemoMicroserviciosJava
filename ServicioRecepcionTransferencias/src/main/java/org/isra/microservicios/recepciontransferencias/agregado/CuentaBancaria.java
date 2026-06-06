package org.isra.microservicios.recepciontransferencias.agregado;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.bson.types.Decimal128;
import org.isra.microservicios.recepciontransferencias.modelo.DineroDepositadoEvento;
import org.isra.microservicios.recepciontransferencias.modelo.DineroRetiradoEvento;
import org.isra.microservicios.recepciontransferencias.modelo.EventoBase;
import org.isra.microservicios.recepciontransferencias.modelo.TransferenciaDevueltaEvento;
import org.isra.microservicios.recepciontransferencias.modelo.TransferenciaRealizadaEvento;
import org.isra.microservicios.recepciontransferencias.modelo.TransferenciaRecibidaEvento;

import lombok.Data;

@Data
public class CuentaBancaria {
    private UUID id;
    private BigDecimal saldo;
    private int version;
    private List<EventoBase> eventos;

    public CuentaBancaria(UUID id) {
        this.eventos = new java.util.ArrayList<>();
        this.id = id;
        this.saldo = BigDecimal.ZERO;
        this.version = 0;
    }

    public List<EventoBase> getEventos() {
        return eventos;
    }

    public void limpiarEventos() {
        eventos.clear();
    }

    public void recibirTransferencia() {
        
    }

    public void devolverTransferncia(){

    }

    public void reconstruirDesdeEventos(List<EventoBase> eventos) {
        for (EventoBase evento : eventos) {
            aplicarEvento(evento);
        }
    }

    private void aplicarEvento(EventoBase evento) {
        switch (evento.getClass().getSimpleName()) {
            case "DineroDepositadoEvento" -> {
                DineroDepositadoEvento eventoDepositado = (DineroDepositadoEvento) evento;
                this.saldo = this.saldo.add(eventoDepositado.getMonto().bigDecimalValue());
                this.version = eventoDepositado.getVersion();
            }

            case "DineroRetiradoEvento" -> {
                DineroRetiradoEvento eventoRetirado = (DineroRetiradoEvento) evento;
                this.saldo = this.saldo.subtract(eventoRetirado.getMonto().bigDecimalValue());
                this.version = eventoRetirado.getVersion();
            }

            case "TransferenciaRealizadaEvento" -> {
                TransferenciaRealizadaEvento eventoEnvioTransferencia = (TransferenciaRealizadaEvento) evento;
                this.saldo = this.saldo.subtract(eventoEnvioTransferencia.getMonto().bigDecimalValue());
                this.version = eventoEnvioTransferencia.getVersion();
            }

            case "TransferenciaRecibidaEvento" -> {
                TransferenciaRecibidaEvento recepcionEnvioTransferencia = (TransferenciaRecibidaEvento) evento;
                this.saldo = this.saldo.add(recepcionEnvioTransferencia.getMonto().bigDecimalValue());
                this.version = recepcionEnvioTransferencia.getVersion();
            }

            case "TransferenciaDevueltaEvento" -> {
                TransferenciaDevueltaEvento devolucionEnvioTransferencia = (TransferenciaDevueltaEvento) evento;
                this.saldo = this.saldo.add(devolucionEnvioTransferencia.getMonto().bigDecimalValue());
                this.version = devolucionEnvioTransferencia.getVersion();
            }

            default -> throw new IllegalArgumentException(
                    "Tipo de evento no reconocido: " + evento.getClass().getSimpleName());
        }
        // Aquí puedes agregar más casos para otros tipos de eventos, como retiros,
        // transferencias, etc.
    }
}
