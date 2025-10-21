package com.GM2.controller;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api/embarcaciones")
public class AssignPatronToEmbarcacion {
    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    public AssignPatronToEmbarcacion(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;
    }

    @GetMapping("/asociarPatron")
    public ModelAndView getAddEmbarcacionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("asociatePatronToEmbarcacionView");
        modelAndView.addObject("Embarcacion", new Embarcacion());
        return modelAndView;
    }

    @PostMapping("/asociarPatron")
    public String asociatePatron(@ModelAttribute Embarcacion embarcacion, SessionStatus sessionStatus) {
        //1.Comprobar si tiene patron
        String nextPage;
        if(embarcacionRepository.isPatronAssignedToEmbarcacion(embarcacion.getIdPatron())) {
            //1A.Si tiene patron, preguntamos si quiere sustituir
            nextPage = "patronAssignedView";
        } else {
            //1B.Si no tiene patron, mostramos patrones libres y seleccionamos uno
            boolean success = embarcacionRepository.updatePatron(embarcacion.getIdPatron(), embarcacion.getMatricula());

            if(success) {
                nextPage = "patronSuccessfullyAssignedView";
            } else {
                nextPage = "patronFailAssignedView";
            }
        }

        sessionStatus.setComplete();;
        return nextPage;
    }


//    public String asociatePatron(String matricula, String patronDni) {
//        // 1. Verificar que el patrón existe
//        if (patronRepository.findPatronByDNI(patronDni) == null) {
//            return "Error: El patrón con DNI " + patronDni + " no existe.";
//        }
//
//        // 2. Verificar si el patrón ya está asignado a OTRA embarcación [cite: 991]
//        if (embarcacionRepository.isPatronAssignedToEmbarcacion(patronDni)) {
//            return "Error: El patrón ya está asignado a otra embarcación.";
//        }
//
//        // 3. Obtener el patrón actual de la embarcación [cite: 986]
//        String patronActual = embarcacionRepository.getPatronAssignedToEmbarcacion(matricula);
//
//        // 4. Ejecutar la asignación
//        embarcacionRepository.updatePatron(patronDni, matricula);
//
//        if (patronActual != null) {
//            // Informar del reemplazo [cite: 986, 992]
//            return "Reemplazo exitoso. Patrón " + patronActual + " ahora está libre.";
//        } else {
//            return "Patrón " + patronDni + " asignado correctamente.";
//        }
//    }
}
