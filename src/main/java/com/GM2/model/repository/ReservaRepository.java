package com.GM2.model.repository;

import com.GM2.model.domain.Reserva; // Importa la clase de dominio 'Reserva'

import org.springframework.dao.DataAccessException; // Para manejar excepciones de acceso a datos de Spring
import org.springframework.jdbc.core.JdbcTemplate; // Herramienta principal de Spring para trabajar con JDBC
import org.springframework.jdbc.core.RowMapper; // Interfaz para mapear filas del ResultSet a objetos
import org.springframework.stereotype.Repository; // Marca la clase como un componente de repositorio (capa de persistencia)
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date; // Clase para manejar fechas SQL
import java.sql.ResultSet; // Objeto que contiene los resultados de una consulta a la base de datos
import java.sql.SQLException; // Para manejar excepciones de SQL
import java.util.List; // Para manejar colecciones de objetos
import java.sql.PreparedStatement;
import java.sql.Statement;

// Clase Repositorio para la entidad Reserva, extiende de una clase base AbstractRepository
@Repository
public class ReservaRepository extends AbstractRepository{

    // Constructor que inyecta JdbcTemplate
    public ReservaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        // Configura la ruta del archivo .properties que contiene las consultas SQL
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Recupera una lista de todas las reservas de la base de datos.
     *
     * @return Una lista de objetos Reserva, o null si ocurre un error.
     */
    public List<Reserva> findAllReservas() {
        try {
            // Obtiene la consulta SQL del archivo de propiedades
            String query = sqlQueries.getProperty("select-findAllReservas");
            if( query != null ) {
                // Ejecuta la consulta y mapea cada fila del ResultSet a un objeto Reserva
                List<Reserva> result = jdbcTemplate.query(query, new RowMapper<Reserva>() {
                    // Implementación del mapeo de fila
                    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Reserva(
                                rs.getInt("id"),
                                // Convierte java.sql.Date a java.time.LocalDate
                                rs.getDate("fecha").toLocalDate(),
                                rs.getInt("plazas"),
                                rs.getDouble("precio"),
                                rs.getString("usuario_id"),
                                rs.getString("matricula_embarcacion"),
                                rs.getString("descripcion")
                        );
                    };
                });

                return result;
            } else return null; // Si no se encuentra la consulta, retorna null
        } catch (DataAccessException exception) {
            // Manejo de errores de acceso a datos
            System.err.println("Unable to find reservas");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca una reserva por su ID.
     *
     * @param id El ID de la reserva a buscar.
     * @return El objeto Reserva si se encuentra, o null si ocurre un error o no se encuentra.
     */
    public Reserva findReservaById(int id) {
        try {
            // Obtiene la consulta SQL del archivo de propiedades
            String query = sqlQueries.getProperty("select-findReservaById");
            // Ejecuta la consulta, usando el método 'mapRowToReserva' para mapear y pasando el ID como parámetro
            // Nota: Este uso de jdbcTemplate.query para un solo resultado puede ser incorrecto si la consulta
            // devuelve una lista; se recomienda usar `queryForObject` o revisar la implementación de `mapRowToReserva`.
            Reserva result = jdbcTemplate.query(query, this::mapRowToReserva, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            // Manejo de errores de acceso a datos
            System.err.println("Unable to find reserva with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Método auxiliar privado para mapear una fila (ResultSet) a un objeto Reserva.
     * Nota: Este método asume que el ResultSet solo contiene UNA fila y usa `row.first()`,
     * lo cual no es el patrón habitual de RowMapper. Está diseñado para ser usado
     * con una consulta que devuelve un solo registro (como por ID).
     *
     * @param row El ResultSet que contiene los datos de la reserva.
     * @return El objeto Reserva mapeado, o null si no hay filas o si ocurre un error SQL.
     */
    private Reserva mapRowToReserva(ResultSet row) {
        try {
            // Intenta posicionar el cursor en la primera fila (solo si se espera un único resultado)
            if(row.first()) {
                // Extrae los valores de las columnas del ResultSet
                int id = row.getInt("id");
                Date fecha = row.getDate("fecha");
                int plazas = row.getInt("plazas");
                double precio = row.getDouble("precio");
                String usuario_id = row.getString("usuario_id");
                String matricula_embarcacion = row.getString("matricula_embarcacion");
                String descripcion = row.getString("descripcion");

                // Crea y retorna el objeto Reserva
                Reserva reserva = new Reserva(id, fecha.toLocalDate(), plazas, precio, usuario_id, matricula_embarcacion, descripcion);
                return reserva;
            } else {
                return null; // Retorna null si no hay filas
            }

        } catch (SQLException exception) {
            // Manejo de errores de SQL
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Inserta una nueva reserva en la base de datos.
     *
     * @param reserva El objeto Reserva con los datos a insertar.
     * @return true si la inserción fue exitosa (se afectó 1 o más filas), false en caso contrario.
     */
    public boolean addReserva(Reserva reserva) {
        try {
            String query = sqlQueries.getProperty("insert-addReserva");
            if(query != null) {
                // 1. Preparamos una "bolsa" (KeyHolder) para recoger la llave que nos dará la BBDD
                KeyHolder keyHolder = new GeneratedKeyHolder();

                // 2. Ejecutamos la actualización de una forma especial para recuperar claves
                int result = jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    // Asignamos los datos (respetando el orden de los ? en tu SQL)
                    ps.setDate(1, Date.valueOf(reserva.getFecha()));
                    ps.setInt(2, reserva.getPlazas());
                    ps.setDouble(3, reserva.getPrecio());
                    ps.setString(4, reserva.getUsuario_id());
                    ps.setString(5, reserva.getMatricula_embarcacion());
                    ps.setString(6, reserva.getDescripcion());
                    return ps;
                }, keyHolder);

                // 3. ¡AQUÍ ESTÁ LA CLAVE!
                // Si la BBDD nos devolvió una llave, actualizamos nuestro objeto Java
                if (result > 0 && keyHolder.getKey() != null) {
                    reserva.setId(keyHolder.getKey().intValue()); // <--- Ahora Java ya sabe que es la 3, la 4, etc.
                    return true;
                }
            }
            return false;
        } catch (DataAccessException exception) {
            System.err.println("Error insertando reserva");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza los datos de una reserva existente.
     * Corresponde a las operaciones PATCH/PUT.
     *
     * @param reserva El objeto reserva con los datos actualizados.
     * @return true si se actualizó correctamente.
     */
    public boolean updateReserva(Reserva reserva) {
        try {
            // Buscamos la query en el archivo sql.properties
            String query = sqlQueries.getProperty("update-reserva");

            if (query != null) {
                // Ejecutamos el update.
                // IMPORTANTE: El orden de estos parámetros debe coincidir con los "?" de tu SQL
                int result = jdbcTemplate.update(query,
                        reserva.getFecha(),                  // 1. Nueva fecha
                        reserva.getPlazas(),                 // 2. Nuevas plazas
                        reserva.getPrecio(),                 // 3. Nuevo precio (si cambiaron plazas)
                        reserva.getDescripcion(),            // 4. Nueva descripción
                        // El ID va al final porque está en la cláusula WHERE
                        reserva.getId()
                );

                return result > 0; // Devuelve true si se modificó alguna fila
            } else {
                return false;
            }
        } catch (DataAccessException exception) {
            System.err.println("Error actualizando la reserva: " + exception.getMessage());
            return false;
        }
    }

    /**
     * Elimina una reserva por su ID.
     * Corresponde a la operación DELETE.
     *
     * @param id El ID de la reserva a eliminar.
     * @return true si se eliminó correctamente.
     */
    public boolean deleteReserva(int id) {
        try {
            String query = sqlQueries.getProperty("delete-reserva");

            if (query != null) {
                int result = jdbcTemplate.update(query, id);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException exception) {
            System.err.println("Error eliminando la reserva con id: " + id);
            return false;
        }
    }
}