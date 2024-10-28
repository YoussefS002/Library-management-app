package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class nouvelEmpruntController {
    Usager usagerSelectionne;
    @FXML
    private ComboBox<String> cbUsager;
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

        //usagers
        ObservableList<String> usagers = FXCollections.observableArrayList();
        String usagers_query = "SELECT nom,prenom,email FROM usagers";
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(usagers_query)) {
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String email = resultSet.getString("email");
                usagers.add(prenom + " " + nom + " - " + email);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cbUsager.setItems(usagers);

        //dateEmprunt
        dateEmprunt = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateEmpruntLBL.setText("Date d'emprunt : " + dateEmprunt.format(formatter));
    }

    @FXML
    private void afficherIsbns() throws SQLException {
        ObservableList<Long> isbns = FXCollections.observableArrayList();
        String isbn_query = "SELECT isbn FROM editions WHERE id_oeuvre = ?";
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
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
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
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
    private void definirDateRetour() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
        String prenomNomEmail = cbUsager.getSelectionModel().getSelectedItem();
        String[] L1 = prenomNomEmail.split("-");
        String prenomNom = L1[0].strip();
        String email = L1[1].strip();
        String[] L2 = prenomNom.split(" ");
        String prenom = L2[0].strip();
        String nom = L2[1].strip();
        usagerSelectionne = new Usager(nom, prenom, email);
        usagerSelectionne.updateCategorie(con);
        int duree_max = usagerSelectionne.categorie.duree_max;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        deadlineRetour=dateEmprunt.plusDays(duree_max);
        deadlineRetourLBL.setText("Deadline de retour : " + deadlineRetour.format(formatter));
    }

    @FXML
    private void ajouterEmprunt() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");

        numeroSelectionne = cbNumero.getSelectionModel().getSelectedItem();

        Emprunt nouvelEmprunt = new Emprunt();
        nouvelEmprunt.usager=usagerSelectionne;
        nouvelEmprunt.oeuvre=oeuvreSelectionnee;
        nouvelEmprunt.isbn=isbnSelectionne;
        nouvelEmprunt.numero=numeroSelectionne;
        nouvelEmprunt.dateEmprunt=dateEmprunt;
        nouvelEmprunt.deadline=deadlineRetour;

        String sql ="INSERT INTO emprunts(email_usager, isbn, numero_exemplaire, date_emprunt, deadline, gestionnaire_emp) VALUES (?, ?,?,?,?,?)";
        PreparedStatement prep_statement = con.prepareStatement(sql);
        prep_statement.setString(1, nouvelEmprunt.usager.email);
        prep_statement.setLong(2, nouvelEmprunt.isbn);
        prep_statement.setInt(3, nouvelEmprunt.numero);
        prep_statement.setDate(4, Date.valueOf(nouvelEmprunt.dateEmprunt));
        prep_statement.setDate(5, Date.valueOf(nouvelEmprunt.deadline));
        prep_statement.setString(6, loginController.currentGestionnaire.email);
        prep_statement.execute();
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
                Stage stage = (Stage) cbOeuvre.getScene().getWindow();
                stage.close();
            }
        };
        new Thread(task).start();
    }
}
