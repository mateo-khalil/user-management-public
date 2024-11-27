package lqqd.asur.menus.actividad;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.Usuario;
import lqqd.asur.service.ActividadService;
import lqqd.asur.service.InscripcionActividadService;
import lqqd.asur.utils.ConsoleColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ActividadMenuActividadesInscribirse {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private InscripcionActividadService inscripcionActividadService;

    public void listarActividadesParaInscripcion(Usuario currentUser) {
        ConsoleColors.limpiarConsola();

        System.out.println(ConsoleColors.CYAN + "=== Actividades para Inscribirse ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Listado de Actividades que requieren inscripción:");
        System.out.println("===========================================================================");
        System.out.println("|   ID   |      Nombre       |      Tipo       |    Fecha    |  Costo  |");
        System.out.println("===========================================================================");

        actividadService.listarActividadesPorRequierenInscripcion()
                .forEach(this::mostrarActividad);

        System.out.println("===========================================================================" + ConsoleColors.RESET);

        while (true) {
            try {
                System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID de la actividad para inscribirse o 0 para salir: " + ConsoleColors.RESET);
                long actividadId = scanner.nextLong();
                scanner.nextLine(); // Limpiar buffer

                if (actividadId == 0) {
                    System.out.println(ConsoleColors.YELLOW + "Saliendo del menú de inscripción." + ConsoleColors.RESET);
                    break;
                }

                mostrarActividadPorId(actividadId, currentUser);
                break;
            } catch (InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }

    private void mostrarActividadPorId(Long actividadId, Usuario currentUser) {
        var actividadOpt = actividadService.buscarActividadPorId(actividadId);
        if (actividadOpt.isPresent()) {
            Actividad actividad = actividadOpt.get();
            System.out.println(ConsoleColors.GREEN + "Detalles de la Actividad:");
            System.out.println("===========================================================================");
            mostrarActividad(actividad);
            System.out.println("===========================================================================" + ConsoleColors.RESET);

            while (true) {
                System.out.print(ConsoleColors.YELLOW + "¿Desea inscribirse en esta actividad? (S/N): " + ConsoleColors.RESET);
                String confirmacion = scanner.nextLine().trim().toUpperCase();

                if (confirmacion.equals("S")) {
                    try {
                        inscripcionActividadService.inscribirUsuario(actividad, currentUser);
                        System.out.println(ConsoleColors.GREEN + "La inscripción a la actividad " + actividad.getNombre() + " se ha registrado con éxito." + ConsoleColors.RESET);
                    } catch (RuntimeException e) {
                        System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
                    }
                    break;
                } else if (confirmacion.equals("N")) {
                    System.out.println(ConsoleColors.YELLOW + "Inscripción cancelada." + ConsoleColors.RESET);
                    break;
                } else {
                    System.out.println(ConsoleColors.RED + "Opción no válida. Por favor, ingrese S para Sí o N para No." + ConsoleColors.RESET);
                }
            }
        } else {
            System.out.println(ConsoleColors.RED + "La actividad con ID " + actividadId + " no existe o no requiere inscripción." + ConsoleColors.RESET);
        }
    }

    private void mostrarActividad(Actividad actividad) {
        String tipoActividadNombre = actividad.getTipoActividad() != null ? actividad.getTipoActividad().getNombre() : "Sin Tipo";
        System.out.printf(
                "| %-6d | %-15s | %-15s | %-10s | %-7.2f |\n",
                actividad.getId(),
                actividad.getNombre(),
                tipoActividadNombre,
                actividad.getFechaInicioConHora().toLocalDate(),
                actividad.getCosto()
        );
    }
}
