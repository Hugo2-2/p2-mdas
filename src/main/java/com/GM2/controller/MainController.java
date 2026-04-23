package com.GM2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web (MVC) para la página principal del sistema.
 * Maneja las peticiones web para mostrar la página principal del sistema.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
public class MainController {

    // Clean Code - Regla 9: Se ha eliminado la cabecera de esta función por ser un método simple y autoexplicativo.
    @GetMapping("/")
    public String home() {
        return "menu";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }
}