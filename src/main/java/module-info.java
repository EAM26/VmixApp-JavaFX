module org.eam.code.vmixapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires org.xerial.sqlitejdbc;


    opens org.eam.code.vmixapp to javafx.fxml;
    exports org.eam.code.vmixapp;
    exports org.eam.code.vmixapp.model;
    opens org.eam.code.vmixapp.model to javafx.fxml;
    exports org.eam.code.vmixapp.controller;
    opens org.eam.code.vmixapp.controller to javafx.fxml;
    exports org.eam.code.vmixapp.service;
    opens org.eam.code.vmixapp.service to javafx.fxml;
}