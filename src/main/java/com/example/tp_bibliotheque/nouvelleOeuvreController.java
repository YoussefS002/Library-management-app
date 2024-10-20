package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.sql.*;

public class nouvelleOeuvreController {
    @FXML
    TextField titreTF;
    @FXML
    TextField premiere_parutionTF;
    @FXML
    TextField mot_cle1TF;
    @FXML
    private VBox vBox;
    private CheckComboBox<String> checkComboBox;

    public void initialize() {
        ObservableList<String> auteurs = FXCollections.observableArrayList();
        String auteurs_query = "SELECT prenom, nom, date_naissance FROM auteurs";
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(auteurs_query)) {
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String date_naissance = resultSet.getString("date_naissance");
                auteurs.add(prenom + " " + nom + " " + date_naissance);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        checkComboBox = new CheckComboBox(auteurs);
        vBox.getChildren().add(3, checkComboBox);
    }
    @FXML
    protected void ajouterOeuvre() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");

            Oeuvre nouvelleOeuvre = new Oeuvre(titreTF.getText(), Integer.parseInt(premiere_parutionTF.getText()), mot_cle1TF.getText());
            ResultSet generatedKeys = nouvelleOeuvre.ajouter(con);
            while (generatedKeys.next()) {
                nouvelleOeuvre.id = generatedKeys.getInt(1);
            }

            ObservableList<String> selectedAuthors = checkComboBox.getCheckModel().getCheckedItems();
            for (int i = 0; i < selectedAuthors.size(); i++) {
                String nomPrenomDateAuteur = selectedAuthors.get(i);
                String[] L = nomPrenomDateAuteur.split(" ");
                Auteur auteurSelectionne = new Auteur();
                auteurSelectionne.prenom = L[0];
                auteurSelectionne.nom = L[1];
                auteurSelectionne.dateNaissance = L[2];
                auteurSelectionne.id = auteurSelectionne.recupererId(con);
                String oeuvres_auteursSql = "INSERT INTO oeuvres_auteurs(id_oeuvre, id_auteur) VALUES (?, ?) ";
                PreparedStatement prep_stmt_oeuvres_auteurs = con.prepareStatement(oeuvres_auteursSql);
                prep_stmt_oeuvres_auteurs.setInt(1, nouvelleOeuvre.id);
                prep_stmt_oeuvres_auteurs.setInt(2, auteurSelectionne.id);
                prep_stmt_oeuvres_auteurs.executeUpdate();
            }

            con.close();
        } catch (Exception e){
            System.out.println(e);
        }

    }
}
