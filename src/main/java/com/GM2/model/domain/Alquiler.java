package com.GM2.model.domain;

import java.time.LocalDate;

public class Alquiler {
    private int id;
    private LocalDate fechainicio;
    private LocalDate fechafin;
    private double precio;
    private int plazas;
    private String usuario_dni;
    private String matricula_embarcacion;


    public Alquiler(int id, LocalDate fechaInicio, LocalDate fechaFin, double precio, int plazas, String usuario_dni, String matricula_embarcacion) {
        this.id = id;
        this.fechainicio = fechaInicio;
        this.fechafin = fechaFin;
        this.precio = precio;
        this.plazas = plazas;
        this.usuario_dni = usuario_dni;
        this.matricula_embarcacion = matricula_embarcacion;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDate getFechainicio() {
        return fechainicio;
    }
    
    public void setFechainicio(LocalDate fechainicio) {
        this.fechainicio = fechainicio;
    }
    
    public LocalDate getFechafin() {
        return fechafin;
    }
    
    public void setFechafin(LocalDate fechafin) {
        this.fechafin = fechafin;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public int getPlazas() {
        return plazas;
    }
    
    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public String getUsuario_dni() {
        return usuario_dni;
    }

    public void setUsuario_dni(String usuario_dni) {
        this.usuario_dni = usuario_dni;
    }

    public String getMatricula_embarcacion() {
        return matricula_embarcacion;
    }

    public void setMatricula_embarcacion(String matricula_embarcacion) {
        this.matricula_embarcacion = matricula_embarcacion;
    }

}
