package com.GM2.model.repository;

import com.GM2.model.domain.Familiar;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class FamiliarRepository extends AbstractRepository{

    public FamiliarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Familiar> findAllFamiliars() {
        try {
            String query = sqlQueries.getProperty("select-findAllFamiliars");
            if( query != null ) {
                List<Familiar> result = jdbcTemplate.query(query, new RowMapper<Familiar>() {
                   public Familiar mapRow(ResultSet rs, int rowNum) throws SQLException {
                       return new Familiar(
                               rs.getString("id"),
                               rs.getString("Inscripcion_id"),
                               rs.getString("Socio_id")
                       );
                   };
                });

                return result;
            } else return null;

        } catch (DataAccessException exception) {
            System.err.println("Unable to find Familiars");
            exception.printStackTrace();
            return null;
        }
    }

    public Familiar findFamiliarById(String id) {
        try {
            String query = sqlQueries.getProperty("select-findFamiliarById");
            Familiar result = jdbcTemplate.query(query, this::mapRowToFamiliar, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find Familiar with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    private Familiar mapRowToFamiliar(ResultSet row) {
        try {

            if (row.first()) {
                String id = row.getString("id");
                String idInscripcion = row.getString("Inscripcion_id");
                String idSocio = row.getString("Socio_id");

                Familiar familiar = new Familiar(id, idInscripcion, idSocio);
                return familiar;
            }else{
                return null;
            }
        } catch (SQLException exception) {
                System.err.println("Unable to retrieve results from the database");
                exception.printStackTrace();
                return null;    
        }
    }

    public boolean addFamiliar(Familiar familiar) {
        try {
            String query = sqlQueries.getProperty("insert-addFamiliar");
            if (query == null) {
                int result = jdbcTemplate.update(query, 
                familiar.getId(), 
                familiar.getIdInscripcion(), 
                familiar.getIdSocio()
            );

            if (result > 0) {
                return true;
            } else {
                return false;
            }

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert Familiar in the database");
        }
        return false;
    }


}

