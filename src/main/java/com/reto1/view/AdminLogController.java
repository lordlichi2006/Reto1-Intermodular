package com.reto1.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controller of the window Admin Log (adminLog.fxml).
 * Only for admins: shows the content of the log file (application.log).
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminLogController extends BaseController {

    /** Name of the log file written by {@link com.reto1.util.LoggerManager}. */
    private static final String LOG_FILE = "application.log";

    @FXML
    private Label lblError;
    @FXML
    private TextArea txtLog;

    /**
     * Runs when the window opens: reads the log file and shows it.
     */
    @FXML
    private void initialize() {
        List<String> logContents;

        lblError.setText("");
        try {
            logContents = Files.readAllLines(Paths.get(LOG_FILE), StandardCharsets.UTF_8);
            txtLog.setText(String.join(System.lineSeparator(), logContents));
            // Scroll to the end: the newest lines are the most interesting ones
            txtLog.positionCaret(txtLog.getLength());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to open log file: {0}", e.getMessage());
            lblError.setText("Unable to open log file: " + e.getMessage());
        }
    }
}
