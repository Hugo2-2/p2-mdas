package com.GM2.model.repository;

import com.GM2.model.domain.Reserva;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Repositorio para operaciones CRUD de la entidad {@link Reserva}.
 *
 * Refactoring 6.1: Se usa super(jdbcTemplate) del AbstractRepository.
 * Refactoring 7.1: Eliminados comentarios redundantes que solo restaban lo que el código hace.
 * Refactoring 7.3: Extraído RowMapper como lambda reutilizable.
 */
@Repository
public class ReservaRepository extends AbstractRepository {

    /**
     * RowMapper reutilizable para convertir una fila del ResultSet a una Reserva.
     */
    private final org.springframework.jdbc.core.RowMapper<Reserva> reservaRowMapper = (rs, rowNum) -> new Reserva(
            rs.getInt("id"),
            rs.getDate("fecha").toLocalDate(),
            rs.getInt("plazas"),
            rs.getDouble("precio"),
            rs.getString("usuario_id"),
            rs.getString("matricula_embarcacion"),
            rs.getString("descripcion")
    );

    public ReservaRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * Recupera una lista de todas las reservas de la base de datos.
     */
    public List<Reserva> findAllReservas() {
        try {
            String query = getSqlQuery("select-findAllReservas");
            if (query == null) return null;

            return getJdbcTemplate().query(query, reservaRowMapper);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find reservas");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Busca una reserva por su ID.
     */
    public Reserva findReservaById(int id) {
        try {
            String query = getSqlQuery("select-findReservaById");
            return getJdbcTemplate().query(query, this::mapRowToReserva, id);
        } catch (DataAccessException exception) {
            System.err.println("Unable to find reserva with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * ResultSetExtractor que mapea la primera fila a una Reserva.
     */
    private Reserva mapRowToReserva(ResultSet row) {
        try {
            if (row.first()) {
                return new Reserva(
                        row.getInt("id"),
                        row.getDate("fecha").toLocalDate(),
                        row.getInt("plazas"),
                        row.getDouble("precio"),
                        row.getString("usuario_id"),
                        row.getString("matricula_embarcacion"),
                        row.getString("descripcion")
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
     * Inserta una nueva reserva en la base de datos.
     * Utiliza {@link KeyHolder} para recuperar el ID generado automáticamente.
     */
    public boolean addReserva(Reserva reserva) {
        try {
            String query = getSqlQuery("insert-addReserva");
            if (query == null) return false;

            KeyHolder keyHolder = new GeneratedKeyHolder();

            int result = getJdbcTemplate().update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setDate(1, Date.valueOf(reserva.getDate()));
                ps.setInt(2, reserva.getSeats());
                ps.setDouble(3, reserva.getPrice());
                ps.setString(4, reserva.getUserNationalId());
                ps.setString(5, reserva.getBoatRegistration());
                ps.setString(6, reserva.getDescription());
                return ps;
            }, keyHolder);

            if (result > 0 && keyHolder.getKey() != null) {
                reserva.setId(keyHolder.getKey().intValue());
                return true;
            }
            return false;
        } catch (DataAccessException exception) {
            System.err.println("Error insertando reserva");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza los datos de una reserva existente.
     */
    public boolean updateReserva(Reserva reserva) {
        try {
            String query = getSqlQuery("update-reserva");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query,
                    reserva.getDate(),
                    reserva.getSeats(),
                    reserva.getPrice(),
                    reserva.getDescription(),
                    reserva.getId()
            );
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Error actualizando la reserva: " + exception.getMessage());
            return false;
        }
    }

    /**
     * Elimina una reserva por su ID.
     */
    public boolean deleteReserva(int id) {
        try {
            String query = getSqlQuery("delete-reserva");
            if (query == null) return false;

            int result = getJdbcTemplate().update(query, id);
            return result > 0;
        } catch (DataAccessException exception) {
            System.err.println("Error eliminando la reserva con id: " + id);
            return false;
        }
    }
}