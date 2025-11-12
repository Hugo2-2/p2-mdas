package com.GM2.model.repository;

import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Socio;
import com.GM2.model.domain.Alquiler;

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
public class ReservaRepository extends AbstractRepository{

    private EmbarcacionRepository embarcacionRepository;
    private SocioRepository socioRepository;
    private AlquilerRepository alquilerRepository;

    public ReservaRepository(JdbcTemplate jdbcTemplate, EmbarcacionRepository embarcacionRepository, SocioRepository socioRepository, AlquilerRepository alquilerRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;
        this.alquilerRepository = alquilerRepository;

        // Configurar las rutas de los .properties si es necesario
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    public List<Reserva> findAllReservas() {
        try {
            String query = sqlQueries.getProperty("select-findAllReservas");
            if( query != null ) {
                List<Reserva> result = jdbcTemplate.query(query, new RowMapper<Reserva>() {
                   public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                       return new Reserva(
                               rs.getInt("id"),
                               rs.getDate("fecha").toLocalDate(),
                               rs.getInt("plazas"),
                               rs.getDouble("precio"),
                               rs.getString("usuario_id"),
                               rs.getString("matricula_embarcacion"),
                               rs.getString("descripcion")

                       );
                   };
                });

                return result;
            } else return null;
        } catch (DataAccessException exception) {
            System.err.println("Unable to find reservas");
            exception.printStackTrace();
            return null;
        }
    }

    public Reserva findReservaById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findReservaById");
            Reserva result = jdbcTemplate.query(query, this::mapRowToReserva, id);
            if( result != null )
                return result;
            else return null;
        } catch(DataAccessException exception) {
            System.err.println("Unable to find reserva with id: " + id);
            exception.printStackTrace();
            return null;
        }
    }

    private Reserva mapRowToReserva(ResultSet row) {
        try {

            if(row.first()) {
                int id = row.getInt("id");
                Date fecha = row.getDate("fecha");
                int plazas = row.getInt("plazas");
                double precio = row.getDouble("precio");
                String usuario_id = row.getString("usuario_id");
                String matricula_embarcacion = row.getString("matricula_embarcacion");
                String descripcion = row.getString("descripcion");

                Reserva reserva = new Reserva(id, fecha.toLocalDate(), plazas, precio, usuario_id, matricula_embarcacion, descripcion);
                return reserva;
            } else {
                return null;
            }

        } catch (SQLException exception) {
            System.err.println("Unable to retrieve results from the database");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addReserva(Reserva reserva) {
        try {
            String query = sqlQueries.getProperty("insert-addReserva");
            if(query != null) {
                int result = jdbcTemplate.update(query,
                   reserva.getFecha(),
                   reserva.getPlazas(),
                   reserva.getPrecio(),
                   reserva.getUsuario_id(),
                   reserva.getMatricula_embarcacion(),
                   reserva.getDescripcion()
                );

                if (result > 0)
                    return true;
                else return false;

            } else return false;

        } catch (DataAccessException exception) {
            System.err.println("Unable to insert reservas in the database");
            exception.printStackTrace();
        }

        return false; 
    }

    // Buscar embarcaciones con patrón disponible en una fecha concreta
    public List<Embarcacion> buscarEmbarcacionesConPatronDisponibles(LocalDate fecha, int plazasSolicitadas) {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        List<Reserva> reservas = findAllReservas();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Embarcacion> disponibles = new ArrayList<>();



        for (Embarcacion e : embarcaciones) {

            boolean ocupada = false;

            for (Alquiler a : alquileres) {
                if (a.getMatricula_embarcacion().equals(e.getMatricula()) && !fecha.isBefore(a.getFechainicio()) && !fecha.isAfter(a.getFechafin())) {
                    ocupada = true;
                    break;
                }
            }

            for (Reserva r : reservas) {
                if (r.getMatricula_embarcacion().equals(e.getMatricula()) && r.getFecha().equals(fecha)) {
                    ocupada = true;
                    break;
                }
            }

            // Comprobar capacidad
            boolean tienePatron = e.getIdPatron() != null; // O llama a embarcacionRepository.tienePatron(e.getMatricula())

            // Comprobar patrón y capacidad
            if (!ocupada && tienePatron && e.getPlazas() >= plazasSolicitadas + 1) {
                disponibles.add(e);
            }
        }
        return disponibles;
    }

    // Crear reserva
    // En la clase ReservaService.java

    // Crear reserva
    public String reservarEmbarcacion(Reserva reserva) {

        // 1. Validaciones del Socio (Existencia y Mayor de Edad)
        Socio socio = socioRepository.findSocioByDNI(reserva.getUsuario_id());

        if (socio == null) {
            return "El DNI del socio solicitante no está registrado.";
        }
        // El enunciado indica que cualquier socio mayor de edad puede solicitar una actividad.
        if (!socio.esMayorEdad()) {
            return "El socio solicitante debe ser mayor de edad para reservar actividades.";
        }

        // Obtener datos clave de la reserva y de la embarcación elegida
        String matriculaElegida = reserva.getMatricula_embarcacion();
        LocalDate fechaReserva = reserva.getFecha();
        int plazasSolicitadas = reserva.getPlazas();

        // Buscar la embarcación elegida por el usuario para obtener sus datos
        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(matriculaElegida);

        // 2. Búsqueda y Validación de la Embarcación

        // 2.1. Validación de existencia
        if (embarcacion == null) {
            return "La matrícula de la embarcación especificada no existe.";
        }

        // 2.2. Validación de Patrón (Requisito D.1: debe tener patrón)
        if (embarcacion.getIdPatron() == null || embarcacion.getIdPatron().isEmpty()) {
            return "No se pudo realizar la reserva (la embarcación elegida no tiene un patrón asignado).";
        }

        // 2.3. Validación de Capacidad (Requisito D.1: plazas totales >= solicitadas + 1 del patrón)
        // Asumimos que getPlazas() devuelve la capacidad total.
        int capacidadTotal = embarcacion.getPlazas();
        if (capacidadTotal < plazasSolicitadas + 1) {
            return String.format("No se pudo realizar la reserva (capacidad insuficiente: Plazas disponibles %d, Plazas solicitadas %d, más 1 para el patrón).", (capacidadTotal - 1), plazasSolicitadas);
        }

        // 2.4. Validación de Ocupación/Disponibilidad (Patrón)
        // Este método debe verificar el solapamiento en las tablas 'reserva' y 'alquiler'
        List<Embarcacion> disponibles = buscarEmbarcacionesConPatronDisponibles(fechaReserva, plazasSolicitadas);
        boolean disponible = false;
        for (Embarcacion e : disponibles) {
            if (e.getMatricula().equals(matriculaElegida)) {
                disponible = true;
                break;
            }
        }
        if (!disponible) {
            return "Embarcación no disponible"; // No hay embarcaciones disponibles
        }

        // 3. Persistencia (Guardado en BBDD)

        // Calcular precio (40€ por persona) y asignar al objeto
        reserva.setPrecio(plazasSolicitadas * 40.0);

        // Nota: La matrícula ya viene seteada desde el formulario, solo falta el ID.
        // La BBDD generará el ID.

        boolean guardadoExitoso = addReserva(reserva);

        if (guardadoExitoso) {
            // 4. Éxito
            // Se asume que el repositorio ha asignado un ID si es autoincremental,
            // o que no necesitamos el ID para el mensaje de éxito inmediato.
            return "EXITO";
        } else {
            // 3. Persistencia fallida
            return "Error interno al intentar guardar la reserva en la base de datos.";
        }
    }


}
