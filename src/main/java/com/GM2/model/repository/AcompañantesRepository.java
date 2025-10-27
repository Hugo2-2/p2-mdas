package com.GM2.model.repository;

import com.GM2.model.domain.Acompañantes;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AcompañantesRepository extends AbstractRepository {
    private JdbcTemplate jdbcTemplate;

    public AcompañantesRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Acompañantes> findAllAcompañantes() {
        try {
            String query = sqlQueries.getProperty("select-findAllAcompanantes");
            if(query != null){
                List<Acompañantes> result = jdbcTemplate.query(query, new RowMapper<Acompañantes>() {
                    public Acompañantes mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Acompañantes(
                                rs.getString("dni"),
                                rs.getInt("id_alquiler")
                        );
                    };
                });

                return result;
            } else return null;

        } catch (DataAccessException exception) {
            System.err.println("Unable to find acompanantes");
            exception.printStackTrace();
            return null;
        }
    }

    public Acompañantes findAcompañanteByDni(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findAcompananteByDni");
            Acompañantes result = jdbcTemplate.query(query, this::mapRowToAcompañantes, dni);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find acompanante with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    public List<Acompañantes> findAcompañantesByAlquiler(int id_alquiler) {
        try {
            String query = sqlQueries.getProperty("select-findAcompanantesByAlquiler");
            List<Acompañantes> result = jdbcTemplate.query(query, this::mapRowFromAlquiler, id_alquiler);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find acompanantes with id_alquiler: " + id_alquiler);
            exception.printStackTrace();
            return null;
        }
    }

    private Acompañantes mapRowFromAlquiler(ResultSet row, int rowNum) throws SQLException {
                String dni = row.getString("dni");
                int id_alquiler = row.getInt("id_alquiler");

                return new Acompañantes(dni, id_alquiler);
            
    }
    
    private Acompañantes mapRowToAcompañantes(ResultSet row) {
        try {

            if(row.first()) {
                String dni = row.getString("dni");
                int id_alquiler = row.getInt("id_alquiler");

                Acompañantes acompañante = new Acompañantes(dni, id_alquiler);
                return acompañante;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            System.err.println("Unable to map row to Acompanantes object");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addAcompañante(Acompañantes acompañante) {
        try {
            String query = sqlQueries.getProperty("insert-addAcompanante");
            int rowsAffected = jdbcTemplate.update(query, acompañante.getDni(), acompañante.getId_alquiler());
            return rowsAffected > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to add acompanante");
            exception.printStackTrace();
            return false;
        }
    }
    
}
