package lqqd.asur.menus.actividad;

import lqqd.asur.model.Actividad;
import lqqd.asur.model.InscripcionActividad;
import lqqd.asur.model.Usuario;
import lqqd.asur.service.InscripcionActividadService;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActividadMenuActividadesInscriptas {

    @Autowired
    private InscripcionActividadService inscripcionActividadService;

    public void listarActividadesInscriptas(Usuario currentUser) {
        ConsoleColors.limpiarConsola();

        System.out.println(ConsoleColors.CYAN + "=== Actividades Inscriptas ===" + ConsoleColors.RESET);
        List<InscripcionActividad> inscripciones = inscripcionActividadService.listarInscriptosPorUsuario(currentUser.getId());

        if (inscripciones.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "No está inscrito en ninguna actividad." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.YELLOW + "Listado de Actividades en las que está inscrito:");
        System.out.println("===========================================================================");
        System.out.println("|   ID   |      Nombre       |      Tipo       |    Fecha    |  Costo  |");
        System.out.println("===========================================================================");

        inscripciones.forEach(inscripcion -> mostrarActividad(inscripcion.getActividad()));

        System.out.println("===========================================================================" + ConsoleColors.RESET);

        ingresarActividadParaBaja(currentUser);
    }

    private void darseDeBajaDeActividad(Long actividadId, Usuario currentUser) {
        if (actividadId == null) {
            System.out.println(ConsoleColors.RED + "Debe ingresar un ID de actividad válido." + ConsoleColors.RESET);
            return;
        }
        try {
            boolean resultado = inscripcionActividadService.cancelarInscripcion(actividadId, currentUser.getId());
            if (resultado) {
                System.out.println(ConsoleColors.GREEN + "Se ha dado de baja de la actividad con éxito." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "No se pudo realizar la baja. Verifique si está inscrito en esta actividad." + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Ocurrió un error al intentar darse de baja: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public void darseDebajaDeActividadMenu(Usuario currentUser) {
        ingresarActividadParaBaja(currentUser);
    }

    private void ingresarActividadParaBaja(Usuario currentUser) {
        MenuUtils.executeMenuString(
                () -> {
                    System.out.print(ConsoleColors.MAGENTA + "Ingrese el ID de la actividad para darse de baja o presione 0 para salir: " + ConsoleColors.RESET);
                },
                input -> {
                    try {
                        long actividadId = Long.parseLong(input);
                        if (actividadId == 0) {
                            System.out.println(ConsoleColors.YELLOW + "Volviendo al menú principal..." + ConsoleColors.RESET);
                        } else {
                            darseDeBajaDeActividad(actividadId, currentUser);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(ConsoleColors.RED + "Entrada inválida. Intente nuevamente." + ConsoleColors.RESET);
                    }
                }
        );
    }

    private void mostrarActividad(Actividad actividad) {
        System.out.printf(ConsoleColors.YELLOW +
                        "| %-6d | %-15s | %-15s | %-10s | %-7.2f |\n",
                actividad.getId(),
                actividad.getNombre(),
                actividad.getTipoActividad().getNombre(),
                actividad.getFechaInicioConHora().toLocalDate(),
                actividad.getCosto()
        );
    }
}
