package lqqd.asur.service;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.InscripcionActividad;
import lqqd.asur.model.TipoActividad;
import lqqd.asur.model.Usuario;
import lqqd.asur.repository.InscripcionActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionActividadService {

    @Autowired
    private InscripcionActividadRepository inscripcionActividadRepository;

    @Autowired
    private ActividadService actividadService;

    private static List<String> getStrings(List<InscripcionActividad> inscripciones) {
        List<String> reporte = new ArrayList<>();
        if (!inscripciones.isEmpty()) {
            for (InscripcionActividad inscripcion : inscripciones) {
                String estado = inscripcion.getCancelada() ? "Cancelada" : "Inscripción";
                reporte.add(String.format("Fecha: %s | Usuario: %s | Actividad: %s | Estado: %s",
                        inscripcion.getFechaInscripcion(),
                        inscripcion.getUsuario(),
                        inscripcion.getActividad().getNombre(),
                        estado));
            }
        }
        return reporte;
    }

    /**
     * Valida si el usuario ya está inscrito en la actividad, y si no lo está, lo inscribe.
     */
    public void inscribirUsuario(Actividad actividad, Usuario usuario) {

        boolean yaInscrito = inscripcionActividadRepository.findByActividad_IdAndCanceladaFalse(actividad.getId())
                .stream()
                .anyMatch(inscripcion -> inscripcion.getUsuario().getId().equals(usuario.getId()));

        if (yaInscrito) {
            throw new RuntimeException("El usuario ya está inscrito en esta actividad.");
        }

        InscripcionActividad inscripcion = new InscripcionActividad();
        inscripcion.setActividad(actividad);
        inscripcion.setUsuario(usuario);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setCancelada(false);

        inscripcionActividadRepository.save(inscripcion);
    }

    /**
     * Cancela la inscripción de un usuario a una actividad.
     */
    public boolean cancelarInscripcion(Long actividadId, Long usuarioId) {
        List<InscripcionActividad> inscripciones = inscripcionActividadRepository.findByActividad_IdAndUsuario_IdAndCanceladaFalse(actividadId, usuarioId);
        if (inscripciones.isEmpty()) {
            return false;
        }
        InscripcionActividad inscripcion = inscripciones.getFirst();
        inscripcion.setCancelada(true);
        inscripcionActividadRepository.save(inscripcion);
        return true;
    }

    /**
     * Lista las inscripciones activas de un usuario.
     */
    public List<InscripcionActividad> listarInscriptosPorUsuario(Long usuarioId) {
        return inscripcionActividadRepository.findByUsuario_IdAndCanceladaFalse(usuarioId);
    }

    /**
     * Genera un reporte de inscripciones y cancelaciones de actividades en un rango de fechas.
     */
    public List<String> generarReporte(LocalDate fechaDesde, LocalDate fechaHasta, String tipoReporte, List<Long> actividadesIds) {
        LocalDateTime desde = fechaDesde.atStartOfDay();
        LocalDateTime hasta = fechaHasta.atTime(23, 59, 59);

        if (actividadesIds.isEmpty()) {
            actividadesIds = actividadService.listarTodasActividades()
                    .stream()
                    .map(Actividad::getId)
                    .toList();
        }

        List<InscripcionActividad> inscripciones = filtrarPorTipoYActividades(desde, hasta, tipoReporte, actividadesIds);

        return getStrings(inscripciones);
    }

    /**
     * Filtra las inscripciones por tipo de reporte y actividades seleccionadas.
     */
    private List<InscripcionActividad> filtrarPorTipoYActividades(LocalDateTime desde, LocalDateTime hasta, String tipoReporte, List<Long> actividadesIds) {
        if (tipoReporte.equals("INSCRIPCIONES")) {
            return actividadesIds.isEmpty()
                    ? inscripcionActividadRepository.findByFechaInscripcionBetweenAndCanceladaFalse(desde, hasta)
                    : inscripcionActividadRepository.findByFechaInscripcionBetweenAndCanceladaFalseAndActividadIdIn(desde, hasta, actividadesIds);
        } else if (tipoReporte.equals("CANCELACIONES")) {
            return actividadesIds.isEmpty()
                    ? inscripcionActividadRepository.findByFechaInscripcionBetweenAndCanceladaTrue(desde, hasta)
                    : inscripcionActividadRepository.findByFechaInscripcionBetweenAndCanceladaTrueAndActividadIdIn(desde, hasta, actividadesIds);
        } else {
            return actividadesIds.isEmpty()
                    ? inscripcionActividadRepository.findByFechaInscripcionBetween(desde, hasta)
                    : inscripcionActividadRepository.findByFechaInscripcionBetweenAndActividadIdIn(desde, hasta, actividadesIds);
        }
    }

    /**
     * Genera un reporte de inscripciones y cancelaciones de actividades por tipo de actividad.
     */
    public List<String> generarReportePorTipoActividad(String tipoReporte, List<String> tiposActividadNombres) {
        List<TipoActividad> tiposActividad = tiposActividadNombres.stream()
                .map(nombre -> actividadService.buscarTipoActividadPorNombre(nombre)
                        .orElseThrow(() -> new IllegalArgumentException("Tipo de actividad no encontrado: " + nombre)))
                .toList();

        List<InscripcionActividad> inscripciones = new ArrayList<>();

        if ("INSCRIPCIONES".equalsIgnoreCase(tipoReporte)) {
            inscripciones = inscripcionActividadRepository.findByCanceladaFalseAndActividad_TipoActividadIn(tiposActividad);
        } else if ("CANCELACIONES".equalsIgnoreCase(tipoReporte)) {
            inscripciones = inscripcionActividadRepository.findByCanceladaTrueAndActividad_TipoActividadIn(tiposActividad);
        } else if ("AMBAS".equalsIgnoreCase(tipoReporte)) {
            inscripciones = inscripcionActividadRepository.findByActividad_TipoActividadIn(tiposActividad);
        }

        return inscripciones.stream()
                .map(inscripcion -> String.format(
                        "Usuario: %s | Actividad: %s | Tipo: %s | Fecha: %s | Cancelada: %s",
                        inscripcion.getUsuario().getNombres(),
                        inscripcion.getActividad().getNombre(),
                        inscripcion.getActividad().getTipoActividad().getNombre(),
                        inscripcion.getFechaInscripcion(),
                        inscripcion.getCancelada() ? "Sí" : "No"
                ))
                .collect(Collectors.toList());
    }


}
