package com.GM2.model.domain;

import java.time.LocalDate;
import java.time.Period;

/**
 * Representa un socio del club náutico.
 * Esta clase almacena la información personal de los socios,
 * incluyendo sus datos de identificación, fechas importantes y privilegios
 * como la titularidad y la licencia de patrón.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Socio {
    private String name;
    private String surname;
    private String nationalId;
    private LocalDate birthDate;
    private String address;
    private LocalDate registrationDate;
    private Boolean isTitular;
    private Boolean hasSkipperLicense;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Socio() {}

    /**
     * Constructor completo para crear un socio con todos sus datos.
     *
     * @param hasSkipperLicense Indica si el socio tiene licencia de patrón.
     * @param isTitular Indica si el socio es titular de la inscripción.
     * @param registrationDate Fecha de inscripción en el club.
     * @param address Dirección del socio.
     * @param birthDate Fecha de nacimiento del socio.
     * @param nationalId DNI del socio.
     * @param surname Apellidos del socio.
     * @param name Nombre del socio.
     */
    public Socio(Boolean hasSkipperLicense, Boolean isTitular, LocalDate registrationDate, String address, LocalDate birthDate, String nationalId, String surname, String name) {
        this.hasSkipperLicense = hasSkipperLicense;
        this.isTitular = isTitular;
        this.registrationDate = registrationDate;
        this.address = address;
        this.birthDate = birthDate;
        this.nationalId = nationalId;
        this.surname = surname;
        this.name = name;
    }

    /**
     * Constructor para crear un socio estableciendo la fecha de inscripción como la fecha actual.
     *
     * @param hasSkipperLicense Indica si el socio tiene licencia de patrón.
     * @param isTitular Indica si el socio es titular de la inscripción.
     * @param address Dirección del socio.
     * @param birthDate Fecha de nacimiento del socio.
     * @param nationalId DNI del socio.
     * @param surname Apellidos del socio.
     * @param name Nombre del socio.
     */
    public Socio(Boolean hasSkipperLicense, Boolean isTitular, String address, LocalDate birthDate, String nationalId, String surname, String name) {
        this.hasSkipperLicense = hasSkipperLicense;
        this.isTitular = isTitular;
        this.registrationDate = LocalDate.now();
        this.address = address;
        this.birthDate = birthDate;
        this.nationalId = nationalId;
        this.surname = surname;
        this.name = name;
    }

    public Socio(String name, String surname, String nationalId, LocalDate birthDate, String address, LocalDate registrationDate, Boolean isTitular, Boolean hasSkipperLicense) {
        this.name = name;
        this.surname = surname;
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.address = address;
        this.registrationDate = registrationDate;
        this.isTitular = hasSkipperLicense;
        this.hasSkipperLicense = hasSkipperLicense;
    }

    public Socio(String name, String surname, String nationalId, LocalDate birthDate, String address, LocalDate registrationDate, Boolean hasSkipperLicense) {
        this.name = name;
        this.surname = surname;
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.address = address;
        this.registrationDate = registrationDate;
        this.hasSkipperLicense = hasSkipperLicense;
        this.isTitular = false;
    }

    public Socio(String name, String surname, String nationalId, LocalDate birthDate, String address, Boolean hasSkipperLicense) {
        this.name = name;
        this.surname = surname;
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.address = address;
        this.hasSkipperLicense = hasSkipperLicense;
    }

    // Getters y Setters

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

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getIsTitular() {
        return isTitular;
    }

    public void setIsTitular(Boolean isTitular) {
        this.isTitular = isTitular;
    }

    public Boolean getHasSkipperLicense() {
        return hasSkipperLicense;
    }

    public void setHasSkipperLicense(Boolean hasSkipperLicense) {
        this.hasSkipperLicense = hasSkipperLicense;
    }

    /**
     * Determina si el socio es mayor de edad.
     * Calcula la edad actual del socio basándose en su fecha de nacimiento
     * y verifica si es mayor o igual a 18 años.
     *
     * @return true si el socio es mayor de edad (18 años o más), false en caso contrario.
     */
    public boolean isOfLegalAge() {
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();
        return age >= 18;
    }

    @Override
    public String toString() {
        return "Socio{" +
                "nombre='" + name + '\'' +
                ", apellidos='" + surname + '\'' +
                ", dni='" + nationalId + '\'' +
                ", fechaNacimiento=" + birthDate +
                ", direccion='" + address + '\'' +
                ", fechaInscripcion=" + registrationDate +
                ", esTitular=" + isTitular +
                ", tieneLicenciaPatron=" + hasSkipperLicense +
                '}';
    }
}


