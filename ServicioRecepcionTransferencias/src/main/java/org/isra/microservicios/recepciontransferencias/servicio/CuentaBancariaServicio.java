package org.isra.microservicios.recepciontransferencias.servicio;

import java.util.List;
import java.util.UUID;

import org.bson.types.Decimal128;
import org.isra.microservicios.recepciontransferencias.agregado.CuentaBancaria;
import org.isra.microservicios.recepciontransferencias.modelo.EventoBase;
import org.isra.microservicios.recepciontransferencias.modelo.MensajeSalida;
import org.isra.microservicios.recepciontransferencias.repositorio.EventosRepositorioInterface;
import org.isra.microservicios.recepciontransferencias.repositorio.MensajeSalidaRepositorioInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

@Service
public class CuentaBancariaServicio {
    private final EventosRepositorioInterface eventosRepositorio;
    private final MensajeSalidaRepositorioInterface mensajeSalidaRepositorio;
    private final ObjectMapper objectMapper;

    public CuentaBancariaServicio(
            EventosRepositorioInterface eventosRepositorio,
            MensajeSalidaRepositorioInterface mensajeSalidaRepositorio,
            ObjectMapper objectMapper) {
        this.eventosRepositorio = eventosRepositorio;
        this.mensajeSalidaRepositorio = mensajeSalidaRepositorio;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void recibirTransferencia(UUID idCuentaDestino, Decimal128 monto) {
        CuentaBancaria cuentaBancariaDestino = obtenerCuentaBancaria(idCuentaDestino);

        cuentaBancariaDestino.recibirTransferencia(idCuentaDestino, monto);

        for (EventoBase evento : cuentaBancariaDestino.getEventos()) {
            eventosRepositorio.save(evento);
            MensajeSalida mensajeSalida = new MensajeSalida();
            mensajeSalida.setId(evento.getEventId());
            mensajeSalida.setTopic("cuentas_movimientos_eventos");
            mensajeSalida.setPayload(objectMapper.writeValueAsString(evento));

            mensajeSalidaRepositorio.save(mensajeSalida);
        }

        cuentaBancariaDestino.limpiarEventos();
    }

    @Transactional
    public void devolverTransferencia(UUID idTransferenciaOrigen, UUID cuentaOrigenId,
            String motivoDevolucion, Decimal128 monto) {
        CuentaBancaria cuentaBancariaOrigen = obtenerCuentaBancaria(cuentaOrigenId);
        cuentaBancariaOrigen.devolverTransferncia(idTransferenciaOrigen, cuentaOrigenId, motivoDevolucion, monto);

        for (EventoBase evento : cuentaBancariaOrigen.getEventos()) {
            eventosRepositorio.save(evento);
            MensajeSalida mensajeSalida = new MensajeSalida();
            mensajeSalida.setId(evento.getEventId());
            mensajeSalida.setTopic("cuentas_movimientos_eventos");
            mensajeSalida.setPayload(objectMapper.writeValueAsString(evento));

            mensajeSalidaRepositorio.save(mensajeSalida);
        }

        cuentaBancariaOrigen.limpiarEventos();
    }

    public CuentaBancaria obtenerCuentaBancaria(UUID id) {
        CuentaBancaria cuentaBancaria = new CuentaBancaria(id);
        List<EventoBase> eventos = eventosRepositorio.findByAggregateIdOrderByVersionAsc(id);
        cuentaBancaria.reconstruirDesdeEventos(eventos);
        return cuentaBancaria;
    }
}
