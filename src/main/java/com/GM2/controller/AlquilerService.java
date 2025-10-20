package com.GM2.controller;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.AlquilerRepository;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlquilerService {

    private AlquilerRepository alquilerRepository;
    private EmbarcacionRepository embarcacionRepository;
    private SocioRepository socioRepository;

    // Constructor con inyección de dependencias
    public AlquilerService(AlquilerRepository alquilerRepository, EmbarcacionRepository embarcacionRepository, SocioRepository socioRepository) {
        this.alquilerRepository = alquilerRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;

        // Configurar las rutas de los .properties si es necesario
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }


    // Listar todos los alquileres
    public List<Alquiler> findAllAlquileres() {
        return alquilerRepository.findAllAlquileres();
    }

    // Buscar alquiler por ID
    public Alquiler findAlquilerById(int id) {
        return alquilerRepository.findAlquilerById(id);
    }

    // Agregar alquiler
    public boolean addAlquiler(Alquiler alquiler) {
        return alquilerRepository.addAlquiler(alquiler);
    }

    //  Buscar embarcaciones disponibles
    public List<Embarcacion> buscarEmbarcacionesDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Embarcacion> disponibles = new ArrayList<>();

        // Comprobar null
        if (embarcaciones == null) embarcaciones = new ArrayList<>();
        if (alquileres == null) alquileres = new ArrayList<>();


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
                disponibles.add(e);
            }
        }
        return disponibles;
    }


    // Alquilar embarcación
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
        return insertado ? "Alquiler registrado con éxito. Precio total: " + precio + " €" : "Error al registrar el alquiler.";
    }


    // Listar alquileres futuros
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
