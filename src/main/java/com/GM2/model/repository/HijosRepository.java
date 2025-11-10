package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HijosRepository extends AbstractRepository {
    private JdbcTemplate jdbcTemplate;

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

    public Hijos findHijoByDni(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findHijoByDni");
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

    private Hijos mapRowFromInscripcion(ResultSet row, int rowNum) throws SQLException {
        String dni = row.getString("dni");
        String nombre = row.getString("nombre");
        String apellidos = row.getString("apellidos");
        LocalDate fechaNacimiento = row.getDate("fecha_nacimiento").toLocalDate();
        int id_inscripcion = row.getInt("id_inscripcion");

        Hijos hijo = new Hijos(dni, nombre, apellidos, fechaNacimiento, id_inscripcion);

        if(hijo != null) {
            return hijo;
        }

        return null;

    }

    public boolean addHijo(Hijos hijo) {
         try {
            String query = sqlQueries.getProperty("insert-addHijo");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                        hijo.getDni(),
                        hijo.getNombre(),
                        hijo.getApellidos(),
                        hijo.getFechaNacimiento(),
                        hijo.getId_inscripcion()
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
}
