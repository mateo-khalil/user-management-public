package lqqd.asur.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidatorFechaNacimiento {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Validates if the given date string matches the format dd/MM/yyyy
     * and represents a valid date.
     *
     * @param fechaNacimiento the date string to validate
     * @return true if valid, false otherwise
     */
    public boolean validateFechaNacimiento(String fechaNacimiento) {
        try {
            LocalDate date = LocalDate.parse(fechaNacimiento, FORMATTER);
            // Optional: Check if the date is not in the future
            return !date.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false; // Invalid format or date
        }
    }
}
