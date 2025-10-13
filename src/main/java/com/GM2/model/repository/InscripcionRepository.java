package com.GM2.model.repository;

import com.GM2.model.domain.Inscripcion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InscripcionRepository extends AbstractRepository{

    public InscripcionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Inscripcion> findAllInscripciones() {
        try {
            String query = sqlQueries.getProperty("select-findAllInscripciones");
            if( query != null ) {
                List<Inscripcion> result = jdbcTemplate.query(query, new RowMapper<Inscripcion>() {
                   public Inscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
                       return new Inscripcion(
                               rs.getString("id"),
                               rs.getDate("fechaCreacion").toLocalDate(),
                               rs.getString("CuotaAnual"),
                               rs.getString("TipoInscripcion"),
                               rs.getString("SocioTitular")
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

    public Inscripcion findInscripcionById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionById");
            Inscripcion result = jdbcTemplate.query(query, this::mapRowToInscripcion, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find inscripcion with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    private Inscripcion mapRowToInscripcion(ResultSet row) {
        try {
            if(row.first()) {
                String id = row.getString("id");
                Date fechaCreacion = row.getDate("fechaCreacion");
                String cuotaAnual = row.getString("CuotaAnual");
                String tipoInscripcion = row.getString("TipoInscripcion");
                String socioTitular = row.getString("SocioTitular");

                Inscripcion inscripcion = new Inscripcion(id, fechaCreacion.toLocalDate(), cuotaAnual, tipoInscripcion, socioTitular);
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

    public boolean addInscripcion(Inscripcion inscripcion) {
        try {
            String query = sqlQueries.getProperty("insert-addInscripcion");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                    inscripcion.getId(),
                    inscripcion.getFechaCreacion(),
                    inscripcion.getCuotaAnual(),
                    inscripcion.getTipo(),
                    inscripcion.getSocioTitularId()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;
         } catch (DataAccessException exception) {
            System.err.println("Unable to insert inscripcion in the database");
        }
        return false;
    }
    
}
