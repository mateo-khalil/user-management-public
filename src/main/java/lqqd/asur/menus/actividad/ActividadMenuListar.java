package lqqd.asur.menus.actividad;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.actividad.Estado;
import lqqd.asur.service.ActividadService;
import lqqd.asur.service.TipoActividadService;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ActividadMenuListar {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private TipoActividadService tipoActividadService;

    public void listarActividadesConFiltros() {
        ConsoleColors.limpiarConsola();

        System.out.println(ConsoleColors.YELLOW + "Actividades Registradas (Activas por defecto):");
        System.out.println("===========================================================================");
        System.out.println("|   ID   |      Nombre       |      Tipo       |   Fecha   |  Costo |");
        System.out.println("===========================================================================");

        actividadService.listarActividadesActivas().forEach(this::mostrarActividad);

        System.out.println("===========================================================================" + ConsoleColors.RESET);

        MenuUtils.executeMenuString(
                () -> {
                    System.out.print(ConsoleColors.MAGENTA + "Filtros (N: Nombres, T: Tipo, F: Fecha, C: Costo, E: Estado)");
                    System.out.print(ConsoleColors.MAGENTA + " | Ingrese el ID de la actividad para ver detalles: " + ConsoleColors.RESET);
                },
                input -> {
                    try {
                        long actividadId = Long.parseLong(input);
                        if (actividadId == 0) {
                            return;
                        }
                        mostrarActividadPorId(actividadId);
                    } catch (NumberFormatException e) {
                        switch (input.toUpperCase()) {
                            case "N" -> listarPorNombres();
                            case "T" -> listarPorTipo();
                            case "F" -> listarPorFecha();
                            case "C" -> listarPorCosto();
                            case "E" -> listarPorEstado();
                            case "0" ->
                                    System.out.println(ConsoleColors.YELLOW + "Saliendo del menú de filtros..." + ConsoleColors.RESET);
                            default ->
                                    System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                        }
                    }
                }
        );
    }

    private void listarPorTipo() {
        System.out.println(ConsoleColors.CYAN + "Seleccione un tipo de actividad de la lista:" + ConsoleColors.RESET);

        tipoActividadService.listarTodosLosTipos().forEach(tipo ->
                System.out.printf("ID: %d | Nombre: %s%n", tipo.getId(), tipo.getNombre())
        );

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID del tipo de actividad: " + ConsoleColors.RESET);
        try {
            Long tipoActividadId = scanner.nextLong();
            scanner.nextLine(); // Limpiar buffer

            actividadService.listarActividadesPorTipo(tipoActividadId).forEach(this::mostrarActividad);
        } catch (InputMismatchException e) {
            System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
            scanner.nextLine(); // Limpiar buffer
        }
    }

    private void mostrarActividadPorId(Long actividadId) {
        var actividad = actividadService.buscarActividadPorId(actividadId);
        if (actividad.isPresent()) {
            System.out.println(ConsoleColors.GREEN + "Detalles de la Actividad:");
            System.out.println("===========================================================================");
            mostrarActividad(actividad.get());
            System.out.println("===========================================================================" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "La actividad con ID " + actividadId + " no existe." + ConsoleColors.RESET);
        }
    }

    private void listarPorNombres() {
        System.out.print(ConsoleColors.MAGENTA + "Ingrese parte del nombre: " + ConsoleColors.RESET);
        String criterio = scanner.nextLine();
        actividadService.listarActividadesPorNombre(criterio).forEach(this::mostrarActividad);
    }

    private void listarPorFecha() {
        System.out.print(ConsoleColors.MAGENTA + "Ingrese la fecha (dd/MM/yyyy): " + ConsoleColors.RESET);
        String fechaStr = scanner.nextLine();
        try {
            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            actividadService.listarActividadesPorFecha(fecha).forEach(this::mostrarActividad);
        } catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.RED + "Formato de fecha inválido. Intente nuevamente." + ConsoleColors.RESET);
        }
    }

    private void listarPorCosto() {
        System.out.print(ConsoleColors.MAGENTA + "Ingrese el costo máximo: " + ConsoleColors.RESET);
        try {
            Double costo = scanner.nextDouble();
            scanner.nextLine(); // Limpiar buffer
            actividadService.listarActividadesPorCosto(costo).forEach(this::mostrarActividad);
        } catch (InputMismatchException e) {
            System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
            scanner.nextLine(); // Limpiar buffer
        }
    }

    private void listarPorEstado() {
        MenuUtils.executeMenu(
                () -> {
                    System.out.println(ConsoleColors.CYAN + "Seleccione el estado:");
                    System.out.println("1. Activas");
                    System.out.println("2. Inactivas");
                    System.out.print("Ingrese su opción: ");
                },
                estadoOpcion -> {
                    Estado estado = switch (estadoOpcion) {
                        case 1 -> Estado.ACTIVA;
                        case 2 -> Estado.INACTIVA;
                        default -> {
                            System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                            yield null;
                        }
                    };
                    if (estado != null) {
                        actividadService.listarActividadesPorEstado(estado).forEach(this::mostrarActividad);
                    }
                }
        );
    }

    private void mostrarActividad(Actividad actividad) {
        System.out.printf(
                ConsoleColors.YELLOW + "| %-6d | %-15s | %-15s | %-10s | %-6.2f |\n" + ConsoleColors.RESET,
                actividad.getId(),
                actividad.getNombre(),
                actividad.getTipoActividad().getNombre(),
                actividad.getFechaInicioConHora().toLocalDate(),
                actividad.getCosto()
        );
    }
}
