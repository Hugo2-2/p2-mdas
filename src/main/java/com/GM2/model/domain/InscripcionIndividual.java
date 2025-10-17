package com.GM2.model.domain;

import java.time.LocalDate;

public class InscripcionIndividual {
    private int id;
    private String socioTitularId;
    private float cuotaAnual;
    private LocalDate fechaCreacion;
 
    public InscripcionIndividual(int id, LocalDate fechaCreacion, float cuotaAnual, String socioTitularId) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.cuotaAnual = cuotaAnual;
        this.socioTitularId = socioTitularId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSocioTitularId() {
        return socioTitularId;
    }

    public void setSocioTitularId(String socioTitularId) {
        this.socioTitularId = socioTitularId;
    }

    public float getCuotaAnual() {
        return cuotaAnual;
    }

    public void setCuotaAnual(float cuotaAnual) {
        this.cuotaAnual = cuotaAnual;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
