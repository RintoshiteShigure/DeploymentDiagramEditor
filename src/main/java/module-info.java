module com.programos.deploymentdiagrameditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.programos.deploymentdiagrameditor to javafx.fxml;
    exports com.programos.deploymentdiagrameditor;
}