package org.isra.microservicios.cuentasmovimientos.servicio;

import java.util.List;
import java.util.UUID;

import org.bson.types.Decimal128;
import org.isra.microservicios.cuentasmovimientos.agregado.CuentaBancaria;
import org.isra.microservicios.cuentasmovimientos.modelo.EventoBase;
import org.isra.microservicios.cuentasmovimientos.repositorio.EventosRepositorioInterface;
import org.springframework.stereotype.Service;

@Service
public class CuentaBancariaServicio {
    private final EventosRepositorioInterface eventosRepositorio;

    public CuentaBancariaServicio(EventosRepositorioInterface eventosRepositorio) {
        this.eventosRepositorio = eventosRepositorio;
    }

    public void depositar(UUID id, Decimal128 monto) {
        CuentaBancaria cuentaBancaria = obtenerCuentaBancaria(id);
        cuentaBancaria.depositar(monto);

        for(EventoBase evento : cuentaBancaria.getEventos()) {
            eventosRepositorio.save(evento);
        }

        cuentaBancaria.limpiarEventos();
    }

    public void retirar(UUID id, Decimal128 monto) {
        CuentaBancaria cuentaBancaria = obtenerCuentaBancaria(id);
        cuentaBancaria.retirar(monto);

        for(EventoBase evento : cuentaBancaria.getEventos()) {
            eventosRepositorio.save(evento);
        }

        cuentaBancaria.limpiarEventos();
    }

    public CuentaBancaria obtenerCuentaBancaria(UUID id) {
        CuentaBancaria cuentaBancaria = new CuentaBancaria(id);
        List<EventoBase> eventos = eventosRepositorio.findByAggregateIdOrderByVersionAsc(id);
        cuentaBancaria.reconstruirDesdeEventos(eventos);
        return cuentaBancaria;
    }
}
