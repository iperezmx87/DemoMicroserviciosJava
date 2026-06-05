package org.isra.microservicios.estadocuenta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EstadocuentaApplication {
	public static void main(String[] args) {
		SpringApplication.run(EstadocuentaApplication.class, args);
	}
}
