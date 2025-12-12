package com.GM2.client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Acompanante;
import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ClienteAlquiler {

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
        System.out.println("\n********** [ALQUILERES] PRUEBAS GET **********");

        // 1. Listar todos los alquileres
        System.out.println("==== REQUEST 1: GET all alquileres ====");
        try {
            ResponseEntity<Alquiler[]> response = rest.getForEntity(baseURL + "/api/alquileres", Alquiler[].class);
            System.out.println("Status code: " + response.getStatusCode());
            List<Alquiler> lista = Arrays.asList(response.getBody());
            for(Alquiler a : lista) {
                System.out.println(a);
                System.out.println("------------------------");
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        // 2. Obtener alquileres futuros a partir de una fecha
        LocalDate fechaFutura = LocalDate.of(2025, 1, 1);
        System.out.println();
        System.out.println("==== REQUEST 2: GET alquileres futuros (a partir de " + fechaFutura + ") ====");
        try {
            ResponseEntity<Alquiler[]> response = rest.getForEntity(
                baseURL + "/api/alquileres?fecha=" + fechaFutura, 
                Alquiler[].class
            );
            System.out.println("Status code: " + response.getStatusCode());
            List<Alquiler> lista = Arrays.asList(response.getBody());
            for(Alquiler a : lista) {
                System.out.println(a);
                System.out.println("------------------------");
            }
            if (lista.isEmpty()) {
                System.out.println("No hay alquileres futuros");
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        // 3. Obtener alquiler por ID específico
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

        // 4. Obtener embarcaciones disponibles en un rango de fechas
        LocalDate fechaInicio = LocalDate.of(2025, 6, 1);
        LocalDate fechaFin = LocalDate.of(2025, 6, 7);
        System.out.println();
        System.out.println("==== REQUEST 4: GET embarcaciones disponibles (" + fechaInicio + " a " + fechaFin + ") ====");
        try {
            ResponseEntity<Embarcacion[]> response = rest.getForEntity(
                baseURL + "/api/alquileres/disponibles?fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin, 
                Embarcacion[].class
            );
            System.out.println("Status code: " + response.getStatusCode());
            List<Embarcacion> lista = Arrays.asList(response.getBody());
            for(Embarcacion e : lista) {
                System.out.println(e);
                System.out.println("------------------------");
            }
            if (lista.isEmpty()) {
                System.out.println("No hay embarcaciones disponibles en esas fechas");
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }
    }

    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS POST **********");

        // 5. Crear alquiler válido
        Alquiler nuevoAlquiler = new Alquiler();
        nuevoAlquiler.setFechainicio(LocalDate.of(2025, 12, 20));
        nuevoAlquiler.setFechafin(LocalDate.of(2025, 12, 22));
        nuevoAlquiler.setPlazas(5);
        nuevoAlquiler.setUsuario_dni("11111111A");
        nuevoAlquiler.setMatricula_embarcacion("XXX111");
        
        System.out.println("==== REQUEST 5: POST alquiler (valid) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(
                baseURL + "/api/alquileres", 
                nuevoAlquiler, 
                Alquiler.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
            
            // Limpiar
            if (response.getBody() != null) {
                rest.delete(baseURL + "/api/alquileres/" + response.getBody().getId());
            }
            
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // 6. Crear alquiler inválido (fechas incorrectas)
        Alquiler alquilerInvalido = new Alquiler();
        alquilerInvalido.setFechainicio(LocalDate.of(2025, 8, 10));
        alquilerInvalido.setFechafin(LocalDate.of(2025, 8, 1));
        alquilerInvalido.setPlazas(5);
        alquilerInvalido.setUsuario_dni("11111111A");
        alquilerInvalido.setMatricula_embarcacion("XXX111");
        
        System.out.println();
        System.out.println("==== REQUEST 6: POST alquiler (invalid - fechas) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/api/alquileres", alquilerInvalido, Alquiler.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        // 7. Crear alquiler inválido (socio no existe)
        Alquiler alquilerSocioNoExiste = new Alquiler();
        alquilerSocioNoExiste.setFechainicio(LocalDate.of(2026, 9, 1));
        alquilerSocioNoExiste.setFechafin(LocalDate.of(2026, 9, 3));
        alquilerSocioNoExiste.setPlazas(4);
        alquilerSocioNoExiste.setUsuario_dni("99999999Z");
        alquilerSocioNoExiste.setMatricula_embarcacion("XXX111");
        
        System.out.println();
        System.out.println("==== REQUEST 7: POST alquiler (invalid - socio no existe) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/api/alquileres", alquilerSocioNoExiste, Alquiler.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS PATCH **********");

        // Crear alquiler temporal
        Alquiler alquilerTemp = new Alquiler();
        alquilerTemp.setFechainicio(LocalDate.of(2025, 7, 1));
        alquilerTemp.setFechafin(LocalDate.of(2025, 7, 3));
        alquilerTemp.setPlazas(3);
        alquilerTemp.setUsuario_dni("11111111A");
        alquilerTemp.setMatricula_embarcacion("XXX111");
        
        Integer alquilerId = null;
        
        try {
            ResponseEntity<Alquiler> responseCreate = rest.postForEntity(
                baseURL + "/api/alquileres", 
                alquilerTemp, 
                Alquiler.class
            );
            
            if (responseCreate.getStatusCode().is2xxSuccessful()) {
                alquilerId = responseCreate.getBody().getId();
                System.out.println("Alquiler temporal creado con ID: " + alquilerId);
            }
        } catch (Exception e) {
            System.out.println("Error creando alquiler temporal: " + e.getMessage());
            return;
        }

        // 8. Añadir acompañante
        String dniAcompanante = "22222222A";
        
        System.out.println("\n==== REQUEST 8: PATCH añadir acompañante ====");
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Content-Type", "text/plain");
            
            org.springframework.http.HttpEntity<String> entity = 
                new org.springframework.http.HttpEntity<>(dniAcompanante, headers);
            
            ResponseEntity<Alquiler> response = rest.exchange(
                baseURL + "/api/alquileres/" + alquilerId + "/acompanantes",
                org.springframework.http.HttpMethod.PATCH,
                entity,
                Alquiler.class
            );
            
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // 9. Eliminar acompañante
        System.out.println("\n==== REQUEST 9: PATCH eliminar acompañante ====");
        try {
            Alquiler a = rest.patchForObject(
                baseURL + "/api/alquileres/" + alquilerId + "/acompanantes/" + dniAcompanante,
                null,
                Alquiler.class
            );
            System.out.println("Éxito. Alquiler actualizado:");
            System.out.println(a);
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // 10. Añadir otro acompañante
        String dniOtroAcompanante = "33333333A";
        
        System.out.println("\n==== REQUEST 10: PATCH añadir otro acompañante ====");
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Content-Type", "text/plain");
            
            org.springframework.http.HttpEntity<String> entity = 
                new org.springframework.http.HttpEntity<>(dniOtroAcompanante, headers);
            
            ResponseEntity<Alquiler> response = rest.exchange(
                baseURL + "/api/alquileres/" + alquilerId + "/acompanantes",
                org.springframework.http.HttpMethod.PATCH,
                entity,
                Alquiler.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
        
        // Limpiar
        System.out.println("\nLimpieza: Eliminando alquiler temporal ID: " + alquilerId);
        try {
            rest.delete(baseURL + "/api/alquileres/" + alquilerId);
        } catch (Exception e) {
            System.out.println("Error eliminando alquiler temporal: " + e.getMessage());
        }
    }

    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS DELETE **********");

        // 11. Cancelar alquiler futuro
        System.out.println("==== REQUEST 11: DELETE cancelar alquiler futuro ====");
        
        Alquiler alquilerParaCancelar = new Alquiler();
        alquilerParaCancelar.setFechainicio(LocalDate.of(2026, 10, 1));
        alquilerParaCancelar.setFechafin(LocalDate.of(2026, 10, 3));
        alquilerParaCancelar.setPlazas(2);
        alquilerParaCancelar.setUsuario_dni("11111111A");
        alquilerParaCancelar.setMatricula_embarcacion("XXX111");
        
        try {
            ResponseEntity<Alquiler> responseCreate = rest.postForEntity(
                baseURL + "/api/alquileres", 
                alquilerParaCancelar, 
                Alquiler.class
            );
            
            if (responseCreate.getStatusCode().is2xxSuccessful()) {
                Alquiler creado = responseCreate.getBody();
                int idParaCancelar = creado.getId();
                
                System.out.println("Alquiler creado: " + creado);
                
                rest.delete(baseURL + "/api/alquileres/" + idParaCancelar);
                System.out.println("Alquiler cancelado correctamente.");
                
                try {
                    rest.getForEntity(baseURL + "/api/alquileres/" + idParaCancelar, Alquiler.class);
                } catch (HttpClientErrorException.NotFound e) {
                    System.out.println("Confirmado: El alquiler ya no existe");
                }
            }
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // 12. Intentar cancelar alquiler inexistente
        int alquilerInexistenteId = 9999;
        
        System.out.println("\n==== REQUEST 12: DELETE cancelar alquiler inexistente ====");
        try {
            rest.delete(baseURL + "/api/alquileres/" + alquilerInexistenteId);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }
}