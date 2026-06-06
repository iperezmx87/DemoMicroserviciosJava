package org.isra.microservicios.saldo.worker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.isra.microservicios.saldo.modelo.EventoRecibidoDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class SaldoProjectionWorker {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public SaldoProjectionWorker(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "cuentas_movimientos_eventos", containerFactory = "kafkaListenerContainerFactory")
    public void consumirEvento(ConsumerRecord<String, String> record) {
        try {
            String mensaje = record.value();
            EventoRecibidoDto evento = objectMapper.readValue(mensaje, EventoRecibidoDto.class);
            actualizarSaldo(evento);
        } catch (Exception e) {
            System.err.println("Error procesando evento de saldo en Kafka: " + e.getMessage());
        }
    }

    private void actualizarSaldo(EventoRecibidoDto evento) {
        // tratamiento del saldo dependiendo del tipo de evento
        switch (evento.getTipoEvento()) {
            case "DineroRetiradoEvento" -> evento.setMonto(evento.getMonto().negate());
            case "TransferenciaRealizadaEvento" -> evento.setMonto(evento.getMonto().negate());
            default -> {
            }
        }

        String sql = """
                    INSERT INTO cuentas.saldos_cuenta (id, saldo, ultima_version, ultima_actualizacion)
                    VALUES (?, ?, ?, CURRENT_TIMESTAMP)
                    ON CONFLICT (id) DO UPDATE
                    SET saldo = cuentas.saldos_cuenta.saldo + EXCLUDED.saldo,
                        ultima_version = EXCLUDED.ultima_version,
                        ultima_actualizacion = CURRENT_TIMESTAMP
                    WHERE cuentas.saldos_cuenta.ultima_version < EXCLUDED.ultima_version;
                """;

        jdbcTemplate.update(sql, evento.getAggregateId(), evento.getMonto(), evento.getVersion());
    }
}
