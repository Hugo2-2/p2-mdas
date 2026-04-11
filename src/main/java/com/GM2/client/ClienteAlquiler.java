package com.GM2.client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente de la API de alquileres.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class ClienteAlquiler {

    private static Integer alquilerCreadoId = null;

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
        
        if (alquilerCreadoId != null) {
            sendPatchRequests(rest, baseURL);
        }
        
        sendDeleteRequests(rest, baseURL);
    }

    /**
     * Método para realizar las pruebas GET de los endpoints de la API de alquileres.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendGetRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS GET **********");

        System.out.println("==== REQUEST 1: GET all alquileres ====");
        try {
            ResponseEntity<Alquiler[]> response = rest.getForEntity(baseURL + "/api/alquileres", Alquiler[].class);
            System.out.println("Status code: " + response.getStatusCode());
            //Clean Code - Reglas de nombrado: el nombre de una coleccion ayuda a entender su contenido (lista -> rentalList)
            List<Alquiler> rentalList = Arrays.asList(response.getBody());
            for(Alquiler a : rentalList) {
                System.out.println(a);
                System.out.println("------------------------");
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        LocalDate fechaFutura = LocalDate.of(2025, 2, 9);
        System.out.println();
        System.out.println("==== REQUEST 2: GET alquileres futuros (a partir de " + fechaFutura + ") ====");
        try {
            ResponseEntity<Alquiler[]> response = rest.getForEntity(
                baseURL + "/api/alquileres?fecha=" + fechaFutura, 
                Alquiler[].class
            );
            System.out.println("Status code: " + response.getStatusCode());
            //Clean Code - Reglas de nombrado: el nombre de una coleccion ayuda a entender su contenido (lista -> FutureRentalList)
            List<Alquiler> futureRentalList = Arrays.asList(response.getBody());
            for(Alquiler a : futureRentalList) {
                System.out.println(a);
                System.out.println("------------------------");
            }
            if (futureRentalList.isEmpty()) {
                System.out.println("No hay alquileres futuros");
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        int alquilerId = 1;
        System.out.println();
        System.out.println("==== REQUEST 3: GET alquiler by ID (" + alquilerId + ") ====");
        try {
            ResponseEntity<Alquiler> response = rest.getForEntity(
                baseURL + "/api/alquileres/" + alquilerId, 
                Alquiler.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Alquiler no encontrado con ID: " + alquilerId);
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        alquilerId = 99999;
        System.out.println();
        System.out.println("==== REQUEST 4: GET alquiler by ID (" + alquilerId + ") (invalid - alquiler no existe) ====");
        try {
            ResponseEntity<Alquiler> response = rest.getForEntity(
                baseURL + "/api/alquileres/" + alquilerId, 
                Alquiler.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Alquiler no encontrado con ID: " + alquilerId);
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        LocalDate fechaInicio = LocalDate.of(2025, 2, 1);
        LocalDate fechaFin = LocalDate.of(2025, 2, 6);
        System.out.println();
        System.out.println("==== REQUEST 5: GET embarcaciones disponibles (" + fechaInicio + " a " + fechaFin + ") ====");
        try {
            ResponseEntity<Embarcacion[]> response = rest.getForEntity(
                baseURL + "/api/alquileres/disponibles?fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin, 
                Embarcacion[].class
            );
            System.out.println("Status code: " + response.getStatusCode());
            //Clean Code - Reglas de nombrado: el nombre de una coleccion ayuda a entender su contenido (lista -> availableBoats)
            List<Embarcacion> availableBoats = Arrays.asList(response.getBody());
            for(Embarcacion e : availableBoats) {
                System.out.println(e);
                System.out.println("------------------------");
            }
            if (availableBoats.isEmpty()) {
                System.out.println("No hay embarcaciones disponibles en esas fechas");
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }
    }

    /**
     * Método para realizar las pruebas POST de los endpoints de la API de alquileres.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS POST **********");

        Alquiler nuevoAlquiler = new Alquiler();
        nuevoAlquiler.setFechainicio(LocalDate.of(2025, 12, 20));
        nuevoAlquiler.setFechafin(LocalDate.of(2025, 12, 22));
        nuevoAlquiler.setPlazas(5);
        nuevoAlquiler.setUsuario_dni("11111111A");
        nuevoAlquiler.setMatricula_embarcacion("XXX111");
        
        System.out.println("==== REQUEST 6: POST alquiler (valid) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(
                baseURL + "/api/alquileres", 
                nuevoAlquiler, 
                Alquiler.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            alquilerCreadoId = response.getBody().getId();
            System.out.println(response.getBody());
            
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        Alquiler alquilerInvalido = new Alquiler();
        alquilerInvalido.setFechainicio(LocalDate.of(2025, 8, 10));
        alquilerInvalido.setFechafin(LocalDate.of(2025, 8, 1));
        alquilerInvalido.setPlazas(5);
        alquilerInvalido.setUsuario_dni("11111111A");
        alquilerInvalido.setMatricula_embarcacion("XXX111");
        
        System.out.println();
        System.out.println("==== REQUEST 7: POST alquiler (invalid - fechas) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/api/alquileres", alquilerInvalido, Alquiler.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        Alquiler alquilerSocioNoExiste = new Alquiler();
        alquilerSocioNoExiste.setFechainicio(LocalDate.of(2026, 9, 1));
        alquilerSocioNoExiste.setFechafin(LocalDate.of(2026, 9, 3));
        alquilerSocioNoExiste.setPlazas(4);
        alquilerSocioNoExiste.setUsuario_dni("99999999Z");
        alquilerSocioNoExiste.setMatricula_embarcacion("XXX111");
        
        System.out.println();
        System.out.println("==== REQUEST 8: POST alquiler (invalid - socio no existe) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/api/alquileres", alquilerSocioNoExiste, Alquiler.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }


        Alquiler alquilerSocioNoPatron = new Alquiler();
        alquilerSocioNoPatron.setFechainicio(LocalDate.of(2026, 9, 1));
        alquilerSocioNoPatron.setFechafin(LocalDate.of(2026, 9, 3));
        alquilerSocioNoPatron.setPlazas(4);
        alquilerSocioNoPatron.setUsuario_dni("33333333A");
        alquilerSocioNoPatron.setMatricula_embarcacion("XXX111");
        
        System.out.println();
        System.out.println("==== REQUEST 9: POST alquiler (invalid - socio sin licencia de patrón) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/api/alquileres", alquilerSocioNoPatron, Alquiler.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

    /**
     * Método para realizar las pruebas PATCH de los endpoints de la API de alquileres.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS PATCH **********");

        // Clean Code - Reglas de nombrado: Sustituir numeracion por nombre con sentido (dniAcompanante1 -> firstCompanionDni)
        String firstCompanionDni = "22222222A";
        
        System.out.println("==== REQUEST 10: PATCH añadir acompañante ====");
        try {
            // Para PATCH con body, usamos patchForObject
            Alquiler alquilerActualizado = rest.patchForObject(
                baseURL + "/api/alquileres/" + alquilerCreadoId + "/acompanantes",
                firstCompanionDni,
                Alquiler.class
            );
            
            System.out.println(alquilerActualizado);
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        
        System.out.println();
        System.out.println("==== REQUEST 11: PATCH añadir acompanante (invalid - acompañante ya introducido) =====");
        try {
            Alquiler alquilerActualizado = rest.patchForObject(
                baseURL + "/api/alquileres/" + alquilerCreadoId + "/acompanantes",
                firstCompanionDni,
                Alquiler.class
            );
            
            System.out.println(alquilerActualizado);
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println("==== REQUEST 12: PATCH añadir acompanante (invalid - añadir al titular como acompañante) =====");
        try {
            Alquiler alquilerActualizado = rest.patchForObject(
                baseURL + "/api/alquileres/" + alquilerCreadoId + "/acompanantes",
                "11111111A",
                Alquiler.class
            );
            
            System.out.println(alquilerActualizado);
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 13: PATCH eliminar acompañante ====");
        try {
            // Para DELETE de acompañante, patchForObject también funciona
            Alquiler alquilerActualizado = rest.patchForObject(
                baseURL + "/api/alquileres/" + alquilerCreadoId + "/acompanantes/" + firstCompanionDni,
                null,
                Alquiler.class
            );
            System.out.println("Éxito. Alquiler actualizado:");
            System.out.println(alquilerActualizado);
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }
    }

    /**
     * Método para realizar las pruebas DELETE de los endpoints de la API de alquileres.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API     
     */
    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS DELETE **********");

        if (alquilerCreadoId != null) {
            System.out.println("==== REQUEST 14: DELETE cancelar alquiler futuro ====");
            
            try {
                rest.delete(baseURL + "/api/alquileres/" + alquilerCreadoId);
                System.out.println("Alquiler cancelado correctamente.");
                
                try {
                    ResponseEntity<Alquiler> response = rest.getForEntity(
                        baseURL + "/api/alquileres/" + alquilerCreadoId, 
                        Alquiler.class
                    );
                    System.out.println("Status code: " + response.getStatusCode());
                    System.out.println(response.getBody());
                } catch (HttpClientErrorException.NotFound e) {
                    System.out.println("Confirmado: El alquiler ya no existe");
                }
                
            } catch (HttpClientErrorException e) { 
                System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
            }
        }

        int alquilerInexistenteId = 9999;
        
        System.out.println();
        System.out.println("==== REQUEST 15: DELETE cancelar alquiler inexistente (error esperado) ====");
        try {
            rest.delete(baseURL + "/api/alquileres/" + alquilerInexistenteId);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode());
        }
    }
}