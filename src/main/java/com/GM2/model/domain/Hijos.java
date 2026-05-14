package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Clase de dominio que representa a un hijo dentro de una inscripción familiar.
 * Almacena datos personales del menor y su relación con la inscripción familiar.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Hijos {
    private String dni;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private int registrationId;

    /**
     * Constructor por defecto.
     */
    public Hijos() {}

    /**
     * Constructor con datos personales básicos.
     *
     * @param dni DNI del hijo.
     * @param name Nombre del hijo.
     * @param surname Apellidos del hijo.
     * @param birthDate Fecha de nacimiento del hijo.
     */
    public Hijos(String dni, String name, String surname, LocalDate birthDate) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }

    /**
     * Constructor completo con ID de inscripción.
     *
     * @param dni DNI del hijo.
     * @param name Nombre del hijo.
     * @param surname Apellidos del hijo.
     * @param birthDate Fecha de nacimiento del hijo.
     * @param registrationId ID de la inscripción a la que pertenece.
     */
    public Hijos(String dni, String name, String surname, LocalDate birthDate, int registrationId) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.registrationId = registrationId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    @Override
    public String toString() {
        return "Hijos{" +
                "dni='" + dni + '\'' +
                ", nombre='" + name + '\'' +
                ", apellidos='" + surname + '\'' +
                ", fechaNacimiento=" + birthDate +
                ", inscripcionId=" + registrationId +
                '}';
    }
}
