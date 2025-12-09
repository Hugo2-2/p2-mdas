package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD (Crear, Leer, Actualizar, Borrar)
 * de la entidad {@link Hijos} en la base de datos.
 * Maneja la información de los menores asociados a las inscripciones familiares
 * del club náutico, permitiendo su registro y consulta.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class HijosRepository extends AbstractRepository {
    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor para la inyección de dependencias de JdbcTemplate.
     * 
     * @param jdbcTemplate El bean de JdbcTemplate gestionado por Spring.
     */
    public HijosRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Hijos> findAllHijos() {
        try {
            String query = sqlQueries.getProperty("select-findAllHijos");
            if(query != null){
                List<Hijos> result = jdbcTemplate.query(query, new RowMapper<Hijos>() {
                    public Hijos mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Hijos(
                                rs.getString("dni"),
                                rs.getString("nombre"),
                                rs.getString("apellidos"),
                                rs.getDate("fecha_nacimiento").toLocalDate(),
                                rs.getInt("id_inscripcion")
                        );
                    };
                });

                return result;
            } else return null;

        } catch (DataAccessException exception) {
            System.err.println("Unable to find hijos");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un hijo específico por su DNI (clave primaria).
     *
     * @param dni El DNI único del hijo a buscar.
     * @return El objeto {@link Hijos} si se encuentra, o null si no existe.
     */
    public Hijos findHijoByDni(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findHijoByDNI");
            Hijos result = jdbcTemplate.query(query, this::mapRowToHijos, dni);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find hijo with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Extrae y mapea la primera fila de un ResultSet a un objeto Hijos.
     * Este método funciona como un ResultSetExtractor que solo procesa un resultado.
     * Mueve el cursor a la primera fila; si no hay filas, devuelve null.
     *
     * @param row El conjunto de resultados (ResultSet) completo devuelto por la consulta JDBC.
     * @return Un objeto {@link Hijos} si se encuentra una fila,
     *         o null si el ResultSet está vacío o si ocurre una SQLException.
     */
    private Hijos mapRowToHijos(ResultSet row) {
        try {

            if(row.first()) {
                String dni = row.getString("dni");
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                LocalDate fechaNacimiento = row.getDate("fecha_nacimiento").toLocalDate();
                int id_inscripcion = row.getInt("id_inscripcion");

                Hijos hijo = new Hijos(dni, nombre, apellidos, fechaNacimiento, id_inscripcion);
                return hijo;
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
     * Busca todos los hijos asociados a una inscripción específica.
     *
     * @param inscripcion El ID de la inscripción cuyos hijos se desean obtener.
     * @return Una lista de {@link Hijos} asociados a la inscripción, o null si no existen o hay error.
     */
    public List<Hijos> findHijosByInscripcion(int inscripcion) {
        try {
            String query = sqlQueries.getProperty("select-findHijosByInscripcion");
            List<Hijos> hijos = jdbcTemplate.query(query, this::mapRowFromInscripcion, inscripcion);

            if( hijos != null )
                return hijos;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to retrieve hijos from the database");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Mapea una fila específica de un ResultSet a un objeto Hijos.
     * Este método se utiliza como RowMapper para consultas que devuelven múltiples resultados.
     *
     * @param row El ResultSet con los datos de la consulta.
     * @param rowNum El número de fila actual (base 0) que se está procesando.
     * @return Un objeto {@link Hijos} con los datos de la fila, o null si hay error.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
    private Hijos mapRowFromInscripcion(ResultSet row, int rowNum) throws SQLException {
        String dni = row.getString("dni");
        String nombre = row.getString("nombre");
        String apellidos = row.getString("apellidos");
        LocalDate fechaNacimiento = row.getDate("fecha_nacimiento").toLocalDate();
        int id_inscripcion = row.getInt("id_inscripcion");

        Hijos hijo = new Hijos(dni, nombre, apellidos, fechaNacimiento, id_inscripcion);
        return hijo;

    }

    /**
     * Inserta un nuevo hijo en la base de datos.
     *
     * @param hijo El objeto {@link Hijos} a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean addHijo(Hijos hijo) {
         try {
            String query = sqlQueries.getProperty("insert-addHijo");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        hijo.getDni(),
                        hijo.getNombre(),
                        hijo.getApellidos(),
                        hijo.getFechaNacimiento(),
                        hijo.getId_inscripcion() == 0 ? null : hijo.getId_inscripcion()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

         } catch (DataAccessException exception) {
            System.err.println("Unable to insert hijo in the database");
            exception.printStackTrace();
            return false;
         }
    }

    /**
     * Inserta múltiples hijos en la base de datos de forma secuencial.
     * Si falla la inserción de cualquier hijo, el proceso se detiene y retorna false.
     * 
     * NOTA: Este método tiene un error de implementación - solo usa DNI e ID de inscripción
     * en lugar de todos los campos del objeto Hijos.
     *
     * @param hijos Lista de objetos {@link Hijos} a insertar.
     * @return true si todas las inserciones fueron exitosas, false si alguna falla.
     */
    public boolean addHijos(List<Hijos> hijos) {
        for(Hijos hijo : hijos) {
            try {
                String query = sqlQueries.getProperty("insert-addHijo");
                if(query != null){
                    int result = jdbcTemplate.update(query,
                            hijo.getDni(),
                            hijo.getId_inscripcion()
                    );

                    if (result <= 0)
                        return false;
                } else return false;
            } catch (DataAccessException exception) {
                System.err.println("Unable to insert hijo in the database");
                exception.printStackTrace();
                return false;
            }
        }

        return true;
    }

    /**
     * Elimina un hijo de la base de datos por su DNI.
     *
     * @param Hijo El objeto hijo a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */

    public boolean updateHijo(Hijos hijo) {

        if( hijo == null ) return false;

        if( findHijoByDni(hijo.getDni()) == null ) return false;

        try {
            String query = sqlQueries.getProperty("update-Hijo");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        hijo.getNombre(),
                        hijo.getApellidos(),
                        hijo.getFechaNacimiento(),
                        hijo.getId_inscripcion(),
                        hijo.getId_inscripcion()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to update hijo in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un hijo de la base de datos por su DNI.
     *
     * @param dni El DNI del hijo a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean deleteHijo(String dni) {
        if(dni == null || dni.isEmpty()) {
            return false;
        }

        // Verificar que el hijo existe
        Hijos hijo = findHijoByDni(dni);
        if(hijo == null) {
            return false;
        }

        try {
            String query = sqlQueries.getProperty("delete-deleteHijo");
            if(query != null) {
                int result = jdbcTemplate.update(query, dni);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to delete hijo from the database");
            ex.printStackTrace();
            return false;
        }
    }
}
