package org.isra.microservicios.cuentasmovimientos.worker;

import java.util.List;

import org.isra.microservicios.cuentasmovimientos.modelo.MensajeSalida;
import org.isra.microservicios.cuentasmovimientos.repositorio.MensajeSalidaRepositorioInterface;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProcesadorMensajesSalidaWorker {
    private final MensajeSalidaRepositorioInterface mensajeSalidaRepositorioInterface;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProcesadorMensajesSalidaWorker(MensajeSalidaRepositorioInterface mensajeSalidaRepositorioInterface,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.mensajeSalidaRepositorioInterface = mensajeSalidaRepositorioInterface;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 10000)
    public void procesarMensajesSalida() {
        // Lógica para procesar mensajes de salida
        List<MensajeSalida> mensajes = mensajeSalidaRepositorioInterface.findByProcessedFalseOrderByOccurredOnAsc();

        if (mensajes.isEmpty()) {
            return;
        }

        for (MensajeSalida mensaje : mensajes) {
            try {
                kafkaTemplate.send("cuentas_movimientos_eventos", mensaje.getId().toString(), mensaje.getPayload())
                        .thenAccept(result -> {
                            mensaje.setProcessed(true);
                            mensajeSalidaRepositorioInterface.save(mensaje);
                        })
                        .exceptionally(ex -> {
                            System.err.println(
                                    "Error al confirmar en Kafka para ID " + mensaje.getId() + ": " + ex.getMessage());
                            return null;
                        });
            } catch (Exception e) {
                // Manejar cualquier excepción que ocurra durante el procesamiento
                System.err.println("Fallo crítico al despachar mensaje " + mensaje.getId() + ": " + e.getMessage());
            }
        }
    }
}
