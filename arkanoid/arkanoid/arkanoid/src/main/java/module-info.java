module com.polytech.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.polytech.arkanoid to javafx.fxml;
    exports com.polytech.arkanoid;
}