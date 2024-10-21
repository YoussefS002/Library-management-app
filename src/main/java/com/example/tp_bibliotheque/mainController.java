package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class mainController {
    @FXML
    private TableView<Oeuvre> tableView;

    @FXML
    private TableColumn<Oeuvre, String> titreColumn;

    @FXML
    private TableColumn<Oeuvre, Integer> premiereParutionColumn;

    @FXML
    private TableColumn<Oeuvre, String> motsClesColumn;

    @FXML
    private TableColumn<Oeuvre, String> auteursColumn;

    @FXML
    private void initialize() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        premiereParutionColumn.setCellValueFactory(new PropertyValueFactory<>("premiere_parution"));
        motsClesColumn.setCellValueFactory(new PropertyValueFactory<>("mots_cles"));
        auteursColumn.setCellValueFactory(new PropertyValueFactory<>("auteurs"));
        ObservableList<Oeuvre> oeuvres = FXCollections.observableArrayList();

        String recupererOeuvres = "SELECT id_oeuvre, titre, premiere_parution, mot_cle1, mot_cle2, mot_cle3, mot_cle4, mot_cle5 FROM oeuvres";
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererOeuvres)) {
             while (resultSet.next()) {
                 int id_oeuvre = resultSet.getInt("id_oeuvre");
                 String titre = resultSet.getString("titre");
                 int premiere_parution = resultSet.getInt("premiere_parution");
                 String mot_cle1 = resultSet.getString("mot_cle1");
                 String mot_cle2 = resultSet.getString("mot_cle2");
                 String mot_cle3 = resultSet.getString("mot_cle3");
                 String mot_cle4 = resultSet.getString("mot_cle4");
                 String mot_cle5 = resultSet.getString("mot_cle5");
                 Oeuvre oeuvreAAfficher = new Oeuvre(titre, premiere_parution, mot_cle1);
                 oeuvreAAfficher.mot_cle2 = mot_cle2;
                 oeuvreAAfficher.mot_cle3 = mot_cle3;
                 oeuvreAAfficher.mot_cle4 = mot_cle4;
                 oeuvreAAfficher.mot_cle5 = mot_cle5;
                 oeuvreAAfficher.updateMotsCles();
                 oeuvreAAfficher.updateAuteursWithId(con, id_oeuvre);
                 oeuvres.add(oeuvreAAfficher);
             }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        tableView.setItems(oeuvres);
    }

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
    private void goToNouvelleOeuvre() throws IOException {
        newWindow("nouvelleOeuvre.fxml");
    }
    @FXML
    private void goToNouvelUsager() throws IOException {
        newWindow("nouvelUsager.fxml");
    }
    @FXML
    private void goToNouvelEmprunt() throws IOException {
        newWindow("nouvelEmprunt.fxml");
    }
    @FXML
    private void goToNouvelleEdition() throws IOException {
        newWindow("nouvelleEdition.fxml");
    }
    @FXML
    private void goToNouveauxExemplaires() throws IOException {
        newWindow("nouveauxExemplaires.fxml");
    }
}
