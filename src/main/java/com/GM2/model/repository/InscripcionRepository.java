package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InscripcionRepository extends AbstractRepository{

    private HijosRepository hijosRepository;

    public InscripcionRepository(JdbcTemplate jdbcTemplate, HijosRepository hijosRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.hijosRepository = hijosRepository;
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

    public boolean updateInscripcion(Inscripcion inscripcion) {
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
                    return true;
                else return false;

            } else return false;
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return false;
        }
    }
}
