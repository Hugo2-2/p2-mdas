package com.GM2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot del Club Náutico GM2.
 *
 * Refactoring 7.2: Eliminada anotación @RestController innecesaria
 * (la clase Application no es un controlador REST).
 */
@SpringBootApplication
public class Gm2Application {

	public static void main(String[] args) {
		SpringApplication.run(Gm2Application.class, args);
	}

}
