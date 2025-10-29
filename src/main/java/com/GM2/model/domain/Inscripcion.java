package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Inscripcion {

    private int id;
    private String socioTitularId;
    private float cuotaAnual;
    private LocalDate fechaCreacion;
    private String segundoAudlto;
    private List<Hijos> hijos;

    public Inscripcion() {
    }

    public Inscripcion(String socioTitularId) {
        this.socioTitularId = socioTitularId;
        this.cuotaAnual = 300;
        this.fechaCreacion = LocalDate.now();
        this.segundoAudlto = null;
        this.hijos = null;
    }

    public Inscripcion(int id, String socioTitularId, float cuotaAnual, LocalDate fechaCreacion) {
        this.id = id;
        this.socioTitularId = socioTitularId;
        this.cuotaAnual = cuotaAnual;
        this.fechaCreacion = fechaCreacion;
    }

    public Inscripcion(int id, String socioTitularId, float cuotaAnual, LocalDate fechaCreacion, String segundoAudlto, List<Hijos> hijos) {
        this.id = id;
        this.socioTitularId = socioTitularId;
        this.cuotaAnual = cuotaAnual;
        this.fechaCreacion = fechaCreacion;
        this.segundoAudlto = segundoAudlto;

        if(!hijos.isEmpty())
            this.hijos = hijos;
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

    public String getSegundoAudlto() {
        return segundoAudlto;
    }

    public void setSegundoAudlto(String segundoAudlto) {
        this.segundoAudlto = segundoAudlto;
    }

    public List<Hijos> getHijos() {
        return hijos;
    }

    public void setHijos(List<Hijos> hijos) {
        this.hijos = hijos;
    }
}
