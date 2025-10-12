package com.GM2.model.repository;

import com.GM2.model.domain.Embarcacion;
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
public class EmbarcacionRepository extends AbstractRepository {

    public EmbarcacionRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Embarcacion> findAllEmbarcaciones() {
        try {
            String query = sqlQueries.getProperty("select-findAllEmbarcaciones");

            if (query != null) {
                List<Embarcacion> result = jdbcTemplate.query(query, new RowMapper<Embarcacion>() {
                    @Override
                    public Embarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Embarcacion(
                                rs.getString("id_patron"),
                                rs.getString("dimensiones"),
                                rs.getInt("plazas"),
                                rs.getString("tipo"),
                                rs.getString("nombre"),
                                rs.getString("matricula")
                        );
                    }
                });

                return result;
            } else {
                return null;
            }

        } catch (DataAccessException exception) {
            System.err.println("Unable to find embarcaciones.");
            exception.printStackTrace();
            return null;
        }
    }

    public Embarcacion findEmbarcacionByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionByMatricula");
            Embarcacion result = jdbcTemplate.query(query, this::mapRowToEmbarcacion, matricula);
            if(result != null)
                return result;
            else
                return null;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find embarcacion with matricula: " + matricula);
            exception.printStackTrace();
            return null;
        }
    }

    private Embarcacion mapRowToEmbarcacion(ResultSet row) {
        try {

            if(row.first()) {
                String id_patron = row.getString("id_patron");
                String dimensiones = row.getString("dimensiones");
                int plazas = row.getInt("plazas");
                String tipo = row.getString("tipo");
                String nombre = row.getString("nombre");
                String matricula = row.getString("matricula");

                Embarcacion embarcacion = new Embarcacion(id_patron, dimensiones, plazas, tipo, nombre, matricula);
                return embarcacion;
            } else {
                return null;
            }

        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addEmbarcacion(Embarcacion embarcacion) {
        try {
            String query = sqlQueries.getProperty("insert-addEmbarcacion");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        embarcacion.getIdPatron(),
                        embarcacion.getDimensiones(),
                        embarcacion.getPlazas(),
                        embarcacion.getTipo(),
                        embarcacion.getNombre(),
                        embarcacion.getMatricula()
                );

                if(result > 0)
                        return true;
                else
                    return false;
            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert reservas in the database");
        }

        return false;
    }
}
