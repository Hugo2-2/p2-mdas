package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;

import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controlador encargado de gestionar las operaciones relacionadas con la creación
 * de nuevas embarcaciones dentro del sistema.
 *
 * Este controlador permite:
 * - Mostrar el formulario para registrar una nueva embarcación.
 * - Procesar el envío del formulario y validar los datos recibidos.
 * - Verificar restricciones como matrícula única, nombre único, plazas mínimas,
 *   dimensiones válidas y disponibilidad del patrón asociado.
 *
 * Forma parte del módulo de gestión de embarcaciones del club náutico.
 *
 * @author gm2equipo1
 * @version 3.0
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class AddEmbarcacionController {

    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    /**
     * Constructor utilizado para la inyección de dependencias necesarias para la
     * gestión de embarcaciones y patrones.
     *
     * También establece el archivo donde se encuentran definidas las consultas SQL
     * utilizadas por los repositorios.
     *
     * @param embarcacionRepository repositorio para operaciones con embarcaciones.
     * @param patronRepository repositorio para operaciones con patrones.
     */
    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    // Clean Code - Regla 9: Se ha eliminado la cabecera de esta función por ser un método simple y autoexplicativo.
    @GetMapping("/addEmbarcacion")
    public ModelAndView getAddEmbarcacionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("embarcacion/addEmbarcacionView");
        modelAndView.addObject("newEmbarcacion", new Embarcacion());
        return modelAndView;
    }

    /**
     * Procesa el envío del formulario (POST) para añadir una nueva embarcacion.
     * Utiliza el patrón Post-Redirect-Get para evitar envíos duplicados.
     *
     * Tras superar las validaciones, se intenta insertar la embarcación en
     * la base de datos y se notifica el resultado mediante mensajes Flash.
     *
     * @param newEmbarcacion     El objeto Embarcacion con los datos rellenados del formulario (@ModelAttribute).
     * @param sessionStatus      Controlador de estado de la sesión para limpiarla tras el envío.
     * @param redirectAttributes Interfaz para pasar mensajes (éxito/error) a la vista de redirección.
     * @return Un String que indica la URL a la que se debe redirigir.
     */
    @PostMapping("/addEmbarcacion")
    public String addEmbarcacion(@ModelAttribute Embarcacion newEmbarcacion,
                                 SessionStatus sessionStatus,
                                 RedirectAttributes redirectAttributes){

        System.out.println("[EmbarcacionController] Informacion recivida: matricula=" + newEmbarcacion.getRegistration() +
                " nombre=" + newEmbarcacion.getName() +
                " tipo=" + newEmbarcacion.getType() +
                " dimensiones=" + newEmbarcacion.getDimensions() +
                " id_patron=" + newEmbarcacion.getSkipperId() +
                " plazas=" + newEmbarcacion.getSeats());

        // 1. Comprobamos que la matrícula sea única, al igual con el nombre
        if (embarcacionRepository.findEmbarcacionByMatricula(newEmbarcacion.getRegistration()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Ya existe una embarcación con esa matrícula");
            sessionStatus.setComplete();
            return "redirect:/api/embarcaciones/addEmbarcacion";
        } else if (embarcacionRepository.findEmbarcacionByNombre(newEmbarcacion.getName()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Ya existe una embarcación con dicho nombre asociado");
            sessionStatus.setComplete();
            return "redirect:/api/embarcaciones/addEmbarcacion";
        }
        // 2. Comprobamos que al menos sea 2 plazas (1 para patron y otra para quien reserva)
        else if (newEmbarcacion.getSeats() < 2) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El número mínimo de plazas debe ser 2");
            sessionStatus.setComplete();
            return "redirect:/api/embarcaciones/addEmbarcacion";
        }
        // 3. Validación de dimensiones, se mete en un try catch, ya que hay que pasar de string a double las dimensiones
        else try {
                double dimensiones = Double.parseDouble(newEmbarcacion.getDimensions());
                if (dimensiones < 1) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error: La embarcación debe tener al menos 1m2");
                    sessionStatus.setComplete();
                    return "redirect:/api/embarcaciones/addEmbarcacion";
                }

                // 4. Validación de Patrón
                String patronDni = newEmbarcacion.getSkipperId();
                // Clean Code - Regla 6: Se ha eliminado el comentario explicativo y extraído la condicional compleja a la función 'esDniValido'.
                if (esDniValido(patronDni)) {
                    if (patronRepository.findPatronByDNI(patronDni) == null) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Error: El patron no existe");
                        sessionStatus.setComplete();
                        return "redirect:/api/embarcaciones/addEmbarcacion";
                    } else if (embarcacionRepository.isPatronAssignedToEmbarcacion(patronDni)) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Error: El patron ya se encuentra asignado a una embarcación");
                        sessionStatus.setComplete();
                        return "redirect:/api/embarcaciones/addEmbarcacion";
                    }
                }

                // 5. Insertamos los datos
                boolean resultado = embarcacionRepository.addEmbarcacion(newEmbarcacion); // Llama al método "tonto"
                if (resultado) {
                    redirectAttributes.addFlashAttribute("successMessage", "La embarcacion se ha ingresado correctamente");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error: No se pudo añadir la embarcación");
                }
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: El formato de las dimensiones no es válido.");
            }

        sessionStatus.setComplete();
        return "redirect:/api/embarcaciones/addEmbarcacion";
    }

    // Clean Code - Regla 7: Se han eliminado los marcadores de separación visuales (ej. --- Métodos privados ---) para evitar ruido innecesario en el código.
    private boolean esDniValido(String dni) {
        return dni != null && !dni.trim().isEmpty();
    }

}
