package com.GM2.controller.reserva;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Socio;
import com.GM2.model.domain.Alquiler;
import com.GM2.model.repository.AlquilerRepository;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.ReservaRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final EmbarcacionRepository embarcacionRepository;
    private final SocioRepository socioRepository;
    private final AlquilerRepository alquilerRepository;

    public ReservaService(ReservaRepository reservaRepository, EmbarcacionRepository embarcacionRepository, SocioRepository socioRepository, AlquilerRepository alquilerRepository) {
        this.reservaRepository = reservaRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;
        this.alquilerRepository = alquilerRepository;

        // Configurar las rutas de los .properties si es necesario
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    // Buscar embarcaciones con patrón disponible en una fecha concreta
    public List<Embarcacion> buscarEmbarcacionesConPatronDisponibles(LocalDate fecha, int plazasSolicitadas) {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        List<Reserva> reservas = reservaRepository.findAllReservas();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Embarcacion> disponibles = new ArrayList<>();


        
        for (Embarcacion e : embarcaciones) {

            boolean ocupada = false;

            for (Alquiler a : alquileres) {
                if (a.getMatricula_embarcacion().equals(e.getMatricula()) && !fecha.isBefore(a.getFechainicio()) && !fecha.isAfter(a.getFechafin())) {
                    ocupada = true;
                    break;
                }
            }

            for (Reserva r : reservas) {
                if (r.getMatricula_embarcacion().equals(e.getMatricula()) && r.getFecha().equals(fecha)) {
                    ocupada = true;
                    break;
                }
            }

            // Comprobar capacidad
            boolean tienePatron = e.getIdPatron() != null; // O llama a embarcacionRepository.tienePatron(e.getMatricula())

            // Comprobar patrón y capacidad
            if (!ocupada && tienePatron && e.getPlazas() >= plazasSolicitadas + 1) {
                disponibles.add(e);
            }
        }
        return disponibles;
    }

    // Crear reserva
    // En la clase ReservaService.java

    // Crear reserva
    public String reservarEmbarcacion(Reserva reserva) {

        // 1. Validaciones del Socio (Existencia y Mayor de Edad)
        Socio socio = socioRepository.findSocioByDNI(reserva.getUsuario_id());

        if (socio == null) {
            return "El DNI del socio solicitante no está registrado.";
        }
        // El enunciado indica que cualquier socio mayor de edad puede solicitar una actividad.
        if (!socio.esMayorEdad()) {
            return "El socio solicitante debe ser mayor de edad para reservar actividades.";
        }

        // Obtener datos clave de la reserva y de la embarcación elegida
        String matriculaElegida = reserva.getMatricula_embarcacion();
        LocalDate fechaReserva = reserva.getFecha();
        int plazasSolicitadas = reserva.getPlazas();

        // Buscar la embarcación elegida por el usuario para obtener sus datos
        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(matriculaElegida);

        // 2. Búsqueda y Validación de la Embarcación

        // 2.1. Validación de existencia
        if (embarcacion == null) {
            return "La matrícula de la embarcación especificada no existe.";
        }

        // 2.2. Validación de Patrón (Requisito D.1: debe tener patrón)
        if (embarcacion.getIdPatron() == null || embarcacion.getIdPatron().isEmpty()) {
            return "No se pudo realizar la reserva (la embarcación elegida no tiene un patrón asignado).";
        }

        // 2.3. Validación de Capacidad (Requisito D.1: plazas totales >= solicitadas + 1 del patrón)
        // Asumimos que getPlazas() devuelve la capacidad total.
        int capacidadTotal = embarcacion.getPlazas();
        if (capacidadTotal < plazasSolicitadas + 1) {
            return String.format("No se pudo realizar la reserva (capacidad insuficiente: Plazas disponibles %d, Plazas solicitadas %d, más 1 para el patrón).", (capacidadTotal - 1), plazasSolicitadas);
        }

        // 2.4. Validación de Ocupación/Disponibilidad (Patrón)
        // Este método debe verificar el solapamiento en las tablas 'reserva' y 'alquiler'
        List<Embarcacion> disponibles = buscarEmbarcacionesConPatronDisponibles(fechaReserva, plazasSolicitadas);
        boolean disponible = false;
        for (Embarcacion e : disponibles) {
            if (e.getMatricula().equals(matriculaElegida)) {
                disponible = true;
                break;
            }
        }
        if (!disponible) {
            return "Embarcación no disponible"; // No hay embarcaciones disponibles
        }

        // 3. Persistencia (Guardado en BBDD)

        // Calcular precio (40€ por persona) y asignar al objeto
        reserva.setPrecio(plazasSolicitadas * 40.0);

        // Nota: La matrícula ya viene seteada desde el formulario, solo falta el ID.
        // La BBDD generará el ID.

        boolean guardadoExitoso = reservaRepository.addReserva(reserva);

        if (guardadoExitoso) {
            // 4. Éxito
            // Se asume que el repositorio ha asignado un ID si es autoincremental,
            // o que no necesitamos el ID para el mensaje de éxito inmediato.
            return "EXITO";
        } else {
            // 3. Persistencia fallida
            return "Error interno al intentar guardar la reserva en la base de datos.";
        }
    }


    // Listar todas las reservas
    public List<Reserva> findAllReservas() {
        return reservaRepository.findAllReservas();
    }

    // Buscar reserva por id
    public Reserva findReservaById(int id) {
        return reservaRepository.findReservaById(id);
    }

    public boolean addReserva(Reserva reserva) {
        return reservaRepository.addReserva(reserva);
    }
}
