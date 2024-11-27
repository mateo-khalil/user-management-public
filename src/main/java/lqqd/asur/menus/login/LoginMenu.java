package lqqd.asur.menus.login;

import lqqd.asur.model.Usuario;
import lqqd.asur.model.usuario.Estado;
import lqqd.asur.model.usuario.Role;
import lqqd.asur.model.usuario.TipoDocumento;
import lqqd.asur.service.UsuarioService;
import lqqd.asur.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
public class LoginMenu {

    private static Usuario currentUser = null;
    private final Scanner scanner = new Scanner(System.in);
    ValidatorCI validatorCi = new ValidatorCI();
    ValidatorEmail validatorEmail = new ValidatorEmail();
    ValidatorPassword validatorPassword = new ValidatorPassword();
    ValidatorFechaNacimiento validatorFechaNacimiento = new ValidatorFechaNacimiento();
    ValidatorTelefono validatorTelefono = new ValidatorTelefono();
    @Autowired
    private UsuarioService usuarioService;

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public void displayLoginMenu() {
        while (true) {
            try {
                System.out.println(ConsoleColors.CYAN + "===== ASUR Página de Login =====");
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Registrarse, nuevo usuario");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");

                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> {
                        login();
                        return;
                    }
                    case 2 -> {
                        registroLoginPage();
                        return;
                    }
                    case 3 -> {
                        exit();
                        return;
                    }
                    default -> System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número entre 1 y 3.");
            }
        }
    }

    private void login() {
        System.out.print("Ingrese su correo electrónico: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        currentUser = usuarioService.login(email, password);
        if (currentUser != null) {
            System.out.println(ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLACK + "Bienvenido:" + currentUser.getNombres() + " " + currentUser.getApellidos() + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Inicio de sesión fallido. Por favor, intente de nuevo.");
        }
    }

    private void registroLoginPage() {
        Usuario usuario = new Usuario();
        System.out.print("Ingrese nombres: ");
        String nombres = scanner.nextLine();
        System.out.print("Ingrese apellidos: ");
        String apellidos = scanner.nextLine();
        System.out.print("Ingrese dirección del Usuario: ");
        String direccion = scanner.nextLine();

        String email;
        boolean emailExiste;
        do {
            emailExiste = false;
            System.out.print("Ingrese email: ");
            email = scanner.nextLine();
            if (!validatorEmail.validateEmail(email)) {
                System.out.println("Email inválido. Por favor, ingrese un email válido.");
            } else if (usuarioService.existeEmail(email)) {
                emailExiste = true;
                System.out.println("Email ya registrado. Por favor, ingrese un email diferente.");
            }
        } while (!validatorEmail.validateEmail(email) || emailExiste);

        String password;
        do {
            System.out.print("Ingrese contraseña, al menos 8 caracteres con al menos una letra y un número: ");
            password = scanner.nextLine();
            if (!validatorPassword.validatePassword(password)) {
                System.out.println("Contraseña inválida. Por favor, ingrese al menos 8 caracteres con al menos una letra y un número.");
            }
        } while (!validatorPassword.validatePassword(password));

        LocalDate fechaNacimiento = null;
        do {
            System.out.print("Ingrese fecha de nacimiento (dd/MM/yyyy): ");
            String input = scanner.nextLine();
            if (validatorFechaNacimiento.validateFechaNacimiento(input)) {
                try {
                    fechaNacimiento = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de fecha inválido. Intente nuevamente.");
                }
            } else {
                System.out.println("Fecha inválida. Por favor, ingrese una fecha válida en formato dd/MM/yyyy.");
            }
        } while (fechaNacimiento == null);

        String telefono1;
        do {
            System.out.print("Ingrese teléfono: ");
            telefono1 = scanner.nextLine();
            if (!validatorTelefono.isTelefonoValid(telefono1)) {
                System.out.println("Teléfono inválido. Por favor, ingrese un teléfono válido.");
            }
        } while (!validatorTelefono.isTelefonoValid(telefono1));

        String telefono2 = null;
        boolean telefonoAdicional = promptBoolean("¿Desea ingresar un teléfono adicional? (0: No, 1: Sí): ");
        if (telefonoAdicional) {
            do {
                System.out.print("Ingrese teléfono adicional: ");
                telefono2 = scanner.nextLine();
                if (!validatorTelefono.isTelefonoValid(telefono2)) {
                    System.out.println("Teléfono adicional inválido. Por favor, ingrese un teléfono válido.");
                }
            } while (!validatorTelefono.isTelefonoValid(telefono2));
        }

        TipoDocumento tipoDocumento = null;
        do {
            try {
                System.out.println("Seleccione el tipo de documento:");
                System.out.println("1: CÉDULA");
                System.out.println("2: PASAPORTE");
                System.out.println("3: OTRO");
                System.out.print("Opción: ");

                String input = scanner.nextLine();
                int tipoDocChoice = Integer.parseInt(input);

                tipoDocumento = switch (tipoDocChoice) {
                    case 1 -> TipoDocumento.CI;
                    case 2 -> TipoDocumento.PASAPORTE;
                    case 3 -> TipoDocumento.OTRO;
                    default -> {
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                        yield null;
                    }
                };
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        } while (tipoDocumento == null);

        Long documento = null;
        if (tipoDocumento == TipoDocumento.CI) {
            do {
                System.out.print("Ingrese documento (cédula): ");
                try {

                    String input = scanner.nextLine();
                    documento = Long.parseLong(input);

                    if (!validatorCi.validateCi(Long.toString(documento))) {
                        System.out.println("Documento inválido. Por favor, ingrese un documento válido.");
                        documento = null; // Resetea el valor para que el bucle continue
                    } else if (usuarioService.existeDocumento(documento)) {
                        System.out.println("Documento ya registrado. Por favor, ingrese un documento diferente.");
                        documento = null; // Resetea el valor para que el bucle continue
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada no válida. Por favor, ingrese solo números.");
                }
            } while (documento == null);
        } else {
            do {
                System.out.print("Ingrese número de documento: ");
                try {
                    String input = scanner.nextLine();
                    documento = Long.parseLong(input);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada no válida. Por favor, ingrese solo números.");
                }
            } while (documento == null);
        }

        Role role = null;
        do {
            System.out.print("Ingrese rol (1: ADMINISTRADOR, 2: AUXILIAR_ADMINISTRATIVO, 3: SOCIO, 4: NO_SOCIO): ");
            try {
                String input = scanner.nextLine();
                int roleChoice = Integer.parseInt(input);

                role = switch (roleChoice) {
                    case 1 -> Role.ADMINISTRADOR;
                    case 2 -> Role.AUXILIAR_ADMINISTRATIVO;
                    case 3 -> Role.SOCIO;
                    case 4 -> Role.NO_SOCIO;
                    default -> {
                        System.out.println("Opción no válida. Por favor, ingrese un número entre 1 y 4.");
                        yield null;
                    }
                };
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número entre 1 y 4.");
            }
        } while (role == null);

        if (role == Role.SOCIO) {
            collectSocioDetails(usuario);
        }

        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setDomicilio(direccion);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setDocumento(documento);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setRole(role);
        usuario.setEstado(Estado.INACTIVO);
        usuario.setTelefono1(telefono1);
        usuario.setTelefono2(telefono2);

        usuarioService.guardarUsuario(usuario);
        System.out.println(ConsoleColors.YELLOW + "Usuario registrado exitosamente. Por favor, espere a que un administrador active su cuenta.");
    }

    public void logout() {
        currentUser = null;
        System.out.println("Sesión cerrada.");
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    private void exit() {
        System.out.println("Gracias por usar el sistema. ¡Hasta pronto!");
        System.exit(0);
    }

    private void collectSocioDetails(Usuario usuario) {
        System.out.print("Ingrese categoría de Socio: ");
        String categoriaSocio = scanner.nextLine();
        usuario.setCategoriaSocio(categoriaSocio);

        usuario.setDificultadAuditiva(promptBoolean("¿Tiene dificultad auditiva? (0: No, 1: Sí): "));
        usuario.setManejaLenguajeSenias(promptBoolean("¿Maneja el uso de lenguaje de señas? (0: No, 1: Sí): "));
        usuario.setParticipaSubcomision(promptBoolean("¿Participa en alguna subcomisión? (0: No, 1: Sí): "));

        if (usuario.getParticipaSubcomision()) {
            System.out.print("Describa o seleccione cual subcomisión: ");
            String descripcionSubcomision = scanner.nextLine();
            usuario.setDescripcionSubcomision(descripcionSubcomision);
        }
    }

    private Boolean promptBoolean(String prompt) {
        Boolean result = null;
        do {
            System.out.print(prompt);
            String choice = scanner.nextLine();
            switch (choice) {
                case "0" -> result = false;
                case "1" -> result = true;
                default -> System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (result == null);
        return result;
    }

}

