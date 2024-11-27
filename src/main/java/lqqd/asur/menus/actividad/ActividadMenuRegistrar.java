package lqqd.asur.menus.actividad;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.TipoActividad;
import lqqd.asur.model.Usuario;
import lqqd.asur.model.actividad.FormaPago;
import lqqd.asur.service.ActividadService;
import lqqd.asur.service.EspacioService;
import lqqd.asur.service.TipoActividadService;
import lqqd.asur.utils.ActividadUtils;
import lqqd.asur.utils.ConsoleColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class ActividadMenuRegistrar {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private EspacioService espacioService;

    @Autowired
    private TipoActividadService tipoActividadService;

    public void registrarActividad(Usuario currentUser) {
        ConsoleColors.limpiarConsola();

        if (!currentUser.getRole().isAdminOrAuxiliar()) {
            System.out.println(ConsoleColors.RED + "No tiene permisos para registrar actividades." + ConsoleColors.RESET);
            return;
        }
        System.out.println(ConsoleColors.ORANGE + "===== Ingreso de Actividad =====");
        System.out.print(ConsoleColors.ORANGE + "Ingrese el nombre de la actividad: ");
        String nombre = scanner.nextLine();
        System.out.print(ConsoleColors.ORANGE + "Ingrese la descripción: ");
        String descripcion = scanner.nextLine();

        TipoActividad tipoActividad = solicitarTipoActividad();

        LocalDateTime fechaHora = ActividadUtils.solicitarFechaYHora();
        System.out.println("Fecha y hora ingresada correctamente: " + fechaHora);

        Duration duracion = ActividadUtils.solicitarDuracion();
        System.out.println("Duración ingresada correctamente: " + duracion.toHours() + " horas y " + (duracion.toMinutes() % 60) + " minutos.");

        LocalDateTime horaFin = actividadService.calcularHoraFin(fechaHora, duracion);

        boolean requiereInscripcion = ActividadUtils.solicitarRequiereInscripcion(scanner);

        var espaciosDisponibles = espacioService.obtenerEspaciosDisponibles(fechaHora, horaFin);
        if (espaciosDisponibles.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No hay espacios disponibles." + ConsoleColors.RESET);
            return;
        }

        espaciosDisponibles.forEach(espacio -> {
            System.out.println("ID: " + espacio.getId() + " | Nombre: " + espacio.getNombre());
        });

        Long espacioId = ActividadUtils.solicitarEspacio(espaciosDisponibles, scanner);
        Double costo = ActividadUtils.solicitarCosto(scanner);
        FormaPago formaPago = ActividadUtils.solicitarFormaPago(scanner);

        System.out.print(ConsoleColors.ORANGE + "Ingrese una observación (opcional): ");
        String observaciones = scanner.nextLine();

        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setTipoActividad(tipoActividad);
        actividad.setFechaInicioConHora(fechaHora);
        actividad.setFechaFinConHora(horaFin);
        actividad.setRequiereInscripcion(requiereInscripcion);
        actividad.setCosto(costo);
        actividad.setFormaPago(formaPago);
        actividad.setObservaciones(observaciones);
        actividad.setLugar(espacioService.obtenerEspacioPorId(espacioId)
                .orElseThrow(() -> new RuntimeException("El espacio con ID " + espacioId + " no existe")));

        actividadService.guardarActividad(actividad);
        System.out.println(ConsoleColors.GREEN + "Actividad registrada con éxito." + ConsoleColors.RESET);
    }

    private TipoActividad solicitarTipoActividad() {
        List<TipoActividad> tiposDeActividad = tipoActividadService.listarTiposDeActividadActivos();

        if (tiposDeActividad.isEmpty()) {
            throw new RuntimeException("No hay tipos de actividad configurados en el sistema.");
        }

        System.out.println(ConsoleColors.CYAN + "Seleccione el tipo de actividad:");
        tiposDeActividad.forEach(tipo -> {
            System.out.printf("%d - %s%n", tipo.getId(), tipo.getNombre());
        });

        while (true) {
            try {
                System.out.print("Ingrese el ID del tipo de actividad: ");
                Long id = scanner.nextLong();
                scanner.nextLine(); // Limpiar el buffer
                return tiposDeActividad.stream()
                        .filter(tipo -> tipo.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("ID no válido. Intente nuevamente."));
            } catch (InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
                scanner.nextLine(); // Limpiar el buffer en caso de error
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColors.RED + e.getMessage() + ConsoleColors.RESET);
            }
        }
    }
}
