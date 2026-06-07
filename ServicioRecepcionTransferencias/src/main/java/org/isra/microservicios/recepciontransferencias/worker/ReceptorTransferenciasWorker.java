package org.isra.microservicios.recepciontransferencias.worker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.isra.microservicios.recepciontransferencias.dto.EventoRecibidoDto;
import org.isra.microservicios.recepciontransferencias.servicio.CuentaBancariaServicio;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class ReceptorTransferenciasWorker {
    private final ObjectMapper objectMapper;
    private final CuentaBancariaServicio cuentaBancariaServicio;

    public ReceptorTransferenciasWorker(ObjectMapper objectMapper,
            CuentaBancariaServicio cuentaBancariaServicio) {
        this.objectMapper = objectMapper;
        this.cuentaBancariaServicio = cuentaBancariaServicio;
    }

    @KafkaListener(topics = "cuentas_movimientos_eventos", containerFactory = "kafkaListenerContainerFactory")
    public void consumirEvento(ConsumerRecord<String, String> record) {
        String mensaje = record.value();
        EventoRecibidoDto evento = objectMapper.readValue(mensaje, EventoRecibidoDto.class);

        // el evento es de tipo "TransferenciaRealizadaEvento"
        switch (evento.getTipoEvento()) {
            case "TransferenciaRealizadaEvento" -> {
                try {
                    cuentaBancariaServicio.recibirTransferencia(evento.getCuentaDestinoId(), evento.getMonto());
                } catch (Exception ex) {
                    System.err.println("Error en la recepcion de transferencia, se devuelve");
                    cuentaBancariaServicio.devolverTransferencia(evento.getEventId(),
                            evento.getAggregateId(), ex.getMessage(), evento.getMonto());
                }
            }
        }
    }
}
