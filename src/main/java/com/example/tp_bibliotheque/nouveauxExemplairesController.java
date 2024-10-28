package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.sql.*;


public class nouveauxExemplairesController {
    @FXML
    private ComboBox<String> cbOeuvre;
    @FXML
    private ComboBox<Long> cbIsbn;
    @FXML
    private TextField tfNbExemplaires;

    @FXML
    private void initialize() throws SQLException {
        ObservableList<String> oeuvres = FXCollections.observableArrayList();
        String oeuvres_query = "SELECT titre, premiere_parution FROM oeuvres";
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
    private void afficherIsbns() throws SQLException {
        ObservableList<Long> isbns = FXCollections.observableArrayList();
        String isbn_query = "SELECT isbn FROM editions WHERE id_oeuvre = ?";
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
        PreparedStatement prep_statement = con.prepareStatement(isbn_query);
        String oeuvreAnnee = cbOeuvre.getSelectionModel().getSelectedItem();
        String[] L = oeuvreAnnee.split("-");
        Oeuvre oeuvreSelectionnee = new Oeuvre(L[0].strip(), Integer.parseInt(L[1].strip()), "?");
        oeuvreSelectionnee.updateId(con);
        prep_statement.setInt(1, oeuvreSelectionnee.id);
        ResultSet resultSet = prep_statement.executeQuery();
        while (resultSet.next()) {
            Long isbn = resultSet.getLong("isbn");
            isbns.add(isbn);
        }
        cbIsbn.setItems(isbns);
    }


    @FXML
    private void ajouterExemplaires() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");

        Edition editionAModifier = new Edition();
        editionAModifier.isbn = cbIsbn.getSelectionModel().getSelectedItem();
        int nombreAAjouter = Integer.parseInt(tfNbExemplaires.getText());
        editionAModifier.ajouterExp(con, nombreAAjouter);
        con.close();
        Notifications.create()
                .title("Exemplaires ajouté")
                .text(nombreAAjouter+" exemplaires ont été ajoutés à l'édition d'ISBN : "+ editionAModifier.isbn+". Il y a maintenant "+ editionAModifier.nbExemplaires+ " exmplaires.")
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
