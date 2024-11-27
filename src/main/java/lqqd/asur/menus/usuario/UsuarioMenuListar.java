package lqqd.asur.menus.usuario;

import lqqd.asur.model.Usuario;
import lqqd.asur.model.usuario.Estado;
import lqqd.asur.model.usuario.Role;
import lqqd.asur.service.UsuarioService;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UsuarioMenuListar {

    @Autowired
    private UsuarioService usuarioService;

    public void listarUsuariosConFiltros() {
        ConsoleColors.limpiarConsola();

        System.out.println(ConsoleColors.YELLOW + "Usuarios Registrados:");
        System.out.println("===============================================================================");
        System.out.println("|   ID   |   Nombres   | Apellidos |     Email     |     Rol     |  Estado  |");
        System.out.println("===============================================================================");

        usuarioService.listarUsuarios().forEach(this::mostrarUsuario);

        System.out.println("===============================================================================" + ConsoleColors.RESET);

        MenuUtils.executeMenuString(
                () -> {
                    System.out.print(ConsoleColors.MAGENTA + "Filtros (E: Estado, N: Nombres/Apellidos, D: Documento, R: Rol)");
                    System.out.print(ConsoleColors.MAGENTA + " | Ingrese el ID del usuario para ver detalles: " + ConsoleColors.RESET);
                },
                input -> {
                    try {
                        // Attempt to parse the input as a number
                        Long userId = Long.parseLong(input);
                        mostrarUsuarioPorId(userId);
                    } catch (NumberFormatException e) {
                        // If not a number, treat as a filter option
                        switch (input.toUpperCase()) {
                            case "E" -> listarPorEstado();
                            case "N" -> listarPorNombresApellidos();
                            case "D" -> listarPorDocumento();
                            case "R" -> listarPorRol();
                            case "0" -> {
                                System.out.println(ConsoleColors.YELLOW + "Saliendo del menú de filtros..." + ConsoleColors.RESET);
                            }
                            default ->
                                    System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                        }
                    }
                }
        );
    }

    private void mostrarUsuarioPorId(Long userId) {
        var usuario = usuarioService.buscarUsuarioPorId(userId);
        if (usuario.isPresent()) {
            System.out.println(ConsoleColors.GREEN + "Detalles del Usuario:");
            System.out.println("===============================================================================");
            mostrarUsuario(usuario.get());
            System.out.println("===============================================================================" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "El usuario con ID " + userId + " no existe." + ConsoleColors.RESET);
        }
    }

    private void listarPorEstado() {
        MenuUtils.executeMenu(
                () -> {
                    System.out.println(ConsoleColors.CYAN + "Seleccione el estado:");
                    System.out.println("1. Validado");
                    System.out.println("2. Sin Validar");
                    System.out.println("3. Inactivo");
                    System.out.print("Ingrese su opción: ");
                },
                estadoOpcion -> {
                    Estado estado = switch (estadoOpcion) {
                        case 1 -> Estado.VALIDADO;
                        case 2 -> Estado.SIN_VALIDAR;
                        case 3 -> Estado.INACTIVO;
                        default -> null;
                    };
                    if (estado != null) {
                        usuarioService.listarUsuariosPorEstado(estado).forEach(usuario -> {
                            System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD + usuario + ConsoleColors.RESET);
                        });
                    } else {
                        System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                    }
                }
        );
    }

    private void listarPorNombresApellidos() {
        System.out.println("Ingrese parte del nombre o apellido: ");
        String criterio = new Scanner(System.in).nextLine();
        usuarioService.listarUsuariosPorNombresApellidos(criterio).forEach(usuario -> {
            System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD + usuario + ConsoleColors.RESET);
        });
    }

    private void listarPorDocumento() {
        System.out.println("Ingrese el documento: ");
        Long documento = new Scanner(System.in).nextLong();
        var usuario = usuarioService.buscarUsuarioPorDocumento(documento);
        System.out.println(usuario != null ? usuario : "No se encontró un usuario con ese documento.");
    }

    private void listarPorRol() {
        MenuUtils.executeMenu(
                () -> {
                    System.out.println("Seleccione el rol:");
                    System.out.println("1. Socio");
                    System.out.println("2. No Socio");
                    System.out.println("3. Auxiliar Administrativo");
                    System.out.print("Ingrese su opción: ");
                },
                rolOpcion -> {
                    Role rol = switch (rolOpcion) {
                        case 1 -> Role.SOCIO;
                        case 2 -> Role.NO_SOCIO;
                        case 3 -> Role.AUXILIAR_ADMINISTRATIVO;
                        default -> null;
                    };
                    if (rol != null) {
                        usuarioService.listarUsuariosPorRol(rol).forEach(usuario -> {
                            System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD + usuario + ConsoleColors.RESET);
                        });
                    } else {
                        System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                    }
                }
        );
    }

    private void mostrarUsuario(Usuario usuario) {
        System.out.printf(
                "| %-6d | %-11s | %-9s | %-12s | %-10s | %-8s |\n",
                usuario.getId(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getEstado()
        );
    }

}
