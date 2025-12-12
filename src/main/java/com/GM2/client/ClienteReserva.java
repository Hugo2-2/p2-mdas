package com.GM2.client;

// Importaciones necesarias para manejo de fechas, colecciones y comunicación HTTP con Spring
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Reserva;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException; // Importante para capturar errores del servidor (código 500)
import org.springframework.web.client.RestTemplate;

public class ClienteReserva {

    // Variable estática (compartida por toda la clase) para guardar el ID de la reserva que crearemos en el POST.
    // Se inicializa en null. Si el POST funciona, guardaremos aquí el ID (ej: 1, 2, etc.) para luego modificarla (PATCH) o borrarla (DELETE).
    private static Integer reservaCreadaId = null;

    public static void main(String[] args) {
        // Inicializamos 'rest', el objeto que nos permite hacer peticiones HTTP (GET, POST, etc.).
        // Se le pasa 'HttpComponentsClientHttpRequestFactory' explícitamente porque el RestTemplate estándar de Java
        // a veces da problemas con el método PATCH. Esta configuración lo soluciona.
        RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        // Definimos la URL base donde está corriendo nuestra API (Backend).
        String baseURL = "http://localhost:8080";

        // --- INICIO DEL FLUJO DE EJECUCIÓN ---

        // 1. Llamamos al método que prueba las lecturas (GET).
        sendGetRequests(rest, baseURL);

        // 2. Llamamos al método que prueba la creación (POST).
        sendPostRequests(rest, baseURL);

        // 3. Lógica de control: Solo intentamos Modificar (PATCH) y Borrar (DELETE) si el paso anterior (POST) tuvo éxito.
        // Si reservaCreadaId sigue siendo null, significa que falló el POST y no tenemos ID para borrar.
        if (reservaCreadaId != null) {
            sendPatchRequests(rest, baseURL);
            sendDeleteRequests(rest, baseURL);
        } else {
            // Mensaje de aviso si no se pudo continuar el flujo.
            System.out.println("⚠ ALERTA: No se ejecutan PATCH/DELETE porque la reserva no se pudo crear (POST falló).");
        }
    }

    // ==========================================
    // 1. GET (Lectura) - Métodos para obtener datos
    // ==========================================
    private static void sendGetRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS GET **********");

        // --- PRUEBA 1: Obtener TODAS las reservas ---
        System.out.println("==== REQUEST 1: GET all reservas ====");
        try {
            // Hacemos una petición GET a "/api/reservas". Esperamos recibir un Array de objetos Reserva (Reserva[].class).
            // 'getForEntity' nos devuelve la respuesta completa (c headers, status code y body).
            ResponseEntity<Reserva[]> response = rest.getForEntity(baseURL + "/api/reservas", Reserva[].class);

            // Imprimimos el código de estado HTTP (ej: 200 OK).
            System.out.println("Status code: " + response.getStatusCode());

            // Si hay datos en el cuerpo de la respuesta...
            if (response.getBody() != null) {
                // Convertimos el Array a una Lista para poder recorrerla fácilmente con un bucle.
                List<Reserva> lista = Arrays.asList(response.getBody());
                for(Reserva r : lista) {
                    // Llamamos al método auxiliar para imprimir los datos bonitos en consola.
                    imprimirReserva(r);
                    System.out.println("------------------------");
                }
            }
        } catch (Exception e) {
            // Si algo falla (ej: servidor apagado), imprimimos el error.
            System.out.println("Error: " + e.getMessage());
        }

