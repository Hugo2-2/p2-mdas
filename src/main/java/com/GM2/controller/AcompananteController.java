package com.GM2.controller;


import com.GM2.model.domain.Acompanante;
import com.GM2.model.domain.Alquiler;
import com.GM2.model.repository.AcompananteRepository;
import com.GM2.model.repository.AlquilerRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador web (MVC) para la gestión de Acompañantes.
 * Maneja las peticiones web para mostrar formularios referentes a los acompañantes.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/acompanantes")
public class AcompananteController {

    AcompananteRepository acompanantesRepository;
    AlquilerRepository alquilerRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param acompanantesRepository Repositorio de acompañantes para operaciones de datos
     * @param alquilerRepository Repositorio de alquileres para operaciones de datos
     */
    public AcompananteController(AcompananteRepository acompanantesRepository, AlquilerRepository alquilerRepository) {
        this.acompanantesRepository = acompanantesRepository;
        this.alquilerRepository = alquilerRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.acompanantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra el formulario para gestionar acompañantes de un alquiler.
     * 
     * @param alquilerId ID del alquiler
     * @param plazas Número de plazas disponibles
     * @return ModelAndView con el formulario de acompañantes
     */
    @GetMapping("/{alquilerId}/{plazas}")
    public ModelAndView mostrarFormularioAcompanantes(@PathVariable Integer alquilerId, @PathVariable int plazas) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAcompanante");

        // 1. Cargar acompañantes existentes de la base de datos
        List<Acompanante> acompanantesExistentes = acompanantesRepository.findAcompananteByAlquiler(alquilerId);
        
        // Si es null, crear lista vacía
        if (acompanantesExistentes == null) {
            acompanantesExistentes = new ArrayList<>();
        }

        // 2. Calcular cuántos acompañantes nuevos se pueden añadir
        int plazasDisponibles = plazas - 1 - acompanantesExistentes.size();
        
        // 3. Crear lista para nuevos acompañantes (solo los que caben)
        List<Acompanante> nuevosAcompanantes = new ArrayList<>();
        for (int i = 0; i < plazasDisponibles; i++) {
            nuevosAcompanantes.add(new Acompanante());
        }

        // 4. Pasar todos los datos al modelo
        modelAndView.addObject("alquilerId", alquilerId);
        modelAndView.addObject("plazas", plazas);
        modelAndView.addObject("acompanantesExistentes", acompanantesExistentes);
        modelAndView.addObject("acompanantes", nuevosAcompanantes); // Los nuevos que se pueden añadir
        modelAndView.addObject("plazasDisponibles", plazasDisponibles);

        return modelAndView;
    }

    /**
     * Agrega acompañantes a un alquiler.
     * 
     * @param alquilerId ID del alquiler
     * @param dnis Lista de DNIs de los acompañantes
     * @param redirectAttributes Atributos para redirección y mensajes flash
     * @return String con la redirección a la vista de alquiler
     */
   @PostMapping
    public ModelAndView addAcompanantes(@RequestParam("alquilerId") Integer alquilerId,
                        @RequestParam("dni") List<String> dnis,
                        RedirectAttributes redirectAttributes) {
        
        ModelAndView modelAndView = new ModelAndView("redirect:/api/alquiler");
        
        // Obtener el DNI del socio creador del alquiler
        Alquiler alquiler = alquilerRepository.findAlquilerById(alquilerId);
        String socioCreadorDNI = "";
        if (alquiler != null) {
            socioCreadorDNI = alquiler.getUsuario_dni();
        }      
        
        int contadorExitos = 0;
        int contadorDuplicados = 0;
        int contadorSocioCreador = 0;
        int contadorErrores = 0;
        
        ArrayList<String> dnisUnicos = new ArrayList<>();
        ArrayList<Acompanante> acompanantesAInsertar = new ArrayList<>();
        
        for (String dni : dnis) {
            
            String dniLimpio = dni.toUpperCase();
            
            // 1. Verificar que no sea el socio creador
            if (dniLimpio.equals(socioCreadorDNI)) {
                contadorSocioCreador++;
                continue;
            }
            
            // 2. Verificar duplicado usando contains
            if (dnisUnicos.contains(dniLimpio)) {
                contadorDuplicados++;
                continue;
            }
            
            // 3. Añadir a la lista de únicos
            dnisUnicos.add(dniLimpio);
            
            
            // Insertar acompañante
            Acompanante a = new Acompanante();
            a.setDni(dniLimpio);
            a.setId_alquiler(alquilerId);
            acompanantesAInsertar.add(a);
        }

        // SEGUNDO: Solo insertar si no hay errores de validación
        if (contadorSocioCreador == 0 && contadorDuplicados == 0) {
            // Insertar todos los acompañantes válidos
            for (Acompanante a : acompanantesAInsertar) {
                if (acompanantesRepository.addAcompanante(a)) {
                    contadorExitos++;
                } else {
                    contadorErrores++;
                }
            }
        }

        if (contadorExitos > 0) {
            String mensajeExito = contadorExitos + " acompañante(s) añadido(s) correctamente.";
            redirectAttributes.addFlashAttribute("mensajeExito", mensajeExito);
        }

        // Mostrar errores de validación
        if (contadorSocioCreador > 0) {
            redirectAttributes.addFlashAttribute("mensajeError", 
                "No se puede añadir al socio creador como acompañante.");
            return modelAndView;
        }
        
        if (contadorDuplicados > 0) {
            redirectAttributes.addFlashAttribute("mensajeError", 
                "Hay DNIs duplicados en el formulario. Corrígelos antes de guardar.");
            return modelAndView;
        }
        
        if (contadorErrores > 0) {
            redirectAttributes.addFlashAttribute("mensajeError", 
                "Error al insertar " + contadorErrores + " acompañante(s).");
        }

        return modelAndView;
    }

    
}