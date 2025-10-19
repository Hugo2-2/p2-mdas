package com.GM2.controller;

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
            if (!ocupada && e.getPlazas() >= plazasSolicitadas) {
                disponibles.add(e);
            }
        }
        return disponibles;
    }

    // Crear reserva
    public boolean reservarEmbarcacion(Reserva reserva) {
        Socio socio = socioRepository.findSocioByDNI(reserva.getUsuario_id());

        if (socio == null){ 
            return false; // Socio no existe
        }
        if (!socio.esMayorEdad()){ 
            return false; // Socio menor de edad
        }

        List<Embarcacion> disponibles = buscarEmbarcacionesConPatronDisponibles(reserva.getFecha(), reserva.getPlazas());
        if (disponibles.isEmpty()){ 
            return false; // No hay embarcaciones disponibles
        }

        // Asignar la primera embarcación disponible
        reserva.setMatricula_embarcacion(disponibles.get(0).getMatricula());
        reserva.setPrecio(reserva.getPlazas() * 40.0); // 40€ por persona

        return reservaRepository.addReserva(reserva);
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
