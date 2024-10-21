package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.*;


public class nouveauxExemplairesController {
    @FXML
    private ComboBox<String> cbOeuvre;
    @FXML
    private ComboBox<String> cbIsbn;
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
        ObservableList<String> isbns = FXCollections.observableArrayList();
        String isbn_query = "SELECT isbn FROM editions WHERE id_oeuvre = ?";
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
        PreparedStatement prep_statement = con.prepareStatement(isbn_query);
        String oeuvreAnnee = cbOeuvre.getSelectionModel().getSelectedItem();
        String[] L = oeuvreAnnee.split("-");
        Oeuvre oeuvreSelectionnee = new Oeuvre(L[0].strip(), Integer.parseInt(L[1].strip()), "?");
        oeuvreSelectionnee.updateId(con);
        System.out.println(oeuvreSelectionnee.id);
        System.out.println(oeuvreSelectionnee.titre);
        System.out.println(oeuvreSelectionnee.premiere_parution);
        prep_statement.setInt(1, oeuvreSelectionnee.id);
        ResultSet resultSet = prep_statement.executeQuery();
        while (resultSet.next()) {
            String isbn = String.valueOf(resultSet.getLong("isbn"));
            isbns.add(isbn);
        }
        cbIsbn.setItems(isbns);
    }


    @FXML
    private void ajouterExemplaires() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");

        Edition editionAModifier = new Edition();
        editionAModifier.isbn = Long.parseLong(cbIsbn.getSelectionModel().getSelectedItem());
        int nombreAAjouter = Integer.parseInt(tfNbExemplaires.getText());
        editionAModifier.ajouterExp(con, nombreAAjouter);
        con.close();
    }
}
