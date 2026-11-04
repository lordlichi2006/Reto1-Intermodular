/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reto1.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 * @author Ekaitz.Rivero
 */
public class LoggerManager {
    private static Logger logger;
    
    /**
     * 
     * @return a single instance of {@link java.util.logging.Logger}
     */
    public static Logger getLogger() {

        if (logger != null) {
            return logger;
        }

        logger = java.util.logging.Logger.getLogger("ApplicationLogger");
        
        // Dont create a file handler if one already exists, this will create a duplicate log file!
        if (logger.getHandlers().length > 0) {
            return logger;
        }


        try {
            FileHandler file = new FileHandler("application.log", true);
            ConsoleHandler console = new ConsoleHandler();

            // Formats the log output so its easier to read
            file.setFormatter(new SimpleFormatter());
            console.setFormatter(new SimpleFormatter());

            logger.addHandler(file);
            logger.addHandler(console);

            // Remove default handlers since we have our custom ones
            logger.setUseParentHandlers(false);

            // Sets the levels of the logger
            logger.setLevel(Level.ALL); // set to all so each handler can choose which logging level you want
            file.setLevel(Level.INFO); // only SEVERE, WARNING and INFO
            console.setLevel(Level.ALL); // ALL includes Debug

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // logger is only created once, send init message here
        logger.log(Level.INFO, "========= Application Initialized =========");
        return logger;
    }
    
}
