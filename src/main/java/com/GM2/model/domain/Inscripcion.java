package com.GM2.model.domain;

import java.time.LocalDate;

public class Inscripcion {
    private String id;
    private String socioTitularId;
    private String tipo;
    private String cuotaAnual;
    private LocalDate fechaCreacion;
 
    public Inscripcion(String id, LocalDate fechaCreacion, String cuotaAnual, String tipo, String socioTitularId) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.cuotaAnual = cuotaAnual;
        this.tipo = tipo;
        this.socioTitularId = socioTitularId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSocioTitularId() {
        return socioTitularId;
    }

    public void setSocioTitularId(String socioTitularId) {
        this.socioTitularId = socioTitularId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCuotaAnual() {
        return cuotaAnual;
    }

    public void setCuotaAnual(String cuotaAnual) {
        this.cuotaAnual = cuotaAnual;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
