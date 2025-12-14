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
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private Boolean patron;
    private String dniTitular;

    /**
     * Constructor por defecto.
     */
    public SocioConInscripcionRequest() {}

    /**
     * Constructor con todos los parámetros.
     *
     * @param dni DNI del nuevo socio.
     * @param nombre Nombre del nuevo socio.
     * @param apellidos Apellidos del nuevo socio.
     * @param fechaNacimiento Fecha de nacimiento del nuevo socio.
     * @param direccion Dirección del nuevo socio.
     * @param patron Indica si el socio tiene licencia de patrón.
     * @param dniTitular DNI del titular de la inscripción existente.
     */
    public SocioConInscripcionRequest(String dni, String nombre, String apellidos, 
                                      LocalDate fechaNacimiento, String direccion, 
                                      Boolean patron, String dniTitular) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.patron = patron;
        this.dniTitular = dniTitular;
    }

    // Getters y Setters

    /**
     * Obtiene el DNI del nuevo socio.
     * @return DNI del socio.
     */
    public String getDni() { 
        return dni; 
    }

    /**
     * Establece el DNI del nuevo socio.
     * @param dni DNI del socio.
     */
    public void setDni(String dni) { 
        this.dni = dni; 
    }

    /**
     * Obtiene el nombre del nuevo socio.
     * @return Nombre del socio.
     */
    public String getNombre() { 
        return nombre; 
    }

    /**
     * Establece el nombre del nuevo socio.
     * @param nombre Nombre del socio.
     */
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    /**
     * Obtiene los apellidos del nuevo socio.
     * @return Apellidos del socio.
     */
    public String getApellidos() { 
        return apellidos; 
    }

    /**
     * Establece los apellidos del nuevo socio.
     * @param apellidos Apellidos del socio.
     */
    public void setApellidos(String apellidos) { 
        this.apellidos = apellidos; 
    }

    /**
     * Obtiene la fecha de nacimiento del nuevo socio.
     * @return Fecha de nacimiento del socio.
     */
    public LocalDate getFechaNacimiento() { 
        return fechaNacimiento; 
    }

    /**
     * Establece la fecha de nacimiento del nuevo socio.
     * @param fechaNacimiento Fecha de nacimiento del socio.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento; 
    }

    /**
     * Obtiene la dirección del nuevo socio.
     * @return Dirección del socio.
     */
    public String getDireccion() { 
        return direccion; 
    }

    /**
     * Establece la dirección del nuevo socio.
     * @param direccion Dirección del socio.
     */
    public void setDireccion(String direccion) { 
        this.direccion = direccion; 
    }

    /**
     * Obtiene si el socio tiene licencia de patrón.
     * @return true si tiene licencia, false en caso contrario.
     */
    public Boolean getPatron() { 
        return patron; 
    }

    /**
     * Establece si el socio tiene licencia de patrón.
     * @param patron true si tiene licencia, false en caso contrario.
     */
    public void setPatron(Boolean patron) { 
        this.patron = patron; 
    }

    /**
     * Obtiene el DNI del titular de la inscripción existente.
     * @return DNI del titular.
     */
    public String getDniTitular() { 
        return dniTitular; 
    }

    /**
     * Establece el DNI del titular de la inscripción existente.
     * @param dniTitular DNI del titular.
     */
    public void setDniTitular(String dniTitular) { 
        this.dniTitular = dniTitular; 
    }
}
