package com.GM2.model.repository;

import com.GM2.exception.DatabaseException;
import com.GM2.exception.EntityNotFoundException;
import com.GM2.exception.ErrorCode;
import com.GM2.exception.ValidationException;
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
            if (query != null ) {
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
            if (result != null )
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

            if (row.first()) {
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

    // Clean Code - Regla 4 Funciones (argumentos): Se ha eliminado el parámetro de salida '[errores]' y ahora la función devuelve directamente el resultado esperado.
    public String addSocio(Socio socio) {
        if (socio == null) return "No se ha ingresado el socio";
        
        if (socio.getBirthDate().getYear() > 2007 && socio.getIsTitular() != null && socio.getIsTitular()) {
            return "Debes de ser mayor de edad para realizar esta inscripcion";
        }

        boolean sqlRes = ejecutarInsertSocio(socio);

        if (socio.getIsTitular() != null && socio.getIsTitular()) {
            Inscripcion inscripcion = new Inscripcion(socio.getNationalId());
            boolean resInscripcion = inscripcionRepository.addInscripcion(inscripcion);
            if (!(sqlRes && resInscripcion)) {
                return "No se ha podido guardar el socio titular";
            }
        } else if (!sqlRes) {
            return "No se ha podido guardar el socio";
        }

        return "EXITO";
    }

    // Clean Code - Regla 3 Funciones (argumentos): Se ha eliminado el parámetro bandera (flag) 'isTitular' dividiendo el comportamiento en métodos específicos.
    public void addSocioTitular(Socio socio) {
        if (socio == null)
            throw new ValidationException(ErrorCode.SOCIO_NO_INGRESADO);

        if (socio.getBirthDate().getYear() > 2007)
            throw new ValidationException(ErrorCode.SOCIO_DEBE_SER_MAYOR_DE_EDAD);

        boolean sqlRes = ejecutarInsertSocio(socio);

        // Creamos su inscripcion simple que posteriormente podrá ser ampliada
        Inscripcion inscripcion = new Inscripcion(socio.getNationalId());
        boolean resInscripcion = inscripcionRepository.addInscripcion(inscripcion);
        if (!(sqlRes && resInscripcion))
            throw new DatabaseException(ErrorCode.SOCIO_NO_GUARDADO);
    }

    // Clean Code - Regla 3 Funciones (argumentos): Se ha eliminado el parámetro bandera (flag) 'isTitular' dividiendo el comportamiento en métodos específicos.
    public void addSocioNoTitular(Socio socio) {
        if (socio == null)
            throw new ValidationException(ErrorCode.SOCIO_NO_INGRESADO);

        boolean sqlRes = ejecutarInsertSocio(socio);

        if (!sqlRes)
            throw new DatabaseException(ErrorCode.SOCIO_NO_GUARDADO);
    }

    private boolean ejecutarInsertSocio(Socio socio) {
        try {
            String query = sqlQueries.getProperty("insert-addSocio");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                   socio.getName(),
                   socio.getSurname(),
                   socio.getNationalId(),
                   socio.getBirthDate(),
                   socio.getAddress(),
                   socio.getRegistrationDate(),
                   socio.getIsTitular(),
                   socio.getHasSkipperLicense()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException exception) {
            System.err.println("Unable to insert socios in the database");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza la información de un socio existente en la base de datos.
     * No permite actualizar el DNI (se usa como clave primaria).
     * Verifica que el socio exista antes de realizar la actualización.
     *
     * @param socio El objeto {@link Socio} con los datos actualizados.
     * @throws ValidationException si los datos del socio no son válidos.
     * @throws EntityNotFoundException si el socio no existe.
     * @throws DatabaseException si la operación de persistencia falla.
     */
    public void updateSocio(Socio socio) {
        if (socio == null)
            throw new ValidationException(ErrorCode.SOCIO_NO_INGRESADO);

        if (socio.getNationalId() == null || socio.getNationalId().isEmpty())
            throw new ValidationException(ErrorCode.SOCIO_DNI_OBLIGATORIO);

        // Verificar que el socio existe
        Socio socioExistente = findSocioByDNI(socio.getNationalId());
        if (socioExistente == null)
            throw new EntityNotFoundException(ErrorCode.SOCIO_NO_EXISTE);

        try {
            String query = sqlQueries.getProperty("update-socio");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                    socio.getName(),
                    socio.getSurname(),
                    socio.getBirthDate(),
                    socio.getAddress(),
                    socio.getRegistrationDate(),
                    socio.getIsTitular(),
                    socio.getHasSkipperLicense(),
                    socio.getNationalId()
                );
                if (result <= 0)
                    throw new DatabaseException(ErrorCode.SOCIO_NO_ACTUALIZADO);
            } else {
                throw new DatabaseException(ErrorCode.SOCIO_NO_ACTUALIZADO);
            }
        } catch (DataAccessException exception) {
            System.err.println("Unable to update socio in the database");
            exception.printStackTrace();
            throw new DatabaseException(ErrorCode.SOCIO_NO_ACTUALIZADO);
        }
    }

    /**
     * Elimina un socio de la base de datos por su DNI.
     * IMPORTANTE: Solo debe usarse si el socio no está vinculado a ninguna inscripción.
     *
     * @param dni El DNI del socio a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean deleteSocio(String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }

        // Verificar que el socio existe
        Socio socio = findSocioByDNI(dni);
        if (socio == null) {
            return false;
        }

        try {
            String query = sqlQueries.getProperty("delete-deleteSocio");
            if (query != null) {
                int result = jdbcTemplate.update(query, dni);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException exception) {
            System.err.println("Unable to delete socio from the database");
            exception.printStackTrace();
            return false;
        }
    }
}

