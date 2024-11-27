package lqqd.asur.menus.tipo_actividad;

import lqqd.asur.model.TipoActividad;
import lqqd.asur.model.Usuario;
import lqqd.asur.service.TipoActividadService;
import lqqd.asur.utils.ConsoleColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class TipoActividadMenu {

    private final Scanner scanner = new Scanner(System.in);
    @Autowired
    private TipoActividadService tipoActividadService;

    public void displayMenu(Usuario currentUser) {
        ConsoleColors.limpiarConsola();
        if (!currentUser.getRole().isAdminOrAuxiliar()) {
            System.out.println(ConsoleColors.RED + "No tiene permisos para gestionar tipos de actividad." + ConsoleColors.RESET);
            return;
        }
        System.out.println(ConsoleColors.CYAN + "===== Gestión de Tipos de Actividad =====");
        System.out.println("1. Registrar Tipo de Actividad");
        System.out.println("2. Listar Tipos de Actividad");
        System.out.println("3. Eliminar Tipo de Actividad (Baja lógica)");
        System.out.println("4. Modificar Tipo de Actividad");
        System.out.println("0. Salir");
        System.out.print(ConsoleColors.MAGENTA + "Seleccione una opción: " + ConsoleColors.RESET);

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir línea extra

            switch (choice) {
                case 1 -> registrarTipoActividad();
                case 2 -> listarTiposDeActividad();
                case 3 -> eliminarTipoActividad();
                case 4 -> modificarTipoActividad();
                case 0 ->
                        System.out.println(ConsoleColors.YELLOW + "Saliendo del menú de Tipos de Actividad..." + ConsoleColors.RESET);
                default -> System.out.println(ConsoleColors.RED + "Opción no válida." + ConsoleColors.RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número." + ConsoleColors.RESET);
            scanner.nextLine(); // Limpiar el buffer
        }
    }

    private void registrarTipoActividad() {
        try {
            ConsoleColors.limpiarConsola();
            System.out.println(ConsoleColors.CYAN + "=== Registrar Tipo de Actividad ===" + ConsoleColors.RESET);

            System.out.print(ConsoleColors.MAGENTA + "Ingrese el nombre del tipo de actividad: " + ConsoleColors.RESET);
            String nombre = scanner.nextLine();

            System.out.print(ConsoleColors.MAGENTA + "Ingrese la descripción: " + ConsoleColors.RESET);
            String descripcion = scanner.nextLine();

            if (nombre.isBlank() || descripcion.isBlank()) {
                System.out.println(ConsoleColors.RED + "El nombre y la descripción son obligatorios." + ConsoleColors.RESET);
                return;
            }

            TipoActividad tipoActividad = new TipoActividad();
            tipoActividad.setNombre(nombre.trim());
            tipoActividad.setDescripcion(descripcion.trim());
            tipoActividad.setActivo(true);

            tipoActividadService.guardarTipoActividad(tipoActividad);
            System.out.println(ConsoleColors.GREEN + "Tipo de actividad registrado con éxito." + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Ocurrió un error al registrar el tipo de actividad: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void listarTiposDeActividad() {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "=== Listar Tipos de Actividad ===" + ConsoleColors.RESET);

        var tipos = tipoActividadService.listarTiposDeActividad();
        if (tipos.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "No hay tipos de actividad registrados." + ConsoleColors.RESET);
            return;
        }

        tipos.forEach(tipo -> {
            System.out.printf(ConsoleColors.YELLOW + "ID: %-5d | Nombre: %-20s | Estado: %s%n",
                    tipo.getId(),
                    tipo.getNombre(),
                    tipo.getActivo() ? "Activo" : "Inactivo");
        });
    }

    private void eliminarTipoActividad() {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "=== Eliminar Tipo de Actividad ===" + ConsoleColors.RESET);

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID del tipo de actividad a eliminar: " + ConsoleColors.RESET);
        try {
            Long id = scanner.nextLong();
            scanner.nextLine(); // Consumir línea extra

            var tipo = tipoActividadService.buscarTipoActividadPorId(id);
            if (tipo.isEmpty()) {
                System.out.println(ConsoleColors.RED + "No se encontró un tipo de actividad con el ID proporcionado." + ConsoleColors.RESET);
                return;
            }

            tipoActividadService.eliminarTipoActividad(id);
            System.out.println(ConsoleColors.GREEN + "Tipo de actividad eliminado (baja lógica) con éxito." + ConsoleColors.RESET);
        } catch (InputMismatchException e) {
            System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
            scanner.nextLine(); // Limpiar el buffer
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Ocurrió un error al eliminar el tipo de actividad: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void modificarTipoActividad() {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "=== Modificar Tipo de Actividad ===" + ConsoleColors.RESET);

        try {
            System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID del tipo de actividad a modificar: " + ConsoleColors.RESET);
            Long id = scanner.nextLong();
            scanner.nextLine(); // Consumir línea extra

            var tipoOpt = tipoActividadService.buscarTipoActividadPorId(id);
            if (tipoOpt.isEmpty()) {
                System.out.println(ConsoleColors.RED + "No se encontró un tipo de actividad con el ID proporcionado." + ConsoleColors.RESET);
                return;
            }

            TipoActividad tipoActividad = tipoOpt.get();
            System.out.printf(ConsoleColors.YELLOW + "Tipo de Actividad Actual: %s%n", tipoActividad.getDescripcion());

            System.out.print(ConsoleColors.MAGENTA + "Ingrese la nueva descripción (deje en blanco para mantener): " + ConsoleColors.RESET);
            String nuevaDescripcion = scanner.nextLine();
            if (!nuevaDescripcion.isBlank()) {
                tipoActividad.setDescripcion(nuevaDescripcion.trim());
            }

            System.out.print(ConsoleColors.YELLOW + "¿Desea guardar los cambios? (S/N): " + ConsoleColors.RESET);
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(confirmacion)) {
                tipoActividadService.guardarTipoActividad(tipoActividad);
                System.out.println(ConsoleColors.GREEN + "El tipo de actividad se modificó con éxito." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Los cambios no fueron guardados." + ConsoleColors.RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
            scanner.nextLine(); // Limpiar el buffer
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Ocurrió un error al modificar el tipo de actividad: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
