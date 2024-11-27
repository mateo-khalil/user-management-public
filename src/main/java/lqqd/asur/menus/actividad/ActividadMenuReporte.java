package lqqd.asur.menus.actividad;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lqqd.asur.model.Usuario;
import lqqd.asur.service.ActividadService;
import lqqd.asur.service.InscripcionActividadService;
import lqqd.asur.utils.ConsoleColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

@Component
public class ActividadMenuReporte {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private InscripcionActividadService inscripcionActividadService;

    public void generarReporte(Usuario currentUser) {
        ConsoleColors.limpiarConsola();

        System.out.println(ConsoleColors.CYAN + "=== Reporte de Inscripciones/Cancelaciones ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.MAGENTA + "Seleccione el tipo de reporte:");
        System.out.println("1. Reporte por Fecha");
        System.out.println("2. Reporte por Tipo de Actividad");
        System.out.print(ConsoleColors.MAGENTA + "Ingrese su opción: " + ConsoleColors.RESET);

        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            switch (opcion) {
                case 1 -> generarReportePorFecha();
                case 2 -> generarReportePorTipoActividad();
                default ->
                        System.out.println(ConsoleColors.RED + "Opción no válida. Volviendo al menú principal." + ConsoleColors.RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número." + ConsoleColors.RESET);
            scanner.nextLine(); // Limpiar buffer
        }
    }

    private void generarReportePorFecha() {
        LocalDate fechaDesde = solicitarFecha("Ingrese la fecha desde (dd/MM/yyyy): ");
        LocalDate fechaHasta = solicitarFecha("Ingrese la fecha hasta (dd/MM/yyyy): ");

        String tipoReporte = solicitarTipoReporte();

        List<Long> actividadesSeleccionadas = solicitarActividades();

        System.out.println(ConsoleColors.CYAN + "\nGenerando reporte por fecha..." + ConsoleColors.RESET);
        var reporte = inscripcionActividadService.generarReporte(fechaDesde, fechaHasta, tipoReporte, actividadesSeleccionadas);

        mostrarReporte(reporte);
    }

    public void generarReportePorTipoActividad() {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "=== Reporte por Tipo de Actividad ===" + ConsoleColors.RESET);

        String tipoReporte = solicitarTipoReporte();
        List<String> tiposSeleccionados = solicitarTiposDeActividad();

        System.out.println(ConsoleColors.CYAN + "\nGenerando reporte por tipo de actividad..." + ConsoleColors.RESET);
        var reporte = inscripcionActividadService.generarReportePorTipoActividad(tipoReporte, tiposSeleccionados);

        mostrarReporte(reporte);
    }

    private String solicitarTipoReporte() {
        while (true) {
            System.out.println(ConsoleColors.CYAN + "Seleccione el tipo de reporte:" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.MAGENTA + "1. Inscripciones");
            System.out.println(ConsoleColors.MAGENTA + "2. Cancelaciones");
            System.out.println(ConsoleColors.MAGENTA + "3. Ambas");
            System.out.print(ConsoleColors.CYAN + "Ingrese su opción: ");
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                return switch (opcion) {
                    case 1 -> "INSCRIPCIONES";
                    case 2 -> "CANCELACIONES";
                    case 3 -> "AMBAS";
                    default -> throw new IllegalArgumentException();
                };
            } catch (IllegalArgumentException | InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }

    private List<String> solicitarTiposDeActividad() {
        System.out.println(ConsoleColors.YELLOW + "Tipos de Actividades Disponibles:" + ConsoleColors.RESET);
        System.out.println("1. Charlas");
        System.out.println("2. Recreativas");
        System.out.println("3. Deportivas");
        System.out.println("4. Sociales");
        System.out.print(ConsoleColors.MAGENTA + "Ingrese los números de los tipos de actividad separados por coma (o presione Enter para todos): " + ConsoleColors.RESET);

        String input = scanner.nextLine();
        if (input.isBlank()) {
            return List.of("CHARLAS", "RECREATIVAS", "DEPORTIVAS", "SOCIALES");
        }

        try {
            return Stream.of(input.split(","))
                    .map(id -> {
                        switch (id.trim()) {
                            case "1" -> {
                                return "CHARLAS";
                            }
                            case "2" -> {
                                return "RECREATIVAS";
                            }
                            case "3" -> {
                                return "DEPORTIVAS";
                            }
                            case "4" -> {
                                return "SOCIALES";
                            }
                            default -> throw new IllegalArgumentException();
                        }
                    })
                    .toList();
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColors.RED + "Entrada inválida. Se seleccionarán todos los tipos de actividad." + ConsoleColors.RESET);
            return List.of("CHARLAS", "RECREATIVAS", "DEPORTIVAS", "SOCIALES");
        }
    }

    private LocalDate solicitarFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(ConsoleColors.MAGENTA + mensaje + ConsoleColors.RESET);
                String fechaStr = scanner.nextLine();
                return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println(ConsoleColors.RED + "Formato de fecha inválido. Intente nuevamente." + ConsoleColors.RESET);
            }
        }
    }

    /**
     * Solicita al usuario los IDs de las actividades a seleccionar. Si no se ingresa nada, se seleccionan todas.
     */
    private List<Long> solicitarActividades() {
        System.out.println(ConsoleColors.YELLOW + "Listado de Actividades Disponibles:" + ConsoleColors.RESET);
        actividadService.listarTodasActividades().forEach(actividad ->
                System.out.printf("ID: %d | Nombre: %s%n", actividad.getId(), actividad.getNombre())
        );

        System.out.print(ConsoleColors.MAGENTA + "Ingrese los IDs de las actividades separadas por coma (o presione Enter para todas): " + ConsoleColors.RESET);
        String input = scanner.nextLine();
        if (input.isBlank()) {
            return List.of();
        }

        try {
            return Stream.of(input.split(","))
                    .map(id -> Long.parseLong(id.trim()))
                    .toList();
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error al procesar los IDs. Se seleccionarán todas las actividades." + ConsoleColors.RESET);
            return List.of();
        }
    }

    private void mostrarReporte(List<String> reporte) {
        if (reporte.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No se encontraron registros para los filtros aplicados." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.YELLOW + "=== Reporte ===");
            reporte.forEach(System.out::println);
            System.out.println(ConsoleColors.YELLOW + "================" + ConsoleColors.RESET);

            guardarReporteComoXml(reporte);
        }
    }

    /**
     * Guarda el reporte como un archivo XML si el usuario lo desea. Solicita la ruta y el nombre del archivo.
     */
    private void guardarReporteComoXml(List<String> reporte) {
        while (true) {
            try {
                System.out.print(ConsoleColors.MAGENTA + "¿Desea guardar el reporte como XML? (0: No, 1: Sí): " + ConsoleColors.RESET);
                int opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion == 1) {
                    System.out.print(ConsoleColors.MAGENTA + "Ingrese la ruta completa y el nombre del archivo (por ejemplo, /ruta/a/reporte.xml): " + ConsoleColors.RESET);
                    String filePath = scanner.nextLine().trim();

                    if (!filePath.endsWith(".xml")) {
                        filePath += ".xml";
                    }

                    File fileToSave = new File(filePath);

                    XmlMapper xmlMapper = new XmlMapper();
                    xmlMapper.writeValue(fileToSave, reporte);
                    System.out.println(ConsoleColors.GREEN + "Reporte guardado como " + fileToSave.getAbsolutePath() + " con éxito." + ConsoleColors.RESET);
                    break;
                } else if (opcion == 0) {
                    System.out.println(ConsoleColors.YELLOW + "El reporte no se guardará." + ConsoleColors.RESET);
                    break;
                } else {
                    System.out.println(ConsoleColors.RED + "Opción inválida. Intente nuevamente." + ConsoleColors.RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese 0 para No o 1 para Sí." + ConsoleColors.RESET);
                scanner.nextLine();
            } catch (IOException e) {
                System.out.println(ConsoleColors.RED + "Error al guardar el archivo XML: " + e.getMessage() + ConsoleColors.RESET);
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Ocurrió un error inesperado: " + e.getMessage() + ConsoleColors.RESET);
            }
        }
    }
}
