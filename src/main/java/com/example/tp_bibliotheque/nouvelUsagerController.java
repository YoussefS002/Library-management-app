package com.example.tp_bibliotheque;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                nouvelUsager.motdepasse=hashPassword(motdepasse);
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
    private void newWindow (String FXMLpath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpath));
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestion de bibliothèque");
        stage.setScene(new Scene(root1, 400, 300));
        stage.show();
    }
    @FXML
    private void retour() throws IOException {
        Stage stage = (Stage) mainContent.getScene().getWindow();
        stage.close();
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
