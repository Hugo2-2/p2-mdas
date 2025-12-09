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
        // Configuración obligatoria para PATCH
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
            for(Patron p : lista) System.out.println("- " + p.getNombre() + " " + p.getApellidos() + " [" + p.getDni() + "]");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [PATRONES] PRUEBAS POST **********");

        // 2. Crear patrón válido
        Patron nuevo = new Patron("Juan", "Elcano", "12345678Z", LocalDate.of(1980, 1, 1), LocalDate.of(2005, 5, 20));
        System.out.println("==== REQUEST 2: POST patron (valid) ====");
        try {
            ResponseEntity<Patron> response = rest.postForEntity(baseURL + "/api/patrones", nuevo, Patron.class);
            System.out.println("Status: " + response.getStatusCode() + " | Creado: " + response.getBody().getNombre());
        } catch (HttpClientErrorException e) { System.out.println("Error: " + e.getStatusCode()); }

        // 3. Crear patrón inválido (Fecha futura)
        Patron malo = new Patron("Marty", "McFly", "88888888X", LocalDate.of(2050, 1, 1), LocalDate.now());
        System.out.println("==== REQUEST 3: POST patron (invalid - fecha futura) ====");
        try {
            rest.postForEntity(baseURL + "/api/patrones", malo, Patron.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode());
        }
    }

    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [PATRONES] PRUEBAS PATCH **********");

        // 4. Actualizar datos (Apellido)
        Patron cambios = new Patron();
        cambios.setApellidos("Elcano Magallanes");
        System.out.println("==== REQUEST 4: PATCH update patron (valid) ====");
        try {
            Patron actualizado = rest.patchForObject(baseURL + "/api/patrones/12345678Z", cambios, Patron.class);
            System.out.println("Actualizado: " + actualizado.getNombre() + " " + actualizado.getApellidos());
        } catch (HttpClientErrorException e) { System.out.println(e); }
    }

    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [PATRONES] PRUEBAS DELETE **********");

        // 5. Borrar patrón
        System.out.println("==== REQUEST 5: DELETE patron (valid) ====");
        try {
            rest.delete(baseURL + "/api/patrones/12345678Z");
            System.out.println("Patrón borrado correctamente.");
        } catch (HttpClientErrorException e) { System.out.println("Error: " + e.getStatusCode()); }
    }
}