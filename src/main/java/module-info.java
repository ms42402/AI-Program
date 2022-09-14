module com.visualizer.visualizationapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.visualizer.visualizationapp to javafx.fxml;
    exports com.visualizer.visualizationapp;
}