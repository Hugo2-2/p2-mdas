package com.GM2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "menu";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }
}