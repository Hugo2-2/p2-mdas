package com.GM2.model.repository;

import com.GM2.model.domain.InscripcionIndividual;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InscripcionIndividualRepository extends AbstractRepository{

    public InscripcionIndividualRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InscripcionIndividual> findAllInscripciones() {
        try {
            String query = sqlQueries.getProperty("select-findAllInscripcionesIndividuales");
            if( query != null ) {
                List<InscripcionIndividual> result = jdbcTemplate.query(query, new RowMapper<InscripcionIndividual>() {
                   public InscripcionIndividual mapRow(ResultSet rs, int rowNum) throws SQLException {
                       return new InscripcionIndividual(
                               rs.getInt("id"),
                               rs.getDate("fecha_creacion").toLocalDate(),
                               rs.getFloat("cuota_anual"),
                               rs.getString("socio_Titular")
                       );
                   };
                });

                return result;
            } else return null;

        } catch (DataAccessException exception) {
            System.err.println("Unable to find inscripciones");
            exception.printStackTrace();
            return null;
        }
    }

    public InscripcionIndividual findInscripcionById(String id) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionIndividualById");
            InscripcionIndividual result = jdbcTemplate.query(query, this::mapRowToInscripcion, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find inscripcion with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    private InscripcionIndividual mapRowToInscripcion(ResultSet row) {
        try {
            if(row.first()) {
                int id = row.getInt("id");
                Date fechaCreacion = row.getDate("fechaCreacion");
                float cuotaAnual = row.getFloat("CuotaAnual");
                String socioTitular = row.getString("SocioTitular");

                InscripcionIndividual inscripcion = new InscripcionIndividual(id, fechaCreacion.toLocalDate(), cuotaAnual, socioTitular);
                return inscripcion;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addInscripcion(InscripcionIndividual inscripcion) {
        try {

            

            String query = sqlQueries.getProperty("insert-addInscripcionIndividual");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                    inscripcion.getId(),
                    inscripcion.getFechaCreacion(),
                    inscripcion.getCuotaAnual(),
                    inscripcion.getSocioTitularId()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;
        } catch (DataAccessException exception) {
            System.err.println("Unable to insert inscripcion in the database");
            exception.printStackTrace();
        }
        return false;
    }
    
}
