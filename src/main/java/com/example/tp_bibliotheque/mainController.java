package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.sql.*;


public class mainController {

    @FXML
    private void newWindow (String FXMLpath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpath));
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Nouvelle oeuvre");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    private void goToOeuvres() throws IOException {
        newWindow("nouvelleOeuvre.fxml");
    }
    @FXML
    private void goToUsagers() throws IOException {
        newWindow("nouvelUsager.fxml");
    }
}
