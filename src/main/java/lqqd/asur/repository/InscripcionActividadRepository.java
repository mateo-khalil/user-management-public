package lqqd.asur.repository;

import lqqd.asur.model.InscripcionActividad;
import lqqd.asur.model.TipoActividad;
import lqqd.asur.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InscripcionActividadRepository extends JpaRepository<InscripcionActividad, Long> {

    List<InscripcionActividad> findByCanceladaFalse();

    List<InscripcionActividad> findByFechaInscripcionBetween(LocalDateTime start, LocalDateTime end);

    List<InscripcionActividad> findByActividadTipoActividadAndCanceladaFalse(TipoActividad actividad_tipoActividad);

    List<InscripcionActividad> findByUsuarioAndCanceladaFalse(Usuario usuario);

    List<InscripcionActividad> findByActividad_IdAndCanceladaFalse(Long actividadId);

    List<InscripcionActividad> findByUsuario_IdAndCanceladaFalse(Long usuarioId);

    List<InscripcionActividad> findByActividad_IdAndUsuario_IdAndCanceladaFalse(Long actividadId, Long usuarioId);

    List<InscripcionActividad> findByFechaInscripcionBetweenAndCanceladaFalse(LocalDateTime desde, LocalDateTime hasta);

    List<InscripcionActividad> findByFechaInscripcionBetweenAndCanceladaTrue(LocalDateTime desde, LocalDateTime hasta);

    List<InscripcionActividad> findByFechaInscripcionBetweenAndCanceladaFalseAndActividadIdIn(LocalDateTime desde, LocalDateTime hasta, List<Long> actividadIds);

    List<InscripcionActividad> findByFechaInscripcionBetweenAndCanceladaTrueAndActividadIdIn(LocalDateTime desde, LocalDateTime hasta, List<Long> actividadIds);

    List<InscripcionActividad> findByFechaInscripcionBetweenAndActividadIdIn(LocalDateTime desde, LocalDateTime hasta, List<Long> actividadIds);

    @Query("SELECT ia FROM InscripcionActividad ia WHERE ia.cancelada = false AND ia.actividad.tipoActividad IN :tiposActividad")
    List<InscripcionActividad> findByCanceladaFalseAndActividad_TipoActividadIn(@Param("tiposActividad") List<TipoActividad> tiposActividad);

    @Query("SELECT ia FROM InscripcionActividad ia WHERE ia.cancelada = true AND ia.actividad.tipoActividad IN :tiposActividad")
    List<InscripcionActividad> findByCanceladaTrueAndActividad_TipoActividadIn(@Param("tiposActividad") List<TipoActividad> tiposActividad);

    @Query("SELECT ia FROM InscripcionActividad ia WHERE ia.actividad.tipoActividad IN :tiposActividad")
    List<InscripcionActividad> findByActividad_TipoActividadIn(@Param("tiposActividad") List<TipoActividad> tiposActividad);


}


