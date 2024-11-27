package lqqd.asur.menus.usuario;

import lqqd.asur.menus.helpers.UsuarioAtributoHelper;
import lqqd.asur.model.Usuario;
import lqqd.asur.service.UsuarioService;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMenuModificar {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioAtributoHelper attributeHelper;

    public void modificarUsuario() {
        System.out.println(ConsoleColors.CYAN + "===== Modificar Usuario =====");

        MenuUtils.executeMenu(
                () -> System.out.print("Ingrese el ID del usuario a modificar: "),
                id -> {
                    Long userId = Long.valueOf(id);
                    Usuario usuario = usuarioService.buscarUsuarioPorId(userId).orElse(null);

                    if (usuario == null) {
                        System.out.println(ConsoleColors.RED + "El usuario con ID " + userId + " no existe." + ConsoleColors.RESET);
                        return;
                    } else {
                        System.out.println(ConsoleColors.GREEN + usuario + ConsoleColors.RESET);
                    }

                    editarUsuario(usuario);
                }
        );
    }

    private void editarUsuario(Usuario usuario) {
        MenuUtils.executeMenu(
                () -> {
                    System.out.println(ConsoleColors.CYAN + "Seleccione el atributo a modificar:");
                    System.out.println("1. Nombre");
                    System.out.println("2. Apellido");
                    System.out.println("3. Dirección");
                    System.out.println("4. Rol");
                    System.out.print("Ingrese su opción: ");
                },
                opcion -> {
                    switch (opcion) {
                        case 1 -> attributeHelper.updateName(usuario);
                        case 2 -> attributeHelper.updateLastName(usuario);
                        case 3 -> attributeHelper.updateAddress(usuario);
                        case 4 -> attributeHelper.updateRole(usuario);
                        default -> System.out.println(ConsoleColors.RED + "Opción no válida." + ConsoleColors.RESET);
                    }
                }
        );

        confirmarModificaciones(usuario);
    }

    private void confirmarModificaciones(Usuario usuario) {
        MenuUtils.executeMenu(
                () -> {
                    System.out.println(ConsoleColors.YELLOW + "¿Confirma las modificaciones realizadas?");
                    System.out.println("1. Sí");
                    System.out.println("2. No");
                    System.out.print("Seleccione una opción: ");
                },
                opcion -> {
                    if (opcion == 1) {
                        usuarioService.guardarUsuario(usuario);
                        System.out.println(ConsoleColors.GREEN + "Modificaciones confirmadas y guardadas exitosamente." + ConsoleColors.RESET);
                    } else {
                        System.out.println(ConsoleColors.RED + "Modificaciones canceladas. No se realizaron cambios." + ConsoleColors.RESET);
                    }
                }
        );
    }
}
