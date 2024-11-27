package lqqd.asur.menus.actividad;

import lqqd.asur.model.Usuario;
import lqqd.asur.utils.ConsoleColors;
import lqqd.asur.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActividadMenu {

    @Autowired
    private ActividadMenuRegistrar actividadRegistrar;

    @Autowired
    private ActividadMenuListar actividadListar;

    @Autowired
    private ActividadMenuModificar actividadModificar;

    @Autowired
    private ActividadMenuBaja actividadMenuBaja;

    @Autowired
    private ActividadMenuActividadesInscribirse actividadMenuInscribirse;

    @Autowired
    private ActividadMenuActividadesInscriptas actividadMenuActividadesInscriptas;

    @Autowired
    private ActividadMenuReporte actividadMenuReporte;

    public void displayMenu(Usuario currentUser) {
        MenuUtils.executeMenu(
                () -> {
                    ConsoleColors.limpiarConsola();
                    System.out.println(ConsoleColors.CYAN + "===== Gestión de Actividades =====");

                    if (hasAccessToIngresoActividad(currentUser)) {
                        System.out.println("1. Ingreso de Actividad");
                    }
                    if (hasAccessToListarActividades(currentUser)) {
                        System.out.println("2. Listar Todas las Actividades");
                    }
                    if (hasAccessToModificarActividad(currentUser)) {
                        System.out.println("3. Modificar Actividad");
                    }
                    if (hasAccessToDarBajaActividad(currentUser)) {
                        System.out.println("4. Dar de Baja o Activar Actividad");
                    }
                    if (hasAccessToInscribirseActividad(currentUser)) {
                        System.out.println("5. Inscribirse a Actividad");
                    }
                    if (hasAccessToInscribirseActividad(currentUser)) {
                        System.out.println("6. Listar Actividades Inscriptas");
                    }
                    if (hasAccessToInscribirseActividad(currentUser)) {
                        System.out.println("7. Darme de Baja de Actividad");
                    }
                    if (hasAccessToReportes(currentUser)) {
                        System.out.println("8. Reportes");
                    }
                    System.out.println("0. Volver al menú principal" + ConsoleColors.RESET);
                    System.out.print("Seleccione una opción: ");
                },
                choice -> {
                    switch (choice) {
                        case 1 -> {
                            if (hasAccessToIngresoActividad(currentUser)) {
                                actividadRegistrar.registrarActividad(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 2 -> {
                            if (hasAccessToListarActividades(currentUser)) {
                                actividadListar.listarActividadesConFiltros();
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 3 -> {
                            if (hasAccessToModificarActividad(currentUser)) {
                                actividadModificar.modificarActividad(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 4 -> {
                            if (hasAccessToDarBajaActividad(currentUser)) {
                                actividadMenuBaja.gestionarBajaOReactivacion(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 5 -> {
                            if (hasAccessToInscribirseActividad(currentUser)) {
                                actividadMenuInscribirse.listarActividadesParaInscripcion(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 6 -> {
                            if (hasAccessToInscribirseActividad(currentUser)) {
                                actividadMenuActividadesInscriptas.listarActividadesInscriptas(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 7 -> {
                            if (hasAccessToInscribirseActividad(currentUser)) {
                                actividadMenuActividadesInscriptas.darseDebajaDeActividadMenu(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 8 -> {
                            if (hasAccessToReportes(currentUser)) {
                                actividadMenuReporte.generarReporte(currentUser);
                            } else {
                                System.out.println(ConsoleColors.RED + "No tiene permisos para acceder a esta funcionalidad." + ConsoleColors.RESET);
                            }
                        }
                        case 0 ->
                                System.out.println(ConsoleColors.YELLOW + "Volviendo al menú principal..." + ConsoleColors.RESET);
                        default ->
                                System.out.println(ConsoleColors.RED + "Opción no válida. Intente nuevamente." + ConsoleColors.RESET);
                    }
                }
        );
    }

    private boolean hasAccessToIngresoActividad(Usuario user) {
        return user.getRole().isAdminOrAuxiliar();
    }

    private boolean hasAccessToListarActividades(Usuario user) {
        return user.getRole().isAdminOrAuxiliar() || user.getRole().isSocioOrNoSocio();
    }

    private boolean hasAccessToModificarActividad(Usuario user) {
        return user.getRole().isAdminOrAuxiliar();
    }

    private boolean hasAccessToDarBajaActividad(Usuario user) {
        return user.getRole().isAdminOrAuxiliar();
    }

    private boolean hasAccessToInscribirseActividad(Usuario user) {
        return user.getRole().isSocioOrNoSocio() || user.getRole().isAdminOrAuxiliar();
    }

    private boolean hasAccessToReportes(Usuario user) {
        return user.getRole().isAdminOrAuxiliar();
    }
}
