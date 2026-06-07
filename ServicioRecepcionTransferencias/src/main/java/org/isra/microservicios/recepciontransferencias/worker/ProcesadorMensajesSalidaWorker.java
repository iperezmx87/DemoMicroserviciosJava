package org.isra.microservicios.recepciontransferencias.worker;

import java.util.List;

import org.isra.microservicios.recepciontransferencias.modelo.EventoBase;
import org.isra.microservicios.recepciontransferencias.modelo.MensajeSalida;
import org.isra.microservicios.recepciontransferencias.repositorio.MensajeSalidaRepositorioInterface;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class ProcesadorMensajesSalidaWorker {
    private final MensajeSalidaRepositorioInterface mensajeSalidaRepositorioInterface;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ProcesadorMensajesSalidaWorker(MensajeSalidaRepositorioInterface mensajeSalidaRepositorioInterface,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.mensajeSalidaRepositorioInterface = mensajeSalidaRepositorioInterface;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 10000)
    public void procesarMensajesSalida() {
        // Lógica para procesar mensajes de salida
        List<MensajeSalida> mensajes = mensajeSalidaRepositorioInterface.findByProcessedFalseOrderByOccurredOnAsc();

        if (mensajes.isEmpty()) {
            return;
        }

        for (MensajeSalida mensaje : mensajes) {
            EventoBase evento = objectMapper.readValue(mensaje.getPayload(), EventoBase.class);
            switch (evento.getTipoEvento()) {
                case "TransferenciaRecibidaEvento", "TransferenciaDevueltaEvento" -> {
                    try {
                        kafkaTemplate
                                .send("cuentas_movimientos_eventos", mensaje.getId().toString(), mensaje.getPayload())
                                .thenAccept(result -> {
                                    mensaje.setProcessed(true);
                                    mensajeSalidaRepositorioInterface.save(mensaje);
                                })
                                .exceptionally(ex -> {
                                    System.err.println(
                                            "Error al confirmar en Kafka para ID " + mensaje.getId() + ": "
                                                    + ex.getMessage());
                                    return null;
                                });
                    } catch (Exception e) {
                        // Manejar cualquier excepción que ocurra durante el procesamiento
                        System.err.println(
                                "Fallo crítico al despachar mensaje " + mensaje.getId() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}
