package com.GM2.controller;


import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.PatronRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/patrones")
public class PatronController {

    PatronRepository patronRepository;

    public PatronController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    @ResponseBody
    public List<Patron> getPatrones() { return patronRepository.findAllPatrones(); }

    @GetMapping("/{dni}")
    @ResponseBody
    public Patron getPatronById(@PathVariable String dni){ return patronRepository.findPatronByDNI(dni); }

//    @PostMapping("/addPatron")
//    public String addPatron(@RequestBody Patron patron) {
//        boolean res = patronRepository.addPatron(patron);
//
//        if(res) {
//            return "Patron was added successfully";
//        } else {
//            return "Patron could not be added";
//        }
//    }

    @GetMapping("/addPatron")
    public ModelAndView getAddPatronView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addPatronView");
        modelAndView.addObject("newPatron", new Patron());
        return modelAndView;
    }

    @PostMapping("/addPatron")
    public String addPatron(@ModelAttribute Patron newPatron, SessionStatus sessionStatus) {

        String nextPage;
        if(patronRepository.isRegistered(newPatron.getDni())) {
            nextPage = "addPatronViewDniRegistered";
        }else {
            System.out.println("[PatronController] Informacion recivida: Nombre=" + newPatron.getNombre() +
                    " Apellidos=" + newPatron.getNombre() +
                    " DNI=" + newPatron.getDni() +
                    " fechaNacimiento=" + newPatron .getFechaNacimiento() +
                    " fecha_expedicion_titulo=" + newPatron.getFechaExpedicionTitulo());

            boolean success = patronRepository.addPatron(newPatron);

            if(success){
                nextPage = "addPatronView";
            }
            else
                nextPage = "addPatronViewFail";
        }

        sessionStatus.setComplete();
        return nextPage;
    }

}
