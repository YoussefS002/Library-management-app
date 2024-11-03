package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    private Button nouvelleOeuvreBtn;
    @FXML
    private Button nouvelleEditionBtn;
    @FXML
    private Button nouvelAuteurBtn;
    @FXML
    private Button nouveauxExemplairesBtn;
    @FXML
    private Tab empruntsTab;
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
    private TableView<Edition> tableViewD;
    @FXML
    private TableColumn<Edition, Long> isbnColumnD;
    @FXML
    private TableColumn<Edition, String> editeurColumnD;
    @FXML
    private TableColumn<Edition, Integer> nbDisposColumnD;
    @FXML
    private TableColumn<Edition, Oeuvre> oeuvreColumnD;


    @FXML
    private TableView<Edition> tableViewI;
    @FXML
    private TableColumn<Edition, Long> isbnColumnI;
    @FXML
    private TableColumn<Edition, String> editeurColumnI;
    @FXML
    private TableColumn<Edition, Oeuvre> oeuvreColumnI;

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
    static int idOeuvreSelectionnee;

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
        stage.setScene(new Scene(root1, 1000, 600));
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
    private void goToNouvelAuteur() throws IOException {
        newView("nouvelAuteur.fxml");
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
        if (LocalDate.now().isAfter(emprunt.getDeadline())) {
            String listeRougeQuery = "UPDATE usagers SET categorie = 'liste rouge' WHERE id_usager = ? ";
            PreparedStatement preparedStatement3 = con.prepareStatement(listeRougeQuery);
            preparedStatement3.setInt(1, idUsagerSelectionne);
            preparedStatement3.execute();
        }
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
    @FXML
    TextField searchField;
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
                Oeuvre oeuvreAAfficher = new Oeuvre();
                oeuvreAAfficher.id = id_oeuvre;
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
            empruntsTab.setText("Mes emprunts");
            usagerColumn.setVisible(false);
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

                LocalDate dateRetour = resultSet.getDate("date_retour") != null ? resultSet.getDate("date_retour").toLocalDate() : null;


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

        String recupererEditions = "Select isbn from editions";
        isbnColumnD.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        editeurColumnD.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        nbDisposColumnD.setCellValueFactory(new PropertyValueFactory<>("nbDispos"));
        oeuvreColumnD.setCellValueFactory(new PropertyValueFactory<>("oeuvre"));

        ObservableList<Edition> editions = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererEditions)) {
            while (resultSet.next()) {
                long isbn = resultSet.getLong("isbn");
                Edition edition = new Edition(isbn);
                edition.updateWithIsbn(con);
                if (edition.nbDispos > 0)
                    editions.add(edition);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FilteredList<Edition> filteredEditions = new FilteredList<>(editions, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEditions.setPredicate(edition -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return edition.getOeuvre().toString().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tableViewD.setItems(filteredEditions);

        //livres indisponibles

        isbnColumnI.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        editeurColumnI.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        oeuvreColumnI.setCellValueFactory(new PropertyValueFactory<>("oeuvre"));

        editions = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(recupererEditions)) {
            while (resultSet.next()) {
                long isbn = resultSet.getLong("isbn");
                Edition edition = new Edition(isbn);
                edition.updateWithIsbn(con);
                if (edition.nbDispos == 0)
                    editions.add(edition);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableViewI.setItems(editions);

        //click emprunt
        retourButton.setVisible(false);
        tableViewE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Emprunt empruntSelectionne = newValue;
                idEmpruntSelectionne = empruntSelectionne.getId();
                if (empruntSelectionne.usager.id==loginController.currentUser.id && empruntSelectionne.dateRetour==null) {
                    retourButton.setVisible(true);
                }

            }
        });


        //permissions
        adminBtn.setVisible(false);
        gestionnaireBtn.setVisible(false);
        emprunteurBtn.setVisible(false);
        listeRougeBtn.setVisible(false);
        if (Objects.equals(loginController.currentUser.categorie.nom, "gestionnaire"))
            gererCategoriesBtn.setVisible(false);
        tableViewU.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idUsagerSelectionne = newValue.getId();
                if (Objects.equals(loginController.currentUser.categorie.nom, "admin")) {
                    adminBtn.setVisible(true);
                    gestionnaireBtn.setVisible(true);
                }
                emprunteurBtn.setVisible(true);
                listeRougeBtn.setVisible(true);
            }
        });

        nouvelleOeuvreBtn.setVisible(false);
        nouvelAuteurBtn.setVisible(false);
        nouvelleEditionBtn.setVisible(false);
        nouveauxExemplairesBtn.setVisible(false);
        if (Objects.equals(loginController.currentUser.categorie.nom, "admin") || Objects.equals(loginController.currentUser.categorie.nom, "gestionnaire")) {
            nouvelleOeuvreBtn.setVisible(true);
            nouvelAuteurBtn.setVisible(true);
            nouvelleEditionBtn.setVisible(true);
            nouveauxExemplairesBtn.setVisible(true);
        }

        //doubleclick usager
        tableViewU.setRowFactory(tv -> {
            TableRow<Usager> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Usager usagerSelectionne = row.getItem();
                    idUsagerSelectionne = usagerSelectionne.getId();
                    try {
                        newWindow("historique.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

        //doubleclick oeuvre
        tableView.setRowFactory(tv -> {
            TableRow<Oeuvre> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Oeuvre oeuvreSelectionnee = row.getItem();
                    idOeuvreSelectionnee = oeuvreSelectionnee.id;
                    try {
                        newWindow("editions.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

    }
    @FXML
    private void disconnect() throws IOException {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
        newWindow("login.fxml");
    }
}
