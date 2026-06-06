package org.isra.microservicios.cuentasmovimientos.dto;

import java.util.UUID;

import org.bson.types.Decimal128;

import lombok.Data;

@Data
public class EnvioTransferenciaRequest {

    private UUID cuentaOrigenId;

    private UUID cuentaDestinoId;

    private Decimal128 monto;
}