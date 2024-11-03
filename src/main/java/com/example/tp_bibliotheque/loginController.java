package com.example.tp_bibliotheque;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.*;

public class loginController {
    @FXML
    TextField emailTF;
    @FXML
    PasswordField passwordTF;
    static Usager currentUser;
    @FXML
    private void login() throws SQLException, IOException {
        String email = this.emailTF.getText().toLowerCase();
        String password = this.passwordTF.getText();
        String query = "SELECT * FROM usagers WHERE email = '" + email + "' AND motdepasse = '" + password + "'";
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            currentUser = new Usager(email);
            currentUser.motdepasse=password;
            currentUser.categorie=new Categorie("emprunteur");
            currentUser.updateWithEmail(con);

            Notifications.create()
                    .title("Connexion réussie")
                    .text("Bienvnue "+ currentUser.prenom+" "+ currentUser.nom+" !")
                    .showInformation();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);
                    return null;
                }
                @Override
                protected void succeeded() {
                    Stage stage = (Stage) emailTF.getScene().getWindow();
                    stage.close();
                }
            };
            new Thread(task).start();
            newWindow("main.fxml");
        } else {
            Notifications.create()
                    .title("Echec de connexion")
                    .text("Email ou mot de passe incorrect")
                    .showInformation();

            }
    }

    @FXML
    private void newWindow (String FXMLpath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpath));
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestion de bibliothèque");
        stage.setScene(new Scene(root1, 800, 600));
        stage.show();
    }

    @FXML
    private void goToNouvelUsager() throws IOException {
        newWindow("nouvelUsager.fxml");
    }
}
