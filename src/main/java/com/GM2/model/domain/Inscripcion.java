package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    /** Cuota base anual para una inscripción individual (en euros). */
    public static final float BASE_ANNUAL_FEE = 300f;

    /** Incremento en la cuota anual por añadir un segundo adulto (en euros). */
    public static final float SECOND_ADULT_FEE = 250f;

    /** Incremento en la cuota anual por cada hijo añadido (en euros). */
    public static final float CHILD_FEE = 100f;

    private int id;
    private String titularMemberId;
    private float annualFee;
    private LocalDate creationDate;
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
     * Establece la cuota anual base, la fecha de creación como la actual,
     * y deja el segundo adulto e hijos como nulos.
     *
     * @param titularMemberId ID del socio titular de la inscripción.
     */
    public Inscripcion(String titularMemberId) {
        this.titularMemberId = titularMemberId;
        this.annualFee = BASE_ANNUAL_FEE;
        this.creationDate = LocalDate.now();
        this.secondAdult = null;
        this.children = null;
    }

    public Inscripcion(String titularMemberId, String secondAdult, List<Hijos> children) {
        this.titularMemberId = titularMemberId;
        this.annualFee = BASE_ANNUAL_FEE;
        this.creationDate = LocalDate.now();

        if (secondAdult != null && !secondAdult.isEmpty()) {
            this.secondAdult = secondAdult;
            this.annualFee += SECOND_ADULT_FEE;
        }

        if (children != null && !children.isEmpty()) {
            this.children = new ArrayList<>(children);
            this.annualFee += children.size() * CHILD_FEE;
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

        if (children != null && !children.isEmpty()) {
            this.children = new ArrayList<>(children);
        }
    }

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

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getSecondAdult() {
        return secondAdult;
    }

    public void setSecondAdult(String secondAdult) {
        this.secondAdult = secondAdult;
    }

    /**
     * Devuelve una vista no modificable de la lista de hijos.
     * Para modificar la lista, use {@link #addChild(Hijos)}.
     */
    public List<Hijos> getChildren() {
        if (children == null) {
            return null;
        }
        return Collections.unmodifiableList(children);
    }

    public void setChildren(List<Hijos> children) {
        this.children = children != null ? new ArrayList<>(children) : null;
    }

    public void addChild(Hijos child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    /**
     * Recalcula la cuota anual basándose en la composición actual de la inscripción.
     */
    public void recalcularCuota() {
        this.annualFee = BASE_ANNUAL_FEE;
        if (secondAdult != null && !secondAdult.isEmpty()) {
            this.annualFee += SECOND_ADULT_FEE;
        }
        if (children != null) {
            this.annualFee += children.size() * CHILD_FEE;
        }
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
