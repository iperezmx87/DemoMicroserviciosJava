package org.isra.microservicios.cuentasmovimientos.servicio;

import java.util.List;
import java.util.UUID;

import org.bson.types.Decimal128;
import org.isra.microservicios.cuentasmovimientos.agregado.CuentaBancaria;
import org.isra.microservicios.cuentasmovimientos.modelo.EventoBase;
import org.isra.microservicios.cuentasmovimientos.modelo.MensajeSalida;
import org.isra.microservicios.cuentasmovimientos.repositorio.EventosRepositorioInterface;
import org.isra.microservicios.cuentasmovimientos.repositorio.MensajeSalidaRepositorioInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

@Service
public class CuentaBancariaServicio {
    private final EventosRepositorioInterface eventosRepositorio;
    private final MensajeSalidaRepositorioInterface mensajeSalidaRepositorio;
    private final ObjectMapper objectMapper;

    public CuentaBancariaServicio(
            EventosRepositorioInterface eventosRepositorio, MensajeSalidaRepositorioInterface mensajeSalidaRepositorio,
            ObjectMapper objectMapper) {
        this.eventosRepositorio = eventosRepositorio;
        this.mensajeSalidaRepositorio = mensajeSalidaRepositorio;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void depositar(UUID id, Decimal128 monto) {
        CuentaBancaria cuentaBancaria = obtenerCuentaBancaria(id);
        cuentaBancaria.depositar(monto);

        for (EventoBase evento : cuentaBancaria.getEventos()) {
            eventosRepositorio.save(evento);

            MensajeSalida mensajeSalida = new MensajeSalida();
            mensajeSalida.setId(UUID.randomUUID());
            mensajeSalida.setTopic("cuentas_movimientos_eventos");
            mensajeSalida.setPayload(objectMapper.writeValueAsString(evento));

            mensajeSalidaRepositorio.save(mensajeSalida);
        }

        cuentaBancaria.limpiarEventos();
    }

    @Transactional
    public void retirar(UUID id, Decimal128 monto) {
        CuentaBancaria cuentaBancaria = obtenerCuentaBancaria(id);
        cuentaBancaria.retirar(monto);

        for (EventoBase evento : cuentaBancaria.getEventos()) {
            eventosRepositorio.save(evento);

            MensajeSalida mensajeSalida = new MensajeSalida();
            mensajeSalida.setId(UUID.randomUUID());
            mensajeSalida.setTopic("cuentas_movimientos_eventos");
            mensajeSalida.setPayload(objectMapper.writeValueAsString(evento));

            mensajeSalidaRepositorio.save(mensajeSalida);
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
