package com.reto1.util;

import java.util.regex.Pattern;

import com.reto1.exceptions.DniException;
import com.reto1.exceptions.EmailException;

/**
 * Validations shared by user-management and customer-profile forms.
 */
public final class UserValidation {

    /** Format of an email: name, @ and a domain with at least one dot. */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
                    + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$");
    /** Format of a DNI: 8 digits and a letter. */
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}[A-Z]$");
    /** Format of a NIE: X, Y or Z, 7 digits and a letter. */
    private static final Pattern NIE_PATTERN = Pattern.compile("^[XYZ]\\d{7}[A-Z]$");
    /** Format of a CIF: a letter, 7 digits and a control digit or letter. */
    private static final Pattern CIF_PATTERN = Pattern.compile("^[ABCDEFGHJNPQRSUVW]\\d{7}[0-9A-J]$");
    /** Control letters of the DNI and the NIE, by the remainder of dividing by 23. */
    private static final String DNI_CONTROL_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    /** Control letters of the CIF, by the control digit. */
    private static final String CIF_CONTROL_LETTERS = "JABCDEFGHI";

    /** Not used: the class only has static methods. */
    private UserValidation() {
    }

    /**
     * Checks the basic email format used by the account forms.
     *
     * @param email value to validate
     * @throws EmailException if the email is missing or malformed
     */
    public static void validateEmail(String email) throws EmailException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new EmailException("Enter a valid email address.");
        }
    }

    /**
     * Checks Spanish DNI, NIE, or CIF syntax and control character.
     *
     * @param idDocument value to validate
     * @throws DniException if the document is missing or invalid
     */
    public static void validateIdDocument(String idDocument) throws DniException {
        if (idDocument == null) {
            throw new DniException("Enter a valid DNI, NIE or CIF.");
        }

        String normalized = idDocument.trim().toUpperCase();
        if (DNI_PATTERN.matcher(normalized).matches()) {
            return;
        } else if (NIE_PATTERN.matcher(normalized).matches()) {
            char firstLetter = normalized.charAt(0);
            int prefix = firstLetter == 'X' ? 0 : firstLetter == 'Y' ? 1 : 2;
            String number = prefix + normalized.substring(1, 8);
            if (DNI_CONTROL_LETTERS.charAt(Integer.parseInt(number) % 23) == normalized.charAt(8)) {
                return;
            }
        } else if (CIF_PATTERN.matcher(normalized).matches() && isValidCif(normalized)) {
            return;
        }

        throw new DniException("Enter a valid DNI, NIE or CIF.");
    }

    /**
     * Checks the control digit or letter of a CIF.
     *
     * @param cif CIF with the right format, in capital letters
     * @return true if the control character is right
     */
    private static boolean isValidCif(String cif) {
        int evenPositionSum = 0;
        int oddPositionSum = 0;
        for (int index = 1; index <= 7; index++) {
            int digit = Character.digit(cif.charAt(index), 10);
            if (index % 2 == 0) {
                evenPositionSum += digit;
            } else {
                int doubled = digit * 2;
                oddPositionSum += doubled / 10 + doubled % 10;
            }
        }

        int controlDigit = (10 - (evenPositionSum + oddPositionSum) % 10) % 10;
        char control = cif.charAt(8);
        char prefix = cif.charAt(0);
        boolean letterControlRequired = "NPQRSW".indexOf(prefix) >= 0;
        boolean digitControlRequired = "ABEH".indexOf(prefix) >= 0;
        return (letterControlRequired || !digitControlRequired && Character.isLetter(control))
                ? CIF_CONTROL_LETTERS.charAt(controlDigit) == control
                : Character.isDigit(control) && controlDigit == Character.digit(control, 10);
    }
}
