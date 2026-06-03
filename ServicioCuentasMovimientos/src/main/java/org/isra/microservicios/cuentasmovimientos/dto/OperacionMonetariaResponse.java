package org.isra.microservicios.cuentasmovimientos.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class OperacionMonetariaResponse {
    private final String mensaje;

    private final UUID idCuenta;

    private final BigDecimal saldoActual;

    public OperacionMonetariaResponse(String mensaje, UUID idCuenta, BigDecimal saldoActual) {
        this.mensaje = mensaje;
        this.idCuenta = idCuenta;
        this.saldoActual = saldoActual;
    }

    public String getMensaje() {
        return mensaje;
    }

    public UUID getIdCuenta() {
        return idCuenta;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }
}