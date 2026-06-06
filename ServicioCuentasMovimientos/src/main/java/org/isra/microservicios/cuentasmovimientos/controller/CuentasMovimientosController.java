package org.isra.microservicios.cuentasmovimientos.controller;

import java.util.UUID;

import org.isra.microservicios.cuentasmovimientos.agregado.CuentaBancaria;
import org.isra.microservicios.cuentasmovimientos.dto.EnvioTransferenciaRequest;
import org.isra.microservicios.cuentasmovimientos.dto.OperacionMonetariaRequest;
import org.isra.microservicios.cuentasmovimientos.dto.OperacionMonetariaResponse;
import org.isra.microservicios.cuentasmovimientos.servicio.CuentaBancariaServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
public class CuentasMovimientosController {

    private final CuentaBancariaServicio cuentaBancariaServicio;

    public CuentasMovimientosController(CuentaBancariaServicio cuentaBancariaServicio) {
        this.cuentaBancariaServicio = cuentaBancariaServicio;
    }

    @PostMapping("/{cuentaId}/depositar")
    public ResponseEntity<Object> depositar(
            @PathVariable UUID cuentaId,
            @RequestBody OperacionMonetariaRequest request) {
        try {
            cuentaBancariaServicio.depositar(cuentaId, request.getMonto());

            CuentaBancaria cuenta = cuentaBancariaServicio.obtenerCuentaBancaria(cuentaId);

            return ResponseEntity.ok(new OperacionMonetariaResponse(
                    "Depósito realizado exitosamente",
                    cuentaId,
                    cuenta.getSaldo()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{cuentaId}/retirar")
    public ResponseEntity<Object> retirar(
            @PathVariable UUID cuentaId,
            @RequestBody OperacionMonetariaRequest request) {
        try {
            cuentaBancariaServicio.retirar(cuentaId, request.getMonto());

            CuentaBancaria cuenta = cuentaBancariaServicio.obtenerCuentaBancaria(cuentaId);

            return ResponseEntity.ok(new OperacionMonetariaResponse(
                    "Retiro realizado exitosamente",
                    cuentaId,
                    cuenta.getSaldo()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<Object> enviarTransferencia(@RequestBody EnvioTransferenciaRequest request) {
        try {
            cuentaBancariaServicio.enviarTransferencia(request.getCuentaOrigenId(), request.getCuentaDestinoId(),
                    request.getMonto());

            CuentaBancaria cuenta = cuentaBancariaServicio.obtenerCuentaBancaria(request.getCuentaOrigenId());

            return ResponseEntity.ok(new OperacionMonetariaResponse(
                    "Transferencia realizada exitosamente", request.getCuentaOrigenId(), cuenta.getSaldo()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
