package lqqd.asur.repository;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.TipoActividad;
import lqqd.asur.model.actividad.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    @Query("SELECT a FROM Actividad a WHERE a.lugar.id = :espacioId AND " +
            "a.fechaInicioConHora BETWEEN :inicioDia AND :finDia")
    List<Actividad> listarActividadesPorEspacioYRango(@Param("espacioId") Long espacioId,
                                                      @Param("inicioDia") LocalDateTime inicioDia,
                                                      @Param("finDia") LocalDateTime finDia);

    List<Actividad> findByEstado(Estado estado);

    List<Actividad> findByNombreContainingIgnoreCase(String criterio);

    List<Actividad> findByTipoActividad(TipoActividad tipoActividad);

    @Query("SELECT a FROM Actividad a WHERE a.fechaInicioConHora BETWEEN :inicio AND :fin")
    List<Actividad> findByFechaInicioConHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Actividad> findByCostoLessThanEqual(Double costo);

    List<Actividad> findByCostoLessThanEqualAndRequiereInscripcionIsTrue(Double costo);

    List<Actividad> findByTipoActividadAndRequiereInscripcionIsTrue(TipoActividad tipoActividad);

    List<Actividad> findByRequiereInscripcionIsTrue();

    List<Actividad> findByNombreContainingIgnoreCaseAndRequiereInscripcionIsTrue(String nombre);

    List<Actividad> findByRequiereInscripcionIsTrueAndEstado(Estado estado);
}
