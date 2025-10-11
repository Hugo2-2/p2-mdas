package com.GM2.model.domain;

public class Familiar {
    private String id;
    private String idInscripcion;
    private String idSocio;

    public Familiar(String id, String idInscripcion, String idSocio) {
        this.id = id;
        this.idInscripcion = idInscripcion;
        this.idSocio = idSocio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(String idSocio) {
        this.idSocio = idSocio;
    }

    public String getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(String idInscripcion) {
        this.idInscripcion = idInscripcion;
    }
}
