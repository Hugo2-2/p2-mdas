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
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD de la entidad {@link Socio}.
 *
 * Refactoring 5.4: Replace Error Code with Exception — el método addSocio() ahora
 * usa las excepciones del sistema en lugar de retornar strings como códigos de error.
 *
 * Refactoring 6.1: Se usa super(jdbcTemplate) del AbstractRepository.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class SocioRepository extends AbstractRepository {

    private final InscripcionRepository inscripcionRepository;

    public SocioRepository(JdbcTemplate jdbcTemplate, @Lazy InscripcionRepository inscripcionRepository) {
        super(jdbcTemplate);
        this.inscripcionRepository = inscripcionRepository;
    }

    /**
     * Recupera una lista de todos los socios de la base de datos.
     */
    public List<Socio> findAllSocios() {
        try {
            String query = getSqlQuery("select-findAllSocios");
            if (query == null) return null;

            return getJdbcTemplate().query(query, (rs, rowNum) -> new Socio(
                    rs.getBoolean("titulo_patron"),
                    rs.getBoolean("es_titular"),
                    rs.getDate("fecha_inscripcion").toLocalDate(),
                    rs.getString("direccion"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("dni"),
                    rs.getString("apellidos"),
                    rs.getString("nombre")
            ));
        } catch (DataAccessException exception) {
            System.err.println("Unable to find socios");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un socio específico por su DNI.
     */
    public Socio findSocioByDNI(String dni) {
        try {
            String query = getSqlQuery("select-findSocioByDNI");
            return getJdbcTemplate().query(query, this::mapRowToSocio, dni);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find socio with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * ResultSetExtractor que mapea la primera fila a un Socio.
     */
    private Socio mapRowToSocio(ResultSet row) {
        try {
            if (row.first()) {
                return new Socio(
                        row.getBoolean("titulo_patron"),
                        row.getBoolean("es_titular"),
                        row.getDate("fecha_inscripcion").toLocalDate(),
                        row.getString("direccion"),
                        row.getDate("fecha_nacimiento").toLocalDate(),
                        row.getString("dni"),
                        row.getString("apellidos"),
                        row.getString("nombre")
                );
            }
            return null;
        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Registra un socio usando el flujo legacy (retorna strings como códigos de error).
     *
     * Refactoring 5.4: Replace Error Code with Exception — se reemplaza el retorno
     * de strings como "EXITO" / "No se ha podido guardar..." por excepciones tipadas.
     *
     * @deprecated Preferir {@link #addSocioTitular(Socio)} o {@link #addSocioNoTitular(Socio)}.
     */
    @Deprecated
    public String addSocio(Socio socio) {
        if (socio == null)
            throw new ValidationException(ErrorCode.SOCIO_NO_INGRESADO);

        //Extract to local variable: se extrae la variable isTitular para mejorar la legibilidad del código.
        Boolean isTitular = socio.getIsTitular();
        if (!socio.isOfLegalAge(java.time.LocalDate.now()) && isTitular != null && isTitular)
            throw new ValidationException(ErrorCode.SOCIO_DEBE_SER_MAYOR_DE_EDAD);

        boolean sqlRes = ejecutarInsertSocio(socio);

        if (isTitular != null && isTitular) {
            Inscripcion inscripcion = new Inscripcion(socio.getNationalId());
            boolean resInscripcion = inscripcionRepository.addInscripcion(inscripcion);
            if (!(sqlRes && resInscripcion))
                throw new DatabaseException(ErrorCode.SOCIO_NO_GUARDADO);
        } else if (!sqlRes) {
            throw new DatabaseException(ErrorCode.SOCIO_NO_GUARDADO);
        }

        return "EXITO";
    }

    /**
     * Registra un socio como titular y crea su inscripción asociada.
     */
    public void addSocioTitular(Socio socio) {
        if (socio == null)
            throw new ValidationException(ErrorCode.SOCIO_NO_INGRESADO);

        if (!socio.isOfLegalAge(java.time.LocalDate.now()))
            throw new ValidationException(ErrorCode.SOCIO_DEBE_SER_MAYOR_DE_EDAD);

        boolean sqlRes = ejecutarInsertSocio(socio);

        Inscripcion inscripcion = new Inscripcion(socio.getNationalId());
        boolean resInscripcion = inscripcionRepository.addInscripcion(inscripcion);
        if (!(sqlRes && resInscripcion))
            throw new DatabaseException(ErrorCode.SOCIO_NO_GUARDADO);
    }

    /**
     * Registra un socio sin titularidad.
     */
    public void addSocioNoTitular(Socio socio) {
        if (socio == null)
            throw new ValidationException(ErrorCode.SOCIO_NO_INGRESADO);

        boolean sqlRes = ejecutarInsertSocio(socio);

        if (!sqlRes)
            throw new DatabaseException(ErrorCode.SOCIO_NO_GUARDADO);
    }

    /**
     * Ejecuta la sentencia INSERT para un socio.
     */
    private boolean ejecutarInsertSocio(Socio socio) {
        try {
            String query = getSqlQuery("insert-addSocio");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    extracted(socio),
                    socio.getSurname(),
                    socio.getNationalId(),
                    socio.getBirthDate(),
                    socio.getAddress(),
                    socio.getRegistrationDate(),
                    socio.getIsTitular(),
                    socio.getHasSkipperLicense()
            );
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to insert socios in the database");
            exception.printStackTrace();
            return false;
        }
    }

    //Refactorizacion automatica, Extract Method: se extrae el método extracted() para obtener el nombre del socio.

    private String extracted(Socio socio) {
        return socio.getName();
    }

    /**
     * Actualiza la información de un socio existente.
     */
    public void updateSocio(Socio socio) {
        if (socio == null)
            throw new ValidationException(ErrorCode.SOCIO_NO_INGRESADO);

        if (socio.getNationalId() == null || socio.getNationalId().isEmpty())
            throw new ValidationException(ErrorCode.SOCIO_DNI_OBLIGATORIO);

        if (findSocioByDNI(socio.getNationalId()) == null)
            throw new EntityNotFoundException(ErrorCode.SOCIO_NO_EXISTE);

        try {
            String query = getSqlQuery("update-socio");
            if (query != null) {
                int result = getJdbcTemplate().update(query,
                        extracted(socio),
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
     */
    public boolean deleteSocio(String dni) {
        if (dni == null || dni.isEmpty()) return false;
        if (findSocioByDNI(dni) == null) return false;

        try {
            String query = getSqlQuery("delete-deleteSocio");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query, dni);
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Unable to delete socio from the database");
            exception.printStackTrace();
            return false;
        }
    }
}
