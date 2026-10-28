module com.reto1.models {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens com.reto1.controllers to javafx.fxml;

    exports com.reto1.controllers;
    exports com.reto1.models;
}