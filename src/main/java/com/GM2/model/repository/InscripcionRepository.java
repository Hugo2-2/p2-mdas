package com.GM2.model.repository;

import com.GM2.model.domain.Inscripcion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InscripcionRepository extends AbstractRepository{

    public InscripcionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Inscripcion findInscripcionById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findInscripcionById");
            Inscripcion result = jdbcTemplate.query(query, this::mapRowToInscripcio, id)
        }
    }
}
