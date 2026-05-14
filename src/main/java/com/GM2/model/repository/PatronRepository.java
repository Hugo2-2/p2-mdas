package com.GM2.model.repository;

import com.GM2.model.domain.Patron;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos relacionadas con los patrones.
 *
 * Refactoring 6.1: Se usa super(jdbcTemplate) del AbstractRepository.
 * Refactoring 7.3: Extraído RowMapper como lambda reutilizable.
 * Refactoring 4.4: Simplificados patrones if/else return.
 */
@Repository
public class PatronRepository extends AbstractRepository {

    /**
     * RowMapper reutilizable para convertir una fila del ResultSet a un Patron.
     */
    private final org.springframework.jdbc.core.RowMapper<Patron> patronRowMapper = (rs, rowNum) -> new Patron(
            rs.getString("nombre"),
            rs.getString("apellidos"),
            rs.getString("dni"),
            rs.getDate("fecha_nacimiento").toLocalDate(),
            rs.getDate("fecha_expedicion_titulo").toLocalDate()
    );

    public PatronRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * Obtiene todos los patrones registrados en la base de datos.
     */
    public List<Patron> findAllPatrones() {
        try {
            String query = getSqlQuery("select-findAllPatrones");
            return getPatrons(query);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find patrones");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todos los patrones que no están asignados a ninguna embarcación.
     */
    public List<Patron> findAllFreePatrones() {
        try {
            String query = getSqlQuery("select-findAllFreePatrones");
            return getPatrons(query);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find patrones");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Ejecuta una consulta SQL para obtener una lista de patrones.
     */
    private List<Patron> getPatrons(String query) {
        if (query == null) return null;
        return getJdbcTemplate().query(query, patronRowMapper);
    }

    /**
     * Busca un patrón por su DNI.
     */
    public Patron findPatronByDNI(String dni) {
        try {
            String query = getSqlQuery("select-findPatronByDNI");
            return getJdbcTemplate().query(query, this::mapRowToPatron, dni);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find patron with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Comprueba si un patrón ya está registrado en la base de datos.
     */
    public boolean isRegistered(String dni) {
        try {
            String query = getSqlQuery("select-countPatronByDNI");
            Integer count = getJdbcTemplate().queryForObject(query, Integer.class, dni);
            return count != null && count > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find patron with dni: " + dni);
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * ResultSetExtractor que mapea la primera fila a un Patron.
     */
    private Patron mapRowToPatron(ResultSet row) {
        try {
            if (row.first()) {
                return new Patron(
                        row.getString("nombre"),
                        row.getString("apellidos"),
                        row.getString("dni"),
                        row.getDate("fecha_nacimiento").toLocalDate(),
                        row.getDate("fecha_expedicion_titulo").toLocalDate()
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
     * Inserta un nuevo patrón en la base de datos.
     */
    public boolean addPatron(Patron patron) {
        try {
            String query = getSqlQuery("insert-addPatron");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    patron.getName(),
                    patron.getSurname(),
                    patron.getNationalId(),
                    patron.getBirthDate(),
                    patron.getTitleIssueDate()
            );
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to insert patron in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza los datos personales de un patrón (no el DNI).
     */
    public boolean updatePatron(Patron patron) {
        try {
            String query = getSqlQuery("update-updatePatronInfo");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    patron.getName(),
                    patron.getSurname(),
                    patron.getBirthDate(),
                    patron.getTitleIssueDate(),
                    patron.getNationalId()
            );
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Error al actualizar el patrón: " + patron.getNationalId());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un patrón de la base de datos.
     */
    public boolean deletePatron(String dni) {
        try {
            String query = getSqlQuery("delete-deletePatron");
            if (query == null) return false;

            int rows = getJdbcTemplate().update(query, dni);
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar el patrón: " + dni);
            e.printStackTrace();
            return false;
        }
    }
}
