package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.List;

public class InscripcionFamiliar extends InscripcionIndividual {

    private String segundoAudlto;
    private List<Hijos> hijos;

    public InscripcionFamiliar(int id, LocalDate fechaCreacion, float cuotaAnual, String socioTitularId, String adultoAdicional, List<Hijos> hijosList) {
        super(id, fechaCreacion, cuotaAnual, socioTitularId);
        this.segundoAudlto = adultoAdicional;
        this.hijos = hijosList;
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
