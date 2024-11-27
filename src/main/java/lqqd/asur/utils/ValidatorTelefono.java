package lqqd.asur.utils;

import java.util.regex.Pattern;

public class ValidatorTelefono {

    private static final Pattern numberPattern = Pattern.compile(".*\\d.*");

    public boolean isTelefonoValid(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return numberPattern.matcher(input).matches();
    }
}