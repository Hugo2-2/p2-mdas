package com.GM2.model.repository;

import com.GM2.model.domain.Patron;
import com.GM2.model.domain.Socio;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PatronRepository extends AbstractRepository {

    public PatronRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Patron> findAllPatrones() {
        try {
            String query = sqlQueries.getProperty("select-findAllPatrones");
            return getPatrons(query);

        } catch (DataAccessException exception) {
            System.err.println("Unable to find patrones");
            exception.printStackTrace();
            return null;
        }
    }

    public List<Patron> findAllFreePatrones() {
        try {
            String query = sqlQueries.getProperty("select-findAllFreePatrones");
            return getPatrons(query);

        } catch (DataAccessException exception) {
            System.err.println("Unable to find patrones");
            exception.printStackTrace();
            return null;
        }
    }

    private List<Patron> getPatrons(String query) {
        if(query != null){
            List<Patron> result = jdbcTemplate.query(query, new RowMapper<Patron>() {
                public Patron mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Patron(
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("dni"),
                            rs.getDate("fecha_nacimiento").toLocalDate(),
                            rs.getDate("fecha_expedicion_titulo").toLocalDate()
                    );
                };
            });

            return result;
        } else return null;
    }

    public Patron findPatronByDNI(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findPatronByDNI");
            Patron result = jdbcTemplate.query(query, this::mapRowToPatron, dni);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find patron with dni: " + dni);
            exception.printStackTrace();
            return null;
        }
    }

    public boolean isRegistered(String dni) {
        try {
            String query = sqlQueries.getProperty("select-countPatronByDNI");
            Integer count = jdbcTemplate.queryForObject(query, Integer.class, dni);
            if( count != null && count > 0 )
                return true;
            else return false;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find patron with dni: " + dni);
            exception.printStackTrace();
            return false;
        }
    }

    private Patron mapRowToPatron(ResultSet row) {
        try {

            if(row.first()) {
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                String dni = row.getString("dni");
                Date fechaNacimiento = row.getDate("fecha_nacimiento");
                Date fechaExpedicionTitulo = row.getDate("fecha_expedicion_titulo");

                Patron patron = new Patron(nombre, apellidos, dni,
                                    fechaNacimiento.toLocalDate(), fechaExpedicionTitulo.toLocalDate());
                return patron;
            } else {
                return null;
            }

        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addPatron(Patron patron) {
        try {
            String query = sqlQueries.getProperty("insert-addPatron");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        patron.getNombre(),
                        patron.getApellidos(),
                        patron.getDni(),
                        patron.getFechaNacimiento(),
                        patron.getFechaExpedicionTitulo()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert patron in the database");
            exception.printStackTrace();
        }

        return false;
    }

}
