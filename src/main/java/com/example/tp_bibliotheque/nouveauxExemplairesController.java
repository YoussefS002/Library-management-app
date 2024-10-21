package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.*;


public class nouveauxExemplairesController {
    @FXML
    private TextField tfIsbn;
    @FXML
    private TextField tfNbExemplaires;

    @FXML
    private void ajouterExemplaires() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");

        Edition editionAModifier = new Edition();
        editionAModifier.isbn = Integer.parseInt(tfIsbn.getText());
        int nombreAAjouter = Integer.parseInt(tfNbExemplaires.getText());
        editionAModifier.ajouterExp(con, nombreAAjouter);

        con.close();
    }
}
