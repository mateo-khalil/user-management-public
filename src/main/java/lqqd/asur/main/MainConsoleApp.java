package lqqd.asur.main;

import lqqd.asur.menus.actividad.ActividadMenu;
import lqqd.asur.menus.helpers.UsuarioModificarMiCuentaHelper;
import lqqd.asur.menus.login.LoginMenu;
import lqqd.asur.menus.pago.PagoMenu;
import lqqd.asur.menus.tipo_actividad.TipoActividadMenu;
import lqqd.asur.menus.usuario.UsuarioMenu;
import lqqd.asur.model.Usuario;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class MainConsoleApp implements CommandLineRunner {

    @Autowired
    private LoginMenu loginMenu;
    @Autowired
    private UsuarioMenu usuarioMenu;
    @Autowired
    private ActividadMenu actividadMenu;
    @Autowired
    private TipoActividadMenu tipoActividadMenu;
    @Autowired
    private PagoMenu pagoMenu;
    @Autowired
    private UsuarioModificarMiCuentaHelper UsuarioModificarMiCuentaHelper;

    @Override
    public void run(String... args) {
        while (true) {
            if (!loginMenu.isLoggedIn()) {
                loginMenu.displayLoginMenu();
            } else {
                displayMainMenu(LoginMenu.getCurrentUser());
            }
        }
    }

    private void displayMainMenu(Usuario currentUser) {
        MenuUtils.executeMenu(
                () -> {
                    System.out.println(ConsoleColors.CYAN + "===== Menú Principal =====");
                    System.out.println("1. Gestión de Usuarios");
                    System.out.println("2. Gestión de Actividades");
                    System.out.println("3. Gestión de Tipos de Actividades");
                    System.out.println("4. Gestión de Pagos");
                    System.out.println("5. Modificar mi cuenta");
                    System.out.println("6. Cerrar sesión");
                    System.out.print("Seleccione una opción: ");
                },
                choice -> {
                    switch (choice) {
                        case 1 -> usuarioMenu.displayMenu(currentUser);
                        case 2 -> actividadMenu.displayMenu(currentUser);
                        case 3 -> tipoActividadMenu.displayMenu(currentUser);
                        case 4 -> pagoMenu.displayMenu(currentUser);
                        case 5 -> UsuarioModificarMiCuentaHelper.modificarMiCuenta(currentUser);
                        case 6 -> loginMenu.logout();
                        default ->
                                System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                    }
                }
        );
    }
}
