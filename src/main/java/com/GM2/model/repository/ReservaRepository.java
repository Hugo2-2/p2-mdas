package com.GM2.model.repository;

import com.GM2.model.domain.Reserva;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReservaRepository extends AbstractRepository{

    public ReservaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        // Configurar las rutas de los .properties si es necesario
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.setSqlQueriesFileName(sqlQueriesFileName);
    }

    public List<Reserva> findAllReservas() {
        try {
            String query = sqlQueries.getProperty("select-findAllReservas");
            if( query != null ) {
                List<Reserva> result = jdbcTemplate.query(query, new RowMapper<Reserva>() {
                   public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                       return new Reserva(
                               rs.getInt("id"),
                               rs.getDate("fecha").toLocalDate(),
                               rs.getInt("plazas"),
                               rs.getDouble("precio"),
                               rs.getString("usuario_id"),
                               rs.getString("matricula_embarcacion"),
                               rs.getString("descripcion")

                       );
                   };
                });

                return result;
            } else return null;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find reservas");
            exception.printStackTrace();
            return null;
        }
    }

    public Reserva findReservaById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findReservaById");
            Reserva result = jdbcTemplate.query(query, this::mapRowToReserva, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find reserva with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    private Reserva mapRowToReserva(ResultSet row) {
        try {

            if(row.first()) {
                int id = row.getInt("id");
                Date fecha = row.getDate("fecha");
                int plazas = row.getInt("plazas");
                double precio = row.getDouble("precio");
                String usuario_id = row.getString("usuario_id");
                String matricula_embarcacion = row.getString("matricula_embarcacion");
                String descripcion = row.getString("descripcion");

                Reserva reserva = new Reserva(id, fecha.toLocalDate(), plazas, precio, usuario_id, matricula_embarcacion, descripcion);
                return reserva;
            } else {
                return null;
            }

        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addReserva(Reserva reserva) {
        try {
            String query = sqlQueries.getProperty("insert-addReserva");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                   reserva.getFecha(),
                   reserva.getPlazas(),
                   reserva.getPrecio(),
                   reserva.getUsuario_id(),
                   reserva.getMatricula_embarcacion(),
                   reserva.getDescripcion()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert reservas in the database");
            exception.printStackTrace();
        }

        return false; 
    }

}
