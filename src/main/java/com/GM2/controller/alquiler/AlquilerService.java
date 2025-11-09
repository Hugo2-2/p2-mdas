package com.GM2.controller.alquiler;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.AlquilerRepository;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.SocioRepository;
import com.GM2.model.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar la lógica de negocio relacionada con los alquileres.
 * Proporciona métodos para buscar, validar y procesar alquileres de embarcaciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Service
public class AlquilerService {

    private AlquilerRepository alquilerRepository;
    private EmbarcacionRepository embarcacionRepository;
    private SocioRepository socioRepository;
    private ReservaRepository reservaRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param alquilerRepository Repositorio de alquileres
     * @param embarcacionRepository Repositorio de embarcaciones
     * @param socioRepository Repositorio de socios
     * @param reservaRepository Repositorio de reservas
     */
    public AlquilerService(AlquilerRepository alquilerRepository, EmbarcacionRepository embarcacionRepository, SocioRepository socioRepository, ReservaRepository reservaRepository) {
        this.alquilerRepository = alquilerRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;
        this.reservaRepository = reservaRepository;

        // Configurar las rutas de los .properties si es necesario
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }


    /**
     * Lista todos los alquileres.
     * 
     * @return Lista de todos los objetos {@link Alquiler} o null si ocurre un error.
     */
    public List<Alquiler> findAllAlquileres() {
        return alquilerRepository.findAllAlquileres();
    }

    /**
     * Busca alquiler por ID.
     * 
     * @param id ID del alquiler a buscar
     * @return El objeto {@link Alquiler} encontrado o null si no existe.
     */
    public Alquiler findAlquilerById(int id) {
        return alquilerRepository.findAlquilerById(id);
    }

    /**
     * Agrega un nuevo alquiler a la base de datos.
     * 
     * @param alquiler Objeto Alquiler a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addAlquiler(Alquiler alquiler) {
        return alquilerRepository.addAlquiler(alquiler);
    }

    /**
     * Busca embarcaciones disponibles entre dos fechas.
     * 
     * @param fechaInicio Fecha de inicio de la búsqueda
     * @param fechaFin Fecha de fin de la búsqueda
     * @return Lista de objetos {@link Embarcacion} disponibles entre las dos fechas o null si ocurre un error.
     */
    public List<Embarcacion> buscarEmbarcacionesDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Embarcacion> disponibles = new ArrayList<>();
        List<Reserva> reservas = reservaRepository.findAllReservas();

        // Comprobar null
        if (embarcaciones == null) embarcaciones = new ArrayList<>();
        if (alquileres == null) alquileres = new ArrayList<>();
        if (reservas == null) reservas = new ArrayList<>();


        for (Embarcacion e : embarcaciones) {
            boolean ocupada = false;

            for (Alquiler a : alquileres) {
                if (a.getMatricula_embarcacion().equals(e.getMatricula())) {
                    if (!(fechaFin.isBefore(a.getFechainicio()) || fechaInicio.isAfter(a.getFechafin()))) {
                        ocupada = true;
                        break;
                    }
                }
            }

            if (!ocupada) {
                for (Reserva r : reservas) {
                    if (r.getMatricula_embarcacion().equals(e.getMatricula())) {
                        // Una reserva ocupa la embarcación por UN DÍA específico
                        // Verificar si alguna fecha del rango de alquiler coincide con la fecha de reserva
                        LocalDate fechaReserva = r.getFecha();
                        if (!fechaReserva.isBefore(fechaInicio) && !fechaReserva.isAfter(fechaFin)) {
                            ocupada = true;
                            break;
                        }
                    }
                }
            }
            
            if (!ocupada) {
                disponibles.add(e);
            }
        }
        return disponibles;
    }


    /**
     * Procesa el alquiler de una embarcación.
     * Comprueba que el alquiler sea válido y se registre en la base de datos.
     * 
     * @param nuevoAlquiler Objeto Alquiler a alquilar
     * @return Mensaje de éxito o error al alquilar la embarcación. 
     */
    public String alquilarEmbarcacion(Alquiler nuevoAlquiler) {
        Socio socio = socioRepository.findSocioByDNI(nuevoAlquiler.getUsuario_dni());

        if (socio == null) return "El socio no existe.";
        if (!socio.getTieneLicenciaPatron()) return "El socio no tiene título de patrón.";

        LocalDate inicio = nuevoAlquiler.getFechainicio();
        LocalDate fin = nuevoAlquiler.getFechafin();
        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;

        if (inicio.isAfter(fin)) return "La fecha de inicio no puede ser posterior a la de fin.";

        int mesInicio = inicio.getMonthValue();
        if (mesInicio >= 10 || mesInicio <= 4) {
            if (dias > 3) return "Solo se permiten hasta 3 días entre octubre y abril.";
        } else if (mesInicio >= 5 && mesInicio <= 9) {
            if (dias != 7 && dias != 14) return "Solo se permiten alquileres de 7 o 14 días entre mayo y septiembre.";
        }

        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(nuevoAlquiler.getMatricula_embarcacion());
        if (embarcacion == null) return "Embarcación no encontrada.";
        if (nuevoAlquiler.getPlazas() > embarcacion.getPlazas()) return "No hay suficientes plazas.";

        List<Embarcacion> disponibles = buscarEmbarcacionesDisponibles(inicio, fin);
        boolean disponible = false;
        for (Embarcacion e : disponibles) {
            if (e.getMatricula().equals(nuevoAlquiler.getMatricula_embarcacion())) {
                disponible = true;
                break;
            }
        }
        if (!disponible) return "La embarcación no está disponible en esas fechas.";



        double precio = 20.0 * nuevoAlquiler.getPlazas() * dias;
        nuevoAlquiler.setPrecio(precio);

        boolean insertado = alquilerRepository.addAlquiler(nuevoAlquiler);
        if (insertado) {
            return "OK:" + nuevoAlquiler.getId(); // devolvemos ID de alquiler para el siguiente paso
        } else {
            return "Error al registrar el alquiler.";
        }    
    }


    /**
     * Obtiene todos los alquileres que tienen fecha de inicio en el futuro.
     * 
     * @return Lista de todos los objetos {@link Alquiler} futuros o null si ocurre un error.   
     */
    public List<Alquiler> listarAlquileresFuturos() {
        LocalDate hoy = LocalDate.now();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Alquiler> futuros = new ArrayList<>();

        for (Alquiler a : alquileres) {

            if (!a.getFechainicio().isBefore(hoy)){ 
                futuros.add(a);
            }

        }

        return futuros;
    
    }
}
