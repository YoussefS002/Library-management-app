package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
    protected void ajouterUsager() {
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
            Usager nouvelUsager = new Usager(nomTF.getText(), prenomTF.getText(), emailTF.getText());
            nouvelUsager.ajouter(con);
            con.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
