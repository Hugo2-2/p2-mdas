package com.GM2.client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.GM2.model.domain.Reserva;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ClienteReserva {

    public static void main(String[] args) {
        // 1. Configuración obligatoria para que RestTemplate soporte PATCH
        RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String baseURL = "http://localhost:8080";

        // 2. Ejecutar pruebas de LECTURA (GET)
        sendGetRequests(rest, baseURL);

        // 3. Ejecutar prueba de CREACIÓN (POST) y capturar la ID generada
        int idGenerada = sendPostRequests(rest, baseURL);

        // 4. Si el POST funcionó, usar esa ID para probar MODIFICACIÓN y BORRADO
        if (idGenerada != -1) {
            sendPatchRequests(rest, baseURL, idGenerada);
            sendDeleteRequests(rest, baseURL, idGenerada);
        } else {
            System.out.println("⚠ ALERTA: No se ejecutan PATCH/DELETE porque la creación (POST) falló.");
        }
    }

    // ==========================================
    // SECCIÓN GET (Lectura)
    // ==========================================
    private static void sendGetRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS GET **********");

        // 1. Listar todas las reservas
        try {
            ResponseEntity<Reserva[]> response = rest.getForEntity(baseURL + "/api/reservas", Reserva[].class);
            System.out.println("==== REQUEST 1: GET all reservas ====");

            if (response.getBody() != null && response.getBody().length > 0) {
                List<Reserva> lista = Arrays.asList(response.getBody());
                for(Reserva r : lista) imprimirReserva(r);
            } else {
                System.out.println("No hay reservas registradas (o status 204).");
            }
        } catch (Exception e) {
            System.out.println("Error en GET all: " + e.getMessage());
        }

        // 2. Obtener reservas futuras
        try {
            LocalDate fechaFiltro = LocalDate.now();
            System.out.println("\n==== REQUEST 2: GET reservas futuras (después de " + fechaFiltro + ") ====");

            ResponseEntity<Reserva[]> response = rest.getForEntity(
                    baseURL + "/api/reservas?fecha=" + fechaFiltro,
                    Reserva[].class
            );

            if (response.getBody() == null || response.getBody().length == 0) {
                System.out.println("No hay reservas futuras.");
            } else {
                List<Reserva> lista = Arrays.asList(response.getBody());
                for(Reserva r : lista) imprimirReserva(r);
            }
        } catch (Exception e) {
            System.out.println("Error en GET futuras: " + e.getMessage());
        }

        // 3. Obtener reserva por ID (Probamos con la ID 2 que sabemos que existe por tus logs)
        try {
            int idPrueba = 2;
            System.out.println("\n==== REQUEST 3: GET reserva by ID (" + idPrueba + ") ====");
            ResponseEntity<Reserva> response = rest.getForEntity(
                    baseURL + "/api/reservas/" + idPrueba,
                    Reserva.class
            );
            imprimirReserva(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("La reserva ID 2 no existe.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==========================================
    // SECCIÓN POST (Creación)
    // ==========================================
    private static int sendPostRequests(RestTemplate rest, String baseURL) {
        System.out.println("\n********** [RESERVAS] PRUEBAS POST **********");

        // 4. Crear reserva VÁLIDA
        Reserva nuevaReserva = new Reserva();
        // Usamos una fecha lejana para evitar conflictos (409) con tus pruebas anteriores
        nuevaReserva.setFecha(LocalDate.of(2028, 8, 20));
        nuevaReserva.setPlazas(4);
        nuevaReserva.setUsuario_id("11111111A");     // Asegúrate que este socio existe
        nuevaReserva.setMatricula_embarcacion("XXX111"); // Asegúrate que este barco existe
        nuevaReserva.setDescripcion("Reserva generada automáticamente por Cliente Java");

        System.out.println("==== REQUEST 4: POST crear reserva (válida) ====");
        try {
            ResponseEntity<Reserva> response = rest.postForEntity(
                    baseURL + "/api/reservas",
                    nuevaReserva,
                    Reserva.class
            );
            Reserva creada = response.getBody();
            System.out.println("   ✓ ÉXITO - Status: " + response.getStatusCode());
            imprimirReserva(creada);

            // Retornamos la ID real asignada por la BBDD para usarla después
            return creada.getId();

        } catch (HttpClientErrorException e) {
            System.out.println("Error en POST: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return -1; // Retornamos -1 para indicar fallo
        }

        // (Opcional) Aquí podrías añadir las pruebas de POST inválidos (exceso de capacidad, etc.)
        // si quieres ver los errores controlados.
    }

    // ==========================================
    // SECCIÓN PATCH (Modificación)
    // ==========================================
    private static void sendPatchRequests(RestTemplate rest, String baseURL, int idParaModificar) {
        System.out.println("\n********** [RESERVAS] PRUEBAS PATCH (Usando ID: " + idParaModificar + ") **********");

        // 7. Modificar fecha
        System.out.println("==== REQUEST 7: PATCH modificar fecha ====");
        Reserva patchFecha = new Reserva();
        // Movemos la fecha un día después
        patchFecha.setFecha(LocalDate.of(2028, 8, 21));

        try {
            Reserva actualizada = rest.patchForObject(
                    baseURL + "/api/reservas/" + idParaModificar + "/fecha",
                    patchFecha,
                    Reserva.class
            );
            System.out.println("   ✓ ÉXITO - Fecha modificada correctamente.");
            imprimirReserva(actualizada);
        } catch (HttpClientErrorException e) {
            System.out.println("Error en PATCH fecha: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        // 8. Modificar detalles
        System.out.println("\n==== REQUEST 8: PATCH modificar detalles ====");
        Reserva patchDetalles = new Reserva();
        patchDetalles.setDescripcion("Descripción actualizada mediante PATCH");
        patchDetalles.setPlazas(2); // Cambiamos las plazas

        try {
            Reserva actualizada = rest.patchForObject(
                    baseURL + "/api/reservas/" + idParaModificar + "/detalles",
                    patchDetalles,
                    Reserva.class
            );
            System.out.println("   ✓ ÉXITO - Detalles modificados correctamente.");
            imprimirReserva(actualizada);
        } catch (HttpClientErrorException e) {
            System.out.println("Error en PATCH detalles: " + e.getStatusCode());
        }
    }

    // ==========================================
    // SECCIÓN DELETE (Borrado)
    // ==========================================
    private static void sendDeleteRequests(RestTemplate rest, String baseURL, int idParaBorrar) {
        System.out.println("\n********** [RESERVAS] PRUEBAS DELETE (Usando ID: " + idParaBorrar + ") **********");

        // 11. Cancelar la reserva que acabamos de crear y modificar
        System.out.println("==== REQUEST 11: DELETE cancelar la reserva creada ====");
        try {
            rest.delete(baseURL + "/api/reservas/" + idParaBorrar);
            System.out.println("   ✓ ÉXITO - Reserva cancelada (Status 204 implícito).");

            // Verificación: Intentamos buscarla de nuevo para asegurar que da 404
            try {
                rest.getForEntity(baseURL + "/api/reservas/" + idParaBorrar, Reserva.class);
            } catch (HttpClientErrorException.NotFound e) {
                System.out.println("   ✓ Verificación Correcta: La reserva ya no existe (Devuelve 404).");
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Error al borrar: " + e.getStatusCode());
        }
    }

    // ==========================================
    // UTILIDADES
    // ==========================================
    private static void imprimirReserva(Reserva r) {
        if (r == null) return;
        System.out.println("- ID: " + r.getId() +
                " | Fecha: " + r.getFecha() +
                " | Matricula: " + r.getMatricula_embarcacion() +
                " | DNI: " + r.getUsuario_id() +
                " | Plazas: " + r.getPlazas() +
                " | Precio: " + r.getPrecio() + "€" +
                " | Desc: " + (r.getDescripcion() != null ? r.getDescripcion() : "N/A"));
    }
}