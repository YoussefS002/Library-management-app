package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class mainController {

    @FXML
    private TableView<Exemplaire> tableViewD;
    @FXML
    private TableColumn<Exemplaire, Integer> numeroColumnD;
    @FXML
    private TableColumn<Exemplaire, Edition> editionColumnD;
    @FXML
    private TableColumn<Exemplaire, Oeuvre> oeuvreColumnD;


    @FXML
    private TableView<Exemplaire> tableViewI;
    @FXML
    private TableColumn<Exemplaire, Integer> numeroColumnI;
    @FXML
    private TableColumn<Exemplaire, Edition> editionColumnI;
    @FXML
    private TableColumn<Exemplaire, Oeuvre> oeuvreColumnI;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab usagersTab;
    @FXML
    private TableView<Emprunt> tableViewE;
    @FXML
    private TableColumn<Emprunt, Integer> idColumnE;
    @FXML
    private TableColumn<Emprunt, Usager> usagerColumn;
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
    private TableView<Usager> tableViewU;
    @FXML
    private TableColumn<Usager, Integer> idColumn;
    @FXML
    private TableColumn<Usager, String> nomColumn;
    @FXML
    private TableColumn<Usager, String> prenomColumn;
    @FXML
    private TableColumn<Usager, String> emailColumn;
    @FXML
    private TableColumn<Usager, Categorie> categorieColumn;
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

    static int idUsagerSelectionne;

    @FXML
    private void initialize() {
        //oeuvres

        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        premiereParutionColumn.setCellValueFactory(new PropertyValueFactory<>("premiere_parution"));
        motsClesColumn.setCellValueFactory(new PropertyValueFactory<>("mots_cles"));
        auteursColumn.setCellValueFactory(new PropertyValueFactory<>("auteurs"));
        ObservableList<Oeuvre> oeuvres = FXCollections.observableArrayList();

        String recupererOeuvres = "SELECT id_oeuvre, titre, premiere_parution, mot_cle1, mot_cle2, mot_cle3, mot_cle4, mot_cle5 FROM oeuvres";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
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
                oeuvreAAfficher.id = id_oeuvre;
                oeuvreAAfficher.mot_cle2 = mot_cle2;
                oeuvreAAfficher.mot_cle3 = mot_cle3;
                oeuvreAAfficher.mot_cle4 = mot_cle4;
                oeuvreAAfficher.mot_cle5 = mot_cle5;
                oeuvreAAfficher.updateWithId(con);
                oeuvres.add(oeuvreAAfficher);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableView.setItems(oeuvres);

        //usagers
        if (Objects.equals(loginController.currentUser.categorie.nom, "gestionnaire") || Objects.equals(loginController.currentUser.categorie.nom, "admin"))
        {
            if (!tabPane.getTabs().contains(usagersTab)) {
                tabPane.getTabs().add(usagersTab);
            }
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
            ObservableList<Usager> usagers = FXCollections.observableArrayList();

            String recupererUsagers = "SELECT id_usager, nom, prenom, email, categorie FROM usagers";
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(recupererUsagers)) {
                while (resultSet.next()) {
                    int id_usager = resultSet.getInt("id_usager");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String email = resultSet.getString("email");
                    String categorie = resultSet.getString("categorie");
                    Usager usagerAAfficher = new Usager(email);
                    usagerAAfficher.nom = nom;
                    usagerAAfficher.prenom = prenom;
                    usagerAAfficher.email = email;
                    usagerAAfficher.id = id_usager;
                    usagerAAfficher.categorie = new Categorie(categorie);
                    usagers.add(usagerAAfficher);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tableViewU.setItems(usagers);
        } else {
            tabPane.getTabs().remove(usagersTab);
        }
        //emprunts
        idColumnE.setCellValueFactory(new PropertyValueFactory<>("id"));
        oeuvreColumn.setCellValueFactory(new PropertyValueFactory<>("oeuvre"));
        usagerColumn.setCellValueFactory(new PropertyValueFactory<>("usager"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        dateEmpruntColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        dateRetourColumn.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));


        ObservableList<Emprunt> emprunts = FXCollections.observableArrayList();

        String recupererEmprunts = "SELECT id_emprunt, id_usager, isbn, numero_exemplaire, date_emprunt, deadline, date_retour FROM emprunts";
        String recupererEmpruntsUsager = "SELECT id_emprunt, id_usager, isbn, numero_exemplaire, date_emprunt, deadline, date_retour FROM emprunts WHERE id_usager="+loginController.currentUser.id;
        String sql;
        if (Objects.equals(loginController.currentUser.categorie.nom, "emprunteur")) {
            sql = recupererEmpruntsUsager;
        } else {
            sql = recupererEmprunts;
        }
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
             while (resultSet.next()) {
                int id_emprunt = resultSet.getInt("id_emprunt");
                int id_usager = resultSet.getInt("id_usager");
                Usager usager = new Usager("?");
                usager.id=id_usager;
                usager.updateWithId(con);

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
                emprunt.usager = usager;
                emprunt.oeuvre = oeuvre;
                emprunts.add(emprunt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableViewE.setItems(emprunts);

        //livres disponibles

        String recupererExemplaires = "Select numero, isbn from exemplaires where emprunte = false";
        numeroColumnD.setCellValueFactory(new PropertyValueFactory<>("numero"));
        editionColumnD.setCellValueFactory(new PropertyValueFactory<>("edition"));
        oeuvreColumnD.setCellValueFactory(new PropertyValueFactory<>("oeuvre"));

        ObservableList<Exemplaire> exemplaires = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererExemplaires)) {
            while (resultSet.next()) {
                int numero = resultSet.getInt("numero");
                Edition edition = new Edition(resultSet.getLong("isbn"));
                edition.updateWithIsbn(con);
                Oeuvre oeuvre = edition.oeuvre;
                Exemplaire exemplaire = new Exemplaire();
                exemplaire.edition=edition;
                exemplaire.numero=numero;
                exemplaire.oeuvre=oeuvre;
                exemplaires.add(exemplaire);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableViewD.setItems(exemplaires);

        //livres indisponibles
        String recupererExemplairesI = "Select numero, isbn from exemplaires where emprunte = true";
        numeroColumnI.setCellValueFactory(new PropertyValueFactory<>("numero"));
        editionColumnI.setCellValueFactory(new PropertyValueFactory<>("edition"));
        oeuvreColumnI.setCellValueFactory(new PropertyValueFactory<>("oeuvre"));

        ObservableList<Exemplaire> exemplairesI = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererExemplaires)) {
            while (resultSet.next()) {
                int numero = resultSet.getInt("numero");
                Edition edition = new Edition(resultSet.getLong("isbn"));
                edition.updateWithIsbn(con);
                Oeuvre oeuvre = edition.oeuvre;
                Exemplaire exemplaire = new Exemplaire();
                exemplaire.edition=edition;
                exemplaire.numero=numero;
                exemplaire.oeuvre=oeuvre;
                exemplairesI.add(exemplaire);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableViewI.setItems(exemplairesI);

        tableViewU.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idUsagerSelectionne = newValue.getId();
                try {
                    newWindow("historique.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
