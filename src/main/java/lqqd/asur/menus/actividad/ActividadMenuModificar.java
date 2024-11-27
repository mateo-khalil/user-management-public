package lqqd.asur.menus.actividad;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.TipoActividad;
import lqqd.asur.model.Usuario;
import lqqd.asur.model.actividad.Estado;
import lqqd.asur.service.ActividadService;
import lqqd.asur.service.TipoActividadService;
import lqqd.asur.utils.ConsoleColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class ActividadMenuModificar {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private TipoActividadService tipoActividadService;

    public void modificarActividad(Usuario currentUser) {
        if (!currentUser.getRole().isAdminOrAuxiliar()) {
            System.out.println(ConsoleColors.RED + "No tiene permisos para modificar actividades." + ConsoleColors.RESET);
            return;
        }

        try {
            System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID de la actividad a modificar: " + ConsoleColors.RESET);
            Long actividadId = Long.parseLong(scanner.nextLine().trim());

            Optional<Actividad> actividadOptional = actividadService.buscarActividadPorId(actividadId);

            if (actividadOptional.isEmpty()) {
                System.out.println(ConsoleColors.RED + "No se encontró una actividad con el ID proporcionado." + ConsoleColors.RESET);
                return;
            }

            Actividad actividad = actividadOptional.get();

            if (actividad.getFechaInicioConHora().isBefore(LocalDateTime.now())) {
                System.out.println(ConsoleColors.RED + "La actividad ya comenzó o finalizó. No se puede modificar." + ConsoleColors.RESET);
                return;
            }

            if (actividad.getRequiereInscripcion() && actividad.getFechaAperturaInscripcion() != null &&
                    actividad.getFechaAperturaInscripcion().isBefore(LocalDateTime.now())) {
                System.out.println(ConsoleColors.RED + "La inscripción ya está abierta. No se puede modificar." + ConsoleColors.RESET);
                return;
            }

            System.out.println(ConsoleColors.CYAN + "Modificando actividad: " + actividad.getNombre() + ConsoleColors.RESET);

            modificarCampos(actividad);

            System.out.print(ConsoleColors.YELLOW + "¿Desea guardar los cambios realizados? (S/N): " + ConsoleColors.RESET);
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            if (confirmacion.equals("S")) {
                actividadService.guardarActividad(actividad);
                System.out.println(ConsoleColors.GREEN + "La actividad ha sido modificada con éxito." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Los cambios no fueron guardados." + ConsoleColors.RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "ID de actividad inválido. Por favor, ingrese un número válido." + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Ocurrió un error inesperado. Por favor, intente nuevamente." + ConsoleColors.RESET);
        }
    }

    private void modificarCampos(Actividad actividad) {
        try {
            System.out.print(ConsoleColors.MAGENTA + "Descripción actual: " + actividad.getDescripcion() + "\nNueva descripción (deje en blanco para mantener): " + ConsoleColors.RESET);
            String descripcion = scanner.nextLine();
            if (!descripcion.isBlank()) {
                actividad.setDescripcion(descripcion);
            }

            modificarTipoActividad(actividad);

            System.out.print(ConsoleColors.MAGENTA + "Fecha de inicio actual: " + actividad.getFechaInicioConHora() +
                    "\nIngrese la nueva fecha y hora de inicio (YYYY-MM-DDTHH:MM o deje en blanco para mantener): " + ConsoleColors.RESET);
            String fechaInicioInput = scanner.nextLine();
            if (!fechaInicioInput.isBlank()) {
                try {
                    LocalDateTime nuevaFechaInicio = LocalDateTime.parse(fechaInicioInput, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    actividad.setFechaInicioConHora(nuevaFechaInicio);
                } catch (DateTimeParseException e) {
                    System.out.println(ConsoleColors.RED + "Formato de fecha y hora inválido. Se mantendrá la fecha actual." + ConsoleColors.RESET);
                }
            }

            System.out.print(ConsoleColors.MAGENTA + "Costo actual: " + actividad.getCosto() + "\nNuevo costo (deje en blanco para mantener): " + ConsoleColors.RESET);
            String costoInput = scanner.nextLine();
            if (!costoInput.isBlank()) {
                try {
                    double nuevoCosto = Double.parseDouble(costoInput);
                    actividad.setCosto(nuevoCosto);
                } catch (NumberFormatException e) {
                    System.out.println(ConsoleColors.RED + "Entrada inválida. Se mantendrá el costo actual." + ConsoleColors.RESET);
                }
            }

            modificarEstadoActividad(actividad);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Ocurrió un error al modificar los campos. Intente nuevamente." + ConsoleColors.RESET);
        }
    }

    private void modificarTipoActividad(Actividad actividad) {
        List<TipoActividad> tiposDeActividad = tipoActividadService.listarTiposDeActividadActivos();

        if (tiposDeActividad.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No hay tipos de actividad configurados." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.MAGENTA + "Tipo de actividad actual: " + actividad.getTipoActividad().getNombre());
        System.out.println(ConsoleColors.MAGENTA + "Seleccione el nuevo tipo de actividad (o deje en blanco para mantener):");
        tiposDeActividad.forEach(tipo -> System.out.printf("%d - %s%n", tipo.getId(), tipo.getNombre()));

        String tipoInput = scanner.nextLine();
        if (!tipoInput.isBlank()) {
            try {
                Long idTipo = Long.parseLong(tipoInput);
                TipoActividad tipoSeleccionado = tiposDeActividad.stream()
                        .filter(tipo -> tipo.getId().equals(idTipo))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("ID de tipo de actividad no válido."));
                actividad.setTipoActividad(tipoSeleccionado);
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Se mantendrá el tipo actual." + ConsoleColors.RESET);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColors.RED + e.getMessage() + ConsoleColors.RESET);
            }
        }
    }

    private void modificarEstadoActividad(Actividad actividad) {
        System.out.println(ConsoleColors.MAGENTA + "Estado actual: " + actividad.getEstado());
        System.out.println("1. Activa");
        System.out.println("2. Inactiva");
        System.out.print("Seleccione el nuevo estado (o deje en blanco para mantener): " + ConsoleColors.RESET);
        String estadoInput = scanner.nextLine();
        if (!estadoInput.isBlank()) {
            try {
                int estadoOpcion = Integer.parseInt(estadoInput);
                Estado nuevoEstado = switch (estadoOpcion) {
                    case 1 -> Estado.ACTIVA;
                    case 2 -> Estado.INACTIVA;
                    default -> throw new IllegalArgumentException("Opción no válida.");
                };
                actividad.setEstado(nuevoEstado);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColors.RED + "Opción inválida. Se mantendrá el estado actual." + ConsoleColors.RESET);
            }
        }
    }
}
