package com.GM2.model;

public class Inscripcion {
    private String id;
    private String socioTitularId;
    private String tipo;
    private String cuotaAnual;
    private String fechaCreacion;

    public Inscripcion(String id, String fechaCreacion, String cuotaAnual, String tipo, String socioTitularId) {
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
