package com.GM2.controller.embarcacion;

import com.GM2.controller.patron.AssignPatronToEmbarcacion;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/embarcaciones")
public class EmbarcacionController {

    EmbarcacionRepository embarcacionRepository;
    EmbarcacionService embarcacionService;
    AssignPatronToEmbarcacion assignPatronToEmbarcacion;

    public EmbarcacionController(EmbarcacionRepository embarcacionRepository, AssignPatronToEmbarcacion assignPatronToEmbarcacion, EmbarcacionService embarcacionService) {
        this.embarcacionRepository = embarcacionRepository;
        this.embarcacionService = embarcacionService;
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

    @GetMapping("/addEmbarcacion")
    public ModelAndView getAddEmbarcacionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addEmbarcacionView");
        modelAndView.addObject("newEmbarcacion", new Embarcacion());
        return modelAndView;
    }

    @PostMapping("/addEmbarcacion")
    public String addEmbarcacion(@ModelAttribute Embarcacion newEmbarcacion,
                                 SessionStatus sessionStatus,
                                 RedirectAttributes redirectAttributes){

        System.out.println("[EmbarcacionController] Informacion recivida: matricula=" + newEmbarcacion.getMatricula() +
                " nombre=" + newEmbarcacion.getNombre() +
                " tipo=" + newEmbarcacion.getTipo() +
                " dimensiones=" + newEmbarcacion.getDimensiones() +
                " id_patron=" + newEmbarcacion.getIdPatron() +
                " plazas=" + newEmbarcacion.getPlazas());
        String resultado = embarcacionService.addEmbarcacion(newEmbarcacion);
        String nextPage;

        if(resultado == "EXITO"){
            redirectAttributes.addFlashAttribute("successMessage", "La embarcacion se ha ingresado correctamente");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", resultado);
        }
        nextPage = "redirect:/api/embarcaciones/addEmbarcacion";

        sessionStatus.setComplete();
        return nextPage;
    }
}
