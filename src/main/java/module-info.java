module com.example.tp_bibliotheque {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.tp_bibliotheque to javafx.fxml;
    exports com.example.tp_bibliotheque;
}