package com.reto1.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Creates the only logger of the application. It writes to the console and
 * to the file application.log, which admins can read in the Admin Log window.
 *
 * @author Ekaitz.Rivero
 */
public final class LoggerManager {

    private static Logger logger;

    private LoggerManager() {
    }

    /**
     * Returns the application logger, creating it the first time.
     *
     * @return a single instance of {@link java.util.logging.Logger}
     */
    public static synchronized Logger getLogger() {
        FileHandler file;
        ConsoleHandler console;

        if (logger == null) {
            logger = Logger.getLogger("ApplicationLogger");

            // Do not add handlers twice, it would create a duplicate log file
            if (logger.getHandlers().length == 0) {
                try {
                    file = new FileHandler("application.log", true);
                    console = new ConsoleHandler();

                    // Formats the log output so it is easier to read
                    file.setFormatter(new SimpleFormatter());
                    console.setFormatter(new SimpleFormatter());
                    logger.addHandler(file);
                    logger.addHandler(console);

                    // Remove default handlers since we have our custom ones
                    logger.setUseParentHandlers(false);

                    // ALL in the logger so each handler chooses its own level
                    logger.setLevel(Level.ALL);
                    file.setLevel(Level.INFO); // only SEVERE, WARNING and INFO
                    console.setLevel(Level.ALL); // ALL includes debug messages
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                // The logger is created only once, so the start message goes here
                logger.log(Level.INFO, "========= Application Initialized =========");
            }
        }
        return logger;
    }
}
