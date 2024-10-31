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

        String recupererEmprunts = "SELECT id_emprunt, id_usager, isbn, numero_exemplaire, date_emprunt, deadline, date_retour FROM emprunts WHERE id_usager = " + mainController.idUsagerSelectionne;
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererEmprunts)) {
            while (resultSet.next()) {
                int id_emprunt = resultSet.getInt("id_emprunt");

                long isbn = resultSet.getLong("isbn");
                Edition edition = new Edition(isbn);
                edition.updateWithIsbn(con);
                Oeuvre oeuvre = edition.oeuvre;

                int numero = resultSet.getInt("numero_exemplaire");
                LocalDate dateEmprunt = LocalDate.parse(resultSet.getString("date_emprunt"));
                LocalDate deadline = LocalDate.parse(resultSet.getString("deadline"));
                String dateRetourString = resultSet.getString("date_retour");
                LocalDate dateRetour = LocalDate.parse("3000-01-01");
                if (dateRetourString != null) {
                    dateRetour = LocalDate.parse(dateRetourString);
                }
                Emprunt emprunt=new Emprunt();
                emprunt.id = id_emprunt;
                emprunt.edition=edition;
                emprunt.numero = numero;
                emprunt.dateEmprunt = dateEmprunt;
                emprunt.deadline = deadline;
                emprunt.dateRetour = dateRetour;
                emprunt.oeuvre = oeuvre;
                emprunts.add(emprunt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableViewE.setItems(emprunts);
    }
}
