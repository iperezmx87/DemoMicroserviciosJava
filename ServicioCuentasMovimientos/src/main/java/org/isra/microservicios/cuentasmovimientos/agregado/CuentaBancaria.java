package org.isra.microservicios.cuentasmovimientos.agregado;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.bson.types.Decimal128;
import org.isra.microservicios.cuentasmovimientos.modelo.DineroDepositadoEvento;
import org.isra.microservicios.cuentasmovimientos.modelo.DineroRetiradoEvento;
import org.isra.microservicios.cuentasmovimientos.modelo.EventoBase;

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

    public void depositar(Decimal128 monto) {
        if (monto.compareTo(Decimal128.POSITIVE_ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor que cero.");
        }

        DineroDepositadoEvento evento = new DineroDepositadoEvento(id, monto, version + 1);

        AplicarEvento(evento);

        eventos.add(evento);
    }

    public void retirar(Decimal128 monto) {
        if (monto.compareTo(Decimal128.POSITIVE_ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor que cero.");
        }

        DineroRetiradoEvento evento = new DineroRetiradoEvento(id, monto, version + 1);

        AplicarEvento(evento);

        eventos.add(evento);
    }

    public void reconstruirDesdeEventos(List<EventoBase> eventos) {
        for (EventoBase evento : eventos) {
            AplicarEvento(evento);
        }
    }

    private void AplicarEvento(EventoBase evento) {
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
            default -> throw new IllegalArgumentException(
                    "Tipo de evento no reconocido: " + evento.getClass().getSimpleName());
        }
        // Aquí puedes agregar más casos para otros tipos de eventos, como retiros,
        // transferencias, etc.
    }
}
