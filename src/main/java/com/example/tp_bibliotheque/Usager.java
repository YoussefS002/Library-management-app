package com.example.tp_bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Usager {
    int id;
    String nom;
    String prenom;
    String email;

    public Usager(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }
    public void ajouter(Connection con) throws SQLException {
        String sql = "INSERT INTO usagers (nom, prenom, email) VALUES (?, ?, ?)";
        PreparedStatement prep_stmt = con.prepareStatement(sql);
        prep_stmt.setString(1, this.nom);
        prep_stmt.setString(2, this.prenom);
        prep_stmt.setString(3, this.email);
        prep_stmt.executeUpdate();
    }
}
