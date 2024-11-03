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

public class historiqueController {
    @FXML
    private TableView<Emprunt> tableViewE;
    @FXML
    private TableColumn<Emprunt, Integer> idColumnE;
    @FXML
    private TableColumn<Emprunt, Oeuvre> oeuvreColumn;
    @FXML
    private TableColumn<Emprunt, Edition> editionColumn;
    @FXML
    private TableColumn<Emprunt, Integer> numeroColumn;
    @FXML
    private TableColumn<Emprunt, LocalDate> dateEmpruntColumn;
    @FXML
    private TableColumn<Emprunt, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Emprunt, LocalDate> dateRetourColumn;
    @FXML
    private void initialize() {
        idColumnE.setCellValueFactory(new PropertyValueFactory<>("id"));
        oeuvreColumn.setCellValueFactory(new PropertyValueFactory<>("oeuvre"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        dateEmpruntColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        dateRetourColumn.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));


        ObservableList<Emprunt> emprunts = FXCollections.observableArrayList();

        String recupererEmprunts = "SELECT id_emprunt FROM emprunts WHERE id_usager = " + mainController.idUsagerSelectionne;
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererEmprunts)) {
            while (resultSet.next()) {
                int id_emprunt = resultSet.getInt("id_emprunt");
                Emprunt emprunt=new Emprunt();
                emprunt.id = id_emprunt;
                emprunt.updateWithId(con);
                emprunts.add(emprunt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableViewE.setItems(emprunts);
    }
}
