package com.reto1.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.util.logging.Level;
import javafx.scene.control.Label;

public class AdminLogController extends BaseController {
    
    @FXML
    private Button btnLog;
    @FXML
    private Label lblError;
    
    private List<String> logContents;
    
    private void initialize() {
        btnLog.setDisable(true); // Disable on start

        try {
            logContents = Files.readAllLines(Paths.get("application.log"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to open log file: {0}", e.getMessage());
            lblError.setText("Unable to open log file: " + e.getMessage());
        }
    }
}
