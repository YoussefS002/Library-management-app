package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class nouvelEmpruntController {
    Usager usagerSelectionne;
    Oeuvre oeuvreSelectionnee;
    @FXML
    private ComboBox<String> cbOeuvre;
    long isbnSelectionne;
    @FXML
    private ComboBox<Long> cbIsbn;
    int numeroSelectionne;
    @FXML
    private ComboBox<Integer> cbNumero;

    LocalDate dateEmprunt;
    @FXML
    private Label dateEmpruntLBL;

    LocalDate deadlineRetour;
    @FXML
    private Label deadlineRetourLBL;

    LocalDate dateRetour;
    @FXML
    private void initialize() {
        //oeuvres
        ObservableList<String> oeuvres = FXCollections.observableArrayList();
        String oeuvres_query = "SELECT titre,premiere_parution FROM oeuvres";
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
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

        //dateEmprunt
        dateEmprunt = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateEmpruntLBL.setText("Date d'emprunt : " + dateEmprunt.format(formatter));

        //deadline
        usagerSelectionne=loginController.currentUser;
        int duree_max = usagerSelectionne.categorie.duree_max;
        deadlineRetour=dateEmprunt.plusDays(duree_max);
        deadlineRetourLBL.setText("Deadline de retour : " + deadlineRetour.format(formatter));
    }

    @FXML
    private void afficherIsbns() throws SQLException {
        ObservableList<Long> isbns = FXCollections.observableArrayList();
        String isbn_query = "SELECT isbn FROM editions WHERE id_oeuvre = ?";
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
        PreparedStatement prep_statement = con.prepareStatement(isbn_query);
        String oeuvreAnnee = cbOeuvre.getSelectionModel().getSelectedItem();
        String[] L = oeuvreAnnee.split("-");
        oeuvreSelectionnee = new Oeuvre(L[0].strip(), Integer.parseInt(L[1].strip()), "?");
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
    private void afficherNumeros() throws SQLException {
        ObservableList<Integer> numeros = FXCollections.observableArrayList();
        String numeros_query = "SELECT numero FROM exemplaires WHERE isbn = ? AND emprunte=false";
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
        PreparedStatement prep_statement = con.prepareStatement(numeros_query);

        isbnSelectionne = cbIsbn.getSelectionModel().getSelectedItem();
        prep_statement.setLong(1, isbnSelectionne);
        ResultSet resultSet = prep_statement.executeQuery();
        while (resultSet.next()) {
            int numero = resultSet.getInt("numero");
            numeros.add(numero);
        }
        cbNumero.setItems(numeros);
    }

    @FXML
    private void ajouterEmprunt() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");

        numeroSelectionne = cbNumero.getSelectionModel().getSelectedItem();

        Emprunt nouvelEmprunt = new Emprunt();
        nouvelEmprunt.usager=usagerSelectionne;
        nouvelEmprunt.oeuvre=oeuvreSelectionnee;
        nouvelEmprunt.edition=new Edition(isbnSelectionne);
        nouvelEmprunt.numero=numeroSelectionne;
        nouvelEmprunt.dateEmprunt=dateEmprunt;
        nouvelEmprunt.deadline=deadlineRetour;

        String sql ="INSERT INTO emprunts(id_usager, isbn, numero_exemplaire, date_emprunt, deadline) VALUES (?,?,?,?,?)";
        PreparedStatement prep_statement = con.prepareStatement(sql);
        prep_statement.setInt(1, nouvelEmprunt.usager.id);
        prep_statement.setLong(2, nouvelEmprunt.edition.isbn);
        prep_statement.setInt(3, nouvelEmprunt.numero);
        prep_statement.setDate(4, Date.valueOf(nouvelEmprunt.dateEmprunt));
        prep_statement.setDate(5, Date.valueOf(nouvelEmprunt.deadline));
        prep_statement.execute();

        String sql2 = "UPDATE exemplaires SET emprunte = true WHERE numero = ?";
        PreparedStatement preparedStatement2 = con.prepareStatement(sql2);
        preparedStatement2.setInt(1, nouvelEmprunt.numero);
        preparedStatement2.execute();

        con.close();

        Notifications.create()
                .title("Nouvel Emprunt")
                .text("Un nouvel emprunt a été fait.")
                .showInformation();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                return null;
            }
            @Override
            protected void succeeded() {
                try {
                    retour();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(task).start();
    }
    @FXML
    AnchorPane mainContent;
    @FXML
    private void newView(String FXMLPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLPath));
        Pane newLoadedPane = loader.load();
        mainContent.getChildren().setAll(newLoadedPane);
    }
    @FXML
    private void retour() throws IOException {
        newView("main.fxml");
    }
}
