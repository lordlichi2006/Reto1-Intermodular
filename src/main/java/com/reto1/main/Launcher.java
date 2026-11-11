package com.reto1.main;

/**
 * Main class of the executable JAR.
 * It does not extend Application, so the shaded JAR can start JavaFX
 * without the "JavaFX runtime components are missing" error.
 *
 * @author Ekaitz.Rivero
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
