package com.GM2.model.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Clase base abstracta para todos los repositorios del sistema.
 * 
 * Proporciona acceso centralizado al {@link JdbcTemplate} y a las consultas SQL
 * externalizadas en el fichero de propiedades.
 *
 * Refactoring 6.1: Pull Up Constructor Body — la inicialización de {@code jdbcTemplate}
 * y las propiedades SQL se centraliza aquí en lugar de repetirse en cada subclase.
 *
 * Refactoring 3.2: Encapsulate Field — los campos son privados y se acceden
 * mediante getters protegidos.
 *
 * Refactoring 2.3: La ruta del fichero SQL se centraliza como constante.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public abstract class AbstractRepository {

    /** Ruta por defecto del fichero de propiedades SQL. */
    public static final String DEFAULT_SQL_PROPERTIES_PATH = "./src/main/resources/db/sql.properties";

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    /**
     * Constructor que inicializa el JdbcTemplate y carga las propiedades SQL
     * desde la ruta por defecto.
     *
     * @param jdbcTemplate JdbcTemplate inyectado por Spring.
     */
    protected AbstractRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        setSqlQueriesFileName(DEFAULT_SQL_PROPERTIES_PATH);
    }

    /**
     * Acceso protegido al JdbcTemplate para las subclases.
     */
    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * Acceso protegido a las propiedades SQL cargadas.
     */
    protected Properties getSqlQueries() {
        return sqlQueries;
    }

    /**
     * Obtiene una consulta SQL por su clave.
     *
     * @param key Clave de la consulta en el fichero de propiedades.
     * @return La sentencia SQL correspondiente.
     */
    protected String getSqlQuery(String key) {
        return sqlQueries.getProperty(key);
    }

    public void setSqlQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        this.sqlQueries = createProperties();
    }

    /**
     * Carga las propiedades SQL desde el fichero configurado.
     */
    private Properties createProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(this.sqlQueriesFileName)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
