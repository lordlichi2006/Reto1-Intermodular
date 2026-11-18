package com.reto1.util;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.reto1.exceptions.DniException;
import com.reto1.exceptions.EmailException;

class UserValidationTest {

    @Test
    void acceptsValidEmailAndSpanishIdDocuments() throws EmailException, DniException {
        UserValidation.validateEmail("user@example.com");
        UserValidation.validateIdDocument("12345678A");
        UserValidation.validateIdDocument("12345678Z");
        UserValidation.validateIdDocument("12345678a");
        UserValidation.validateIdDocument("X0000000T");
        UserValidation.validateIdDocument("B12345674");
    }

    @Test
    void rejectsMalformedEmailAndIdDocuments() {
        assertInvalidEmail("not-an-email");
        assertInvalidEmail("user@example..com");
        assertInvalidEmail("user@-example.com");

        assertInvalidIdDocument("1234567A");
        assertInvalidIdDocument("12345678");
        assertInvalidIdDocument("X0000000A");
    }

    private static void assertInvalidEmail(String email) {
        try {
            UserValidation.validateEmail(email);
            fail("Expected an EmailException for an invalid email: " + email);
        } catch (EmailException expected) {
        }
    }

    private static void assertInvalidIdDocument(String idDocument) {
        try {
            UserValidation.validateIdDocument(idDocument);
            fail("Expected a DniException for an invalid ID document: " + idDocument);
        } catch (DniException expected) {
        }
    }
}
