package lqqd.asur.utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Consumer;

public class MenuUtils {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * El metodo accept de la interfaz funcional Consumer<T> se utiliza para ejecutar la operación definida en el
     * consumidor con el argumento proporcionado.
     */
    public static void executeMenu(Runnable menuPrinter, Consumer<Integer> optionHandler) {
        while (true) {
            try {
                menuPrinter.run();
                int opcion = scanner.nextInt();
                scanner.nextLine();
                optionHandler.accept(opcion);
                break;
            } catch (InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, ingrese un número." + ConsoleColors.RESET);
                scanner.nextLine();
            }
        }
    }

    public static void executeMenuString(Runnable menuPrinter, Consumer<String> optionHandler) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                menuPrinter.run();
                String input = scanner.nextLine().trim();
                optionHandler.accept(input);
                break;
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Entrada inválida. Por favor, intente nuevamente." + ConsoleColors.RESET);
            }
        }
    }

}

