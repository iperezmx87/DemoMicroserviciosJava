package org.isra.microservicios.recepciontransferencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RecepciontransferenciasApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecepciontransferenciasApplication.class, args);
	}
}
