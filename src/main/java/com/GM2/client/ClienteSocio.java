package com.GM2.client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.GM2.model.domain.Socio;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente de la API de socios.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class ClienteSocio {

    private static String socioCreadoDni = null;

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
        
        if (socioCreadoDni != null) {
            sendPatchRequests(rest, baseURL);
        }
        
        sendDeleteRequests(rest, baseURL);
    }

    /**
     * Método para realizar las pruebas GET de los endpoints de la API de socios.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendGetRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [SOCIOS] PRUEBAS GET **********");

        System.out.println("==== REQUEST 1: GET all socios ====");
        try {
            ResponseEntity<Socio[]> response = rest.getForEntity(baseURL + "/api/socios", Socio[].class);
            System.out.println("Status code: " + response.getStatusCode());
            List<Socio> lista = Arrays.asList(response.getBody());
            for(Socio s : lista) {
                System.out.println(s);
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        String dniSocio = "11111111A";
        System.out.println();
        System.out.println("==== REQUEST 2: GET socio by DNI (" + dniSocio + ") ====");
        try {
            ResponseEntity<Socio> response = rest.getForEntity(
                baseURL + "/api/socios/" + dniSocio, 
                Socio.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Socio no encontrado con DNI: " + dniSocio);
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        String dniNoExiste = "99999999Z";
        System.out.println();
        System.out.println("==== REQUEST 3: GET socio by DNI inexistente (" + dniNoExiste + ") ====");
        try {
            ResponseEntity<Socio> response = rest.getForEntity(
                baseURL + "/api/socios/" + dniNoExiste, 
                Socio.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Resultado esperado: Socio no encontrado");
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }
    }

    /**
     * Método para realizar las pruebas POST de los endpoints de la API de socios.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [SOCIOS] PRUEBAS POST **********");

        // Crear socio sin inscripción (adulto)
        Socio nuevoSocio = new Socio();
        nuevoSocio.setNombre("Luis");
        nuevoSocio.setApellidos("Jimenez Marquez");
        nuevoSocio.setDni("55555555A");
        nuevoSocio.setFechaNacimiento(LocalDate.of(1998, 4, 22));
        nuevoSocio.setDireccion("Atalaya del Arcipreste");
        nuevoSocio.setTieneLicenciaPatron(true);
        
        System.out.println("==== REQUEST 4: POST crear socio sin inscripción (adulto) ====");
        try {
            ResponseEntity<Socio> response = rest.postForEntity(
                baseURL + "/api/socios/socioSinInscripcion", 
                nuevoSocio, 
                Socio.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            socioCreadoDni = response.getBody().getDni();
            System.out.println(response.getBody());
            
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        // Crear socio menor (hijo)
        Socio socioMenor = new Socio();
        socioMenor.setNombre("Luis");
        socioMenor.setApellidos("Jimenez Marquez");
        socioMenor.setDni("55555555A");
        socioMenor.setFechaNacimiento(LocalDate.of(2008, 4, 22));
        socioMenor.setDireccion("Atalaya del Arcipreste");
        
        System.out.println();
        System.out.println("==== REQUEST 5: POST crear socio menor (hijo) - sin inscripción ====");
        try {
            ResponseEntity<Socio> response = rest.postForEntity(
                baseURL + "/api/socios/socioSinInscripcion", 
                socioMenor, 
                Socio.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - Hijo necesita ID de inscripción");
        }

        // Crear socio inválido (sin datos obligatorios)
        Socio socioInvalido = new Socio();
        socioInvalido.setDni("");
        socioInvalido.setNombre("");
        
        System.out.println();
        System.out.println("==== REQUEST 6: POST crear socio inválido (sin datos) ====");
        try {
            ResponseEntity<Socio> response = rest.postForEntity(
                baseURL + "/api/socios/socioSinInscripcion", 
                socioInvalido, 
                Socio.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - Datos incompletos");
        }
    }

    /**
     * Método para realizar las pruebas PATCH de los endpoints de la API de socios.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [SOCIOS] PRUEBAS PATCH **********");

        System.out.println("==== REQUEST 7: PATCH actualizar dirección del socio ====");
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("direccion", "Nueva Calle Actualizada 456");
            
            Socio socioActualizado = rest.patchForObject(
                baseURL + "/api/socios/" + socioCreadoDni,
                updates,
                Socio.class
            );
            
            System.out.println("Socio actualizado: " + socioActualizado);
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 8: PATCH actualizar licencia de patrón ====");
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("tieneLicenciaPatron", true);
            
            Socio socioActualizado = rest.patchForObject(
                baseURL + "/api/socios/" + socioCreadoDni,
                updates,
                Socio.class
            );
            
            System.out.println("Socio actualizado: " + socioActualizado);
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 9: PATCH actualizar múltiples campos ====");
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("nombre", "Juan Carlos");
            updates.put("apellidos", "Pérez Martínez");
            updates.put("direccion", "Avenida Principal 789");
            
            Socio socioActualizado = rest.patchForObject(
                baseURL + "/api/socios/" + socioCreadoDni,
                updates,
                Socio.class
            );
            
            System.out.println("Socio actualizado: " + socioActualizado);
        
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 10: PATCH actualizar socio inexistente (error esperado) ====");
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("direccion", "Test");
            
            rest.patchForObject(
                baseURL + "/api/socios/00000000Z",
                updates,
                Socio.class
            );
        } catch (HttpClientErrorException e) { 
            System.out.println("Error esperado: " + e.getStatusCode() + " - Socio no encontrado"); 
        }
    }

    /**
     * Método para realizar las pruebas DELETE de los endpoints de la API de socios.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [SOCIOS] PRUEBAS DELETE **********");
        socioCreadoDni = "55555555A";

        if (socioCreadoDni != null) {
            System.out.println("==== REQUEST 11: DELETE eliminar socio sin inscripción ====");
            try {
                rest.delete(baseURL + "/api/socios/" + socioCreadoDni);
                System.out.println("Socio eliminado exitosamente");
                
                // Verificar que se eliminó
                try {
                    rest.getForEntity(
                        baseURL + "/api/socios" + socioCreadoDni,
                        Socio.class
                    );
                    System.out.println("Advertencia: El socio todavía existe");
                } catch (HttpClientErrorException.NotFound e) {
                    System.out.println("Verificación: Socio eliminado correctamente");
                }
                
            } catch (HttpClientErrorException e) { 
                System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
            }
        }

        System.out.println();
        System.out.println("==== REQUEST 12: DELETE eliminar socio vinculado a inscripción (error esperado) ====");
        try {
            String dniVinculado = "22222222A"; // Asumiendo que este socio tiene inscripción
            rest.delete(baseURL + "/api/socios", dniVinculado);
            System.out.println("Advertencia: Socio con inscripción eliminado (no debería permitirse)");
        } catch (HttpClientErrorException.Conflict e) { 
            System.out.println("Error esperado: " + e.getStatusCode() + " - Socio vinculado a inscripción"); 
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 13: DELETE eliminar socio inexistente (error esperado) ====");
        try {
            String dniInexistente = "00000000Z";
            rest.delete(baseURL + "/api/socios", dniInexistente);
        } catch (HttpClientErrorException.NotFound e) { 
            System.out.println("Error esperado: " + e.getStatusCode() + " - Socio no encontrado"); 
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }
    }
}
