package com.reto1.util;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Creates the only logger of the application. It writes to the console and to
 * the file application.log, which admins can read in the Admin Log window.
 *
 * @author Ekaitz.Rivero
 */
public final class LoggerManager {

    private static Logger logger;

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
     * Custom Formatter used to more easily read logs
     * @return custom formatter with the following format: [ERROR LEVEL] DATE | USER | CLASS&METHOD | MESSAGE
     */
    public static Formatter customFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                 
                return String.format(
                        "[ %2$s ] %1$tF %1$tT | %6$s | %3$s.%4$s | %5$s%n",
                        new Date(record.getMillis()),
                        record.getLevel(),
                        record.getSourceClassName(),
                        record.getSourceMethodName(),
                        formatMessage(record),
                        currentUser);
            }
        };
    }

    /**
     * Returns the application logger, creating it the first time.
     *
     * @return a single instance of {@link java.util.logging.Logger}
     */
    public static synchronized Logger getLogger() {
        FileHandler file;
        ConsoleHandler console;

        final Formatter customFormatter = customFormatter();

        if (logger == null) {
            logger = Logger.getLogger("ApplicationLogger");

            if (logger.getHandlers().length == 0) {
                try {
                    file = new FileHandler("application.log", true);
                    console = new ConsoleHandler();

                    file.setFormatter(customFormatter);
                    console.setFormatter(customFormatter);
                    logger.addHandler(file);
                    logger.addHandler(console);

                    logger.setUseParentHandlers(false);

                    logger.setLevel(Level.ALL);
                    file.setLevel(Level.INFO);
                    console.setLevel(Level.ALL);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                logger.log(Level.INFO, "========= Application Initialized =========");
            }
        }
        return logger;
    }
}
