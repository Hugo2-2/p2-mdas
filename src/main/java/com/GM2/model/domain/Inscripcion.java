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
    private String titularMemberId;
    private float annualFee;
    private LocalDate creationDate;
    // Clean Code - Reglas de nombrado: cambiar nombre de variable por nombre pronunciable (segundoAudlto -> secondAdult)
    private String secondAdult;
    private List<Hijos> children;

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
     * @param titularMemberId ID del socio titular de la inscripción.
     */
    public Inscripcion(String titularMemberId) {
        this.titularMemberId = titularMemberId;
        this.annualFee = 300;
        this.creationDate = LocalDate.now();
        this.secondAdult = null;
        this.children = null;
    }

    public Inscripcion(String titularMemberId, String secondAdult, List<Hijos> children) {
        this.titularMemberId = titularMemberId;
        this.annualFee = 300;
        this.creationDate = LocalDate.now();

        if( secondAdult != null && !secondAdult.isEmpty()) {
            this.secondAdult = secondAdult;
            this.annualFee += 250;
        }

        if( children != null && !children.isEmpty()) {
            this.children = children;
            this.annualFee += children.size() * 100;
        }
    }

    /**
     * Constructor para crear una inscripción con datos básicos.
     *
     * @param id ID único de la inscripción.
     * @param titularMemberId ID del socio titular de la inscripción.
     * @param annualFee Cuota anual de la inscripción.
     * @param creationDate Fecha de creación de la inscripción.
     */
    public Inscripcion(int id, String titularMemberId, float annualFee, LocalDate creationDate) {
        this.id = id;
        this.titularMemberId = titularMemberId;
        this.annualFee = annualFee;
        this.creationDate = creationDate;
    }

    /**
     * Constructor completo para crear una inscripción con todos los datos.
     * Incluye la opción de añadir un segundo adulto e hijos a la inscripción.
     *
     * @param id ID único de la inscripción.
     * @param titularMemberId ID del socio titular de la inscripción.
     * @param annualFee Cuota anual de la inscripción.
     * @param creationDate Fecha de creación de la inscripción.
     * @param secondAdult ID del segundo adulto asociado a la inscripción (opcional).
     * @param children Lista de hijos asociados a la inscripción.
     */
    public Inscripcion(int id, String titularMemberId, float annualFee, LocalDate creationDate, String secondAdult, List<Hijos> children) {
        this.id = id;
        this.titularMemberId = titularMemberId;
        this.annualFee = annualFee;
        this.creationDate = creationDate;
        this.secondAdult = secondAdult;

        if(!children.isEmpty())
            this.children = children;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitularMemberId() {
        return titularMemberId;
    }

    public void setTitularMemberId(String titularMemberId) {
        this.titularMemberId = titularMemberId;
    }

    public float getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(float annualFee) {
        this.annualFee = annualFee;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Establece la fecha de creación como la fecha actual.
     * Método de conveniencia para actualizar la fecha de creación.
     */
    public void setCreationDate() {
        this.creationDate = LocalDate.now();
    }

    public String getSecondAdult() {
        return secondAdult;
    }

    public void setSecondAdult(String secondAdult) {
        this.secondAdult = secondAdult;
    }

    public List<Hijos> getChildren() {
        return children;
    }

    public void setChildren(List<Hijos> children) {
        this.children = children;
    }

    public void addChildren(Hijos child) {
        this.children.add(child);
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", socioTitularId='" + titularMemberId + '\'' +
                ", cuotaAnual=" + annualFee +
                ", fechaCreacion=" + creationDate +
                ", segundoAdulto='" + secondAdult + '\'' +
                ", hijos=" + children +
                '}';
    }
}
