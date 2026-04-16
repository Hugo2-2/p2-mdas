package com.GM2.model.repository;

import com.GM2.model.domain.Embarcacion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD (Crear, Leer, Actualizar, Borrar)
 * de la entidad {@link Embarcacion} en la base de datos.
 *
 * @author gm2equipo1
 * @version 2.0 (Refactorizado para eliminar clases de servicio)
 */
@Repository
public class EmbarcacionRepository extends AbstractRepository {

    /**
     * Constructor para la inyección de dependencias de JdbcTemplate.
     * @param jdbcTemplate El bean de JdbcTemplate gestionado por Spring.
     */
    public EmbarcacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Recupera una lista de todas las embarcaciones de la base de datos.
     *
     * @return Una lista de {@link Embarcacion}.
     */
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

    /**
     * Busca una embarcación específica por su matrícula (clave primaria).
     *
     * @param matricula La matrícula única de la embarcación.
     * @return El objeto {@link Embarcacion} si se encuentra, o null si no existe.
     */
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

    /**
     * Busca una embarcación específica por su nombre (que debe ser único).
     *
     * @param nombre El nombre único de la embarcación.
     * @return El objeto {@link Embarcacion} si se encuentra, o null si no existe.
     */
    public Embarcacion findEmbarcacionByNombre(String nombre) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionByNombre");
            Embarcacion result = jdbcTemplate.query(query, this::mapRowToEmbarcacion, nombre);
            if(result != null)
                return result;
            else
                return null;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find embarcacion with nombre: " + nombre);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Recupera una lista de todas las embarcaciones que coinciden con un tipo.
     *
     * @param tipo El tipo de embarcación a buscar (ej. "VELERO").
     * @return Una lista de {@link Embarcacion}.
     */
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

    /**
     * Extrae y mapea la *primera* fila de un ResultSet a un objeto Embarcacion.
     * Este método funciona como un ResultSetExtractor que solo procesa un resultado.
     * Mueve el cursor a la primera fila; si no hay filas, devuelve null.
     *
     * @param row El conjunto de resultados (ResultSet) completo devuelto por la consulta JDBC.
     * @return Un objeto {@link Embarcacion} si se encuentra una fila,
     * o null si el ResultSet está vacío o si ocurre una SQLException.
     */
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

    /**
     * Inserta una nueva embarcación en la base de datos.
     *
     * @param embarcacion El objeto {@link Embarcacion} a insertar.
     * @return true si la inserción fue exitosa (1 fila afectada), false en caso contrario.
     */
    public boolean addEmbarcacion(Embarcacion embarcacion) {
        try {
            String query = sqlQueries.getProperty("insert-addEmbarcacion");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        embarcacion.getRegistration(),
                        embarcacion.getName(),
                        embarcacion.getType(),
                        embarcacion.getSeats(),
                        embarcacion.getDimensions(),
                        embarcacion.getSkipperId()
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

    /**
     * Comprueba si un patrón ya está asignado a cualquier embarcación.
     *
     * @param patronDni El DNI del patrón a verificar.
     * @return true si el patrón ya tiene una embarcación, false en caso contrario.
     */
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

    /**
     * Obtiene el DNI del patrón actualmente asignado a una embarcación.
     *
     * @param matricula La matrícula de la embarcación a consultar.
     * @return El DNI del patrón como un String, o null si no hay ningún patrón asignado.
     */
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

    /**
     * Asigna o actualiza el patrón de una embarcación.
     *
     * @param patronDni El DNI del nuevo patrón (o null para desasignar).
     * @param matricula La matrícula de la embarcación a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean updatePatron(String patronDni,  String matricula) {
        String query = sqlQueries.getProperty("update-updatePatron");
        if(query != null) {
            jdbcTemplate.update(query, patronDni, matricula);
            return true;
        }
        return false;
    }

    /**
     * Actualiza los datos modificables de una embarcación existente en la base de datos.
     *
     * Los campos que se actualizan son: nombre, tipo, plazas y dimensiones.
     * La matrícula se utiliza como criterio de búsqueda (WHERE) y no se modifica.
     *
     * @param embarcacion Objeto {@link Embarcacion} con los datos actualizados.
     * @return true si la operación tuvo éxito (se modificó 1 fila), false en caso contrario.
     */
    public boolean updateEmbarcacion(Embarcacion embarcacion) {
        try {
            String query = sqlQueries.getProperty("update-updateEmbarcacion");

            if (query != null) {
                int result = jdbcTemplate.update(query,
                        embarcacion.getName(),
                        embarcacion.getType(),
                        embarcacion.getSeats(),
                        embarcacion.getDimensions(),
                        embarcacion.getRegistration()
                );

                return result > 0;
            }
            return false;

        } catch (DataAccessException e) {
            System.err.println("Error al actualizar la embarcación: " + embarcacion.getRegistration());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si una embarcación tiene historial de alquileres.
     * Utilizado para validar la integridad referencial antes de un borrado.
     *
     * @param matricula Identificador de la embarcación a consultar.
     * @return true si existe al menos un registro en la tabla de alquileres, false si está libre.
     */
    public boolean isEmbarcacionAlquilada(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-ocupadoAlquileresByMatricula");

            Integer count = jdbcTemplate.queryForObject(query, Integer.class, matricula);

            // Si devuelve null o 0, no está alquilada
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si una embarcación tiene historial de reservas.
     * Utilizado para validar la integridad referencial antes de un borrado.
     *
     * @param matricula Identificador de la embarcación a consultar.
     * @return true si existe al menos un registro en la tabla de reservas, false si está libre.
     */
    public boolean isEmbarcacionReservada(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-ocupadaReservasByMatricula");

            Integer count = jdbcTemplate.queryForObject(query, Integer.class, matricula);

            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una embarcación de la base de datos.
     *
     * Nota: Esta operación debe realizarse solo si la embarcación no tiene
     * dependencias (alquileres o reservas), lo cual debe validarse previamente en el servicio/controlador.
     *
     * @param matricula La matrícula de la embarcación a eliminar.
     * @return true si se eliminó el registro correctamente, false en caso contrario.
     */
    public boolean deleteEmbarcacion(String matricula) {
        try {
            String query = sqlQueries.getProperty("delete-deleteEmbarcacion");
            if (query != null) {
                int rows = jdbcTemplate.update(query, matricula);
                return rows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
