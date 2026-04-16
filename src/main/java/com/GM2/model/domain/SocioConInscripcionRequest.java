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

    // Getters y Setters

    /**
     * Obtiene el DNI del nuevo socio.
     * @return DNI del socio.
     */
    public String getNationalId() { 
        return nationalId; 
    }

    /**
     * Establece el DNI del nuevo socio.
     * @param nationalId DNI del socio.
     */
    public void setNationalId(String nationalId) { 
        this.nationalId = nationalId; 
    }

    /**
     * Obtiene el nombre del nuevo socio.
     * @return Nombre del socio.
     */
    public String getName() { 
        return name; 
    }

    /**
     * Establece el nombre del nuevo socio.
     * @param name Nombre del socio.
     */
    public void setName(String name) { 
        this.name = name; 
    }

    /**
     * Obtiene los apellidos del nuevo socio.
     * @return Apellidos del socio.
     */
    public String getSurname() { 
        return surname; 
    }

    /**
     * Establece los apellidos del nuevo socio.
     * @param surname Apellidos del socio.
     */
    public void setSurname(String surname) { 
        this.surname = surname; 
    }

    /**
     * Obtiene la fecha de nacimiento del nuevo socio.
     * @return Fecha de nacimiento del socio.
     */
    public LocalDate getBirthDate() { 
        return birthDate; 
    }

    /**
     * Establece la fecha de nacimiento del nuevo socio.
     * @param birthDate Fecha de nacimiento del socio.
     */
    public void setBirthDate(LocalDate birthDate) { 
        this.birthDate = birthDate; 
    }

    /**
     * Obtiene la dirección del nuevo socio.
     * @return Dirección del socio.
     */
    public String getAddress() { 
        return address; 
    }

    /**
     * Establece la dirección del nuevo socio.
     * @param address Dirección del socio.
     */
    public void setAddress(String address) { 
        this.address = address; 
    }

    /**
     * Obtiene si el socio tiene licencia de patrón.
     * @return true si tiene licencia, false en caso contrario.
     */
    public Boolean getIsSkipper() { 
        return isSkipper; 
    }

    /**
     * Establece si el socio tiene licencia de patrón.
     * @param isSkipper true si tiene licencia, false en caso contrario.
     */
    public void setIsSkipper(Boolean isSkipper) { 
        this.isSkipper = isSkipper; 
    }

    /**
     * Obtiene el DNI del titular de la inscripción existente.
     * @return DNI del titular.
     */
    public String getTitularNationalId() { 
        return titularNationalId; 
    }

    /**
     * Establece el DNI del titular de la inscripción existente.
     * @param titularNationalId DNI del titular.
     */
    public void setTitularNationalId(String titularNationalId) { 
        this.titularNationalId = titularNationalId; 
    }
}
