package lqqd.asur.repository;

import lqqd.asur.model.TipoActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoActividadRepository extends JpaRepository<TipoActividad, Long> {
    List<TipoActividad> findByActivo(Boolean activo);

    Optional<TipoActividad> findByNombre(String nombre);
}

