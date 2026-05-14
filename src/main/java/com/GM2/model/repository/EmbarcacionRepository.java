package com.GM2.model.repository;

import com.GM2.model.domain.Embarcacion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD de la entidad {@link Embarcacion}.
 *
 * Refactoring 7.3: Extraído RowMapper duplicado como lambda reutilizable.
 * Refactoring 4.4: Simplificados patrones if/else return.
 * Refactoring 6.1: Se usa super(jdbcTemplate) del AbstractRepository.
 *
 * @author gm2equipo1
 * @version 2.0
 */
@Repository
public class EmbarcacionRepository extends AbstractRepository {

    /**
     * RowMapper reutilizable para convertir una fila del ResultSet a un Embarcacion.
     * Refactoring 7.3: Extraído de los RowMapper anónimos duplicados en findAllEmbarcaciones() y findAllEmbarcacionesByTipo().
     */
    private final org.springframework.jdbc.core.RowMapper<Embarcacion> embarcacionRowMapper = (rs, rowNum) -> new Embarcacion(
            rs.getString("id_patron"),
            rs.getString("dimensiones"),
            rs.getInt("plazas"),
            rs.getString("tipo"),
            rs.getString("nombre"),
            rs.getString("matricula")
    );

    public EmbarcacionRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * Recupera una lista de todas las embarcaciones de la base de datos.
     */
    public List<Embarcacion> findAllEmbarcaciones() {
        try {
            String query = getSqlQuery("select-findAllEmbarcaciones");
            if (query == null) return null;

            return getJdbcTemplate().query(query, embarcacionRowMapper);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find embarcaciones.");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca una embarcación específica por su matrícula (clave primaria).
     */
    public Embarcacion findEmbarcacionByMatricula(String matricula) {
        try {
            String query = getSqlQuery("select-findEmbarcacionByMatricula");
            return getJdbcTemplate().query(query, this::mapRowToEmbarcacion, matricula);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find embarcacion with matricula: " + matricula);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca una embarcación específica por su nombre (que debe ser único).
     */
    public Embarcacion findEmbarcacionByNombre(String nombre) {
        try {
            String query = getSqlQuery("select-findEmbarcacionByNombre");
            return getJdbcTemplate().query(query, this::mapRowToEmbarcacion, nombre);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find embarcacion with nombre: " + nombre);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Recupera una lista de embarcaciones que coinciden con un tipo.
     */
    public List<Embarcacion> findAllEmbarcacionesByTipo(String tipo) {
        try {
            String query = getSqlQuery("select-findAllEmbarcacionesByTipo");
            if (query == null) return null;

            return getJdbcTemplate().query(query, embarcacionRowMapper, tipo);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find embarcaciones.");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * ResultSetExtractor que mapea la primera fila a un Embarcacion.
     */
    private Embarcacion mapRowToEmbarcacion(ResultSet row) {
        try {
            if (row.first()) {
                return new Embarcacion(
                        row.getString("id_patron"),
                        row.getString("dimensiones"),
                        row.getInt("plazas"),
                        row.getString("tipo"),
                        row.getString("nombre"),
                        row.getString("matricula")
                );
            }
            return null;
        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Inserta una nueva embarcación en la base de datos.
     */
    public boolean addEmbarcacion(Embarcacion embarcacion) {
        try {
            String query = getSqlQuery("insert-addEmbarcacion");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    embarcacion.getRegistration(),
                    embarcacion.getName(),
                    embarcacion.getType(),
                    embarcacion.getSeats(),
                    embarcacion.getDimensions(),
                    embarcacion.getSkipperId()
            );
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to insert embarcacion in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Comprueba si un patrón ya está asignado a cualquier embarcación.
     */
    public boolean isPatronAssignedToEmbarcacion(String patronDni) {
        try {
            String query = getSqlQuery("select-isPatronAssignedToEmbarcacion");
            Integer count = getJdbcTemplate().queryForObject(query, Integer.class, patronDni);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            System.err.println("Error al comprobar la asignación del patrón: " + patronDni);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene el DNI del patrón actualmente asignado a una embarcación.
     */
    public String getPatronAssignedToEmbarcacion(String matricula) {
        try {
            String query = getSqlQuery("select-getPatronAssignedToEmbarcacion");
            return getJdbcTemplate().queryForObject(query, String.class, matricula);
        } catch (DataAccessException e) {
            System.err.println("No hay ningún patron asignado a la embarcación actual");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Asigna o actualiza el patrón de una embarcación.
     */
    public boolean updatePatron(String patronDni, String matricula) {
        String query = getSqlQuery("update-updatePatron");
        if (query == null) return false;

        getJdbcTemplate().update(query, patronDni, matricula);
        return true;
    }

    /**
     * Actualiza los datos modificables de una embarcación existente.
     */
    public boolean updateEmbarcacion(Embarcacion embarcacion) {
        try {
            String query = getSqlQuery("update-updateEmbarcacion");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    embarcacion.getName(),
                    embarcacion.getType(),
                    embarcacion.getSeats(),
                    embarcacion.getDimensions(),
                    embarcacion.getRegistration()
            );
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Error al actualizar la embarcación: " + embarcacion.getRegistration());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si una embarcación tiene historial de alquileres.
     */
    public boolean isEmbarcacionAlquilada(String matricula) {
        try {
            String query = getSqlQuery("select-ocupadoAlquileresByMatricula");
            Integer count = getJdbcTemplate().queryForObject(query, Integer.class, matricula);
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si una embarcación tiene historial de reservas.
     */
    public boolean isEmbarcacionReservada(String matricula) {
        try {
            String query = getSqlQuery("select-ocupadaReservasByMatricula");
            Integer count = getJdbcTemplate().queryForObject(query, Integer.class, matricula);
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una embarcación de la base de datos.
     */
    public boolean deleteEmbarcacion(String matricula) {
        try {
            String query = getSqlQuery("delete-deleteEmbarcacion");
            if (query == null) return false;

            int rows = getJdbcTemplate().update(query, matricula);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