        // --- PRUEBA 2: Obtener reservas filtradas por fecha ---
        LocalDate fechaFiltro = LocalDate.of(2025, 1, 1); // Definimos una fecha de corte.
        System.out.println();
        System.out.println("==== REQUEST 2: GET reservas futuras (a partir de " + fechaFiltro + ") ====");
        try {
            // Hacemos GET pasando un parámetro en la URL (?fecha=2025-01-01).
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
                // Si la lista está vacía, avisamos por consola.
                if (lista.isEmpty()) {
                    System.out.println("No hay reservas futuras");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // --- PRUEBA 3: Obtener una reserva específica por su ID ---
        int reservaId = 2; // Suponemos que el ID 2 existe en la base de datos para probar.
        System.out.println();
        System.out.println("==== REQUEST 3: GET reserva by ID (" + reservaId + ") ====");
        try {
            // Hacemos GET a "/api/reservas/2". Esperamos un único objeto Reserva.class (no un array).
            ResponseEntity<Reserva> response = rest.getForEntity(
                    baseURL + "/api/reservas/" + reservaId,
                    Reserva.class
            );
            System.out.println("Status code: " + response.getStatusCode());
            imprimirReserva(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            // Capturamos específicamente el error 404 (Not Found) si el ID no existe.
            System.out.println("Reserva no encontrada con ID: " + reservaId);
        } catch (Exception e) {
            // Capturamos cualquier otro error genérico.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==========================================
    // 2. POST (Creación) - Métodos para guardar datos
    // ==========================================
    private static void sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS POST **********");

        // --- PRUEBA 4: Crear una reserva VÁLIDA ---
        Reserva nuevaReserva = new Reserva();
        // Configuramos los datos del objeto Java. Usamos una fecha lejana (2029) para asegurar que no choque con otras reservas.
        nuevaReserva.setFecha(LocalDate.of(2026, 3, 22));
        nuevaReserva.setPlazas(4);
        nuevaReserva.setUsuario_id("11111111A"); // DNI de un usuario existente (clave ajena)
        nuevaReserva.setMatricula_embarcacion("XXX111"); // Matrícula existente
        nuevaReserva.setDescripcion("Viaje por el Atlántico");

        System.out.println("==== REQUEST 4: POST reserva (valid) ====");
        try {
            // Enviamos la petición POST. El objeto 'nuevaReserva' se convierte automáticamente a JSON.
            ResponseEntity<Reserva> response = rest.postForEntity(
                    baseURL + "/api/reservas",
                    nuevaReserva,
                    Reserva.class
            );
            System.out.println("Status code: " + response.getStatusCode());

            if (response.getBody() != null) {
                // ¡IMPORTANTE! Aquí capturamos el ID que la base de datos generó automáticamente.
                // Lo guardamos en la variable estática 'reservaCreadaId' para usarlo en el PATCH y DELETE más adelante.
                reservaCreadaId = response.getBody().getId();
                imprimirReserva(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            // Si falla por validación (ej: 400 Bad Request), imprimimos el mensaje del servidor.
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        // --- PRUEBA 5: Intentar crear reserva INVÁLIDA (Exceso de plazas) ---
        Reserva reservaInvalida = new Reserva();
        reservaInvalida.setFecha(LocalDate.of(2026, 3, 23));
        reservaInvalida.setPlazas(50); // Ponemos 50 plazas, lo cual debería violar la lógica de negocio.
        reservaInvalida.setUsuario_id("11111111A");
        reservaInvalida.setMatricula_embarcacion("XXX111");

        System.out.println();
        System.out.println("==== REQUEST 5: POST reserva (invalid - plazas) ====");
        try {
            rest.postForEntity(baseURL + "/api/reservas", reservaInvalida, Reserva.class);
        } catch (HttpClientErrorException e) {
            // Esperamos que falle, así que capturamos el error y lo mostramos como "Error esperado".
            System.out.println("Error esperado: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        // --- PRUEBA 6: Intentar crear reserva INVÁLIDA (Usuario no existe) ---
        Reserva reservaSocioNoExiste = new Reserva();
        reservaSocioNoExiste.setFecha(LocalDate.of(2026, 3, 24));
        reservaSocioNoExiste.setPlazas(2);
        reservaSocioNoExiste.setUsuario_id("99999999Z"); // Ponemos un DNI que no existe en BBDD.
        reservaSocioNoExiste.setMatricula_embarcacion("XXX111");

        System.out.println();
        System.out.println("==== REQUEST 6: POST reserva (invalid - socio no existe) ====");
        try {
            rest.postForEntity(baseURL + "/api/reservas", reservaSocioNoExiste, Reserva.class);
        } catch (HttpClientErrorException e) {
            // Error cliente (4xx)
            System.out.println("Error esperado (Cliente): " + e.getStatusCode());
        } catch (HttpServerErrorException e) {
            // Capturamos el 500 (Internal Server Error) que suele ocurrir cuando falla una clave foránea en BBDD.
            // Esto evita que el programa Java se detenga abruptamente y muestra un mensaje limpio.
            System.out.println("Error esperado (Servidor): " + e.getStatusCode() + " INTERNAL_SERVER_ERROR");
        }
    }

    // ==========================================
    // 3. PATCH (Modificación) - Actualización parcial
    // ==========================================
    private static void sendPatchRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS PATCH **********");

        // --- PRUEBA 7: Cambiar solo la fecha ---
        System.out.println("==== REQUEST 7: PATCH modificar fecha ====");
        Reserva patchFecha = new Reserva();
        patchFecha.setFecha(LocalDate.of(2026, 4, 1)); // Definimos la nueva fecha en un objeto

        try {
            // Usamos 'patchForObject'. La URL incluye el ID que guardamos antes: /api/reservas/{id}/fecha
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

        // --- PRUEBA 8: Cambiar descripción y plazas ---
        System.out.println();
        System.out.println("==== REQUEST 8: PATCH modificar descripción y plazas ====");
        Reserva patchDetalles = new Reserva();
        patchDetalles.setDescripcion("Viaje por el Mediterráneo"); //Cambio la descripción
        patchDetalles.setPlazas(2); // Cambiamos plazas a 2

        try {
            // Llamamos al endpoint específico para detalles: /api/reservas/{id}/detalles
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

    // ==========================================
    // 4. DELETE (Borrado) - Eliminar datos
    // ==========================================
    private static void sendDeleteRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS DELETE **********");

        // Verificamos de nuevo que el ID exista
        if (reservaCreadaId != null) {
            // --- PRUEBA 9: Cancelar (Borrar) la reserva que creamos ---
            System.out.println("==== REQUEST 9: DELETE cancelar reserva futura ====");
            try {
                // Ejecutamos DELETE contra la URL con el ID específico.
                rest.delete(baseURL + "/api/reservas/" + reservaCreadaId);
                System.out.println("Reserva cancelada correctamente.");

                // --- Verificación de borrado ---
                // Intentamos leerla de nuevo. Debería dar un error 404 (Not Found).
                try {
                    rest.getForEntity(baseURL + "/api/reservas/" + reservaCreadaId, Reserva.class);
                } catch (HttpClientErrorException.NotFound e) {
                    System.out.println("Confirmado: La reserva ya no existe (404 OK)");
                }
            } catch (HttpClientErrorException e) {
                System.out.println("Error: " + e.getStatusCode());
            }
        }

        // --- PRUEBA 10: Intentar borrar una reserva que no existe ---
        System.out.println();
        System.out.println("==== REQUEST 10: DELETE cancelar reserva inexistente ====");
        try {
            // Probamos con el ID 9999, que seguramente no existe.
            rest.delete(baseURL + "/api/reservas/9999");
        } catch (HttpClientErrorException e) {
            // Esperamos que el servidor nos devuelva un error (ej: 404 Not Found).
            System.out.println("Error esperado: " + e.getStatusCode());
        }
    }

    // Método auxiliar para imprimir bonito en consola los datos de una Reserva.
    // Evita repetir System.out.println en cada parte del código principal.
    private static void imprimirReserva(Reserva r) {
        if (r == null) return; // Si el objeto es nulo, no hace nada para evitar errores.
        System.out.println("Reserva {");
        System.out.println("  ID: " + r.getId());
        System.out.println("  Fecha: " + r.getFecha());
        System.out.println("  Plazas: " + r.getPlazas());
        System.out.println("  Precio: " + r.getPrecio() + "€");
        System.out.println("  DNI Socio: " + r.getUsuario_id());
        System.out.println("  Matrícula: " + r.getMatricula_embarcacion());
        // Operador ternario: si descripción es null, imprime "N/A", si no, imprime la descripción.
        System.out.println("  Descripción: " + (r.getDescripcion() != null ? r.getDescripcion() : "N/A"));
        System.out.println("}");
    }
}