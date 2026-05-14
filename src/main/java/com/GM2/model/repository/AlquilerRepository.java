package com.GM2.model.repository;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.AlquilerRentalDetails;
import com.GM2.model.domain.Acompanante;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con los alquileres.
 * Proporciona métodos para buscar, agregar y gestionar alquileres de embarcaciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class AlquilerRepository extends AbstractRepository {

    private final AcompananteRepository acompanantesRepository;

    /**
     * Constructor del repositorio de alquileres.
     * 
     * Refactoring 6.1: Se delega la inicialización del JdbcTemplate y SQL properties
     * al constructor de {@link AbstractRepository}.
     *
     * @param jdbcTemplate Template JDBC para operaciones de base de datos
     * @param acompanantesRepository Repositorio de acompañantes para cargar relaciones
     */
    public AlquilerRepository(JdbcTemplate jdbcTemplate, AcompananteRepository acompanantesRepository) {
        super(jdbcTemplate);
        this.acompanantesRepository = acompanantesRepository;
    }

    /**
     * Obtiene todos los alquileres registrados en el sistema.
     * 
     * Refactoring 7.3: Extraído el RowMapper anónimo como lambda.
     *
     * @return Lista de todos los objetos {@link Alquiler} registrados o null si ocurre un error.
     */
    public List<Alquiler> findAllAlquileres() {
        try {
            String query = getSqlQuery("select-findAllAlquileres");
            if (query == null) return null;

            return getJdbcTemplate().query(query, (rs, rowNum) -> {
                int idAlquiler = rs.getInt("id");
                List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(idAlquiler);
                AlquilerRentalDetails rentalDetails = new AlquilerRentalDetails(
                        rs.getDate("fechainicio").toLocalDate(),
                        rs.getDate("fechafin").toLocalDate(),
                        rs.getDouble("precio"),
                        rs.getInt("plazas"),
                        rs.getString("usuario_dni"),
                        rs.getString("matricula_embarcacion")
                );
                return new Alquiler(idAlquiler, rentalDetails, acompanantes);
            });
        } catch (DataAccessException exception) {
            System.err.println("Unable to find alquileres");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un alquiler por su ID.
     * 
     * @param id ID del alquiler a buscar
     * @return El objeto {@link Alquiler} encontrado o null si no existe.
     */
    public Alquiler findAlquilerById(int id) {
        try {
            String query = getSqlQuery("select-findAlquilerById");
            return getJdbcTemplate().query(query, this::mapRowToAlquiler, id);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find alquiler with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * ResultSetExtractor para mapear la primera fila del ResultSet a un Alquiler.
     */
    private Alquiler mapRowToAlquiler(ResultSet row) {
        try {
            if (row.first()) {
                int id = row.getInt("id");
                AlquilerRentalDetails rentalDetails = new AlquilerRentalDetails(
                        row.getDate("fechainicio").toLocalDate(),
                        row.getDate("fechafin").toLocalDate(),
                        row.getDouble("precio"),
                        row.getInt("plazas"),
                        row.getString("usuario_dni"),
                        row.getString("matricula_embarcacion")
                );
                List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
                return new Alquiler(id, rentalDetails, acompanantes);
            }
            return null;
        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Agrega un nuevo alquiler a la base de datos.
     * 
     * Refactoring 4.4: Simplificado {@code if (result <= 0) return false; return true;}
     * a {@code return result > 0;} donde es posible.
     *
     * @param alquiler Objeto {@link Alquiler} a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addAlquiler(Alquiler alquiler) {
        try {
            String query = getSqlQuery("insert-addAlquiler");
            String lastIdQuery = getSqlQuery("select-lastInsertedAlquilerId");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    Date.valueOf(alquiler.getStartDate()),
                    Date.valueOf(alquiler.getEndDate()),
                    alquiler.getPrice(),
                    alquiler.getSeats(),
                    alquiler.getUserNationalId(),
                    alquiler.getBoatRegistration()
            );

            if (result <= 0) return false;

            Integer idAlquiler = getJdbcTemplate().queryForObject(lastIdQuery, Integer.class);
            alquiler.setId(idAlquiler);
            return true;
        } catch (DataAccessException exception) {
            System.err.println("Unable to insert alquileres in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un alquiler en la base de datos.
     * 
     * @param alquiler Objeto {@link Alquiler} a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean updateAlquiler(Alquiler alquiler) {
        try {
            String query = getSqlQuery("update-updateAlquiler");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    Date.valueOf(alquiler.getStartDate()),
                    Date.valueOf(alquiler.getEndDate()),
                    alquiler.getPrice(),
                    alquiler.getSeats(),
                    alquiler.getUserNationalId(),
                    alquiler.getBoatRegistration(),
                    alquiler.getId()
            );
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to update alquileres in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un alquiler y sus acompañantes de la base de datos.
     * 
     * @param id ID del alquiler a eliminar
     * @return true si se eliminó correctamente, false en caso contrario     
     */
    public boolean deleteAlquiler(int id) {
        try {
            String deleteAcom = getSqlQuery("delete-deleteAcompanantesByAlquiler");
            getJdbcTemplate().update(deleteAcom, id);

            String query = getSqlQuery("delete-deteleAlquiler");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query, id);
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to delete alquileres in the database");
            exception.printStackTrace();
            return false;
        }
    }
}
