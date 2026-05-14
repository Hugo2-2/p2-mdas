package com.GM2.model.repository;

import com.GM2.model.domain.Acompanante;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con los acompañantes.
 * Proporciona métodos para buscar, agregar y gestionar acompañantes de alquileres.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class AcompananteRepository extends AbstractRepository {

    /**
     * Constructor para la inyección de dependencias de JdbcTemplate.
     *
     * Refactoring 3.4: Eliminado campo duplicado {@code private JdbcTemplate jdbcTemplate}
     * que sombreaba al heredado de {@link AbstractRepository}.
     *
     * Refactoring 6.1: Se delega al constructor de {@link AbstractRepository}
     * la inicialización del JdbcTemplate y las propiedades SQL.
     *
     * @param jdbcTemplate El bean de JdbcTemplate gestionado por Spring.
     */
    public AcompananteRepository(JdbcTemplate jdbcTemplate) { 
        super(jdbcTemplate);
    }

    /**
     * Obtiene todos los acompañantes registrados en el sistema.
     * 
     * @return Lista de todos los {@link Acompanante} registrados o null si ocurre un error.
     */
    public List<Acompanante> findAllAcompanantes() {
        try {
            String query = getSqlQuery("select-findAllAcompanantes");
            if (query == null) return null;

            return getJdbcTemplate().query(query, (rs, rowNum) -> new Acompanante(
                    rs.getInt("id"),
                    rs.getString("dni"),
                    rs.getInt("id_alquiler")
            ));
        } catch (DataAccessException exception) {
            System.err.println("Unable to find acompanantes");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un acompañante por su DNI.
     * 
     * @param dni DNI del acompañante a buscar
     * @return El {@link Acompanante} encontrado o null si no existe.
     */
    public Acompanante findAcompananteByDni(String dni) {
        try {
            String query = getSqlQuery("select-findAcompananteByDni");
            return getJdbcTemplate().query(query, this::mapRowToAcompanante, dni);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find acompanante with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca todos los acompañantes de un alquiler.
     * 
     * @param idAlquiler ID del alquiler al que pertenecen.
     * @return Lista de {@link Acompanante} o null si ocurre un error.
     */
    public List<Acompanante> findAcompananteByAlquiler(int idAlquiler) {
        try {
            String query = getSqlQuery("select-findAcompanantesByAlquiler");
            return getJdbcTemplate().query(query, this::mapRowFromAlquiler, idAlquiler);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find acompanantes with id_alquiler: " + idAlquiler);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * RowMapper para mapear una fila del ResultSet a un objeto Acompanante.
     */
    private Acompanante mapRowFromAlquiler(ResultSet row, int rowNum) throws SQLException {
        return new Acompanante(
                row.getInt("id"),
                row.getString("dni"),
                row.getInt("id_alquiler")
        );
    }
    
    /**
     * ResultSetExtractor para mapear la primera fila del ResultSet a un Acompanante.
     */
    private Acompanante mapRowToAcompanante(ResultSet row) {
        try {
            if (row.first()) {
                return new Acompanante(
                        row.getInt("id"),
                        row.getString("dni"),
                        row.getInt("id_alquiler")
                );
            }
            return null;
        } catch (SQLException exception) {
            System.err.println("Unable to map row to Acompanantes object");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Agrega un nuevo acompañante a la base de datos.
     * 
     * @param acompanante Objeto Acompañante a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addAcompanante(Acompanante acompanante) {
        try {
            String query = getSqlQuery("insert-addAcompanante");
            int rowsAffected = getJdbcTemplate().update(query, acompanante.getNationalId(), acompanante.getRentalId());
            return rowsAffected > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to add acompanante");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un acompanante de la base de datos.
     * 
     * @param idAlquiler ID del alquiler al que pertenece
     * @param dniSocio DNI del socio a desvincular
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean deleteAcompanante(int idAlquiler, String dniSocio) {
        try {
            String query = getSqlQuery("delete-deleteAcompanante");
            int rowsAffected = getJdbcTemplate().update(query, idAlquiler, dniSocio);
            return rowsAffected > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to delete acompanante");
            exception.printStackTrace();
            return false;
        }
    }
}
