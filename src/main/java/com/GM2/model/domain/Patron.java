package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Clase que representa a un patrón en el sistema.
 * Contiene los datos personales y la información relevante sobre su título.
 *
 * Cada instancia de esta clase corresponde a un patrón que puede estar
 * asociado a embarcaciones en la aplicación.
 */
public class Patron {

    private String name;
    private String surname;
    private String nationalId;
    private LocalDate birthDate;
    private LocalDate titleIssueDate;

    /**
     * Constructor vacío.
     * Necesario para frameworks como Spring al crear instancias mediante reflexión.
     */
    public Patron() {
    }

    /**
     * Constructor completo.
     * Permite crear un patrón con toda su información básica.
     *
     * @param name Nombre del patrón
     * @param surname Apellidos del patrón
     * @param nationalId DNI del patrón (identificador único)
     * @param birthDate Fecha de nacimiento
     * @param titleIssueDate Fecha de expedición del título de patrón
     */
    public Patron(String name, String surname, String nationalId, LocalDate birthDate, LocalDate titleIssueDate) {
        this.name = name;
        this.surname = surname;
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.titleIssueDate = titleIssueDate;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getName() {
        return name;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setName(String name) {
        this.name = name;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getSurname() {
        return surname;
    }

     //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setSurname(String surname) {
        this.surname = surname;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getNationalId() {
        return nationalId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public LocalDate getBirthDate() {
        return birthDate;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public LocalDate getTitleIssueDate() {
        return titleIssueDate;
    }

      //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setTitleIssueDate(LocalDate titleIssueDate) {
        this.titleIssueDate = titleIssueDate;
    }

    /**
     * Devuelve una representación en forma de cadena de texto del patrón.
     * Muestra la información personal del patrón, incluyendo su identificador único (DNI),
     * nombre completo y fechas de nacimiento y expedición del título.
     *
     * @return Una cadena (String) con la información del patrón.
     */
    @Override
    public String toString() {
        return "Patron {" +
                "dni='" + nationalId + '\'' +
                ", nombre='" + name + '\'' +
                ", apellidos='" + surname + '\'' +
                ", fechaNacimiento=" + birthDate +
                ", fechaExpedicionTitulo=" + titleIssueDate +
                '}';
    }
}
