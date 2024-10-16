package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.*;


public class HelloController {
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
}