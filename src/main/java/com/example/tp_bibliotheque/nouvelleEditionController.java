package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import javafx.concurrent.Task;
import java.sql.*;
import java.util.List;

public class nouvelleEditionController {
    @FXML
    private ComboBox<String> cbOeuvre;
    @FXML
    private TextField tfIsbn;
    @FXML
    private TextField tfNbExemplaires;
    @FXML
    private TextField tfEditeur;
    @FXML
    private TextField tfAnneeEdition;

    @FXML
    private void initialize() {
        ObservableList<String> oeuvres = FXCollections.observableArrayList();
        String oeuvres_query = "SELECT titre,premiere_parution FROM oeuvres";
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(oeuvres_query)) {
            while (resultSet.next()) {
                String titre = resultSet.getString("titre");
                int premiere_parution = resultSet.getInt("premiere_parution");
                oeuvres.add(titre + " - " + premiere_parution);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cbOeuvre.setItems(oeuvres);
    }

    @FXML
    private void ajouterEdition() throws SQLException, InterruptedException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");


        Edition nouvelleEdition = new Edition();
        nouvelleEdition.isbn = Long.parseLong(tfIsbn.getText());
        nouvelleEdition.nbExemplaires = Integer.parseInt(tfNbExemplaires.getText());
        nouvelleEdition.Editeur = tfEditeur.getText();
        nouvelleEdition.anneeEdition = Integer.parseInt(tfAnneeEdition.getText());


        String oeuvreAnnee = cbOeuvre.getSelectionModel().getSelectedItem();
        String[] L = oeuvreAnnee.split("-");
        Oeuvre oeuvre = new Oeuvre(L[0].strip(), Integer.parseInt(L[1].strip()), "?");
        oeuvre.updateId(con);

        nouvelleEdition.oeuvre = oeuvre;
        nouvelleEdition.ajouter(con);
        con.close();
        Notifications.create()
                .title("Edition ajoutée")
                .text("L'édition "+ nouvelleEdition.isbn+" de "+nouvelleEdition.oeuvre.titre+" a été ajoutée")
                .showInformation();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                return null;
            }

            @Override
            protected void succeeded() {
                Stage stage = (Stage) cbOeuvre.getScene().getWindow();
                stage.close();
            }
        };
        new Thread(task).start();

    }
}
