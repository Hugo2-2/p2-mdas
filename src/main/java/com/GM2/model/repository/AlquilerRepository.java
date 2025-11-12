package com.GM2.model.repository;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Acompanante;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con los alquileres.
 * Proporciona métodos para buscar, agregar y gestionar alquileres de embarcaciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class AlquilerRepository extends AbstractRepository{

    private AcompananteRepository acompanantesRepository;
    

    /**
     * Constructor del repositorio de alquileres.
     * 
     * @param jdbcTemplate Template JDBC para operaciones de base de datos
     * @param acompanantesRepository Repositorio de acompañantes para cargar relaciones
     */
    public AlquilerRepository(JdbcTemplate jdbcTemplate, AcompananteRepository acompanantesRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.acompanantesRepository = acompanantesRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.setSqlQueriesFileName(sqlQueriesFileName);
        this.acompanantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Obtiene todos los alquileres registrados en el sistema.
     * 
     * @return Lista de todos los objetos {@link Alquiler} registrados o null si ocurre un error.
     */
    public List<Alquiler> findAllAlquileres() {
        try {
            String query = sqlQueries.getProperty("select-findAllAlquileres");
            if( query != null ) {
                List<Alquiler> result = jdbcTemplate.query(query, new RowMapper<Alquiler>() {
                    public Alquiler mapRow(ResultSet rs, int rowNum) throws SQLException {
                 
                        int idAlquiler = rs.getInt("id");
                        List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(idAlquiler);
                        return new Alquiler(
                                idAlquiler,
                                rs.getDate("fechainicio").toLocalDate(),
                                rs.getDate("fechafin").toLocalDate(),
                                rs.getDouble("precio"),
                                rs.getInt("plazas"),
                                rs.getString("usuario_dni"),
                                rs.getString("matricula_embarcacion"),
                                acompanantes
                        );
                    };
                });

                return result;
            } else return null;
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
            String query = sqlQueries.getProperty("select-findAlquilerById");
            Alquiler result = jdbcTemplate.query(query, this::mapRowToAlquiler, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find alquiler with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Extrae y mapea la fila de un ResultSet a un objeto Alquiler.
     * Este método funciona como un ResultSetExtractor que solo procesa un resultado.
     * Mueve el cursor a la fila; si no hay filas, devuelve null.
     *  
     * @param row El conjunto de resultados (ResultSet) completo devuelto por la consulta JDBC.
     * @return Un objeto {@link Alquiler} si se encuentra una fila,
     * o null si el ResultSet está vacío o si ocurre una SQLException.  
     */
    private Alquiler mapRowToAlquiler(ResultSet row) {
        try {

            if(row.first()) {
                int id = row.getInt("id");
                Date fechainicio = row.getDate("fechainicio");
                Date fechafin = row.getDate("fechafin");
                double precio = row.getDouble("precio");
                int plazas = row.getInt("plazas");
                String usuario_dni = row.getString("usuario_dni");
                String matricula_embarcacion = row.getString("matricula_embarcacion");
                
                List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
                Alquiler alquiler = new Alquiler(id, fechainicio.toLocalDate(), fechafin.toLocalDate(), precio, plazas, usuario_dni, matricula_embarcacion, acompanantes);
                return alquiler;

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
     * Agrega un nuevo alquiler a la base de datos.
     * 
     * @param alquiler Objeto {@link Alquiler} a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addAlquiler(Alquiler alquiler) {
        try {
            String query = sqlQueries.getProperty("insert-addAlquiler");
            String lastIdQuery = sqlQueries.getProperty("select-lastInsertedAlquilerId");
            
            if(query != null) {

                int result = jdbcTemplate.update(query,
                   
                   Date.valueOf(alquiler.getFechainicio()),
                   Date.valueOf(alquiler.getFechafin()),
                   alquiler.getPrecio(),
                   alquiler.getPlazas(),
                   alquiler.getUsuario_dni(),
                   alquiler.getMatricula_embarcacion()
                   
                );

                if (result <= 0){
                    return false;
                }

                //Obtener el ID del último alquiler insertado (puedes ajustarlo según tu BBDD)
                Integer idAlquiler = jdbcTemplate.queryForObject(lastIdQuery, Integer.class);
                alquiler.setId(idAlquiler);

                return true;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert alquileres in the database");
            exception.printStackTrace();
        }

        return false;
    }


    /**
     * Obtiene todos los alquileres que tienen fecha de inicio en el futuro.
     * 
     * @return Lista de todos los objetos {@link Alquiler} futuros o null si ocurre un error.   
     */
    public List<Alquiler> listarAlquileresFuturos() {
        LocalDate hoy = LocalDate.now();
        List<Alquiler> alquileres = findAllAlquileres();
        List<Alquiler> futuros = new ArrayList<>();

        for (Alquiler a : alquileres) {

            if (!a.getFechainicio().isBefore(hoy)){ 
                futuros.add(a);
            }

        }

        return futuros;
    
    }
}
