package com.GM2.client;

import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron; // Necesario para la respuesta de vinculación
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ClienteEmbarcacion {

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
        System.out.println("\n********** [EMBARCACIONES] PRUEBAS GET **********");

        // 1. Listar todas
        try {
            ResponseEntity<Embarcacion[]> response = rest.getForEntity(baseURL + "/api/embarcaciones", Embarcacion[].class);
            List<Embarcacion> lista = Arrays.asList(response.getBody());
            System.out.println("==== REQUEST 1: GET all embarcaciones ====");
            for(Embarcacion e : lista) {
                System.out.println(e);
                System.out.println("------------------------");
            }
        } catch (Exception e) { e.printStackTrace(); }

        // 2. Filtrar por tipo
        try {
            ResponseEntity<Embarcacion[]> response = rest.getForEntity(baseURL + "/api/embarcaciones?tipo=VELERO", Embarcacion[].class);
            List<Embarcacion> lista = Arrays.asList(response.getBody());
            System.out.println();
            System.out.println("==== REQUEST 2: GET embarcaciones by type (VELERO) ====");
            for(Embarcacion e : lista) {
                System.out.println(e);
                System.out.println("------------------------");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [EMBARCACIONES] PRUEBAS POST **********");

        // 3. Crear embarcación válida
        Embarcacion nuevo = new Embarcacion(null, "20.0", 6, "LANCHA", "Lancha Rápida", "L-999");
        System.out.println("==== REQUEST 3: POST embarcacion (valid) ====");
        try {
            ResponseEntity<Embarcacion> response = rest.postForEntity(baseURL + "/api/embarcaciones", nuevo, Embarcacion.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response body:\n" + response.getBody());
            System.out.println("------------------------");
        } catch (HttpClientErrorException e) { System.out.println("Error: " + e); }

        // 4. Crear duplicada (Error)
        System.out.println();
        System.out.println("==== REQUEST 4: POST embarcacion (invalid - duplicada) ====");
        try {
            rest.postForEntity(baseURL + "/api/embarcaciones", nuevo, Embarcacion.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e);
        }
    }

    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [EMBARCACIONES] PRUEBAS PATCH **********");

        // 5. Actualizar datos (Nombre y Plazas)
        Embarcacion cambios = new Embarcacion();
        cambios.setNombre("Lancha Muy Rápida");
        cambios.setPlazas(8);
        System.out.println("==== REQUEST 5: PATCH update embarcacion (valid) ====");
        try {
            Embarcacion actualizado = rest.patchForObject(baseURL + "/api/embarcaciones/L-999", cambios, Embarcacion.class);
            System.out.println("Nuevos datos de la embarcacion: ");
            System.out.println(actualizado);
        } catch (HttpClientErrorException e) { System.out.println(e); }

        // 6. VINCULAR Patrón (usando endpoint /patron)
        // Asumimos que existe un patrón con DNI '11112222Z'
        String dniPatron = "11112222Z";
        System.out.println();
        System.out.println("==== REQUEST 6: PATCH vincular patron (/patron) ====");
        try {
            Patron p = rest.patchForObject(baseURL + "/api/patrones/L-999/patron", dniPatron, Patron.class);
            System.out.println("Éxito. Vinculado patrón: " + p.getDni());
        } catch (HttpClientErrorException e) { System.out.println("Error al vincular: " + e); }

        // 7. DESVINCULAR Patrón (usando endpoint /noPatron)
        System.out.println();
        System.out.println("==== REQUEST 7: PATCH desvincular patron (/noPatron) ====");
        try {
            Patron p = rest.patchForObject(baseURL + "/api/patrones/L-999/noPatron", dniPatron, Patron.class);
            System.out.println("Éxito. Desvinculado patrón: " + p.getDni());
        } catch (HttpClientErrorException e) { System.out.println("Error al desvincular: " + e);}
    }

    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [EMBARCACIONES] PRUEBAS DELETE **********");

        // 8. Borrar embarcación
        System.out.println("==== REQUEST 8: DELETE embarcacion (valid) ====");
        try {
            rest.delete(baseURL + "/api/embarcaciones/L-999");
            System.out.println("Embarcación borrada correctamente.");
        } catch (HttpClientErrorException e) { System.out.println("Error: " + e); }
    }
}