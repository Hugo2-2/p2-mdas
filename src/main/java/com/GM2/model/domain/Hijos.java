package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Representa a los hijos asociados a una inscripción en el club náutico.
 * Esta clase almacena la información personal de los menores que forman
 * parte de una inscripción familiar, incluyendo sus datos de identificación
 * y la relación con la inscripción a la que pertenecen.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Hijos {
    private String nationalId;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private int registrationId;

    /**
     * Constructor completo para crear un registro de hijo con todos sus datos.
     *
     * @param nationalId DNI del hijo.
     * @param name Nombre del hijo.
     * @param surname Apellidos del hijo.
     * @param birthDate Fecha de nacimiento del hijo.
     * @param registrationId ID de la inscripción a la que pertenece el hijo.
     */
    public Hijos(String nationalId, String name, String surname, LocalDate birthDate, int registrationId) {
        this.nationalId = nationalId;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.registrationId = registrationId;
    }

    public Hijos(String nationalId, String name, String surname, LocalDate birthDate) {
        this.nationalId = nationalId;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Hijos() {

    }

    // Getters y Setters

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Hijos{" +
                "dni='" + nationalId + '\'' +
                ", nombre='" + name + '\'' +
                ", apellidos='" + surname + '\'' +
                ", fechaNacimiento=" + birthDate +
                ", id_inscripcion=" + registrationId +
                '}';
    }
}
