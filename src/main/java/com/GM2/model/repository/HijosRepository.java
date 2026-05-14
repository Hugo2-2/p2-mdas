package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD de la entidad {@link Hijos}.
 * Maneja la información de los menores asociados a las inscripciones familiares.
 *
 * Refactoring 3.4: Eliminado campo {@code private JdbcTemplate jdbcTemplate} duplicado.
 * Refactoring 6.1: Se usa super(jdbcTemplate) del AbstractRepository.
 * Refactoring 7.3: Extraído RowMapper como lambda reutilizable.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class HijosRepository extends AbstractRepository {

    /**
     * RowMapper reutilizable para convertir una fila del ResultSet a un Hijos.
     */
    private final org.springframework.jdbc.core.RowMapper<Hijos> hijosRowMapper = (rs, rowNum) -> new Hijos(
            rs.getString("dni"),
            rs.getString("nombre"),
            rs.getString("apellidos"),
            rs.getDate("fecha_nacimiento").toLocalDate(),
            rs.getObject("id_inscripcion") != null ? rs.getInt("id_inscripcion") : 0
    );

    public HijosRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * Obtiene todos los hijos registrados en el sistema.
     */
    public List<Hijos> findAllHijos() {
        try {
            String query = getSqlQuery("select-findAllHijos");
            if (query == null) return null;

            return getJdbcTemplate().query(query, hijosRowMapper);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find hijos");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un hijo específico por su DNI.
     */
    public Hijos findHijoByDni(String dni) {
        try {
            String query = getSqlQuery("select-findHijoByDNI");
            return getJdbcTemplate().query(query, this::mapRowToHijos, dni);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find hijo with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * ResultSetExtractor que mapea la primera fila a un Hijos.
     */
    private Hijos mapRowToHijos(ResultSet row) {
        try {
            if (row.first()) {
                return new Hijos(
                        row.getString("dni"),
                        row.getString("nombre"),
                        row.getString("apellidos"),
                        row.getDate("fecha_nacimiento").toLocalDate(),
                        row.getObject("id_inscripcion") != null ? row.getInt("id_inscripcion") : 0
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
     * Busca todos los hijos asociados a una inscripción específica.
     */
    public List<Hijos> findHijosByInscripcion(int inscripcion) {
        try {
            String query = getSqlQuery("select-findHijosByInscripcion");
            return getJdbcTemplate().query(query, hijosRowMapper, inscripcion);
        } catch (DataAccessException exception) {
            System.err.println("Unable to retrieve hijos from the database");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Inserta un nuevo hijo en la base de datos.
     */
    public boolean addHijo(Hijos hijo) {
        try {
            String query = getSqlQuery("insert-addHijo");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    hijo.getDni(),
                    hijo.getName(),
                    hijo.getSurname(),
                    hijo.getBirthDate(),
                    hijo.getRegistrationId() == 0 ? null : hijo.getRegistrationId()
            );
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to insert hijo in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Inserta múltiples hijos en la base de datos de forma secuencial.
     *
     * Bug fix: El método original solo pasaba DNI e ID de inscripción.
     * Ahora pasa todos los campos del hijo (nombre, apellidos, fecha de nacimiento).
     */
    public boolean addHijos(List<Hijos> hijos) {
        for (Hijos hijo : hijos) {
            if (!addHijo(hijo)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Actualiza los datos de un hijo existente.
     */
    public boolean updateHijo(Hijos hijo) {
        if (hijo == null) return false;
        if (findHijoByDni(hijo.getDni()) == null) return false;

        try {
            String query = getSqlQuery("update-hijo");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    hijo.getName(),
                    hijo.getSurname(),
                    hijo.getBirthDate(),
                    hijo.getRegistrationId() == 0 ? null : hijo.getRegistrationId(),
                    hijo.getDni()
            );
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to update hijo in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un hijo de la base de datos por su DNI.
     */
    public boolean deleteHijo(String dni) {
        if (dni == null || dni.isEmpty()) return false;
        if (findHijoByDni(dni) == null) return false;

        try {
            String query = getSqlQuery("delete-deleteHijo");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query, dni);
            return result > 0;
        } catch (DataAccessException ex) {
            System.err.println("Unable to delete hijo from the database");
            ex.printStackTrace();
            return false;
        }
    }
}
