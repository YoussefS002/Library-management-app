package com.example.tp_bibliotheque;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class nouvelUsagerController {
    @FXML
    TextField nomTF;
    @FXML
    TextField prenomTF;
    @FXML
    TextField emailTF;
    @FXML
    VBox vBox;
    @FXML
    protected void ajouterUsager() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
            Usager nouvelUsager = new Usager(nomTF.getText(), prenomTF.getText(), emailTF.getText());
            nouvelUsager.ajouter(con);
            con.close();

            Notifications.create()
                    .title("Oeuvre ajoutée")
                    .text("L'usager "+ nouvelUsager.prenom + " " + nouvelUsager.prenom +" a été ajouté.")
                    .showInformation();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);
                    return null;
                }

                @Override
                protected void succeeded() {
                    Stage stage = (Stage) vBox.getScene().getWindow();
                    stage.close();
                }
            };
            new Thread(task).start();

        } catch (Exception e){
            System.out.println(e);
        }
    }
}
