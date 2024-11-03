package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class nouvelleOeuvreController {
    @FXML
    private HBox hBox;
    @FXML
    TextField titreTF;
    @FXML
    TextField premiere_parutionTF;
    @FXML
    TextField mot_cle1TF;
    @FXML
    TextField mot_cle2TF;
    @FXML
    TextField mot_cle3TF;
    @FXML
    TextField mot_cle4TF;
    @FXML
    TextField mot_cle5TF;
    @FXML
    private VBox vBox;
    private CheckComboBox<Auteur> checkComboBox;

    public void initialize() {
        ObservableList<Auteur> auteurs = FXCollections.observableArrayList();
        String auteurs_query = "SELECT id_auteur FROM auteurs";
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(auteurs_query)) {
             while (resultSet.next()) {
                 int id_auteur = resultSet.getInt("id_auteur");
                 Auteur auteur = new Auteur();
                 auteur.id=id_auteur;
                 auteur.updateWithId(con);
                 auteurs.add(auteur);
             }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        checkComboBox = new CheckComboBox<Auteur>(auteurs);
        hBox.getChildren().add(1, checkComboBox);
    }
    @FXML
    protected void ajouterOeuvre() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");

            Oeuvre nouvelleOeuvre = new Oeuvre();
            nouvelleOeuvre.titre = titreTF.getText();
            nouvelleOeuvre.premiere_parution = Integer.parseInt(premiere_parutionTF.getText());
            nouvelleOeuvre.mot_cle1=mot_cle1TF.getText();
            nouvelleOeuvre.mot_cle2=mot_cle2TF.getText();
            nouvelleOeuvre.mot_cle3=mot_cle3TF.getText();
            nouvelleOeuvre.mot_cle4=mot_cle4TF.getText();
            nouvelleOeuvre.mot_cle5=mot_cle5TF.getText();

            ResultSet generatedKeys = nouvelleOeuvre.ajouter(con);
            while (generatedKeys.next()) {
                nouvelleOeuvre.id = generatedKeys.getInt(1);
            }

            ObservableList<Auteur> selectedAuthors = checkComboBox.getCheckModel().getCheckedItems();
            for (int i = 0; i < selectedAuthors.size(); i++) {
                Auteur auteurSelectionne = selectedAuthors.get(i);
                String oeuvres_auteursSql = "INSERT INTO oeuvres_auteurs(id_oeuvre, id_auteur) VALUES (?, ?) ";
                PreparedStatement prep_stmt_oeuvres_auteurs = con.prepareStatement(oeuvres_auteursSql);
                prep_stmt_oeuvres_auteurs.setInt(1, nouvelleOeuvre.id);
                prep_stmt_oeuvres_auteurs.setInt(2, auteurSelectionne.id);
                prep_stmt_oeuvres_auteurs.executeUpdate();
            }
            con.close();

            Notifications.create()
                    .title("Oeuvre ajoutée")
                    .text("L'oeuvre "+ nouvelleOeuvre.titre +" a été ajoutée.")
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
        } catch (Exception e){
            System.out.println(e);
        }

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
