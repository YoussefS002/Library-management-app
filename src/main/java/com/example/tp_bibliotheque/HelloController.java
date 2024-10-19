package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.sql.*;


public class HelloController {
    @FXML
    TextField nomTF;
    @FXML
    TextField prenomTF;
    @FXML
    TextField emailTF;
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
    protected void ajouterUsager() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
            String sql = "INSERT INTO usagers (nom, prenom, email) VALUES (?, ?, ?)";

            String nom = nomTF.getText();
            String prenom = prenomTF.getText();
            String email = emailTF.getText();

            PreparedStatement prep_stmt = con.prepareStatement(sql);
            prep_stmt.setString(1, nom);
            prep_stmt.setString(2, prenom);
            prep_stmt.setString(3, email);
            prep_stmt.executeUpdate();

            con.close();

        } catch (Exception e){
            System.out.println(e);
        }

    }
    @FXML
    protected void ajouterOeuvre() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");

            String oeuvreSql = "INSERT INTO oeuvres (titre, premiere_parution, mot_cle1) VALUES (?, ?, ?)";
            String titre = titreTF.getText();
            int premiere_parution = Integer.parseInt(premiere_parutionTF.getText());
            String mot_cle1 = mot_cle1TF.getText();
            PreparedStatement prep_stmt_oeuvre = con.prepareStatement(oeuvreSql, Statement.RETURN_GENERATED_KEYS);
            prep_stmt_oeuvre.setString(1, titre);
            prep_stmt_oeuvre.setInt(2, premiere_parution);
            prep_stmt_oeuvre.setString(3, mot_cle1);
            prep_stmt_oeuvre.executeUpdate();
            ResultSet generatedKeys = prep_stmt_oeuvre.getGeneratedKeys();
            int id_oeuvre=0;
            while (generatedKeys.next()) {
                id_oeuvre = generatedKeys.getInt(1);
            }

            String idRecupSql = "SELECT id_auteur FROM auteurs WHERE nom = ? AND prenom = ? AND date_naissance = ?";
            ObservableList<String> selectedAuthors = checkComboBox.getCheckModel().getCheckedItems();
            for (int i = 0; i < selectedAuthors.size(); i++) {
                String nomPrenomDateAuteur = selectedAuthors.get(i);
                String[] L = nomPrenomDateAuteur.split(" ");
                String prenomAuteur = L[0];
                String nomAuteur = L[1];
                String dateAuteur = L[2];
                PreparedStatement prep_stmt_idRecup = con.prepareStatement(idRecupSql);
                prep_stmt_idRecup.setString(1, nomAuteur);
                prep_stmt_idRecup.setString(2, prenomAuteur);
                prep_stmt_idRecup.setString(3, dateAuteur);
                ResultSet ids = prep_stmt_idRecup.executeQuery();
                int id_auteur =0;
                while (ids.next()) {
                    id_auteur = ids.getInt("id_auteur");
                }

                String oeuvres_auteursSql = "INSERT INTO oeuvres_auteurs(id_oeuvre, id_auteur) VALUES (?, ?) ";
                PreparedStatement prep_stmt_oeuvres_auteurs = con.prepareStatement(oeuvres_auteursSql);
                prep_stmt_oeuvres_auteurs.setInt(1, id_oeuvre);
                prep_stmt_oeuvres_auteurs.setInt(2, id_auteur);
                prep_stmt_oeuvres_auteurs.executeUpdate();
            }



            con.close();
        } catch (Exception e){
            System.out.println(e);
        }

    }
}