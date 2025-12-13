package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un alquiler de embarcación en el sistema del Club Náutico.
 * Contiene información sobre las fechas, plazas, precios y relaciones con socios y embarcaciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class Alquiler {
    private int id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precio;
    private int plazas;
    private String usuario_dni;
    private String matricula_embarcacion;
    private List<Acompanante> acompanantes;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Alquiler() {
        this.acompanantes = new ArrayList<>();
    }

    /**
     * Constructor con todos los parámetros.
     * 
     * @param id ID del alquiler
     * @param fechaInicio Fecha de inicio del alquiler
     * @param fechaFin Fecha de fin del alquiler
     * @param precio Precio total del alquiler
     * @param plazas Número de plazas reservadas
     * @param usuario_dni DNI del usuario que realiza el alquiler
     * @param matricula_embarcacion Matrícula de la embarcación alquilada
     * @param acompanantes Lista de acompañantes
     */
    public Alquiler(int id, LocalDate fechaInicio, LocalDate fechaFin, double precio, int plazas, String usuario_dni, String matricula_embarcacion, List<Acompanante> acompanantes) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precio = precio;
        this.plazas = plazas;
        this.usuario_dni = usuario_dni;
        this.matricula_embarcacion = matricula_embarcacion;
        this.acompanantes = acompanantes;
    }

    /**
     * Obtiene el ID del alquiler.
     * 
     * @return ID del alquiler
     */
    public int getId() {
        return id;
    }
    
    /**
     * Establece el ID del alquiler.
     * 
     * @param id ID del alquiler     
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtiene la fecha de inicio del alquiler.
     * 
     * @return Fecha de inicio del alquiler         
     */
    public LocalDate getFechainicio() {
        return fechaInicio;
    }
    
    /**
     * Establece la fecha de inicio del alquiler.
     * 
     * @param fechainicio Fecha de inicio del alquiler         
     */
    public void setFechainicio(LocalDate fechainicio) {
        this.fechaInicio = fechainicio;
    }
    
    /**
     * Obtiene la fecha de fin del alquiler.
     * 
     * @return Fecha de fin del alquiler         
     */
    public LocalDate getFechafin() {
        return fechaFin;
    }
    
    /**
     * Establece la fecha de fin del alquiler.
     * 
     * @param fechafin Fecha de fin del alquiler         
     */
    public void setFechafin(LocalDate fechafin) {
        this.fechaFin = fechafin;
    }
    
    /**
     * Obtiene el precio total del alquiler.
     * 
     * @return Precio total del alquiler         
     */
    public double getPrecio() {
        return precio;
    }
    
    /**
     * Establece el precio total del alquiler.
     * 
     * @param precio Precio total del alquiler         
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    /**
     * Obtiene el número de plazas reservadas.
     * 
     * @return Número de plazas reservadas         
     */
    public int getPlazas() {
        return plazas;
    }
    
    /**
     * Establece el número de plazas reservadas.
     * 
     * @param plazas Número de plazas reservadas         
     */
    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    /**
     * Obtiene el DNI del usuario que realiza el alquiler.
     * 
     * @return DNI del usuario         
     */
    public String getUsuario_dni() {
        return usuario_dni;
    }

    /**
     * Establece el DNI del usuario que realiza el alquiler.
     * 
     * @param usuario_dni DNI del usuario         
     */
    public void setUsuario_dni(String usuario_dni) {
        this.usuario_dni = usuario_dni;
    }

    /**
     * Obtiene la lista de acompañantes del alquiler.
     * 
     * @return Lista de acompañantes del alquiler         
     */
    public List<Acompanante> getAcompanantes() {
        return acompanantes;
    }

    /**
     * Establece la lista de acompañantes del alquiler.
     * 
     * @param acompanantes Lista de acompañantes del alquiler         
     */
    public void setAcompanantes(List<Acompanante> acompanantes) {
        this.acompanantes = acompanantes;
    }

    /**
     * Obtiene la matrícula de la embarcación alquilada.
     * 
     * @return Matrícula de la embarcación alquilada         
     */
    public String getMatricula_embarcacion() {
        return matricula_embarcacion;
    }

    /**
     * Establece la matrícula de la embarcación alquilada.
     * 
     * @param matricula_embarcacion Matrícula de la embarcación alquilada         
     */
    public void setMatricula_embarcacion(String matricula_embarcacion) {
        this.matricula_embarcacion = matricula_embarcacion;
    }


    /**
     * Representación de un alquiler en String.
     * 
     * @return Representación de un alquiler en String
     */
    @Override
    public String toString() {
        String result = "Alquiler {\n";
        result += "  ID: " + id + "\n";
        result += "  Fecha inicio: " + fechaInicio + "\n";
        result += "  Fecha fin: " + fechaFin + "\n";
        result += "  Plazas: " + plazas + "\n";
        result += "  Precio: " + precio + "€\n";
        result += "  DNI Socio: " + usuario_dni + "\n";
        result += "  Matrícula: " + matricula_embarcacion + "\n";
        
        if (acompanantes != null && !acompanantes.isEmpty()) {
            result += "  Acompañantes (" + acompanantes.size() + "):\n";
            for (Acompanante a : acompanantes) {
                result += "    - " + a + "\n"; // Llama a a.toString()
            }
        } else {
            result += "  Acompañantes: Ninguno\n";
        }
        
        result += "}";
        return result;
    }   
}
