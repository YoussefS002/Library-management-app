package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class editionsController {
    @FXML
    private TableView<Edition> tableView;
    @FXML
    private TableColumn<Edition, Long> isbnColumn;
    @FXML
    private TableColumn<Edition, String> editeurColumn;
    @FXML
    private TableColumn<Edition, Integer> anneeEditionColumn;
    @FXML
    private TableColumn<Edition, Integer> nbExemplairesColumn;
    @FXML
    private void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        editeurColumn.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        anneeEditionColumn.setCellValueFactory(new PropertyValueFactory<>("anneeEdition"));
        nbExemplairesColumn.setCellValueFactory(new PropertyValueFactory<>("nbExemplaires"));


        ObservableList<Edition> editions = FXCollections.observableArrayList();

        String recupererEditions = "SELECT isbn FROM editions WHERE id_oeuvre = " + mainController.idOeuvreSelectionnee;
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererEditions)) {
            while (resultSet.next()) {
                long isbn = resultSet.getLong("isbn");
                Edition edition = new Edition(isbn);
                edition.updateWithIsbn(con);
                editions.add(edition);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableView.setItems(editions);
    }

}
