package com.GM2.model.repository;

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
public class SocioRepository extends AbstractRepository {

    public SocioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public boolean addSocio(Socio socio) {
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
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert socios in the database");
            exception.printStackTrace();
        }

        return false;
    }
}

