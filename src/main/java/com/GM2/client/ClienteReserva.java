package com.GM2.client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Reserva;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente de la API de reservas.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class ClienteReserva {

    private static Integer reservaCreadaId = null;

    /**
     * Método principal del cliente.
     *
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String baseURL = "http://localhost:8080";

        sendGetRequests(rest, baseURL);
        sendPostRequests(rest, baseURL);

        if (reservaCreadaId != null) {
            sendPatchRequests(rest, baseURL);
            sendDeleteRequests(rest, baseURL);
        } else {
            System.out.println("No se ejecutan PATCH/DELETE porque la reserva no se pudo crear (POST falló).");
        }
    }

    /**
     * Método para realizar las pruebas GET de los endpoints de la API de reservas.
     *
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendGetRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS GET **********");

        System.out.println("==== REQUEST 1: GET all reservas ====");
        try {
            ResponseEntity<Reserva[]> response = rest.getForEntity(baseURL + "/api/reservas", Reserva[].class);
            System.out.println("Status code: " + response.getStatusCode());

            if (response.getBody() != null) {
                List<Reserva> lista = Arrays.asList(response.getBody());
                for(Reserva r : lista) {
                    imprimirReserva(r);
                    System.out.println("------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        LocalDate fechaFiltro = LocalDate.of(2025, 1, 1);
        System.out.println();
        System.out.println("==== REQUEST 2: GET reservas futuras (a partir de " + fechaFiltro + ") ====");
        try {
            ResponseEntity<Reserva[]> response = rest.getForEntity(
                    baseURL + "/api/reservas?fecha=" + fechaFiltro,
                    Reserva[].class
            );
            System.out.println("Status code: " + response.getStatusCode());

            if (response.getBody() != null) {
                List<Reserva> lista = Arrays.asList(response.getBody());
                for(Reserva r : lista) {
                    imprimirReserva(r);
                    System.out.println("------------------------");
                }
                if (lista.isEmpty()) {
                    System.out.println("No hay reservas futuras");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        int reservaId = 2;
        System.out.println();
        System.out.println("==== REQUEST 3: GET reserva by ID (" + reservaId + ") ====");
        try {
            ResponseEntity<Reserva> response = rest.getForEntity(
                    baseURL + "/api/reservas/" + reservaId,
                    Reserva.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            imprimirReserva(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Reserva no encontrada con ID: " + reservaId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Método para realizar las pruebas POST de los endpoints de la API de reservas.
     *
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS POST **********");

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setDate(LocalDate.of(2026, 3, 22));
        nuevaReserva.setSeats(4);
        nuevaReserva.setUserNationalId("11111111A");
        nuevaReserva.setBoatRegistration("XXX111");
        nuevaReserva.setDescription("Viaje por el Atlántico");

        System.out.println("==== REQUEST 4: POST reserva (valid) ====");
        try {
            ResponseEntity<Reserva> response = rest.postForEntity(
                    baseURL + "/api/reservas",
                    nuevaReserva,
                    Reserva.class
            );
            System.out.println("Status code: " + response.getStatusCode());

            if (response.getBody() != null) {
                reservaCreadaId = response.getBody().getId();
                imprimirReserva(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        Reserva reservaInvalida = new Reserva();
        reservaInvalida.setDate(LocalDate.of(2026, 3, 23));
        reservaInvalida.setSeats(50);
        reservaInvalida.setUserNationalId("11111111A");
        reservaInvalida.setBoatRegistration("XXX111");

        System.out.println();
        System.out.println("==== REQUEST 5: POST reserva (invalid - plazas) ====");
        try {
            rest.postForEntity(baseURL + "/api/reservas", reservaInvalida, Reserva.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        Reserva reservaSocioNoExiste = new Reserva();
        reservaSocioNoExiste.setDate(LocalDate.of(2026, 3, 24));
        reservaSocioNoExiste.setSeats(2);
        reservaSocioNoExiste.setUserNationalId("99999999Z");
        reservaSocioNoExiste.setBoatRegistration("XXX111");

        System.out.println();
        System.out.println("==== REQUEST 6: POST reserva (invalid - socio no existe) ====");
        try {
            rest.postForEntity(baseURL + "/api/reservas", reservaSocioNoExiste, Reserva.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado (Cliente): " + e.getStatusCode());
        } catch (HttpServerErrorException e) {
            System.out.println("Error esperado (Servidor): " + e.getStatusCode() + " INTERNAL_SERVER_ERROR");
        }
    }

    /**
     * Método para realizar las pruebas PATCH de los endpoints de la API de reservas.
     *
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS PATCH **********");

        System.out.println("==== REQUEST 7: PATCH modificar fecha ====");
        Reserva patchFecha = new Reserva();
        patchFecha.setDate(LocalDate.of(2026, 4, 1));

        try {
            Reserva actualizada = rest.patchForObject(
                    baseURL + "/api/reservas/" + reservaCreadaId + "/fecha",
                    patchFecha,
                    Reserva.class
            );
            System.out.println("Fecha actualizada:");
            imprimirReserva(actualizada);
        } catch (HttpClientErrorException e) {
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        System.out.println();
        System.out.println("==== REQUEST 8: PATCH modificar descripción y plazas ====");
        Reserva patchDetalles = new Reserva();
        patchDetalles.setDescription("Viaje por el Mediterráneo");
        patchDetalles.setSeats(2);

        try {
            Reserva actualizada = rest.patchForObject(
                    baseURL + "/api/reservas/" + reservaCreadaId + "/detalles",
                    patchDetalles,
                    Reserva.class
            );
            System.out.println("Detalles actualizados:");
            imprimirReserva(actualizada);
        } catch (HttpClientErrorException e) {
            System.out.println("Error: " + e.getStatusCode());
        }
    }

    /**
     * Método para realizar las pruebas DELETE de los endpoints de la API de reservas.
     *
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS DELETE **********");

        if (reservaCreadaId != null) {
            System.out.println("==== REQUEST 9: DELETE cancelar reserva futura ====");
            try {
                rest.delete(baseURL + "/api/reservas/" + reservaCreadaId);
                System.out.println("Reserva cancelada correctamente.");

                try {
                    rest.getForEntity(baseURL + "/api/reservas/" + reservaCreadaId, Reserva.class);
                } catch (HttpClientErrorException.NotFound e) {
                    System.out.println("Confirmado: La reserva ya no existe (404 OK)");
                }
            } catch (HttpClientErrorException e) {
                System.out.println("Error: " + e.getStatusCode());
            }
        }

        System.out.println();
        System.out.println("==== REQUEST 10: DELETE cancelar reserva inexistente ====");
        try {
            rest.delete(baseURL + "/api/reservas/9999");
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode());
        }
    }

    /**
     * Método auxiliar para imprimir los datos de una reserva.
     *
     * @param r Objeto Reserva a imprimir
     */
    private static void imprimirReserva(Reserva r) {
        if (r == null) return;
        System.out.println("Reserva {");
        System.out.println("  ID: " + r.getId());
        System.out.println("  Fecha: " + r.getDate());
        System.out.println("  Plazas: " + r.getSeats());
        System.out.println("  Precio: " + r.getPrice() + "€");
        System.out.println("  DNI Socio: " + r.getUserNationalId());
        System.out.println("  Matrícula: " + r.getBoatRegistration());
        System.out.println("  Descripción: " + (r.getDescription() != null ? r.getDescription() : "N/A"));
        System.out.println("}");
    }
}