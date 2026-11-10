package com.reto1.util;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Creates the only logger of the application. It writes to the console and to
 * the file application.log, which admins can read in the Admin Log window.
 *
 * @author Ekaitz.Rivero
 */
public final class LoggerManager {

    private static Logger logger;

    /**
     * Email of the user that is using the program. It is written in every
     * line of the log ("-" while nobody has logged in).
     */
    private static String currentUser = "-";

    private LoggerManager() {
    }

    /**
     * Saves who is using the program, so every line of the log says which
     * user did each operation. Called after the login (with the email) and
     * after the logout (with "-").
     *
     * @param user email of the logged user, or "-" if nobody
     */
    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    /**
     * Returns the application logger, creating it the first time.
     *
     * @return a single instance of {@link java.util.logging.Logger}
     */
    public static synchronized Logger getLogger() {
        FileHandler file;
        ConsoleHandler console;

        Formatter customFormatter = new Formatter() {
            @Override
            public String format(LogRecord record) {

                // [ERROR LEVEL] DATE | USER | CLASS&METHOD | MESSAGE
                return String.format(
                        "[ %2$s ] %1$tF %1$tT | %6$s | %3$s.%4$s | %5$s%n",
                        new Date(record.getMillis()),
                        record.getLevel(),
                        record.getSourceClassName(),
                        record.getSourceMethodName(),
                        formatMessage(record), // Formats the message so it can accept extra arguments
                        currentUser
                );
            }
        };

        if (logger == null) {
            logger = Logger.getLogger("ApplicationLogger");

            // Do not add handlers twice, it would create a duplicate log file
            if (logger.getHandlers().length == 0) {
                try {
                    file = new FileHandler("application.log", true);
                    console = new ConsoleHandler();

                    // Formats the log output so it is easier to read
                    file.setFormatter(customFormatter);
                    console.setFormatter(customFormatter);
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
