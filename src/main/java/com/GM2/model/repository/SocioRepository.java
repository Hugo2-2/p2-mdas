package com.GM2.model.repository;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD (Crear, Leer, Actualizar, Borrar)
 * de la entidad {@link Socio} en la base de datos.
 * También maneja la creación de inscripciones automáticas para socios titulares.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class SocioRepository extends AbstractRepository {

    InscripcionRepository inscripcionRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Configura JdbcTemplate y el repositorio de inscripciones usando @Lazy
     * para evitar dependencias circulares.
     *
     * @param jdbcTemplate El bean de JdbcTemplate gestionado por Spring.
     * @param inscripcionRepository Repositorio de inscripciones (cargado de forma lazy).
     */
    public SocioRepository(JdbcTemplate jdbcTemplate, @Lazy InscripcionRepository inscripcionRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.inscripcionRepository = inscripcionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Recupera una lista de todos los socios de la base de datos.
     *
     * @return Una lista de {@link Socio}, o null si no se encuentran resultados o hay error.
     */
    public List<Socio> findAllSocios() {
        try {
            String query = sqlQueries.getProperty("select-findAllSocios");
            if( query != null ) {
                List<Socio> result = jdbcTemplate.query(query, new RowMapper<Socio>() {
                   public Socio mapRow(ResultSet rs, int rowNum) throws SQLException {
                       return new Socio(
                               rs.getBoolean("titulo_patron"),
                               rs.getBoolean("es_titular"),
                               //Date.valueOf(rs.getString("fecha_inscripcion")).toLocalDate(),
                               rs.getDate("fecha_inscripcion").toLocalDate(),
                               rs.getString("direccion"),
                               //Date.valueOf(rs.getString("fecha_nacimiento")).toLocalDate(),
                               rs.getDate("fecha_nacimiento").toLocalDate(),
                               rs.getString("dni"),
                               rs.getString("apellidos"),
                               rs.getString("nombre")
                       );
                   };
                });

                return result;
            } else return null;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find socios");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un socio específico por su DNI (clave primaria).
     *
     * @param dni El DNI único del socio a buscar.
     * @return El objeto {@link Socio} si se encuentra, o null si no existe.
     */
    public Socio findSocioByDNI(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findSocioByDNI");
            Socio result = jdbcTemplate.query(query, this::mapRowToSocio, dni);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find socio with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Extrae y mapea la primera fila de un ResultSet a un objeto Socio.
     * Este método funciona como un ResultSetExtractor que solo procesa un resultado.
     * Mueve el cursor a la primera fila; si no hay filas, devuelve null.
     *
     * @param row El conjunto de resultados (ResultSet) completo devuelto por la consulta JDBC.
     * @return Un objeto {@link Socio} si se encuentra una fila,
     *         o null si el ResultSet está vacío o si ocurre una SQLException.
     */
    private Socio mapRowToSocio(ResultSet row) {
        try {

            if(row.first()) {
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                String dni = row.getString("dni");
                Date fechaNacimiento = row.getDate("fecha_nacimiento");
                String direccion = row.getString("direccion");
                Date fechaInscripcion = row.getDate("fecha_inscripcion");
                Boolean esTitular = row.getBoolean("es_titular");
                Boolean tituloPatron = row.getBoolean("titulo_patron");

                Socio socio = new Socio(tituloPatron, esTitular, fechaInscripcion.toLocalDate(),
                        direccion, fechaNacimiento.toLocalDate(), dni, apellidos, nombre);
                return socio;
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
     * Inserta un nuevo socio en la base de datos.
     * Realiza validaciones de negocio (edad mínima para titularidad) y
     * crea automáticamente una inscripción si el socio es titular.
     *
     * @param socio El objeto {@link Socio} a insertar.
     * @return "EXITO" si la inserción fue exitosa, mensaje de error en caso contrario.
     */
    public String addSocio(Socio socio) {

        boolean sqlRes;

        if( socio == null ) return "No se ha ingresado el socio";

        if( socio.getFechaNacimiento().getYear() > 2007 && socio.getEsTitular() )
            return "Debes de ser mayor de edad para realizar esta inscripcion";

        try {
            String query = sqlQueries.getProperty("insert-addSocio");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                   socio.getNombre(),
                   socio.getApellidos(),
                   socio.getDni(),
                   socio.getFechaNacimiento(),
                   socio.getDireccion(),
                   socio.getFechaInscripcion(),
                   socio.getEsTitular(),
                   socio.getTieneLicenciaPatron()
                );

                if (result > 0)
                    sqlRes = true;
                else sqlRes = false;

            } else sqlRes = false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert socios in the database");
            exception.printStackTrace();
            sqlRes = false;
        }

        // Creamos su inscripcion simple que posteriormente podrá se ampliada
        if(socio.getEsTitular() && socio.getEsTitular() != null) {

            Inscripcion inscripcion = new Inscripcion(socio.getDni());

            boolean resInscripcion = inscripcionRepository.addInscripcion(inscripcion);

            if( sqlRes & resInscripcion ) {
                return "EXITO";
            } else {
                return "No se ha podido guardar el socio";
            }
        }


        if( sqlRes ) {
            return "EXITO";
        } else {
            return "No se ha podido guardar el socio";
        }
    }

    /**
     * Actualiza la información de un socio existente en la base de datos.
     * No permite actualizar el DNI (se usa como clave primaria).
     * Verifica que el socio exista antes de realizar la actualización.
     *
     * @param socio El objeto {@link Socio} con los datos actualizados.
     * @return "EXITO" si la actualización fue exitosa, mensaje de error en caso contrario.
     */
    public String updateSocio(Socio socio) {
        if(socio == null) {
            return "No se ha ingresado el socio";
        }

        if(socio.getDni() == null || socio.getDni().isEmpty()) {
            return "El DNI es obligatorio para actualizar el socio";
        }

        // Verificar que el socio existe
        Socio socioExistente = findSocioByDNI(socio.getDni());
        if(socioExistente == null) {
            return "No se puede actualizar, el socio no existe";
        }

        try {
            String query = sqlQueries.getProperty("update-socio");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                    socio.getNombre(),
                    socio.getApellidos(),
                    socio.getFechaNacimiento(),
                    socio.getDireccion(),
                    socio.getFechaInscripcion(),
                    socio.getEsTitular(),
                    socio.getTieneLicenciaPatron(),
                    socio.getDni()
                );

                if(result > 0) {
                    return "EXITO";
                } else {
                    return "No se ha podido actualizar el socio";
                }
            } else {
                return "No se ha podido actualizar el socio";
            }
        } catch (DataAccessException exception) {
            System.err.println("Unable to update socio in the database");
            exception.printStackTrace();
            return "No se ha podido actualizar el socio";
        }
    }
}

