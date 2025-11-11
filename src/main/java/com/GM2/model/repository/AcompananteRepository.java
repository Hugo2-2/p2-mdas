package com.GM2.model.repository;

import com.GM2.model.domain.Acompanante;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con los acompañantes.
 * Proporciona métodos para buscar, agregar y gestionar acompañantes de alquileres.
 * Representa a la clase {@link Acompanante} para el manejo de datos.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class AcompananteRepository extends AbstractRepository {
    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor para la inyección de dependencias de JdbcTemplate.
     * @param jdbcTemplate El bean de JdbcTemplate gestionado por Spring.
     */
    public AcompananteRepository(JdbcTemplate jdbcTemplate) { 
        this.jdbcTemplate = jdbcTemplate; 
    }

    /**
     * Obtiene todos los acompañantes registrados en el sistema.
     * 
     * @return Lista de todos los {@link Acompanante} registrados o null si ocurre un error.
     */
    public List<Acompanante> findAllAcompanantes() {
        try {
            String query = sqlQueries.getProperty("select-findAllAcompanantes");
            if(query != null){
                List<Acompanante> result = jdbcTemplate.query(query, new RowMapper<Acompanante>() {
                    public Acompanante mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Acompanante(
                                rs.getInt("id"),
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


    /**
     * Busca un acompañante por su DNI.
     * 
     * @param dni DNI del acompañante a buscar
     * @return El {@link Acompanante} encontrado o null si no existe.
     */
    public Acompanante findAcompananteByDni(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findAcompananteByDni");
            Acompanante result = jdbcTemplate.query(query, this::mapRowToAcompanante, dni);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find acompanante with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca todos los acompañantes de un alquiler.
     * 
     * @param id_alquiler ID del alquiler al que pertenecen.
     * @return Lista de {@link Acompanante} o null si ocurre un error.
     */
    public List<Acompanante> findAcompananteByAlquiler(int id_alquiler) {
        try {
            String query = sqlQueries.getProperty("select-findAcompanantesByAlquiler");
            List<Acompanante> result = jdbcTemplate.query(query, this::mapRowFromAlquiler, id_alquiler);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find acompanantes with id_alquiler: " + id_alquiler);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Extrae y mapea la *primera* fila de un ResultSet a un objeto Acompanante.
     * Este método funciona como un ResultSetExtractor que solo procesa un resultado.
     * Mueve el cursor a la primera fila; si no hay filas, devuelve null.
     * 
     * @param row El conjunto de resultados (ResultSet) completo devuelto por la consulta JDBC.
     * @param rowNum Número de fila.
     * @return Un objeto {@link Acompanante} si se encuentra una fila,
     * o null si el ResultSet está vacío o si ocurre una SQLException.
     */
    private Acompanante mapRowFromAlquiler(ResultSet row, int rowNum) throws SQLException {
                int id = row.getInt("id");
                String dni = row.getString("dni");
                int id_alquiler = row.getInt("id_alquiler");

                return new Acompanante(id, dni, id_alquiler);
            
    }
    
    /**
     * Extrae y mapea la fila de un ResultSet a un objeto Acompanante.
     * Este método funciona como un ResultSetExtractor que solo procesa un resultado.
     * Mueve el cursor a la fila; si no hay filas, devuelve null.
     *  
     * @param row El conjunto de resultados (ResultSet) completo devuelto por la consulta JDBC.
     * @return Un objeto {@link Acompanante} si se encuentra una fila,
     * o null si el ResultSet está vacío o si ocurre una SQLException.  
     */
    private Acompanante mapRowToAcompanante(ResultSet row) {
        try {

            if(row.first()) {
                int id = row.getInt("id");
                String dni = row.getString("dni");
                int id_alquiler = row.getInt("id_alquiler");

                Acompanante acompanante = new Acompanante(id, dni, id_alquiler);
                return acompanante;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            System.err.println("Unable to map row to Acompanantes object");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Agrega un nuevo acompañante a la base de datos.
     * 
     * @param acompañante Objeto Acompañante a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addAcompanante(Acompanante acompanante) {
        try {
            String query = sqlQueries.getProperty("insert-addAcompanante");
            int rowsAffected = jdbcTemplate.update(query, acompanante.getDni(), acompanante.getId_alquiler());
            return rowsAffected > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to add acompanante");
            exception.printStackTrace();
            return false;
        }
    }
    
}
