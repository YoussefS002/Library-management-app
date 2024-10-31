package com.example.tp_bibliotheque;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Objects;

public class nouvelUsagerController {
    @FXML
    TextField nomTF;
    @FXML
    TextField prenomTF;
    @FXML
    TextField emailTF;
    @FXML
    PasswordField passwordTF;
    @FXML
    PasswordField confirmationTF;
    @FXML
    VBox vBox;
    @FXML
    protected void ajouterUsager() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
            Usager nouvelUsager = new Usager(emailTF.getText());
            nouvelUsager.nom = nomTF.getText();
            nouvelUsager.prenom = prenomTF.getText();
            nouvelUsager.categorie = new Categorie("emprunteur");
            String motdepasse = passwordTF.getText();
            String confirmation = confirmationTF.getText();
            if (Objects.equals(motdepasse, confirmation)){
                nouvelUsager.motdepasse=motdepasse;
                nouvelUsager.ajouter(con);
                con.close();

                Notifications.create()
                        .title("Emprunteur ajouté")
                        .text("L'emprunteur "+ nouvelUsager.prenom + " " + nouvelUsager.prenom +" a été ajouté.")
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
            } else {
                Notifications.create()
                        .title("Les mots de passe ne correspondent pas.")
                        .text("Les mots de passe ne correspondent pas. Merci de les saisir à nouveau.")
                        .showInformation();
            }


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
