package com.GM2.model;

import java.time.LocalDate;

public class Alquiler {
    private int id;
    private LocalDate fechainicio;
    private LocalDate fechafin;
    private double precio;
    private int plazas;
    private String usuario_id;
    


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

}
