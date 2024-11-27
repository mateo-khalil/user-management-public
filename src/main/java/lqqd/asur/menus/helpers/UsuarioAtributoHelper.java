package lqqd.asur.menus.helpers;

import lqqd.asur.model.Usuario;
import lqqd.asur.model.usuario.Role;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UsuarioAtributoHelper {

    private final Scanner scanner = new Scanner(System.in);

    public void updateName(Usuario usuario) {
        System.out.print("Ingrese el nuevo nombre del usuario: ");
        String nuevoNombre = scanner.nextLine();
        usuario.setNombres(nuevoNombre);
    }

    public void updateLastName(Usuario usuario) {
        System.out.print("Ingrese el nuevo apellido del usuario: ");
        String nuevoApellido = scanner.nextLine();
        usuario.setApellidos(nuevoApellido);
    }

    public void updateAddress(Usuario usuario) {
        System.out.print("Ingrese la nueva dirección del usuario: ");
        String nuevaDireccion = scanner.nextLine();
        usuario.setDomicilio(nuevaDireccion);
    }

    public void updateRole(Usuario usuario) {
        MenuUtils.executeMenu(
                () -> {
                    System.out.println("Seleccione el nuevo tipo de usuario:");
                    System.out.println("1. Socio");
                    System.out.println("2. No Socio");
                    System.out.println("3. Auxiliar Administrativo");
                    System.out.print("Ingrese su opción: ");
                },
                opcion -> {
                    Role nuevoRole = switch (opcion) {
                        case 1 -> Role.SOCIO;
                        case 2 -> Role.NO_SOCIO;
                        case 3 -> Role.AUXILIAR_ADMINISTRATIVO;
                        default -> null;
                    };
                    if (nuevoRole != null && nuevoRole != usuario.getRole()) {
                        usuario.setRole(nuevoRole);
                        notifyRoleChange(usuario, nuevoRole);
                    } else {
                        System.out.println(ConsoleColors.YELLOW + "No se realizaron cambios en el rol." + ConsoleColors.RESET);
                    }
                }
        );
    }

    private void notifyRoleChange(Usuario usuario, Role nuevoRole) {
        System.out.println(ConsoleColors.BLUE + "Notificación enviada al correo: " + usuario.getEmail() + ConsoleColors.RESET);
        if (nuevoRole == Role.SOCIO) {
            System.out.println("Se le cobrará la cuota de socio a partir del próximo mes.");
        } else if (nuevoRole == Role.NO_SOCIO) {
            System.out.println("Se dejará de cobrar la cuota de socio a partir del próximo mes.");
        }
    }
}

