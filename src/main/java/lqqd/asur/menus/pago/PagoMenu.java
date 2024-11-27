package lqqd.asur.menus.pago;

import lqqd.asur.model.Pago;
import lqqd.asur.model.Usuario;
import lqqd.asur.model.actividad.FormaPago;
import lqqd.asur.service.PagoService;
import lqqd.asur.service.UsuarioService;
import lqqd.asur.utils.ActividadUtils;
import lqqd.asur.utils.ConsoleColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Component
public class PagoMenu {

    private final Scanner scanner = new Scanner(System.in);
    @Autowired
    private PagoService pagoService;
    @Autowired
    private UsuarioService usuarioService;

    public void displayMenu(Usuario currentUser) {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "===== Gestión de Pagos =====" + ConsoleColors.RESET);
        System.out.println("1. Registrar Pago");
        System.out.println("2. Modificar Pago");
        System.out.println("3. Listar Pagos");
        System.out.println("4. Buscar Pagos por Usuario");
        System.out.println("5. Buscar Pagos por Rango de Fechas");
        System.out.println("0. Salir");
        System.out.print(ConsoleColors.MAGENTA + "Seleccione una opción: " + ConsoleColors.RESET);

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consumir línea extra

        switch (choice) {
            case 1 -> registrarPago();
            case 2 -> modificarPago();
            case 3 -> listarPagos();
            case 4 -> buscarPagosPorUsuario();
            case 5 -> buscarPagosPorFechas();
            case 0 -> System.out.println(ConsoleColors.YELLOW + "Saliendo del menú de pagos..." + ConsoleColors.RESET);
            default -> System.out.println(ConsoleColors.RED + "Opción no válida." + ConsoleColors.RESET);
        }
    }

    private void registrarPago() {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "=== Registrar Pago ===" + ConsoleColors.RESET);

        List<Usuario> usuarios = usuarioService.listarTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No hay usuarios disponibles." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.YELLOW + "Lista de Usuarios:");
        usuarios.forEach(usuario ->
                System.out.printf("ID: %-5d | Nombre: %-20s | Socio: %s%n",
                        usuario.getId(),
                        usuario.getNombres() + " " + usuario.getApellidos(),
                        usuario.isSocio() ? "Sí" : "No")
        );

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID del usuario: " + ConsoleColors.RESET);
        Long usuarioId = scanner.nextLong();
        scanner.nextLine(); // Consumir línea extra

        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        System.out.println("Usuario seleccionado: " + usuario.getNombres() + " " + usuario.getApellidos());

        String concepto = obtenerConceptoPago(usuario);

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el monto: " + ConsoleColors.RESET);
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Consumir línea extra
        FormaPago formaPago = ActividadUtils.solicitarFormaPago(scanner);

        LocalDateTime fechaCobro = LocalDateTime.now();

        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setConcepto(concepto);
        pago.setMonto(monto);
        pago.setFormaDePago(formaPago);
        pago.setFechaPago(fechaCobro);

        pagoService.guardarPago(pago);
        System.out.println(ConsoleColors.GREEN + "Pago registrado con éxito." + ConsoleColors.RESET);
    }

    private void modificarPago() {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "=== Modificar Pago ===" + ConsoleColors.RESET);

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID del pago a modificar: " + ConsoleColors.RESET);
        Long idPago = scanner.nextLong();
        scanner.nextLine(); // Consumir línea extra

        Pago pago = pagoService.buscarPagoPorId(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado."));

        System.out.printf(ConsoleColors.YELLOW + "Pago actual: Usuario: %s | Concepto: %s | Monto: %.2f | Forma de Pago: %s%n",
                pago.getUsuario().getNombres() + " " + pago.getUsuario().getApellidos(),
                pago.getConcepto(), pago.getMonto(), pago.getFormaDePago());

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el nuevo concepto (o deje en blanco para mantener): " + ConsoleColors.RESET);
        String nuevoConcepto = scanner.nextLine();
        if (!nuevoConcepto.isBlank()) {
            pago.setConcepto(nuevoConcepto.trim());
        }

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el nuevo monto (o deje en blanco para mantener): " + ConsoleColors.RESET);
        String nuevoMontoInput = scanner.nextLine();
        if (!nuevoMontoInput.isBlank()) {
            pago.setMonto(Double.parseDouble(nuevoMontoInput.trim()));
        }

        FormaPago formaPago = ActividadUtils.solicitarNuevaFormaPago(scanner);
        if (formaPago != null) {
            pago.setFormaDePago(formaPago);
        }

        System.out.print(ConsoleColors.YELLOW + "¿Desea guardar los cambios realizados? (S/N): " + ConsoleColors.RESET);
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        if ("S".equals(confirmacion)) {
            pagoService.guardarPago(pago);
            System.out.println(ConsoleColors.GREEN + "Pago modificado con éxito." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Los cambios no fueron guardados." + ConsoleColors.RESET);
        }
    }

    private void listarPagos() {
        ConsoleColors.limpiarConsola();
        System.out.println(ConsoleColors.CYAN + "=== Listar Pagos ===" + ConsoleColors.RESET);
        pagoService.listarTodosLosPagos().forEach(pago -> {
            System.out.printf(ConsoleColors.YELLOW + "ID: %-5d | Usuario: %-20s | Concepto: %-15s | Monto: %.2f | Fecha: %s%n",
                    pago.getId(),
                    pago.getUsuario().getNombres() + " " + pago.getUsuario().getApellidos(),
                    pago.getConcepto(),
                    pago.getMonto(),
                    pago.getFechaPago());
        });
    }

    private void buscarPagosPorUsuario() {
        List<Usuario> usuarios = usuarioService.listarTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No hay usuarios disponibles." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.YELLOW + "Lista de Usuarios:");
        usuarios.forEach(usuario ->
                System.out.printf("ID: %-5d | Nombre: %-20s%n",
                        usuario.getId(),
                        usuario.getNombres() + " " + usuario.getApellidos())
        );

        System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID del usuario: " + ConsoleColors.RESET);
        Long usuarioId = scanner.nextLong();
        scanner.nextLine(); // Consumir línea extra

        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        List<Pago> pagos = pagoService.listarPagosPorUsuario(usuario);
        if (pagos.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No se encontraron pagos para el usuario seleccionado." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.YELLOW + "Pagos encontrados:");
        pagos.forEach(pago ->
                System.out.printf("ID: %-5d | Concepto: %-15s | Monto: %.2f | Fecha: %s%n",
                        pago.getId(), pago.getConcepto(), pago.getMonto(), pago.getFechaPago()));
    }

    private void buscarPagosPorFechas() {
        System.out.print(ConsoleColors.MAGENTA + "Ingrese la fecha de inicio (YYYY-MM-DDTHH:MM): " + ConsoleColors.RESET);
        LocalDateTime inicio = LocalDateTime.parse(scanner.nextLine());

        System.out.print(ConsoleColors.MAGENTA + "Ingrese la fecha de fin (YYYY-MM-DDTHH:MM): " + ConsoleColors.RESET);
        LocalDateTime fin = LocalDateTime.parse(scanner.nextLine());

        List<Pago> pagos = pagoService.listarPagosPorFecha(inicio, fin);
        if (pagos.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No se encontraron pagos en el rango de fechas especificado." + ConsoleColors.RESET);
            return;
        }

        pagos.forEach(pago ->
                System.out.printf(ConsoleColors.YELLOW + "ID: %-5d | Usuario: %-20s | Concepto: %-15s | Monto: %.2f | Fecha: %s%n",
                        pago.getId(),
                        pago.getUsuario().getNombres() + " " + pago.getUsuario().getApellidos(),
                        pago.getConcepto(),
                        pago.getMonto(),
                        pago.getFechaPago()));
    }

    private String obtenerConceptoPago(Usuario usuario) {
        if (usuario.isSocio()) {
            System.out.print(ConsoleColors.MAGENTA + "¿Es pago de cuota? (S/N): " + ConsoleColors.RESET);
            String opcionCuota = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(opcionCuota)) {
                return "Cuota";
            }
        }
        System.out.println(ConsoleColors.MAGENTA + "Seleccione el concepto:");
        System.out.println("1. Inscripción a Actividad");
        System.out.println("2. Reserva de Espacios");
        System.out.print("Ingrese su opción: " + ConsoleColors.RESET);

        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir línea extra
        return switch (opcion) {
            case 1 -> "Inscripción a Actividad";
            case 2 -> "Reserva de Espacios";
            default -> throw new RuntimeException("Opción no válida.");
        };
    }
}
