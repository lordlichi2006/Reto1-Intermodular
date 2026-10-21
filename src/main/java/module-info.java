module com.reto1.models {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.reto1.models to javafx.fxml;
    exports com.reto1.models;
}
