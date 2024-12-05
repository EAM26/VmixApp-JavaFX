module org.eam.code.vmixapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.eam.code.vmixapp to javafx.fxml;
    exports org.eam.code.vmixapp;
}