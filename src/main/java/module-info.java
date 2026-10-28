module com.reto1.models {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens com.reto1.controllers to javafx.fxml;
    opens com.reto1.models;
    exports com.reto1.controllers;
    exports com.reto1.models;
}
