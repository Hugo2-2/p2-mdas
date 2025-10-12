package com.GM2.model.repository;

import com.GM2.model.domain.Alquiler;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AlquilerRepository {

    public AlquilerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Alquiler> findAllAlquileres() {
        try {
            String query = sqlQueries.getProperty("select-findAllAlquileres");
            if( query != null ) {
                List<Alquiler> result = jdbcTemplate.query(query, new RowMapper<Alquiler>() {
                   public Alquiler mapRow(ResultSet rs, int rowNum) throws SQLException {
                       return new Alquiler(
                               rs.getInt("id"),
                               rs.getDate("fechainicio").toLocalDate(),
                               rs.getDate("fechafin").toLocalDate(),
                               rs.getDouble("precio"),
                               rs.getInt("plazas"),
                               rs.getString("usuario_dni"),
                               rs.getString("matricula_embarcacion"),
                               rs.getString("acompanantes_dni")
                       );
                   };
                });

                return result;
            } else return null;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find alquileres");
            exception.printStackTrace();
            return null;
        }
    }

    public Alquiler findAlquilerById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findAlquilerById");
            Alquiler result = jdbcTemplate.query(query, this::mapRowToAlquiler, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find alquiler with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    private Alquiler mapRowToAlquiler(ResultSet row) {
        try {

            if(row.first()) {
                int id = row.getInt("id");
                Date fechainicio = row.getDate("fechainicio");
                Date fechafin = row.getDate("fechafin");
                double precio = row.getDouble("precio");
                int plazas = row.getInt("plazas");
                String usuario_dni = row.getString("usuario_dni");
                String matricula_embarcacion = row.getString("matricula_embarcacion");
                String acompanantes_dni = row.getString("acompanantes_dni");

                Alquiler alquiler = new Alquiler(id, fechainicio.toLocalDate(), fechafin.toLocalDate(), precio, plazas, usuario_dni, matricula_embarcacion, acompanantes_dni);
                return alquiler;

            } else {
                return null;
            }

        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addAlquiler(Alquiler alquiler) {
        try {
            String query = sqlQueries.getProperty("insert-addAlquiler");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                   
                   alquiler.getId(),
                   alquiler.getFechainicio(),
                   alquiler.getFechafin(),
                   alquiler.getPrecio(),
                   alquiler.getPlazas(),
                   alquiler.getUsuario_dni(),
                   alquiler.getMatricula_embarcacion(),
                   alquiler.getAcompanantes_dni()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert alquileres in the database");
        }

        return false;
    }

}
