package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa una inscripción en el club náutico.
 * Esta clase gestiona la información de las inscripciones de los socios,
 * incluyendo la cuota anual, fechas de creación, el socio titular,
 * un segundo adulto opcional y los hijos asociados a la inscripción.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Inscripcion {

    private int id;
    private String socioTitularId;
    private float cuotaAnual;
    private LocalDate fechaCreacion;
    private String segundoAudlto;
    private List<Hijos> hijos;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Inscripcion() {
    }

    /**
     * Constructor para crear una inscripción básica con valores por defecto.
     * Establece la cuota anual a 300, la fecha de creación como la actual,
     * y deja el segundo adulto e hijos como nulos.
     *
     * @param socioTitularId ID del socio titular de la inscripción.
     */
    public Inscripcion(String socioTitularId) {
        this.socioTitularId = socioTitularId;
        this.cuotaAnual = 300;
        this.fechaCreacion = LocalDate.now();
        this.segundoAudlto = null;
        this.hijos = null;
    }

    public Inscripcion(String socioTitularId, String segundoAdulto, List<Hijos> hijos) {
        this.socioTitularId = socioTitularId;
        this.cuotaAnual = 300;
        this.fechaCreacion = LocalDate.now();

        if( segundoAdulto != null && !segundoAdulto.isEmpty()) {
            this.segundoAudlto = segundoAdulto;
            this.cuotaAnual += 250;
        }

        if( hijos != null && !hijos.isEmpty()) {
            this.hijos = hijos;
            this.cuotaAnual += hijos.size() * 100;
        }
    }

    /**
     * Constructor para crear una inscripción con datos básicos.
     *
     * @param id ID único de la inscripción.
     * @param socioTitularId ID del socio titular de la inscripción.
     * @param cuotaAnual Cuota anual de la inscripción.
     * @param fechaCreacion Fecha de creación de la inscripción.
     */
    public Inscripcion(int id, String socioTitularId, float cuotaAnual, LocalDate fechaCreacion) {
        this.id = id;
        this.socioTitularId = socioTitularId;
        this.cuotaAnual = cuotaAnual;
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Constructor completo para crear una inscripción con todos los datos.
     * Incluye la opción de añadir un segundo adulto e hijos a la inscripción.
     *
     * @param id ID único de la inscripción.
     * @param socioTitularId ID del socio titular de la inscripción.
     * @param cuotaAnual Cuota anual de la inscripción.
     * @param fechaCreacion Fecha de creación de la inscripción.
     * @param segundoAudlto ID del segundo adulto asociado a la inscripción (opcional).
     * @param hijos Lista de hijos asociados a la inscripción.
     */
    public Inscripcion(int id, String socioTitularId, float cuotaAnual, LocalDate fechaCreacion, String segundoAudlto, List<Hijos> hijos) {
        this.id = id;
        this.socioTitularId = socioTitularId;
        this.cuotaAnual = cuotaAnual;
        this.fechaCreacion = fechaCreacion;
        this.segundoAudlto = segundoAudlto;

        if(!hijos.isEmpty())
            this.hijos = hijos;
    }

    // Getters y Setters

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

    /**
     * Establece la fecha de creación como la fecha actual.
     * Método de conveniencia para actualizar la fecha de creación.
     */
    public void setFechaCreacion() {
        this.fechaCreacion = LocalDate.now();
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

    public void addHijo(Hijos hijo) {
        this.hijos.add(hijo);
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", socioTitularId='" + socioTitularId + '\'' +
                ", cuotaAnual=" + cuotaAnual +
                ", fechaCreacion=" + fechaCreacion +
                ", segundoAdulto='" + segundoAudlto + '\'' +
                ", hijos=" + hijos +
                '}';
    }
}
