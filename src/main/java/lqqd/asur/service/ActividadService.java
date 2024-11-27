package lqqd.asur.service;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.TipoActividad;
import lqqd.asur.model.actividad.Estado;
import lqqd.asur.repository.ActividadRepository;
import lqqd.asur.repository.TipoActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private TipoActividadRepository tipoActividadRepository;

    public List<Actividad> listarTodasActividades() {
        return actividadRepository.findAll();
    }

    public List<Actividad> listarActividadesPorTipo(Long tipoActividadId) {
        TipoActividad tipoActividad = tipoActividadRepository.findById(tipoActividadId)
                .orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado."));
        return actividadRepository.findByTipoActividad(tipoActividad);
    }

    public Optional<Actividad> buscarActividadPorId(Long id) {
        return actividadRepository.findById(id);
    }

    public void guardarActividad(Actividad actividad) {
        actividadRepository.save(actividad);
    }

    public List<Actividad> listarActividadesPorEspacioYDia(Long espacioId, LocalDate dia) {
        LocalDateTime inicioDia = dia.atStartOfDay();
        LocalDateTime finDia = dia.atTime(LocalTime.MAX);
        return actividadRepository.listarActividadesPorEspacioYRango(espacioId, inicioDia, finDia);
    }

    public LocalDateTime calcularHoraFin(LocalDateTime horaInicio, Duration duracion) {
        if (horaInicio == null || duracion == null || duracion.isNegative() || duracion.isZero()) {
            throw new IllegalArgumentException("La hora de inicio y la duración deben ser válidas y mayores a 0.");
        }
        return horaInicio.plus(duracion);
    }

    public List<Actividad> listarActividadesActivas() {
        return actividadRepository.findByEstado(Estado.ACTIVA);
    }

    public List<Actividad> listarActividadesPorEstado(Estado estado) {
        return actividadRepository.findByEstado(estado);
    }

    public List<Actividad> listarActividadesPorNombre(String criterio) {
        return actividadRepository.findByNombreContainingIgnoreCase(criterio);
    }

    public List<Actividad> listarActividadesPorFecha(LocalDate fecha) {
        return actividadRepository.findByFechaInicioConHoraBetween(
                fecha.atStartOfDay(),
                fecha.atTime(23, 59, 59)
        );
    }

    public List<Actividad> listarActividadesPorCosto(Double costo) {
        return actividadRepository.findByCostoLessThanEqual(costo);
    }

    public Iterable<Actividad> listarActividadesPorRequierenInscripcion() {
        return actividadRepository.findByRequiereInscripcionIsTrue();
    }

    public Optional<TipoActividad> buscarTipoActividadPorNombre(String nombre) {
        return tipoActividadRepository.findByNombre(nombre);
    }

}
