module com.reto1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    // FXML needs reflection to inject @FXML fields and call handlers
    opens com.reto1.controller to javafx.fxml;
    // The MongoDB POJO codec needs reflection to build the model objects
    opens com.reto1.model;

    exports com.reto1.controller;
    exports com.reto1.main;
    exports com.reto1.model;
    exports com.reto1.dao;
    exports com.reto1.util;
}
