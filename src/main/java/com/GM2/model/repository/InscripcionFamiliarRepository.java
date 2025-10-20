package com.GM2.model.repository;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.InscripcionFamiliar;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InscripcionFamiliarRepository extends AbstractRepository {

    HijosRepository hijosRepository;

    public InscripcionFamiliarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InscripcionFamiliar> findAllInscripciones(){
        try {
            String query = sqlQueries.getProperty("select-findAllInscripcionesFamiliares");
            if( query != null ) {
                List<InscripcionFamiliar> result = jdbcTemplate.query(query, new RowMapper<InscripcionFamiliar>() {
                    public InscripcionFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new InscripcionFamiliar(
                                rs.getInt("id"),
                                rs.getDate("fecha_creacion").toLocalDate(),
                                rs.getFloat("cuota_anual"),
                                rs.getString("socio_titular"),
                                rs.getString("segundo_adulto"),
                                hijosRepository.findHijosByInscripcion(rs.getInt("id"))
                        );
                    };
                });

                return result;

            } else return null;
        } catch (DataAccessException ex) {
            System.err.println("Unable to find inscripciones familiares");
            ex.printStackTrace();
            return null;
        }
    }

    public InscripcionFamiliar findInscripcionFamiliarById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionFamiliarById");
            List<InscripcionFamiliar> result = jdbcTemplate.query(query, this::mapRowToInscripcion, id);

            if( result != null ) {
                return result.getFirst();
            } else return null;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find inscripcionFamiliar with id: " +  id);
            exception.printStackTrace();
            return null;
        }
    }

    private InscripcionFamiliar mapRowToInscripcion(ResultSet rs, int rowNum) throws SQLException {
        try {
            if(rs.first()) {
                int id = rs.getInt("id");
                LocalDate fechaCreacion = rs.getDate("fecha_creacion").toLocalDate();
                float cuotaAnual = rs.getFloat("coutal_anual");
                String socioTitular = rs.getString("socio_titular");
                String adultoAdicional = rs.getString("segundo_adulto");
                List<Hijos> hijos = hijosRepository.findHijosByInscripcion(id);

                InscripcionFamiliar inscripcionFamiliar = new InscripcionFamiliar(id, fechaCreacion, cuotaAnual, socioTitular, adultoAdicional, hijos);
                return inscripcionFamiliar;
            } else return null;
        } catch (SQLException ex) {
            System.err.println("Unable to find inscripcionFamiliar");
            ex.printStackTrace();
            return null;
        }
    }

    public boolean addInscripcionFamiliar(InscripcionFamiliar inscripcionFamiliar) {
        try {
            String query = sqlQueries.getProperty("insert-addInscripcionFamiliar");
            if( query != null ) {
                int firstResult = jdbcTemplate.update(query,
                        inscripcionFamiliar.getSocioTitularId(),
                        inscripcionFamiliar.getCuotaAnual(),
                        inscripcionFamiliar.getFechaCreacion(),
                        inscripcionFamiliar.getSegundoAudlto()
                );

                // Ternaria para parsear un boolean a un integer.
                int seconResult = hijosRepository.addHijos(inscripcionFamiliar.getHijos()) ? 1 : 0;

                if( firstResult == 1 && seconResult == 1 ) {
                    return true;
                }  else return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to add inscripcionFamiliar");
            ex.printStackTrace();
        }

        return false;
    }
}
