package com.GM2.controller;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.EmbarcacionRepository;

import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/embarcaciones")
public class EmbarcacionController {

    EmbarcacionRepository embarcacionRepository;
    AssignPatronToEmbarcacion assignPatronToEmbarcacion;

    public EmbarcacionController(EmbarcacionRepository embarcacionRepository, AssignPatronToEmbarcacion assignPatronToEmbarcacion) {
        this.embarcacionRepository = embarcacionRepository;
        this.assignPatronToEmbarcacion = assignPatronToEmbarcacion;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    @ResponseBody
    public List<Embarcacion> getEmbarcaciones(){ return embarcacionRepository.findAllEmbarcaciones(); }

    @GetMapping("/{matricula}")
    @ResponseBody
    public Embarcacion getEmbarcacionByMatricula(@PathVariable String matricula){ return embarcacionRepository.findEmbarcacionByMatricula(matricula); }

//    @PostMapping()
//    public String addEmbarcacion(@RequestBody Embarcacion embarcacion){
//        boolean res = embarcacionRepository.addEmbarcacion(embarcacion);
//
//        if(res) {
//            return "Embarcacion was added successfully";
//        } else {
//            return "Embarcacion could not be added";
//        }
//    }

    @GetMapping("/addEmbarcacion")
    public ModelAndView getAddEmbarcacionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addEmbarcacionView");
        modelAndView.addObject("newEmbarcacion", new Embarcacion());
        return modelAndView;
    }

    @PostMapping("/addEmbarcacion")
    public String addEmbarcacion(@ModelAttribute Embarcacion newEmbarcacion, SessionStatus sessionStatus){

        System.out.println("[EmbarcacionController] Informacion recivida: matricula=" + newEmbarcacion.getMatricula() +
                " nombre=" + newEmbarcacion.getNombre() +
                " tipo=" + newEmbarcacion.getTipo() +
                " dimensiones=" + newEmbarcacion.getDimensiones() +
                " id_patron=" + newEmbarcacion.getIdPatron() +
                " plazas=" + newEmbarcacion.getPlazas());
        boolean success = embarcacionRepository.addEmbarcacion(newEmbarcacion);
        String nextPage;

        if(success){
            nextPage = "addEmbarcacionView";
        }
        else
            nextPage = "addEmbarcacionViewFail";

        sessionStatus.setComplete();
        return nextPage;
    }

    //TENGO QUE HACER AQUI LA DE OBTENER EMBARCACION POR TIPO

//
//    @PutMapping("/{matricula}/patron")
//    @ResponseBody
//    public String asociatePatronToEmbarcacion(@PathVariable String matricula, @RequestBody String patronDni){
//        return assignPatronToEmbarcacion.asociatePatron(matricula, patronDni);
//    }
}
