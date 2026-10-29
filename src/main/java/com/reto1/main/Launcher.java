package com.reto1.main;

/**
 * Main class of the executable JAR.
 * It does not extend Application, so the shaded JAR can start JavaFX
 * without the "JavaFX runtime components are missing" error.
 *
 * @author Ekaitz.Rivero
 */
public class Launcher {
        //Cuando ejecutas un .jar con java -jar, Java mira la clase principal antes de arrancar. 
        // Si esa clase hereda de Application, como App, Java exige que JavaFX esté instalado como módulo del sistema.
    public static void main(String[] args) {
        App.main(args);
    }
}
