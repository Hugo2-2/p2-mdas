package com.GM2.controller.patron;


import com.GM2.model.domain.Patron;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/patrones")
public class AddPatronController {

    PatronRepository patronRepository;

    public AddPatronController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/addPatron")
    public ModelAndView getAddPatronView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("patron/addPatronView");
        modelAndView.addObject("newPatron", new Patron());
        return modelAndView;
    }

    @PostMapping("/addPatron")
    public String addPatron(@ModelAttribute Patron newPatron,
                            SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes) {

        if(patronRepository.isRegistered(newPatron.getDni())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El patron ya existe.");
        }else {
            System.out.println("[PatronController] Informacion recivida: Nombre=" + newPatron.getNombre() +
                    " Apellidos=" + newPatron.getApellidos() +
                    " DNI=" + newPatron.getDni() +
                    " fechaNacimiento=" + newPatron .getFechaNacimiento() +
                    " fecha_expedicion_titulo=" + newPatron.getFechaExpedicionTitulo());

            if(newPatron.getFechaNacimiento().isAfter(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Fecha de nacimiento inválida.");
                sessionStatus.setComplete();
                return "redirect:/api/patrones/addPatron";
            }

            if(newPatron.getFechaExpedicionTitulo().isAfter(LocalDate.now()) ||
                newPatron.getFechaExpedicionTitulo().isBefore(newPatron.getFechaNacimiento())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Fecha de expedición del título inválida.");
                sessionStatus.setComplete();
                return "redirect:/api/patrones/addPatron";
            }

            boolean success = patronRepository.addPatron(newPatron);

            if(success){
                redirectAttributes.addFlashAttribute("successMessage", "Patron guardado exitosamente.");
            }
            else {
                redirectAttributes.addFlashAttribute("errorMessage", "El patron no pudo ser guardado.");
            }
        }

        sessionStatus.setComplete();
        return "redirect:/api/patrones/addPatron";
    }
}
