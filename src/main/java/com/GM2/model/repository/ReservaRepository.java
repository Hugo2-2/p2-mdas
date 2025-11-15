package com.GM2.model.repository;

import com.GM2.model.domain.Reserva; // Importa la clase de dominio 'Reserva'

import org.springframework.dao.DataAccessException; // Para manejar excepciones de acceso a datos de Spring
import org.springframework.jdbc.core.JdbcTemplate; // Herramienta principal de Spring para trabajar con JDBC
import org.springframework.jdbc.core.RowMapper; // Interfaz para mapear filas del ResultSet a objetos
import org.springframework.stereotype.Repository; // Marca la clase como un componente de repositorio (capa de persistencia)

import java.sql.Date; // Clase para manejar fechas SQL
import java.sql.ResultSet; // Objeto que contiene los resultados de una consulta a la base de datos
import java.sql.SQLException; // Para manejar excepciones de SQL
import java.util.List; // Para manejar colecciones de objetos

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
            // Obtiene la consulta SQL de inserción
            String query = sqlQueries.getProperty("insert-addReserva");
            if(query != null) {
                // Ejecuta la actualización (INSERT) pasando los atributos de la reserva como parámetros
                int result = jdbcTemplate.update(query,
                        reserva.getFecha(), // Nota: Asume que getFecha() devuelve un tipo que JdbcTemplate puede manejar (ej. LocalDate, que debe ser convertido a Date/Timestamp en la consulta si la BD lo requiere)
                        reserva.getPlazas(),
                        reserva.getPrecio(),
                        reserva.getUsuario_id(),
                        reserva.getMatricula_embarcacion(),
                        reserva.getDescripcion()
                );

                // Comprueba si se insertó al menos una fila
                if (result > 0)
                    return true;
                else return false;

            } else return false; // Si no se encuentra la consulta, retorna false

        } catch (DataAccessException exception) {
            // Manejo de errores de acceso a datos durante la inserción
            System.err.println("Unable to insert reservas in the database");
            exception.printStackTrace();
        }

        return false; // Retorna false por defecto si hay una excepción
    }

}