package org.isra.microservicios.cuentasmovimientos.dto;

import org.bson.types.Decimal128;

public class OperacionMonetariaRequest {
    private Decimal128 monto;

    public Decimal128 getMonto() {
        return monto;
    }
}
