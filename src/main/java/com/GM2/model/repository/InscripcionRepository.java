package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InscripcionRepository extends AbstractRepository{

    final private HijosRepository hijosRepository;
    final private SocioRepository socioRepository;

    public InscripcionRepository(JdbcTemplate jdbcTemplate, HijosRepository hijosRepository, @Lazy SocioRepository socioRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.hijosRepository = hijosRepository;
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    private Inscripcion mapRowToInscripcion(ResultSet rs) {

        try {
            if(rs.first()) {
                int id = rs.getInt("id");
                String socioTitular = rs.getString("socio_Titular");
                float cuotaAnual = rs.getFloat("cuota_anual");
                LocalDate fechaCreacion = rs.getDate("fecha_creacion").toLocalDate();
                String segundoAdulto = rs.getString("segundo_adulto");

                List<Hijos> hijos = new ArrayList<>();

                if(cuotaAnual > 300) {
                    hijos = hijosRepository.findHijosByInscripcion(id);
                }

                Inscripcion inscripcion = new Inscripcion(id, socioTitular, cuotaAnual, fechaCreacion, segundoAdulto, hijos);

                return inscripcion;
            } else {
                return null;
            }



        } catch (SQLException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }

    }

    public Inscripcion findInscripcionById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionById");
            Inscripcion result = jdbcTemplate.query(query, this::mapRowToInscripcion, id);

            if(result != null)
                return result;
            else return null;

        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }

    }

    public Inscripcion findInscripcionByDNITitular(String dniTitular) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionByDniTitular");
            Inscripcion result = jdbcTemplate.query(query, this::mapRowToInscripcion, dniTitular);

            if(result != null)
                return result;
            else return null;
        }  catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }
    }

    public boolean addInscripcion(Inscripcion inscripcion) {

        if( inscripcion == null ) return false;

        if( findInscripcionByDNITitular( inscripcion.getSocioTitularId() ) != null ) return false;

        try {
            String query = sqlQueries.getProperty("insert-addInscripcion");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                    inscripcion.getSocioTitularId(),
                    inscripcion.getCuotaAnual(),
                    inscripcion.getFechaCreacion()
                );

                if(result > 0)
                    return true;
                else return false;
            } else return false;

        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return false;
        }

    }

    public String updateInscripcion(Inscripcion inscripcion) {

        if( inscripcion == null ) return "No se ha podido ingresar la inscripcion";

        if( findInscripcionByDNITitular(inscripcion.getSocioTitularId()) == null )
            return "No puedes actualizar la inscripcion porque no existe";

        inscripcion.setFechaCreacion();

        try {
            String query = sqlQueries.getProperty("update-Inscripcion");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                    inscripcion.getCuotaAnual(),
                    inscripcion.getFechaCreacion(),
                    inscripcion.getSegundoAudlto(),
                    inscripcion.getId()
                );

                if(result > 0)
                    return "EXITO";
                else return "No se ha podido actualizar la inscripcion";

            } else return "No se ha podido actualizar la inscripcion";
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return "No se ha podido actualizar la inscripcion";
        }
    }

    public String updateInscripcioSinHijos(String dniTitular, String dniSegundoAdulto){

        if(socioRepository.findSocioByDNI(dniTitular) == null && socioRepository.findSocioByDNI(dniSegundoAdulto) == null) {
            return "El titular y el segundo adulto no están registrados como socios";
        } else if(socioRepository.findSocioByDNI(dniTitular) == null) {
            return "El titular no está registrado como socio";
        } else if(socioRepository.findSocioByDNI(dniSegundoAdulto) == null) {
            return "El segundo adulto no está registrado como socio.";
        }

        if(!socioRepository.findSocioByDNI(dniTitular).getEsTitular()) {
            return "El socio introducido como titular no es titular de ninguna inscripción";
        }

        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null) {
            return "No puedes actualizar la inscripcion porque no existe";
        }

        inscripcion.setCuotaAnual(inscripcion.getCuotaAnual() + 250);
        inscripcion.setSegundoAudlto(dniSegundoAdulto);

        return updateInscripcion(inscripcion);

    }

    public String updateInscripcioConHijos(String dniTitular, List<String> dnisHijos, List<String> nombreHijos, List<String> apellidosHijos, List<LocalDate> fechaNacimientoHijos){
        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null) {
            return "No puedes actualizar la inscripcion porque no existe";
        }

        for (int i = 0; i < dnisHijos.size(); i++) {

            String dni = dnisHijos.get(i);

            // Solo procesamos si el DNI no está vacío
            if (dni != null && !dni.trim().isEmpty()) {

                Hijos hijo = new Hijos();
                hijo.setDni(dni);
                hijo.setNombre(nombreHijos.get(i));
                hijo.setApellidos(apellidosHijos.get(i));

                // Asignamos el ID de la inscripción principal
                hijo.setId_inscripcion(inscripcion.getId());

                // Parseamos y validamos la fecha
                try {
                    hijo.setFechaNacimiento(fechaNacimientoHijos.get(i));
                } catch (DateTimeParseException e) {
                    return "Error: El formato de fecha del hijo " + (i + 1) + " no es válido (use AAAA-MM-DD).";
                }

                // Guardamos el hijo completo en la base de datos
                hijosRepository.addHijo(hijo);

                // Actualizamos la cuota en el objeto de inscripción (100€ por hijo)
                inscripcion.setCuotaAnual(inscripcion.getCuotaAnual() + 100);
            }
        }

        return updateInscripcion(inscripcion);

    }
}
