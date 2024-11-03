package com.example.tp_bibliotheque;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class nouvelAuteurController {
    @FXML
    private AnchorPane mainContent;
    @FXML
    private VBox vBox;
    @FXML
    private TextField nomTF;
    @FXML
    private TextField prenomTF;
    @FXML
    private DatePicker naissanceDF;
    @FXML
    private void ajouterAuteur() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");

            Auteur auteur = new Auteur();
            auteur.nom = nomTF.getText();
            auteur.prenom = prenomTF.getText();
            auteur.dateNaissance = naissanceDF.getValue();

            String nouvelAuteurSql = "INSERT INTO auteurs(nom, prenom, date_naissance) VALUES (?, ?, ?) ";
            PreparedStatement prep_stmt = con.prepareStatement(nouvelAuteurSql);
            prep_stmt.setString(1, auteur.nom);
            prep_stmt.setString(2, auteur.prenom);
            prep_stmt.setDate(3, Date.valueOf(auteur.dateNaissance));
            prep_stmt.executeUpdate();
            con.close();

            Notifications.create()
                    .title("Auteur ajouté")
                    .text("L'auteur' "+ auteur.prenom + " " + auteur.nom +" a été ajouté.")
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
