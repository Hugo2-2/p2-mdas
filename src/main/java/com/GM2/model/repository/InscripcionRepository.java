package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones CRUD (Crear, Leer, Actualizar, Borrar)
 * de la entidad {@link Inscripcion} en la base de datos.
 * Maneja las inscripciones del club náutico, incluyendo la gestión de cuotas,
 * segundos adultos e hijos asociados a las inscripciones familiares.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Repository
public class InscripcionRepository extends AbstractRepository{

    final private HijosRepository hijosRepository;
    final private SocioRepository socioRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Configura JdbcTemplate y los repositorios relacionados, usando @Lazy
     * para SocioRepository para evitar dependencias circulares.
     *
     * @param jdbcTemplate El bean de JdbcTemplate gestionado por Spring.
     * @param hijosRepository Repositorio para gestionar los hijos de las inscripciones.
     * @param socioRepository Repositorio de socios (cargado de forma lazy).
     */
    public InscripcionRepository(JdbcTemplate jdbcTemplate, HijosRepository hijosRepository, @Lazy SocioRepository socioRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.hijosRepository = hijosRepository;
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Recupera una lista de todas las inscripciones de la base de datos.
     * Incluye automáticamente los hijos asociados si la cuota es superior a 300€.
     *
     * @return Una lista de {@link Inscripcion}, o null si no se encuentran resultados o hay error.
     */
    public List<Inscripcion> findAllInscripciones() {
        try {
            String query = sqlQueries.getProperty("select-findAllInscripciones");
            if( query != null ) {
                List<Inscripcion> result = jdbcTemplate.query(query, new RowMapper<Inscripcion>() {
                    public Inscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        int id = rs.getInt("id");
                        String titular = rs.getString("socio_Titular");
                        float cuota = rs.getFloat("cuota_anual");
                        LocalDate fechaCreacion = rs.getDate("fecha_creacion").toLocalDate();
                        String segundoAdulto = rs.getString("segundo_adulto");

                        List<Hijos> hijos = new ArrayList<>();

                        if(cuota > 300) {
                            hijos = hijosRepository.findHijosByInscripcion(id);
                        }

                        return new Inscripcion(id, titular, cuota, fechaCreacion, segundoAdulto, hijos);
                    };
                });

                return result;
            } else return null;
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Extrae y mapea la primera fila de un ResultSet a un objeto Inscripcion.
     * Este método funciona como un ResultSetExtractor que solo procesa un resultado.
     * Carga automáticamente los hijos asociados si la cuota anual es superior a 300€.
     *
     * @param rs El conjunto de resultados (ResultSet) completo devuelto por la consulta JDBC.
     * @return Un objeto {@link Inscripcion} si se encuentra una fila,
     *         o null si el ResultSet está vacío o si ocurre una SQLException.
     */
    private Inscripcion mapRowToInscripcion(ResultSet rs) {
        try {
            if(rs.first()) {
                int id = rs.getInt("id");
                String socioTitular = rs.getString("socio_Titular");
                float cuotaAnual = rs.getFloat("cuota_anual");
                LocalDate fechaCreacion = rs.getDate("fecha_creacion").toLocalDate();
                String segundoAdulto = rs.getString("segundo_adulto");

                List<Hijos> hijos = new ArrayList<>();

                if(cuotaAnual > 300) {
                    hijos = hijosRepository.findHijosByInscripcion(id);
                }

                Inscripcion inscripcion = new Inscripcion(id, socioTitular, cuotaAnual, fechaCreacion, segundoAdulto, hijos);

                return inscripcion;
            } else {
                return null;
            }



        } catch (SQLException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Busca una inscripción específica por su ID (clave primaria).
     *
     * @param id El ID único de la inscripción a buscar.
     * @return El objeto {@link Inscripcion} si se encuentra, o null si no existe.
     */
    public Inscripcion findInscripcionById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionById");
            Inscripcion result = jdbcTemplate.query(query, this::mapRowToInscripcion, id);

            if(result != null)
                return result;
            else return null;

        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Busca una inscripción por el DNI del socio titular.
     *
     * @param dniTitular El DNI del socio titular de la inscripción.
     * @return El objeto {@link Inscripcion} si se encuentra, o null si no existe.
     */
    public Inscripcion findInscripcionByDNITitular(String dniTitular) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionByDniTitular");
            Inscripcion result = jdbcTemplate.query(query, this::mapRowToInscripcion, dniTitular);

            if(result != null)
                return result;
            else return null;
        }  catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Inserta una nueva inscripción en la base de datos.
     * Verifica que no exista ya una inscripción para el mismo titular.
     *
     * @param inscripcion El objeto {@link Inscripcion} a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean addInscripcion(Inscripcion inscripcion) {
        if( inscripcion == null ) return false;

        if( findInscripcionByDNITitular( inscripcion.getTitularMemberId() ) != null ) return false;

        try {
            String query = sqlQueries.getProperty("insert-addInscripcion");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                    inscripcion.getTitularMemberId(),
                    inscripcion.getAnnualFee(),
                    inscripcion.getCreationDate()
                );

                if(result > 0)
                    return true;
                else return false;
            } else return false;

        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * Actualiza una inscripción existente en la base de datos.
     * Verifica que la inscripción exista antes de realizar la actualización.
     * Actualiza automáticamente la fecha de creación al momento actual.
     *
     * @param inscripcion El objeto {@link Inscripcion} con los datos actualizados.
     * @return "EXITO" si la actualización fue exitosa, mensaje de error en caso contrario.
     */
    public String updateInscripcion(Inscripcion inscripcion) {
        if( inscripcion == null ) return "No se ha podido ingresar la inscripcion";

        if( findInscripcionByDNITitular(inscripcion.getTitularMemberId()) == null )
            return "No puedes actualizar la inscripcion porque no existe";

        inscripcion.setCreationDate();

        try {
            String query = sqlQueries.getProperty("update-Inscripcion");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                    inscripcion.getAnnualFee(),
                    inscripcion.getCreationDate(),
                    inscripcion.getSecondAdult(),
                    inscripcion.getId()
                );

                if(result > 0)
                    return "EXITO";
                else return "No se ha podido actualizar la inscripcion";

            } else return "No se ha podido actualizar la inscripcion";
        } catch (DataAccessException ex) {
            System.err.println("Unable to retrieve results from the database");
            ex.printStackTrace();
            return "No se ha podido actualizar la inscripcion";
        }
    }

    /**
     * Actualiza una inscripción añadiendo un segundo adulto sin hijos.
     * Realiza validaciones para verificar que ambos socios estén registrados
     * y que el titular tenga permisos de titularidad.
     * Incrementa la cuota anual en 250€ por el segundo adulto.
     *
     * @param dniTitular DNI del socio titular de la inscripción.
     * @param dniSegundoAdulto DNI del segundo adulto a añadir.
     * @return "EXITO" si la actualización fue exitosa, mensaje de error en caso contrario.
     */
    public String updateInscripcionSinHijos(String dniTitular, String dniSegundoAdulto){
        if(socioRepository.findSocioByDNI(dniTitular) == null && socioRepository.findSocioByDNI(dniSegundoAdulto) == null) {
            return "El titular y el segundo adulto no están registrados como socios";
        } else if(socioRepository.findSocioByDNI(dniTitular) == null) {
            return "El titular no está registrado como socio";
        } else if(socioRepository.findSocioByDNI(dniSegundoAdulto) == null) {
            return "El segundo adulto no está registrado como socio.";
        }

        if(!socioRepository.findSocioByDNI(dniTitular).getIsTitular()) {
            return "El socio introducido como titular no es titular de ninguna inscripción";
        }

        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null) {
            return "No puedes actualizar la inscripcion porque no existe";
        }

        inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 250);
        inscripcion.setSecondAdult(dniSegundoAdulto);

