package lqqd.asur.utils;

import lqqd.asur.model.Espacio;
import lqqd.asur.model.actividad.FormaPago;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ActividadUtils {

    public static LocalDateTime solicitarFechaYHora() {
        Scanner scanner = new Scanner(System.in);
        LocalDate fecha = null;
        LocalTime hora = null;

        do {
            System.out.print(ConsoleColors.ORANGE + "Ingrese la fecha de la actividad (dd/MM/yyyy): ");
            try {
                String fechaStr = scanner.nextLine();
                fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println(ConsoleColors.RED + "Formato de fecha inválido. Intente nuevamente." + ConsoleColors.RESET);
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Error al ingresar la fecha. Intente nuevamente." + ConsoleColors.RESET);
            }
        } while (fecha == null);

        do {
            System.out.print(ConsoleColors.ORANGE + "Ingrese la hora de la actividad (HH:mm): ");
            try {
                String horaStr = scanner.nextLine();
                hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println(ConsoleColors.RED + "Formato de hora inválido. Intente nuevamente." + ConsoleColors.RESET);
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Error al ingresar la hora. Intente nuevamente." + ConsoleColors.RESET);
            }
        } while (hora == null);

        return LocalDateTime.of(fecha, hora);
    }

    public static Duration solicitarDuracion() {
        Scanner scanner = new Scanner(System.in);
        Duration duracion = null;

        do {
            System.out.print(ConsoleColors.ORANGE + "Ingrese la duración en horas y minutos (HH:mm o H:mm): ");
            try {
                String input = scanner.nextLine();
                String[] partes = input.split(":");
                if (partes.length != 2) {
                    throw new IllegalArgumentException("El formato debe ser HH:mm.");
                }
                int horas = Integer.parseInt(partes[0].trim());
                int minutos = Integer.parseInt(partes[1].trim());

                if (horas < 0 || minutos < 0 || minutos >= 60) {
                    throw new IllegalArgumentException("Las horas deben ser >= 0 y los minutos entre 0 y 59.");
                }

                duracion = Duration.ofHours(horas).plusMinutes(minutos);

                if (duracion.isZero() || duracion.isNegative()) {
                    throw new IllegalArgumentException("La duración debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED + "Los valores deben ser números enteros. Intente nuevamente." + ConsoleColors.RESET);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColors.RED + e.getMessage() + ConsoleColors.RESET);
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Intente nuevamente." + ConsoleColors.RESET);
            }
        } while (duracion == null);

        return duracion;
    }

    public static boolean solicitarRequiereInscripcion(Scanner scanner) {
        boolean requiereInscripcion = false;
        boolean validInput = false;

        do {
            try {
                System.out.print(ConsoleColors.ORANGE + "¿Requiere inscripción? (0 - No, 1 - Sí): ");
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input == 0) {
                    validInput = true;
                } else if (input == 1) {
                    requiereInscripcion = true;
                    validInput = true;
                } else {
                    System.out.println(ConsoleColors.RED + "Por favor, ingrese 0 para No o 1 para Sí." + ConsoleColors.RESET);
                }
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
                scanner.nextLine();
            }
        } while (!validInput);

        return requiereInscripcion;
    }

    public static Long solicitarEspacio(List<Espacio> espaciosDisponibles, Scanner scanner) {
        Long espacioId = null;

        do {
            try {
                System.out.print(ConsoleColors.ORANGE + "Seleccione el ID del espacio: ");
                espacioId = scanner.nextLong();
                scanner.nextLine();

                Long finalEspacioId = espacioId;
                if (espaciosDisponibles.stream().noneMatch(espacio -> espacio.getId().equals(finalEspacioId))) {
                    System.out.println(ConsoleColors.RED + "ID inválido. Seleccione un espacio de la lista." + ConsoleColors.RESET);
                    espacioId = null;
                }
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
                scanner.nextLine();
            }
        } while (espacioId == null);

        return espacioId;
    }

    public static Double solicitarCosto(Scanner scanner) {
        Double costo = null;
        do {
            try {
                System.out.print(ConsoleColors.ORANGE + "Ingrese el costo de la actividad: ");
                costo = scanner.nextDouble();
                scanner.nextLine();
                if (costo < 0) {
                    System.out.println(ConsoleColors.RED + "El costo no puede ser negativo." + ConsoleColors.RESET);
                    costo = null;
                }
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
                scanner.nextLine();
            }
        } while (costo == null);
        return costo;
    }

    public static FormaPago solicitarFormaPago(Scanner scanner) {
        FormaPago formaPago = null;
        boolean validInput = false;
        do {
            try {
                System.out.print(ConsoleColors.ORANGE + "Seleccione la forma de pago (1 - EFECTIVO, 2 - TRANSFERENCIA, 3 - DÉBITO, 4 - CRÉDITO): ");
                int input = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer del scanner

                switch (input) {
                    case 1 -> {
                        formaPago = FormaPago.EFECTIVO;
                        validInput = true;
                    }
                    case 2 -> {
                        formaPago = FormaPago.TRANSFERENCIA;
                        validInput = true;
                    }
                    case 3 -> {
                        formaPago = FormaPago.DEBITO;
                        validInput = true;
                    }
                    case 4 -> {
                        formaPago = FormaPago.CREDITO;
                        validInput = true;
                    }
                    default ->
                            System.out.println(ConsoleColors.RED + "Por favor, ingrese un número entre 1 y 4." + ConsoleColors.RESET);
                }
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido." + ConsoleColors.RESET);
                scanner.nextLine();
            }
        } while (!validInput);
        return formaPago;
    }

    public static FormaPago solicitarNuevaFormaPago(Scanner scanner) {
        FormaPago formaPago = null;
        boolean validInput = false;

        do {
            try {
                System.out.print(ConsoleColors.ORANGE +
                        "Ingrese la nueva forma de cobro (o deje en blanco para mantener): " +
                        "(1 - EFECTIVO, 2 - TRANSFERENCIA, 3 - DÉBITO, 4 - CRÉDITO): " +
                        ConsoleColors.RESET);
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    // Entrada opcional, mantener el valor actual
                    validInput = true;
                } else {
                    int opcion = Integer.parseInt(input);

                    switch (opcion) {
                        case 1 -> {
                            formaPago = FormaPago.EFECTIVO;
                            validInput = true;
                        }
                        case 2 -> {
                            formaPago = FormaPago.TRANSFERENCIA;
                            validInput = true;
                        }
                        case 3 -> {
                            formaPago = FormaPago.DEBITO;
                            validInput = true;
                        }
                        case 4 -> {
                            formaPago = FormaPago.CREDITO;
                            validInput = true;
                        }
                        default ->
                                System.out.println(ConsoleColors.RED + "Por favor, ingrese un número entre 1 y 4." + ConsoleColors.RESET);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número válido o deje en blanco." + ConsoleColors.RESET);
            }
        } while (!validInput);

        return formaPago;
    }

}
