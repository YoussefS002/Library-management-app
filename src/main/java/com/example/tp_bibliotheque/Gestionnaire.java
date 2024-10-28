package com.example.tp_bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Gestionnaire {
    String nom;
    String prenom;
    String email;
    String motdepasse;
    Categorie categorie;

    public Gestionnaire(String email) {
        this.email = email;
    }

    public void update (Connection con) throws SQLException {
        String nomRecupSql = "SELECT prenom, nom FROM usagers WHERE email = ?";
        PreparedStatement prep_stmt_nomRecup = con.prepareStatement(nomRecupSql);
        prep_stmt_nomRecup.setString(1, this.email);
        ResultSet rs = prep_stmt_nomRecup.executeQuery();
        while (rs.next()) {
            this.nom = rs.getString("nom");
            this.prenom = rs.getString("prenom");
        }
    }
    public void updateCategorie (Connection con) throws SQLException {
        String categorieRecupSql = "SELECT categorie FROM usagers WHERE email = ?";
        PreparedStatement prep_stmt_categorieRecup = con.prepareStatement(categorieRecupSql);
        prep_stmt_categorieRecup.setString(1, this.email);
        ResultSet categories = prep_stmt_categorieRecup.executeQuery();
        while (categories.next()) {
            this.categorie = new Categorie();
            this.categorie.nom = categories.getString("categorie");
        }
        this.categorie.update();
    }
}
