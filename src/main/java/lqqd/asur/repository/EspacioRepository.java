package lqqd.asur.repository;

import lqqd.asur.model.Espacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {
    /**
     * Verifica si un espacio está disponible en un rango de fechas y horas.
     * Verifica si el :fechaInicio está dentro del rango de una actividad existente (a.fechaInicioConHora a a.fechaFinConHora).
     * Verifica si el :fechaFin está dentro del rango de una actividad existente (a.fechaInicioConHora a a.fechaFinConHora).
     * Verifica si la fecha de inicio de una actividad existente está dentro del rango (:fechaInicio a :fechaFin).
     */
    @Query("SELECT COUNT(a) = 0 FROM Actividad a WHERE a.lugar.id = :espacioId AND " +
            "((:fechaInicio BETWEEN a.fechaInicioConHora AND a.fechaFinConHora) OR " +
            "(:fechaFin BETWEEN a.fechaInicioConHora AND a.fechaFinConHora) OR " +
            "(a.fechaInicioConHora BETWEEN :fechaInicio AND :fechaFin))")
    boolean espacioDisponible(@Param("espacioId") Long espacioId,
                              @Param("fechaInicio") LocalDateTime fechaInicio,
                              @Param("fechaFin") LocalDateTime fechaFin);
}
