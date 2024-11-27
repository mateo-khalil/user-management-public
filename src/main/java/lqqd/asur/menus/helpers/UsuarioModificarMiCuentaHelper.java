package lqqd.asur.menus.helpers;

import lqqd.asur.model.Usuario;
import lqqd.asur.service.UsuarioService;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import lqqd.asur.utils.ValidatorPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class UsuarioModificarMiCuentaHelper {
    private final Scanner scanner = new Scanner(System.in);

    ValidatorPassword validatorPassword = new ValidatorPassword();

    @Autowired
    private UsuarioService usuarioService;

    public void modificarMiCuenta(Usuario usuario) {
        System.out.println(ConsoleColors.YELLOW + "===== Modificar Mi Cuenta =====");
        AtomicBoolean continuar = new AtomicBoolean(true);
        while (continuar.get()) {
            MenuUtils.executeMenu(
                    () -> {
                        ConsoleColors.limpiarConsola();
                        System.out.println(ConsoleColors.YELLOW + "Seleccione el dato a modificar:");
                        System.out.println("1. Nombres");
                        System.out.println("2. Apellidos");
                        System.out.println("3. Contraseña");
                        System.out.println("4. Dirección");
                        System.out.println("5. Teléfono Principal");
                        System.out.println("6. Teléfono Secundario");
                        System.out.println("0. Cancelar");
                        System.out.print("Seleccione una opción: ");
                    },
                    option -> {
                        switch (option) {
                            case 1 -> modificarNombres(usuario);
                            case 2 -> modificarApellidos(usuario);
                            case 3 -> modificarPassword(usuario);
                            case 4 -> modificarDireccion(usuario);
                            case 5 -> modificarTelefonoPrincipal(usuario);
                            case 6 -> modificarTelefonoSecundario(usuario);
                            case 0 -> {
                                System.out.println(ConsoleColors.YELLOW + "Cancelando modificaciones..." + ConsoleColors.RESET);
                                continuar.set(false);
                            }
                            default ->
                                    System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                        }
                    }
            );
        }
    }

    private void modificarNombres(Usuario usuario) {
        MenuUtils.executeMenuString(
                () -> System.out.print(ConsoleColors.YELLOW + "Ingrese su nuevo nombre: "),
                input -> {
                    usuario.setNombres(input);
                    guardarCambios(usuario, "Nombres actualizados correctamente.");
                }
        );
    }

    private void modificarApellidos(Usuario usuario) {
        MenuUtils.executeMenuString(
                () -> System.out.print(ConsoleColors.YELLOW + "Ingrese su nuevo apellido: "),
                input -> {
                    usuario.setApellidos(input);
                    guardarCambios(usuario, "Apellidos actualizados correctamente.");
                }
        );
    }

    private void modificarPassword(Usuario usuario) {
        String password;
        do {
            System.out.print(ConsoleColors.YELLOW + "Ingrese contraseña, al menos 8 caracteres con al menos una letra y un número: ");
            password = scanner.nextLine().trim();
            if (!validatorPassword.validatePassword(password)) {
                System.out.println(ConsoleColors.RED + "Contraseña inválida. Por favor, ingrese al menos 8 caracteres con al menos una letra y un número." + ConsoleColors.RESET);
            } else if (usuario.getPassword().equals(password)) {
                System.out.println(ConsoleColors.RED + "La nueva contraseña no puede ser igual a la actual." + ConsoleColors.RESET);
            }
        } while (!validatorPassword.validatePassword(password));
        usuario.setPassword(password);
        guardarCambios(usuario, "Contraseña actualizada correctamente.");
    }

    private void modificarDireccion(Usuario usuario) {
        MenuUtils.executeMenuString(
                () -> System.out.print(ConsoleColors.YELLOW + "Ingrese su nueva dirección: "),
                input -> {
                    usuario.setDomicilio(input);
                    guardarCambios(usuario, "Dirección actualizada correctamente.");
                }
        );
    }

    private void modificarTelefonoPrincipal(Usuario usuario) {
        MenuUtils.executeMenuString(
                () -> System.out.print(ConsoleColors.YELLOW + "Ingrese su nuevo teléfono principal: "),
                input -> {
                    usuario.setTelefono1(input);
                    guardarCambios(usuario, "Teléfono principal actualizado correctamente.");
                }
        );
    }

    private void modificarTelefonoSecundario(Usuario usuario) {
        MenuUtils.executeMenuString(
                () -> System.out.print(ConsoleColors.YELLOW + "Ingrese su nuevo teléfono secundario: "),
                input -> {
                    usuario.setTelefono2(input);
                    guardarCambios(usuario, "Teléfono secundario actualizado correctamente.");
                }
        );
    }

    private void guardarCambios(Usuario usuario, String mensajeExito) {
        usuarioService.guardarUsuario(usuario);
        System.out.println(ConsoleColors.GREEN + mensajeExito + ConsoleColors.RESET);
    }
}
