package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Clase que encapsula la petición para crear un socio
 * y asociarlo a una inscripción familiar existente.
 * 
 * Contiene los datos del nuevo socio y el DNI del titular
 * de la inscripción a la que se desea añadir.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class SocioConInscripcionRequest {
    private String nationalId;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String address;
    private Boolean isSkipper;
    private String titularNationalId;

    /**
     * Constructor por defecto.
     */
    public SocioConInscripcionRequest() {}

    /**
     * Constructor con todos los parámetros.
     *
     * @param nationalId DNI del nuevo socio.
     * @param name Nombre del nuevo socio.
     * @param surname Apellidos del nuevo socio.
     * @param birthDate Fecha de nacimiento del nuevo socio.
     * @param address Dirección del nuevo socio.
     * @param isSkipper Indica si el socio tiene licencia de patrón.
     * @param titularNationalId DNI del titular de la inscripción existente.
     */
    public SocioConInscripcionRequest(String nationalId, String name, String surname, 
                                      LocalDate birthDate, String address, 
                                      Boolean isSkipper, String titularNationalId) {
        this.nationalId = nationalId;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.address = address;
        this.isSkipper = isSkipper;
        this.titularNationalId = titularNationalId;
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
    public LocalDate getBirthDate() { 
        return birthDate; 
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setBirthDate(LocalDate birthDate) { 
        this.birthDate = birthDate; 
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getAddress() { 
        return address; 
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setAddress(String address) { 
        this.address = address; 
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public Boolean getIsSkipper() { 
        return isSkipper; 
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setIsSkipper(Boolean isSkipper) { 
        this.isSkipper = isSkipper; 
    }

     //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getTitularNationalId() { 
        return titularNationalId; 
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setTitularNationalId(String titularNationalId) { 
        this.titularNationalId = titularNationalId; 
    }
}
