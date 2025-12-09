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
        try {
            ResponseEntity<Alquiler[]> response = rest.getForEntity(baseURL + "/api/alquileres", Alquiler[].class);
            List<Alquiler> lista = Arrays.asList(response.getBody());
            System.out.println("==== REQUEST 1: GET all alquileres ====");
            for(Alquiler a : lista) {
                System.out.println("- ID: " + a.getId() + 
                ", Fechainicio: " + a.getFechainicio() + 
                ", Fechafin: " + a.getFechafin() + 
                ", Precio: " + a.getPrecio() + 
                ", Plazas: " + a.getPlazas() + 
                ", Usuario DNI: " + a.getUsuario_dni() + 
                ", Matricula Embarcación: " + a.getMatricula_embarcacion());
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        // 2. Obtener alquileres futuros a partir de una fecha
        try {
            LocalDate fechaFutura = LocalDate.of(2025, 1, 1);
            ResponseEntity<Alquiler[]> response = rest.getForEntity(
                baseURL + "/api/alquileres?fecha=" + fechaFutura, 
                Alquiler[].class
            );
            List<Alquiler> lista = Arrays.asList(response.getBody());
            System.out.println("\n==== REQUEST 2: GET alquileres futuros (a partir de " + fechaFutura + ") ====");
            if (lista.isEmpty()) {
                System.out.println("No hay alquileres futuros");
            } else {
                for(Alquiler a : lista) {
                    System.out.println("- ID: " + a.getId() + 
                ", Fechainicio: " + a.getFechainicio() + 
                ", Fechafin: " + a.getFechafin() + 
                ", Precio: " + a.getPrecio() + 
                ", Plazas: " + a.getPlazas() + 
                ", Usuario DNI: " + a.getUsuario_dni() + 
                ", Matricula Embarcación: " + a.getMatricula_embarcacion());
                }
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        // 3. Obtener alquiler por ID específico
        try {
            int alquilerId = 1; // Cambia por un ID existente
            ResponseEntity<Alquiler> response = rest.getForEntity(
                baseURL + "/api/alquileres/" + alquilerId, 
                Alquiler.class
            );
            System.out.println("\n==== REQUEST 3: GET alquiler by ID (" + alquilerId + ") ====");
            Alquiler alquiler = response.getBody();
            System.out.println("Alquiler encontrado:\nID: " + alquiler.getId() + 
                ", Fechainicio: " + alquiler.getFechainicio() + 
                ", Fechafin: " + alquiler.getFechafin() + 
                ", Precio: " + alquiler.getPrecio() + 
                ", Plazas: " + alquiler.getPlazas() + 
                ", Usuario DNI: " + alquiler.getUsuario_dni() + 
                ", Matricula Embarcación: " + alquiler.getMatricula_embarcacion());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Alquiler no encontrado con ID: " + 1);
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        // 4. Obtener embarcaciones disponibles en un rango de fechas
        try {
            LocalDate fechaInicio = LocalDate.of(2025, 6, 1);
            LocalDate fechaFin = LocalDate.of(2025, 6, 7);
            ResponseEntity<Embarcacion[]> response = rest.getForEntity(
                baseURL + "/api/alquileres/disponibles?fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin, 
                Embarcacion[].class
            );
            List<Embarcacion> lista = Arrays.asList(response.getBody());
            System.out.println("\n==== REQUEST 4: GET embarcaciones disponibles (" + fechaInicio + " a " + fechaFin + ") ====");
            if (lista.isEmpty()) {
                System.out.println("No hay embarcaciones disponibles en esas fechas");
            } else {
                for(Embarcacion e : lista) {
                    System.out.println("- " + e.getNombre() + 
                        " (" + e.getMatricula() + 
                        "), Plazas: " + e.getPlazas() + 
                        ", Tipo: " + e.getTipo() +
                        ", Dimensiones: " + e.getDimensiones() + "m²");
                }
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
        nuevoAlquiler.setUsuario_dni("11111111A"); // Socio existente con licencia
        nuevoAlquiler.setMatricula_embarcacion("XXX111"); // Embarcación existente
        
        System.out.println("==== REQUEST 5: POST alquiler (valid) ====");
        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(
                baseURL + "/api/alquileres", 
                nuevoAlquiler, 
                Alquiler.class
            );
            Alquiler creado = response.getBody();
            System.out.println("Status: " + response.getStatusCode() + 
                " | Creado ID: " + creado.getId() + 
                ", Precio: " + creado.getPrecio() + "€");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // 6. Crear alquiler inválido (fechas incorrectas)
        Alquiler alquilerInvalido = new Alquiler();
        alquilerInvalido.setFechainicio(LocalDate.of(2025, 8, 10));
        alquilerInvalido.setFechafin(LocalDate.of(2025, 8, 1)); // Fecha fin antes de inicio
        alquilerInvalido.setPlazas(5);
        alquilerInvalido.setUsuario_dni("11111111A");
        alquilerInvalido.setMatricula_embarcacion("XXX111");
        
        System.out.println("\n==== REQUEST 6: POST alquiler (invalid - fechas) ====");
        try {
            rest.postForEntity(baseURL + "/api/alquileres", alquilerInvalido, Alquiler.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode());
        }

        // 7. Crear alquiler inválido (socio no existe)
        Alquiler alquilerSocioNoExiste = new Alquiler();
        alquilerSocioNoExiste.setFechainicio(LocalDate.of(2026, 9, 1));
        alquilerSocioNoExiste.setFechafin(LocalDate.of(2026, 9, 3));
        alquilerSocioNoExiste.setPlazas(4);
        alquilerSocioNoExiste.setUsuario_dni("99999999Z"); // Socio que no existe
        alquilerSocioNoExiste.setMatricula_embarcacion("XXX111");
        
        System.out.println("\n==== REQUEST 7: POST alquiler (invalid - socio no existe) ====");
        try {
            rest.postForEntity(baseURL + "/api/alquileres", alquilerSocioNoExiste, Alquiler.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode());
        }
    }

    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS PATCH **********");

        // 8. Añadir acompañante a alquiler futuro
        int alquilerId = 10; // Cambia por ID de alquiler futuro existente
        String dniAcompanante = "22222222A"; // Socio existente que no sea el titular
        
        System.out.println("==== REQUEST 8: PATCH añadir acompañante ====");
        System.out.println("URL: " + baseURL + "/api/alquileres/" + alquilerId + "/acompanantes");
        System.out.println("Body: " + dniAcompanante);
        System.out.println("Headers: Content-Type: text/plain");
        
        try {
            // Crear la URL (SIN query parameter)
            String url = baseURL + "/api/alquileres/" + alquilerId + "/acompanantes";
            
            // Configurar headers para texto plano
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Content-Type", "text/plain"); // IMPORTANTE: text/plain, no application/json
            
            // Crear la entidad HTTP con el DNI como string en el body
            org.springframework.http.HttpEntity<String> entity = 
                new org.springframework.http.HttpEntity<>(dniAcompanante, headers);
            
            // Realizar la petición PATCH usando exchange()
            ResponseEntity<Alquiler> response = rest.exchange(
                url,
                org.springframework.http.HttpMethod.PATCH,
                entity,
                Alquiler.class
            );
            
            System.out.println("   ✓ ÉXITO - Status: " + response.getStatusCode());
            Alquiler a = response.getBody();
            System.out.println("- ID: " + a.getId() + 
                ", Fechainicio: " + a.getFechainicio() + 
                ", Fechafin: " + a.getFechafin() + 
                ", Precio: " + a.getPrecio() + 
                ", Plazas: " + a.getPlazas() + 
                ", Usuario DNI: " + a.getUsuario_dni() + 
                ", Matricula Embarcación: " + a.getMatricula_embarcacion());

            if (a.getAcompanantes() != null && !a.getAcompanantes().isEmpty()) {
                System.out.println("\n  Acompañantes:");
                for (Acompanante acompanante : a.getAcompanantes()) {
                    System.out.println("- DNI: " + acompanante.getDni() + " (ID: " + acompanante.getId() + ")");
                }
            } else {
                System.out.println("\n  Acompañantes: Ninguno");
            }
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // 9. Eliminar acompañante de alquiler futuro
        String dniAcompananteEliminar = "22222222A"; // Acompañante existente en el alquiler
        
        System.out.println("\n==== REQUEST 9: PATCH eliminar acompañante ====");
        System.out.println("URL: " + baseURL + "/api/alquileres/" + alquilerId + "/acompanantes/" + dniAcompananteEliminar);
        
        try {
            Alquiler a = rest.patchForObject(
                baseURL + "/api/alquileres/" + alquilerId + "/acompanantes/" + dniAcompananteEliminar,
                null, // Body vacío
                Alquiler.class
            );
            System.out.println("- ID: " + a.getId() + 
                ", Fechainicio: " + a.getFechainicio() + 
                ", Fechafin: " + a.getFechafin() + 
                ", Precio: " + a.getPrecio() + 
                ", Plazas: " + a.getPlazas() + 
                ", Usuario DNI: " + a.getUsuario_dni() + 
                ", Matricula Embarcación: " + a.getMatricula_embarcacion());

                if (a.getAcompanantes() != null && !a.getAcompanantes().isEmpty()) {
                    System.out.println("\n  Acompañantes:");
                    for (Acompanante acompanante : a.getAcompanantes()) {
                        System.out.println("- DNI: " + acompanante.getDni() + " (ID: " + acompanante.getId() + ")");
                    }
                } else {
                    System.out.println("\n  Acompañantes: Ninguno");
                }
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // 10. Intentar añadir acompañante a alquiler pasado (error)
        int alquilerPasadoId = 2; // Cambia por ID de alquiler pasado
        String dniOtroAcompanante = "33333333A";
        
        System.out.println("\n==== REQUEST 10: PATCH añadir acompañante (alquiler pasado - error esperado) ====");
        try {
            rest.patchForObject(
                baseURL + "/api/alquileres/" + alquilerPasadoId + "/acompanantes?dniSocio=" + dniOtroAcompanante,
                null,
                Alquiler.class
            );
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode());
        }
    }

    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [ALQUILERES] PRUEBAS DELETE **********");

        // 11. Cancelar alquiler futuro
        // Primero creamos un alquiler para cancelarlo
        Alquiler alquilerParaCancelar = new Alquiler();
        alquilerParaCancelar.setFechainicio(LocalDate.of(2026, 10, 1));
        alquilerParaCancelar.setFechafin(LocalDate.of(2026, 10, 3));
        alquilerParaCancelar.setPlazas(1);
        alquilerParaCancelar.setUsuario_dni("11111111A");
        alquilerParaCancelar.setMatricula_embarcacion("XXX111");
        
        System.out.println("==== REQUEST 11: DELETE cancelar alquiler futuro ====");
        
        try {
            // Creamos un alquiler para luego cancelarlo
            ResponseEntity<Alquiler> responseCreate = rest.postForEntity(
                baseURL + "/api/alquileres", 
                alquilerParaCancelar, 
                Alquiler.class
            );
            
            if (responseCreate.getStatusCode().is2xxSuccessful()) {
                Alquiler creado = responseCreate.getBody();
                int idParaCancelar = creado.getId();
                
                System.out.println("Alquiler creado con ID: " + idParaCancelar + ", cancelando...");
                
                // Cancelamos el alquiler
                rest.delete(baseURL + "/api/alquileres/" + idParaCancelar);
                System.out.println("Alquiler cancelado correctamente.");
            }
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }

        // 12. Intentar cancelar alquiler pasado (error)
        int alquilerPasadoId = 1; // Cambia por ID de alquiler pasado
        
        System.out.println("\n==== REQUEST 12: DELETE cancelar alquiler pasado (error esperado) ====");
        try {
            rest.delete(baseURL + "/api/alquileres/" + alquilerPasadoId);
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode());
        }
    }
}