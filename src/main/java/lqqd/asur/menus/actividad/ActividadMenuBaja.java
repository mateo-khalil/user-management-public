package lqqd.asur.menus.actividad;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.Usuario;
import lqqd.asur.model.actividad.Estado;
import lqqd.asur.service.ActividadService;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

@Component
public class ActividadMenuBaja {

    private final Scanner scanner = new Scanner(System.in);
    @Autowired
    private ActividadService actividadService;

    public void gestionarBajaOReactivacion(Usuario currentUser) {
        if (!currentUser.getRole().isAdminOrAuxiliar()) {
            System.out.println(ConsoleColors.RED + "No tiene permisos para gestionar la baja o reactivación de actividades." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.CYAN + "=== Gestión de Actividades (Baja/Reactivate) ===" + ConsoleColors.RESET);
        MenuUtils.executeMenu(
                () -> {
                    System.out.println("1. Ver actividades activas");
                    System.out.println("2. Ver actividades inactivas");
                    System.out.print("Seleccione una opción: ");
                },
                opcion -> {
                    switch (opcion) {
                        case 1 -> listarActividadesPorEstado(Estado.ACTIVA);
                        case 2 -> listarActividadesPorEstado(Estado.INACTIVA);
                        default ->
                                System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                    }
                }
        );
    }

    private void listarActividadesPorEstado(Estado estado) {
        System.out.println(ConsoleColors.YELLOW + "Listado de Actividades - Estado: " + estado + ConsoleColors.RESET);
        actividadService.listarActividadesPorEstado(estado).forEach(this::mostrarActividad);

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID de la actividad para modificar su estado (o 0 para salir): " + ConsoleColors.RESET);
        long actividadId = scanner.nextLong();
        scanner.nextLine();

        if (actividadId == 0) {
            System.out.println(ConsoleColors.YELLOW + "Saliendo del menú de gestión de actividades." + ConsoleColors.RESET);
            return;
        }

        Optional<Actividad> actividadOptional = actividadService.buscarActividadPorId(actividadId);
        if (actividadOptional.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No se encontró una actividad con el ID proporcionado." + ConsoleColors.RESET);
            return;
        }

        Actividad actividad = actividadOptional.get();
        if (estado == Estado.ACTIVA) {
            gestionarBaja(actividad);
        } else if (estado == Estado.INACTIVA) {
            gestionarReactivacion(actividad);
        }
    }

    private void gestionarBaja(Actividad actividad) {
        if (actividad.getFechaInicioConHora().isAfter(LocalDateTime.now()) ||
                (actividad.getRequiereInscripcion() && actividad.getFechaAperturaInscripcion() != null &&
                        actividad.getFechaAperturaInscripcion().isAfter(LocalDateTime.now())) ||
                actividad.getFechaFinConHora().isBefore(LocalDateTime.now())) {

            System.out.print(ConsoleColors.YELLOW + "¿Está seguro de que desea inactivar esta actividad? (S/N): " + ConsoleColors.RESET);
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            if (confirmacion.equals("S")) {
                actividad.setEstado(Estado.INACTIVA);
                actividadService.guardarActividad(actividad);
                System.out.println(ConsoleColors.GREEN + "La actividad ha sido inactivada con éxito." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "La actividad no fue inactivada." + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.RED + "No se puede inactivar la actividad debido a sus restricciones de estado o tiempo." + ConsoleColors.RESET);
        }
    }

    private void gestionarReactivacion(Actividad actividad) {
        System.out.print(ConsoleColors.YELLOW + "¿Está seguro de que desea reactivar esta actividad? (S/N): " + ConsoleColors.RESET);
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        if (confirmacion.equals("S")) {
            actividad.setEstado(Estado.ACTIVA);
            actividadService.guardarActividad(actividad);
            System.out.println(ConsoleColors.GREEN + "La actividad ha sido reactivada con éxito." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "La actividad no fue reactivada." + ConsoleColors.RESET);
        }
    }

    private void mostrarActividad(Actividad actividad) {
        System.out.printf(ConsoleColors.YELLOW +
                        "| %-6d | %-15s | %-15s | %-15s | %-10s |\n",
                actividad.getId(),
                actividad.getNombre(),
                actividad.getTipoActividad().getNombre(),
                actividad.getEstado(),
                actividad.getFechaInicioConHora()
        );
    }
}
