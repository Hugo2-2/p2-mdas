package com.GM2.client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Patron;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ClientePatron {

    public static void main(String[] args) {
        RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String baseURL = "http://localhost:8080";

        sendGetRequests(rest, baseURL);
        sendPostRequests(rest, baseURL);
        sendPatchRequests(rest, baseURL);
        sendDeleteRequests(rest, baseURL);
    }

    private static void sendGetRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [PATRONES] PRUEBAS GET **********");

        // 1. Listar todos
        try {
            ResponseEntity<Patron[]> response = rest.getForEntity(baseURL + "/api/patrones", Patron[].class);
            List<Patron> lista = Arrays.asList(response.getBody());
            System.out.println("==== REQUEST 1: GET all patrones ====");
            for(Patron p : lista) {
                System.out.println(p);
                System.out.println("------------------------");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [PATRONES] PRUEBAS POST **********");

        // 2. Crear patrón válido
        Patron nuevo = new Patron("Juan", "Elcano", "12345678Z", LocalDate.of(1980, 1, 1), LocalDate.of(2005, 5, 20));
        System.out.println("==== REQUEST 2: POST patron (valid) ====");
        try {
            ResponseEntity<Patron> response = rest.postForEntity(baseURL + "/api/patrones", nuevo, Patron.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Creado:\n" + response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error (Posible duplicado): " + e);
        }

        // 3. Crear patrón inválido (Fecha futura)
        Patron malo = new Patron("Marty", "McFly", "88888888X", LocalDate.of(2050, 1, 1), LocalDate.now());
        System.out.println();
        System.out.println("==== REQUEST 3: POST patron (invalid - fecha futura) ====");
        try {
            rest.postForEntity(baseURL + "/api/patrones", malo, Patron.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e);
        }
    }

    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [PATRONES] PRUEBAS PATCH **********");

        // 4. Actualizar datos (Apellido)
        // Probamos cambiar el apellido de "Elcano" a "Elcano Magallanes"
        Patron cambios = new Patron();
        cambios.setApellidos("Elcano Magallanes");

        System.out.println("==== REQUEST 4: PATCH update patron (valid) ====");
        try {
            // A) Ejecutar la modificación
            Patron devuelto = rest.patchForObject(baseURL + "/api/patrones/12345678Z", cambios, Patron.class);
            System.out.println("-> API devolvió: " + devuelto.getNombre() + " " + devuelto.getApellidos());

            // B) Verificación: Consultar a BD cómo quedó realmente
            ResponseEntity<Patron> enBD = rest.getForEntity(baseURL + "/api/patrones/12345678Z", Patron.class);
            System.out.println("-> Verificación en BD (GET): " + enBD.getBody());

        } catch (HttpClientErrorException e) { System.out.println(e); }
    }

    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [PATRONES] PRUEBAS DELETE **********");

        // 5. Borrar patrón
        System.out.println("==== REQUEST 5: DELETE patron (valid) ====");
        try {
            // A) Ejecutar borrado
            rest.delete(baseURL + "/api/patrones/12345678Z");
            System.out.println("Solicitud DELETE enviada correctamente.");

            // B) Verificación: Intentar buscarlo (Debería dar 404 Not Found)
            try {
                rest.getForEntity(baseURL + "/api/patrones/12345678Z", Patron.class);
                System.out.println("ERROR: El patrón sigue existiendo en BD.");
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().value() == 404) {
                    System.out.println("-> Verificación ÉXITO: El patrón ya no existe (404 Not Found).");
                } else {
                    System.out.println("-> Verificación EXTRAÑA: " + e);
                }
            }

        } catch (HttpClientErrorException e) {
            System.out.println("Error al borrar: " + e.getStatusCode());
            if (e.getStatusCode().value() == 409) {
                System.out.println("(Probablemente el patrón tiene barco asignado. Desvincula primero).");
            }
        }
    }
}