        return updateInscripcion(inscripcion);

    }

    /**
     * Actualiza una inscripción añadiendo hijos a la inscripción familiar.
     * Procesa una lista de hijos, los registra en la base de datos y
     * actualiza la cuota anual incrementándola en 100€ por cada hijo.
     *
     * @param dniTitular DNI del socio titular de la inscripción.
     * @param hijos Lista de objetos Hijos con los datos de los hijos.
     * @return "EXITO" si la actualización fue exitosa, mensaje de error en caso contrario.
     */
    public String updateInscripcionConHijos(String dniTitular, List<Hijos> hijos){ //Clean Code - Regla 2: Se reduce el número de parámetros de 5 a 2, pasando una lista de objetos Hijos
        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null) {
            return "No puedes actualizar la inscripcion porque no existe";
        }

        for (Hijos hijo : hijos) {

            if (hijo.getNationalId() == null || hijo.getNationalId().trim().isEmpty()) {
                return "DNI de hijo no válido o vacío.";            
            }

            if (hijo.getName() == null || hijo.getName().trim().isEmpty()) {
                return "Nombre de hijo no proporcionado para DNI: " + hijo.getNationalId();
            }
            
            if (hijo.getSurname() == null || hijo.getSurname().trim().isEmpty()) {
                return "Apellidos de hijo no proporcionados para DNI: " + hijo.getNationalId();
            }

            if (hijo.getBirthDate() == null) {
                return "Fecha de nacimiento no proporcionada para DNI: " + hijo.getNationalId();
            }

            // Asignar el ID de la inscripción al hijo
            hijo.setRegistrationId(inscripcion.getId());

            // Guardar el hijo completo en la base de datos
            boolean resultado = hijosRepository.addHijo(hijo);
            
            if (!resultado) {
                return "Error al guardar el hijo con DNI: " + hijo.getNationalId();
            }

            // Actualizar la cuota en el objeto de inscripción (100€ por hijo)
            inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 100);
        }

        return updateInscripcion(inscripcion);

    }

    /**
     * Elimina una inscripción por el DNI del titular.
     * IMPORTANTE: Solo elimina la inscripción, no los socios ni los hijos.
     * Los hijos quedan huérfanos y deberían ser gestionados por separado si es necesario.
     *
     * @param dniTitular El DNI del socio titular de la inscripción a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean deleteInscripcionByDniTitular(String dniTitular) {
        if(dniTitular == null || dniTitular.isEmpty()) {
            return false;
        }

        // Verificar que la inscripción existe
        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);
        if(inscripcion == null) {
            return false;
        }

        try {
            String query = sqlQueries.getProperty("delete-deleteInscripcionByDniTitular");
            if(query != null) {
                int result = jdbcTemplate.update(query, dniTitular);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to delete inscription from the database");
            ex.printStackTrace();
            return false;
        }
    }
}
