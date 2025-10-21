package com.GM2.model.repository;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
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

    PatronRepository patronRepository;

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

    public List<Embarcacion> findAllEmbarcacionesByTipo(String tipo) {
        try {
            String query = sqlQueries.getProperty("select-findAllEmbarcacionesByTipo");

            if (query != null) {
                List<Embarcacion> result = jdbcTemplate.query(
                        query, new RowMapper<Embarcacion>() {
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
                        },
                        tipo
                );
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
                        embarcacion.getMatricula(),
                        embarcacion.getNombre(),
                        embarcacion.getTipo(),
                        embarcacion.getPlazas(),
                        embarcacion.getDimensiones(),
                        embarcacion.getIdPatron()
                );

                if(result > 0)
                        return true;
                else
                    return false;
            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert embarcacion in the database");
            exception.printStackTrace();
        }

        return false;
    }

    //Funcion para comprobar si la embarcacion tiene un patron asignado
    public boolean isPatronAssignedToEmbarcacion(String patronDni) {
        try {
            // Esta consulta cuenta cuántas embarcaciones tienen este DNI de patrón.
            String query = sqlQueries.getProperty("select-isPatronAssignedToEmbarcacion");

            // Usamos queryForObject porque esperamos un único valor (un número).
            Integer count = jdbcTemplate.queryForObject(query, Integer.class, patronDni);

            // Si el contador es null (improbable pero posible) o 0, no está asignado.
            // Si es mayor que 0, sí lo está.
            return count != null && count > 0;

        } catch (DataAccessException e) {
            System.err.println("Error al comprobar la asignación del patrón: " + patronDni);
            e.printStackTrace();
            return false;
        }
    }

    //Funcion para obtener el patron de la embarcacion
    public String getPatronAssignedToEmbarcacion(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-getPatronAssignedToEmbarcacion");
            //Guardamos el dni del patron asignado a la embarcacion
            String patronAsignadoDni =  jdbcTemplate.queryForObject(query, String.class, matricula);
            return patronAsignadoDni;
        } catch (DataAccessException e) {
            System.err.println("No hay ningún patron asignado a la embarcación actual");
            e.printStackTrace();
            return null;
        }
    }

    //Funcion para cambiar el patron de la embarcacion
    public boolean updatePatron(String patronDni,  String matricula) {
        String query = sqlQueries.getProperty("update-updatePatron");
        if(query != null) {
            jdbcTemplate.update(query, patronDni, matricula);
            return true;
        }
        return false;
    }
}
