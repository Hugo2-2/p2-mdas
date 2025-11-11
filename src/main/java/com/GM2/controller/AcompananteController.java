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
 * Controlador REST para gestionar las operaciones relacionadas con acompañantes.
 * Proporciona una API REST completa para crear, consultar y gestionar acompañantes
 * de alquileres mediante endpoints que devuelven datos en formato JSON.
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
     */
    public AcompananteController(AcompananteRepository acompanantesRepository, AlquilerRepository alquilerRepository) {
        this.acompanantesRepository = acompanantesRepository;
        this.alquilerRepository = alquilerRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.acompanantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
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