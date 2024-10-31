package com.example.tp_bibliotheque;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class mainController {
    @FXML
    private Button gererCategoriesBtn;
    @FXML
    private Button adminBtn;
    @FXML
    private Button gestionnaireBtn;
    @FXML
    private Button emprunteurBtn;
    @FXML
    private Button listeRougeBtn;
    @FXML
    private Button retourButton;
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
    static int idEmpruntSelectionne;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private void initialize() {
        initData();
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
    private void newView (String FXMLpath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpath));
        Pane newLoadedPane = loader.load();
        mainContent.getChildren().setAll(newLoadedPane);
    }

    @FXML
    private void goToNouvelleOeuvre() throws IOException {
        newView("nouvelleOeuvre.fxml");
    }

    @FXML
    private void goToNouvelEmprunt() throws IOException {
        newView("nouvelEmprunt.fxml");
    }
    @FXML
    private void goToNouvelleEdition() throws IOException {
        newView("nouvelleEdition.fxml");
    }
    @FXML
    private void goToNouveauxExemplaires() throws IOException {
        newView("nouveauxExemplaires.fxml");
    }
    @FXML
    private void effectuerRetour() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
        String retourQuery = "UPDATE emprunts SET date_retour = ? WHERE id_emprunt = ?";
        PreparedStatement preparedStatement = con.prepareStatement(retourQuery);
        preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
        preparedStatement.setInt(2, idEmpruntSelectionne);
        preparedStatement.execute();
        Emprunt emprunt=new Emprunt();
        emprunt.id=idEmpruntSelectionne;
        emprunt.updateWithId(con);
        String retourQuery2 = "UPDATE exemplaires SET emprunte = false WHERE numero = ?";
        PreparedStatement preparedStatement2 = con.prepareStatement(retourQuery2);
        preparedStatement2.setInt(1, emprunt.numero);
        preparedStatement2.execute();
        initData();
    }

    @FXML
    private void gererCategories () throws IOException {
        newView("categories.fxml");
    }
    private void changerCategorie(String categorieNom) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
        Usager usager = new Usager("?");
        usager.id=idUsagerSelectionne;
        usager.updateWithId(con);

        Categorie categorie = new Categorie(categorieNom);
        categorie.updateWithNom(con);
        usager.categorie=categorie;

        String changerCategorieQuery = "UPDATE usagers SET categorie = ? WHERE id_usager = ?";
        PreparedStatement preparedStatement = con.prepareStatement(changerCategorieQuery);
        preparedStatement.setString(1, categorie.nom);
        preparedStatement.setInt(2, usager.id);
        preparedStatement.execute();
        initData();
    }


    @FXML
    private void rendreAdmin() throws SQLException {
        changerCategorie("admin");
    }
    @FXML
    private void rendreGestionnaire() throws SQLException {
        changerCategorie( "gestionnaire");
    }
    @FXML
    private void rendreEmprunteur() throws SQLException {
        changerCategorie( "emprunteur");
    }
    @FXML
    private void placerEnListeRouge() throws SQLException {
        changerCategorie( "liste rouge");
    }

    public void initData() {
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
        //dateRetourColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateRetour()));
        dateRetourColumn.setCellFactory(column -> new TableCell<Emprunt, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date == null) {
                    setText("Non rendu"); // Texte alternatif
                } else {
                    setText(date.toString()); // Affiche la date si elle existe
                }
            }
        });


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
                LocalDate dateRetour = null;
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
             ResultSet resultSet = statement.executeQuery(recupererExemplairesI)) {
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


        //click emprunt
        retourButton.setVisible(false);
        if (Objects.equals(loginController.currentUser.categorie.nom, "emprunteur")) {
            tableViewE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    idEmpruntSelectionne = newValue.getId();
                    retourButton.setVisible(true);
                }
            });
        }

        //click usager
        adminBtn.setVisible(false);
        gestionnaireBtn.setVisible(false);
        emprunteurBtn.setVisible(false);
        listeRougeBtn.setVisible(false);
        gererCategoriesBtn.setVisible(false);
        if (Objects.equals(loginController.currentUser.categorie.nom, "admin")) {
            gererCategoriesBtn.setVisible(true);
            tableViewU.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    idUsagerSelectionne = newValue.getId();
                    adminBtn.setVisible(true);
                    gestionnaireBtn.setVisible(true);
                    emprunteurBtn.setVisible(true);
                    listeRougeBtn.setVisible(true);
                }
            });
        }

        //doubleclick usager
        tableViewU.setRowFactory(tv -> {
            TableRow<Usager> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Usager usagerSelectionn = row.getItem();
                    idUsagerSelectionne = usagerSelectionn.getId();
                    try {
                        newWindow("historique.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

    }
}
