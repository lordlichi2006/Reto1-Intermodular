module com.reto1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.logging;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens com.reto1.controller to javafx.fxml;
    opens com.reto1.model to javafx.base;

    exports com.reto1.main;
    exports com.reto1.controller;
    exports com.reto1.dao;
    exports com.reto1.exceptions;
    exports com.reto1.model;
    exports com.reto1.util;
}
