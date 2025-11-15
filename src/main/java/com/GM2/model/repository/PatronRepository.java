package com.GM2.model.repository;

import com.GM2.model.domain.Patron;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos relacionadas con los patrones.
 * Se encarga de consultar, insertar y validar la existencia de patrones.
 * Utiliza JdbcTemplate para ejecutar las consultas SQL definidas en un archivo de propiedades.
 */
@Repository
public class PatronRepository extends AbstractRepository {

    /**
     * Constructor que recibe la instancia de JdbcTemplate.
     * @param jdbcTemplate Instancia para ejecutar consultas SQL en la base de datos.
     */
    public PatronRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    /**
     * Obtiene todos los patrones registrados en la base de datos.
     * @return Lista de patrones o null si ocurre un error.
     */
    public List<Patron> findAllPatrones() {
        try {
            String query = sqlQueries.getProperty("select-findAllPatrones");
            return getPatrons(query);

        } catch (DataAccessException exception) {
            System.err.println("Unable to find patrones");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todos los patrones que actualmente no están asignados a ninguna embarcación.
     * @return Lista de patrones libres o null si ocurre un error.
     */
    public List<Patron> findAllFreePatrones() {
        try {
            String query = sqlQueries.getProperty("select-findAllFreePatrones");
            return getPatrons(query);

        } catch (DataAccessException exception) {
            System.err.println("Unable to find patrones");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Ejecuta una consulta SQL para obtener una lista de patrones.
     * @param query Consulta SQL que retorna patrones
     * @return Lista de objetos Patron o null si la consulta es nula
     */
    private List<Patron> getPatrons(String query) {
        if(query != null){
            List<Patron> result = jdbcTemplate.query(query, new RowMapper<Patron>() {
                public Patron mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Patron(
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("dni"),
                            rs.getDate("fecha_nacimiento").toLocalDate(),
                            rs.getDate("fecha_expedicion_titulo").toLocalDate()
                    );
                };
            });

            return result;
        } else return null;
    }

    /**
     * Busca un patrón por su DNI.
     * @param dni DNI del patrón
     * @return Patron encontrado o null si no existe o hay error
     */
    public Patron findPatronByDNI(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findPatronByDNI");
            Patron result = jdbcTemplate.query(query, this::mapRowToPatron, dni);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find patron with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Comprueba si un patrón ya está registrado en la base de datos.
     * @param dni DNI del patrón
     * @return true si el patrón existe, false en caso contrario o error
     */
    public boolean isRegistered(String dni) {
        try {
            String query = sqlQueries.getProperty("select-countPatronByDNI");
            Integer count = jdbcTemplate.queryForObject(query, Integer.class, dni);
            if( count != null && count > 0 )
                return true;
            else return false;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find patron with dni: " + dni);
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Convierte el primer resultado de un ResultSet en un objeto Patron.
     * @param row ResultSet de la consulta
     * @return Patron o null si no hay resultados
     */
    private Patron mapRowToPatron(ResultSet row) {
        try {

            if(row.first()) {
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                String dni = row.getString("dni");
                Date fechaNacimiento = row.getDate("fecha_nacimiento");
                Date fechaExpedicionTitulo = row.getDate("fecha_expedicion_titulo");

                Patron patron = new Patron(nombre, apellidos, dni,
                                    fechaNacimiento.toLocalDate(), fechaExpedicionTitulo.toLocalDate());
                return patron;
            } else {
                return null;
            }

        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Inserta un nuevo patrón en la base de datos.
     * @param patron Objeto Patron a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean addPatron(Patron patron) {
        try {
            String query = sqlQueries.getProperty("insert-addPatron");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        patron.getNombre(),
                        patron.getApellidos(),
                        patron.getDni(),
                        patron.getFechaNacimiento(),
                        patron.getFechaExpedicionTitulo()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert patron in the database");
            exception.printStackTrace();
        }

        return false;
    }

}
