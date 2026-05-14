package com.GM2.model.repository;

import com.GM2.exception.DatabaseException;
import com.GM2.exception.EntityNotFoundException;
import com.GM2.exception.ErrorCode;
import com.GM2.exception.ValidationException;
import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD de la entidad {@link Inscripcion}.
 *
 * Refactoring 6.1: Se usa super(jdbcTemplate) del AbstractRepository.
 * Refactoring 4.3: Consolidada la validación de socios en updateInscripcionSinHijos.
 * Refactoring 3.1: Uso de constantes de cuota de {@link Inscripcion}.
 * Refactoring 7.2: Eliminado import no usado (DateTimeParseException).
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class InscripcionRepository extends AbstractRepository {

    private final HijosRepository hijosRepository;
    private final SocioRepository socioRepository;

    public InscripcionRepository(JdbcTemplate jdbcTemplate, HijosRepository hijosRepository, @Lazy SocioRepository socioRepository) {
        super(jdbcTemplate);
        this.hijosRepository = hijosRepository;
        this.socioRepository = socioRepository;
    }

    /**
     * Recupera una lista de todas las inscripciones de la base de datos.
     * Incluye automáticamente los hijos asociados si la cuota supera la base individual.
     */
    public List<Inscripcion> findAllInscripciones() {
        try {
            String query = getSqlQuery("select-findAllInscripciones");
            if (query == null) return null;

            return getJdbcTemplate().query(query, (rs, rowNum) -> {
                int id = rs.getInt("id");
                String titular = rs.getString("socio_Titular");
                float cuota = rs.getFloat("cuota_anual");
                LocalDate fechaCreacion = rs.getDate("fecha_creacion").toLocalDate();
                String segundoAdulto = rs.getString("segundo_adulto");

                List<Hijos> hijos = new ArrayList<>();
                if (cuota > Inscripcion.BASE_ANNUAL_FEE) {
                    hijos = hijosRepository.findHijosByInscripcion(id);
                }

                return new Inscripcion(id, titular, cuota, fechaCreacion, segundoAdulto, hijos);
            });
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * ResultSetExtractor que mapea la primera fila a una Inscripcion.
     */
    private Inscripcion mapRowToInscripcion(ResultSet rs) {
        try {
            if (rs.first()) {
                int id = rs.getInt("id");
                String socioTitular = rs.getString("socio_Titular");
                float cuotaAnual = rs.getFloat("cuota_anual");
                LocalDate fechaCreacion = rs.getDate("fecha_creacion").toLocalDate();
                String segundoAdulto = rs.getString("segundo_adulto");

                List<Hijos> hijos = new ArrayList<>();
                if (cuotaAnual > Inscripcion.BASE_ANNUAL_FEE) {
                    hijos = hijosRepository.findHijosByInscripcion(id);
                }

                return new Inscripcion(id, socioTitular, cuotaAnual, fechaCreacion, segundoAdulto, hijos);
            }
            return null;
        } catch (SQLException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Busca una inscripción específica por su ID.
     */
    public Inscripcion findInscripcionById(int id) {
        try {
            String query = getSqlQuery("select-findInscripcionById");
            return getJdbcTemplate().query(query, this::mapRowToInscripcion, id);
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Busca una inscripción por el DNI del socio titular.
     */
    public Inscripcion findInscripcionByDNITitular(String dniTitular) {
        try {
            String query = getSqlQuery("select-findInscripcionByDniTitular");
            return getJdbcTemplate().query(query, this::mapRowToInscripcion, dniTitular);
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Inserta una nueva inscripción en la base de datos.
     */
    public boolean addInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null) return false;
        if (findInscripcionByDNITitular(inscripcion.getTitularMemberId()) != null) return false;

        try {
            String query = getSqlQuery("insert-addInscripcion");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    inscripcion.getTitularMemberId(),
                    inscripcion.getAnnualFee(),
                    inscripcion.getCreationDate()
            );
            return result > 0;
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza una inscripción existente en la base de datos.
     *
     * Refactoring 1.7: Remove Assignments to Parameters — se usa variable local
     * para la fecha en lugar de modificar el parámetro directamente.
     */
    public void updateInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null)
            throw new ValidationException(ErrorCode.INSCRIPCION_NO_INGRESADA);

        if (findInscripcionByDNITitular(inscripcion.getTitularMemberId()) == null)
            throw new EntityNotFoundException(ErrorCode.INSCRIPCION_NO_EXISTE);

        LocalDate updatedDate = LocalDate.now();
        inscripcion.setCreationDate(updatedDate);

        try {
            String query = getSqlQuery("update-Inscripcion");
            if (query != null) {
                int result = getJdbcTemplate().update(query,
                        inscripcion.getAnnualFee(),
                        inscripcion.getCreationDate(),
                        inscripcion.getSecondAdult(),
                        inscripcion.getId()
                );
                if (result <= 0)
                    throw new DatabaseException(ErrorCode.INSCRIPCION_NO_ACTUALIZADA);
            } else {
                throw new DatabaseException(ErrorCode.INSCRIPCION_NO_ACTUALIZADA);
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            throw new DatabaseException(ErrorCode.INSCRIPCION_NO_ACTUALIZADA);
        }
    }

    /**
     * Actualiza una inscripción añadiendo un segundo adulto sin hijos.
     *
     * Refactoring 4.3: Consolidate Conditional Expression — se buscan titular y
     * segundo adulto una sola vez y se reutilizan las variables, en lugar de
     * llamar a findSocioByDNI() cuatro veces consecutivas.
     */
    public void updateInscripcionSinHijos(String dniTitular, String dniSegundoAdulto) {
        var titular = socioRepository.findSocioByDNI(dniTitular);
        var segundoAdulto = socioRepository.findSocioByDNI(dniSegundoAdulto);

        if (titular == null && segundoAdulto == null)
            throw new EntityNotFoundException(ErrorCode.TITULAR_Y_SEGUNDO_ADULTO_NO_SOCIOS);

        if (titular == null)
            throw new EntityNotFoundException(ErrorCode.TITULAR_NO_SOCIO);

        if (segundoAdulto == null)
            throw new EntityNotFoundException(ErrorCode.SEGUNDO_ADULTO_NO_SOCIO);

        if (!titular.getIsTitular())
            throw new ValidationException(ErrorCode.TITULAR_NO_ES_TITULAR_INSCRIPCION);

        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);
        if (inscripcion == null)
            throw new EntityNotFoundException(ErrorCode.INSCRIPCION_NO_EXISTE);

        inscripcion.setAnnualFee(inscripcion.getAnnualFee() + Inscripcion.SECOND_ADULT_FEE);
        inscripcion.setSecondAdult(dniSegundoAdulto);

        updateInscripcion(inscripcion);
    }

    /**
     * Actualiza una inscripción añadiendo hijos a la inscripción familiar.
     */
    public void updateInscripcionConHijos(String dniTitular, List<Hijos> hijos) {
        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null)
            throw new EntityNotFoundException(ErrorCode.INSCRIPCION_NO_EXISTE);

        for (Hijos hijo : hijos) {
            validarDatosHijo(hijo);

            hijo.setRegistrationId(inscripcion.getId());

            boolean resultado = hijosRepository.addHijo(hijo);
            if (!resultado)
                throw new DatabaseException(ErrorCode.HIJO_NO_GUARDADO, hijo.getDni());

            inscripcion.setAnnualFee(inscripcion.getAnnualFee() + Inscripcion.CHILD_FEE);
        }

        updateInscripcion(inscripcion);
    }

    /**
     * Valida los datos obligatorios de un hijo antes de su inserción.
     *
     * Refactoring 1.1: Extract Method — extraída la validación de datos de hijo
     * del bucle principal de updateInscripcionConHijos para mejorar la legibilidad.
     */
    private void validarDatosHijo(Hijos hijo) {
        if (hijo.getDni() == null || hijo.getDni().trim().isEmpty())
            throw new ValidationException(ErrorCode.HIJO_DNI_INVALIDO);

        if (hijo.getName() == null || hijo.getName().trim().isEmpty())
            throw new ValidationException(ErrorCode.HIJO_NOMBRE_NO_PROPORCIONADO, hijo.getDni());

        if (hijo.getSurname() == null || hijo.getSurname().trim().isEmpty())
            throw new ValidationException(ErrorCode.HIJO_APELLIDOS_NO_PROPORCIONADOS, hijo.getDni());

        if (hijo.getBirthDate() == null)
            throw new ValidationException(ErrorCode.HIJO_FECHA_NO_PROPORCIONADA, hijo.getDni());
    }

    /**
     * Elimina una inscripción por el DNI del titular.
     */
    public boolean deleteInscripcionByDniTitular(String dniTitular) {
        if (dniTitular == null || dniTitular.isEmpty()) return false;
        if (findInscripcionByDNITitular(dniTitular) == null) return false;

        try {
            String query = getSqlQuery("delete-deleteInscripcionByDniTitular");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query, dniTitular);
            return result > 0;
        } catch (DataAccessException ex) {
            System.err.println("Unable to delete inscription from the database");
            ex.printStackTrace();
            return false;
        }
    }
}
