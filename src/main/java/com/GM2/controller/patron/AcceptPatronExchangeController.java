package com.GM2.controller.patron;

import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/embarcaciones")
public class AcceptPatronExchangeController {

    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    public AcceptPatronExchangeController(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Procesa la confirmación del reemplazo del patrón de una embarcación.
     * Este endpoint es llamado desde la vista "patronAssignedView".
     *
     * @param matricula        Matrícula de la embarcación cuyo patrón se quiere reemplazar.
     * @param dniPatronNuevo   DNI del nuevo patrón.
     */
    @PostMapping("/confirmarReemplazoPatron")
    public String confirmarReemplazo(@RequestParam String matricula,
                                     @RequestParam String dniPatronNuevo,
                                     RedirectAttributes redirectAttributes) {

        // Ejecutamos la actualización
        boolean success = embarcacionRepository.updatePatron(dniPatronNuevo, matricula);

        if(success) {
            redirectAttributes.addFlashAttribute("successMessage", "Patrón reemplazado con éxito.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al reemplazar el patrón.");
        }

        // Redirigimos de vuelta al formulario principal
        return "redirect:/api/embarcaciones/asociarPatron";
    }

}
