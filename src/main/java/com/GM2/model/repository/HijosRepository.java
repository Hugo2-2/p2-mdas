package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HijosRepository extends AbstractRepository {
    private JdbcTemplate jdbcTemplate;

    public HijosRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Hijos> findAllHijos() {
        try {
            String query = sqlQueries.getProperty("select-findAllHijos");
            if(query != null){
                List<Hijos> result = jdbcTemplate.query(query, new RowMapper<Hijos>() {
                    public Hijos mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Hijos(
                                rs.getString("dni"),
                                rs.getInt("id_inscripcion")
                        );
                    };
                });

                return result;
            } else return null;

        } catch (DataAccessException exception) {
            System.err.println("Unable to find hijos");
            exception.printStackTrace();
            return null;
        }
    }

    public Hijos findHijoByDni(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findHijoByDni");
            Hijos result = jdbcTemplate.query(query, this::mapRowToHijos, dni);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find hijo with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    private Hijos mapRowToHijos(ResultSet row) {
        try {

            if(row.first()) {
                String dni = row.getString("dni");
                int id_inscripcion = row.getInt("id_inscripcion");

                Hijos hijo = new Hijos(dni, id_inscripcion);
                return hijo;
            } else {
                return null;
            }

        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addHijo(Hijos hijo) {
         try {
            String query = sqlQueries.getProperty("insert-addHijo");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        hijo.getDni(),
                        hijo.getId_inscripcion()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert patron in the database");
        }

        return false;
    }
}
