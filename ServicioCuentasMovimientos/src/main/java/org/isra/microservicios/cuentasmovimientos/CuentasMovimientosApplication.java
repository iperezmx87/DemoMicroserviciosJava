package org.isra.microservicios.cuentasmovimientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class CuentasMovimientosApplication {
	public static void main(String[] args) {
		SpringApplication.run(CuentasMovimientosApplication.class, args);
	}
}
