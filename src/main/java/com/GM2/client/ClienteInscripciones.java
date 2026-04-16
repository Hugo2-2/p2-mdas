package com.GM2.client;

import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Inscripcion;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente de la API de inscripciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class ClienteInscripciones {

    private static Integer inscripcionCreadaId = null;
    private static String dniTitularTest = null;

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
        
        if (inscripcionCreadaId != null) {
            sendPatchRequests(rest, baseURL);
        }
        
        sendDeleteRequests(rest, baseURL);
    }

    /**
     * Método para realizar las pruebas GET de los endpoints de la API de inscripciones.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendGetRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [INSCRIPCIONES] PRUEBAS GET **********");

        System.out.println("==== REQUEST 1: GET inscripciones individuales ====");
        try {
            ResponseEntity<Inscripcion[]> response = rest.getForEntity(
                baseURL + "/api/inscripciones/individuales", 
                Inscripcion[].class
            );
            System.out.println("Status code: " + response.getStatusCode());
            List<Inscripcion> lista = Arrays.asList(response.getBody());
            for(Inscripcion i : lista) {
                System.out.println(i);
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 2: GET inscripciones familiares ====");
        try {
            ResponseEntity<Inscripcion[]> response = rest.getForEntity(
                baseURL + "/api/inscripciones/familiares", 
                Inscripcion[].class
            );
            System.out.println("Status code: " + response.getStatusCode());
            List<Inscripcion> lista = Arrays.asList(response.getBody());
            for(Inscripcion i : lista) {
                System.out.println(i);
            }
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        String dniTitular = "77777777O";
        System.out.println();
        System.out.println("==== REQUEST 3: GET inscripción by DNI titular (" + dniTitular + ") ====");
        try {
            ResponseEntity<Inscripcion> response = rest.getForEntity(
                baseURL + "/api/inscripciones/titular/" + dniTitular, 
                Inscripcion.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Inscripción no encontrada para titular: " + dniTitular);
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }

        String dniNoExiste = "99999999Z";
        System.out.println();
        System.out.println("==== REQUEST 4: GET inscripción by DNI titular inexistente (" + dniNoExiste + ") ====");
        try {
            ResponseEntity<Inscripcion> response = rest.getForEntity(
                baseURL + "/api/inscripciones/titular/" + dniNoExiste, 
                Inscripcion.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Resultado esperado: Inscripción no encontrada");
        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage()); 
        }
    }

    /**
     * Método para realizar las pruebas POST de los endpoints de la API de inscripciones.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [INSCRIPCIONES] PRUEBAS POST **********");

        // Crear inscripción para un titular existente
        System.out.println("==== REQUEST 5: POST crear inscripción para titular existente ====");
        try {
            // Primero necesitamos asegurarnos de que existe un socio titular sin inscripción
            dniTitularTest = "11111111A"; // DNI de prueba
            
            Inscripcion nuevaInscripcion = new Inscripcion();
            nuevaInscripcion.setTitularMemberId(dniTitularTest);
            
            ResponseEntity<Inscripcion> response = rest.postForEntity(
                baseURL + "/api/inscripciones", 
                nuevaInscripcion, 
                Inscripcion.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            Inscripcion inscripcionCreada = response.getBody();
            System.out.println(inscripcionCreada);
            
            inscripcionCreadaId = inscripcionCreada.getId();
            
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Error: Socio titular no encontrado");
        } catch (HttpClientErrorException.Conflict e) {
            System.out.println("Error: El titular ya tiene una inscripción");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 6: POST crear inscripción duplicada (error esperado) ====");
        try {
            Inscripcion inscripcionDuplicada = new Inscripcion();
            inscripcionDuplicada.setTitularMemberId(dniTitularTest);
            
            rest.postForEntity(
                baseURL + "/api/inscripciones", 
                inscripcionDuplicada, 
                Inscripcion.class
            );
            System.out.println("Advertencia: Se permitió crear inscripción duplicada");
        } catch (HttpClientErrorException.Conflict e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - Inscripción ya existe");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 7: POST crear inscripción para titular inexistente (error esperado) ====");
        try {
            Inscripcion inscripcionInvalida = new Inscripcion();
            inscripcionInvalida.setTitularMemberId("00000000Z");
            
            rest.postForEntity(
                baseURL + "/api/inscripciones", 
                inscripcionInvalida, 
                Inscripcion.class
            );
            System.out.println("Advertencia: Se creó inscripción para titular inexistente");
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Error esperado: " + e.getStatusCode() + " - Titular no encontrado");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }
    }

    /**
     * Método para realizar las pruebas PATCH de los endpoints de la API de inscripciones.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [INSCRIPCIONES] PRUEBAS PATCH **********");

        System.out.println("==== REQUEST 8: PATCH añadir segundo adulto a inscripción: "+ inscripcionCreadaId +" ====");
        try {
            String dniSegundoAdulto = "11111111C";
            
            Inscripcion inscripcionActualizada = rest.patchForObject(
                baseURL + "/api/inscripciones/addMiembro/" + inscripcionCreadaId,
                dniSegundoAdulto,
                Inscripcion.class
            );
            
            System.out.println("Inscripción actualizada: " + inscripcionActualizada);
        
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Error: Inscripción o socio no encontrado");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 9: PATCH añadir hijo a inscripción familiar ====");
        try {
            String dniHijo = "55555555A";
            
            Inscripcion inscripcionActualizada = rest.patchForObject(
                baseURL + "/api/inscripciones/addMiembro/" + inscripcionCreadaId,
                dniHijo,
                Inscripcion.class
            );
            
            System.out.println("Inscripción actualizada: " + inscripcionActualizada);
        
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Error: Inscripción o hijo no encontrado");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 10: PATCH remover hijo de inscripción familiar ====");
        try {
            String dniHijo = "55555555A";
            
            Inscripcion inscripcionActualizada = rest.patchForObject(
                baseURL + "/api/inscripciones/removeMiembro/" + inscripcionCreadaId,
                dniHijo,
                Inscripcion.class
            );
            
            System.out.println("Inscripción actualizada: " + inscripcionActualizada);
        
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Error: Inscripción no encontrada");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 11: PATCH remover segundo adulto de inscripción ====");
        try {
            String dniSegundoAdulto = "11111111C";
            
            Inscripcion inscripcionActualizada = rest.patchForObject(
                baseURL + "/api/inscripciones/removeMiembro/" + inscripcionCreadaId,
                dniSegundoAdulto,
                Inscripcion.class
            );
            
            System.out.println("Inscripción actualizada: " + inscripcionActualizada);
        
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Error: Inscripción no encontrada");
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 12: PATCH añadir miembro a inscripción inexistente (error esperado) ====");
        try {
            rest.patchForObject(
                baseURL + "/api/inscripciones/addMiembro/99999",
                "99887766F",
                Inscripcion.class
            );
        } catch (HttpClientErrorException.NotFound e) { 
            System.out.println("Error esperado: " + e.getStatusCode() + " - Inscripción no encontrada"); 
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }
    }

    /**
     * Método para realizar las pruebas DELETE de los endpoints de la API de inscripciones.
     * 
     * @param rest Instancia de RestTemplate
     * @param baseURL URL base de la API
     */
    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [INSCRIPCIONES] PRUEBAS DELETE **********");


        if (dniTitularTest != null) {
            System.out.println("==== REQUEST 13: DELETE eliminar inscripción creada ====");
            try {
                rest.delete(baseURL + "/api/inscripciones/" + dniTitularTest);
                System.out.println("Inscripción eliminada exitosamente");
                
                // Verificar que se eliminó
                try {
                    rest.getForEntity(
                        baseURL + "/api/inscripciones/titular/" + "11111111A",
                        Inscripcion.class
                    );
                    System.out.println("Advertencia: La inscripción todavía existe");
                } catch (HttpClientErrorException.NotFound e) {
                    System.out.println("Verificación: Inscripción eliminada correctamente");
                }
                
            } catch (HttpClientErrorException e) { 
                System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); 
            }
        }

        System.out.println();
        System.out.println("==== REQUEST 14: DELETE eliminar inscripción inexistente (error esperado) ====");
        try {
            String dniInexistente = "00000000Z";
            rest.delete(baseURL + "/api/inscripciones", dniInexistente);
        } catch (HttpClientErrorException.NotFound e) { 
            System.out.println("Error esperado: " + e.getStatusCode() + " - Inscripción no encontrada"); 
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }

        System.out.println();
        System.out.println("==== REQUEST 15: DELETE eliminar inscripción con datos vacíos (error esperado) ====");
        try {
            rest.delete(baseURL + "/api/inscripciones", "");
        } catch (HttpClientErrorException.BadRequest e) { 
            System.out.println("Error esperado: " + e.getStatusCode() + " - Datos incompletos"); 
        } catch (HttpClientErrorException e) { 
            System.out.println("Error: " + e.getStatusCode()); 
        }
    }
}
