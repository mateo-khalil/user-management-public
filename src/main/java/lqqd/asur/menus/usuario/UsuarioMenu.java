package lqqd.asur.menus.usuario;

import lqqd.asur.model.Usuario;
import lqqd.asur.model.usuario.Estado;
import lqqd.asur.service.UsuarioService;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMenu {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioMenuListar usuarioMenuListar;
    @Autowired
    private UsuarioMenuModificar usuarioMenuModificar;

    public void displayMenu(Usuario currentUser) {
        MenuUtils.executeMenu(
                () -> {
                    ConsoleColors.limpiarConsola();
                    System.out.println(ConsoleColors.CYAN + "===== Gestión de Usuarios =====");
                    System.out.println("1. Listar Usuarios");
                    System.out.println("2. Modificar Usuario");
                    System.out.println("3. Baja De Usuario");
                    System.out.println("4. Volver al menú principal");
                    System.out.print("Seleccione una opción: ");
                },
                choice -> {
                    switch (choice) {
                        case 1 -> {
                            if (hasAccessToListadoUsuarios(currentUser)) {
                                usuarioMenuListar.listarUsuariosConFiltros();
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 2 -> {
                            if (hasAccessToModificarUsuario(currentUser)) {
                                usuarioMenuModificar.modificarUsuario();
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 3 -> {
                            if (hasAccessToModificarUsuario(currentUser)) {
                                bajaUsuario(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 4 -> {
                            System.out.println(ConsoleColors.YELLOW + "Volviendo al menú principal..." + ConsoleColors.RESET);
                        }
                        default ->
                                System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                    }
                }
        );
    }

    private boolean hasAccessToListadoUsuarios(Usuario user) {
        return user.getRole().isAdminOrAuxiliar();
    }

    private boolean hasAccessToModificarUsuario(Usuario user) {
        return user.getRole().isAdminOrAuxiliar();
    }

    private void bajaUsuario(Usuario currentUser) {
        MenuUtils.executeMenu(
                () -> System.out.println("Ingrese ID del usuario a desactivar (0 para salir): "),
                id -> {
                    Long userId = Long.valueOf(id);

                    if (userId == 0) {
                        System.out.println(ConsoleColors.YELLOW + "Operación cancelada. No se modificó ningún usuario." + ConsoleColors.RESET);
                        return;
                    }

                    if (userId.equals(currentUser.getId())) {
                        System.out.println(ConsoleColors.RED + "No se puede desactivar el usuario actual." + ConsoleColors.RESET);
                    } else {
                        var usuario = usuarioService.buscarUsuarioPorId(userId);
                        if (usuario.isEmpty()) {
                            System.out.println(ConsoleColors.YELLOW + "El usuario con ID " + userId + " no existe." + ConsoleColors.RESET);
                        } else {
                            usuario.get().setEstado(Estado.INACTIVO);
                            usuarioService.guardarUsuario(usuario.get());
                            System.out.println(ConsoleColors.GREEN + "Usuario desactivado con éxito." + ConsoleColors.RESET);
                        }
                    }
                }
        );
    }

}
