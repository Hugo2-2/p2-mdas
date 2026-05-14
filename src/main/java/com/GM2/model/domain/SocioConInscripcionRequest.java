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

    public String getNationalId() { 
        return nationalId; 
    }

    public void setNationalId(String nationalId) { 
        this.nationalId = nationalId; 
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

    public String getAddress() { 
        return address; 
    }

    public void setAddress(String address) { 
        this.address = address; 
    }

    public Boolean getIsSkipper() { 
        return isSkipper; 
    }

    public void setIsSkipper(Boolean isSkipper) { 
        this.isSkipper = isSkipper; 
    }

    public String getTitularNationalId() { 
        return titularNationalId; 
    }

    public void setTitularNationalId(String titularNationalId) { 
        this.titularNationalId = titularNationalId; 
    }
}
