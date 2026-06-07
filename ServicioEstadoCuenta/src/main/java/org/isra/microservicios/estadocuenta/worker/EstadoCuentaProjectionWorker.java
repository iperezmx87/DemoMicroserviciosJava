package org.isra.microservicios.estadocuenta.worker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.isra.microservicios.estadocuenta.modelo.EventoRecibidoDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class EstadoCuentaProjectionWorker {
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    public EstadoCuentaProjectionWorker(
            ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @KafkaListener(topics = "cuentas_movimientos_eventos", containerFactory = "kafkaListenerContainerFactory")
    public void consumirEvento(ConsumerRecord<String, String> record) {
        try {
            String mensaje = record.value();
            EventoRecibidoDto evento = objectMapper.readValue(mensaje, EventoRecibidoDto.class);
            actualizarEstadoCuenta(evento);
        } catch (Exception e) {
            System.err.println("Error procesando evento de saldo en Kafka: " + e.getMessage());
        }
    }

    private void actualizarEstadoCuenta(EventoRecibidoDto evento) {
        // Aquí se implementaría la lógica para actualizar el estado de cuenta en la
        // base de datos
        // utilizando el evento recibido. Esto podría incluir consultas SQL o llamadas a
        // un repositorio.
        try {
            String sql = """
                    IF NOT EXISTS (SELECT 1 FROM MovimientosCuenta WHERE AggregateId = ? AND Version = ?)
                    BEGIN
                        INSERT INTO MovimientosCuenta (AggregateId, TipoMovimiento, Monto, Version, MotivoDevolucion)
                        VALUES (?, ?, ?, ?, ?);
                    END
                    """;

            jdbcTemplate.update(sql, evento.getAggregateId(), evento.getVersion(),
                    evento.getAggregateId(), evento.getTipoEvento(), evento.getMonto(), evento.getVersion(),
                    evento.getMotivoDevolucion());

        } catch (Exception ex) {
            System.err.println("Error en proyeccion EdoCuenta: " + ex.getMessage());
        }
    }
}
