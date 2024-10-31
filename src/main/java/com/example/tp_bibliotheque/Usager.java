package com.example.tp_bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usager {
    int id;
    String nom;
    String prenom;
    String email;
    String motdepasse;
    Categorie categorie;

    public Usager(String email) {
        this.email = email;
    }
    public void ajouter(Connection con) throws SQLException {
        String sql = "INSERT INTO usagers (nom, prenom, email, motdepasse, categorie) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement prep_stmt = con.prepareStatement(sql);
        prep_stmt.setString(1, this.nom);
        prep_stmt.setString(2, this.prenom);
        prep_stmt.setString(3, this.email);
        prep_stmt.setString(4, this.motdepasse);
        prep_stmt.setString(5, this.categorie.toString());
        prep_stmt.executeUpdate();
    }
    public void updateWithEmail(Connection con) throws SQLException {
        String nomRecupSql = "SELECT id_usager, prenom, nom, categorie FROM usagers WHERE email = ?";
        PreparedStatement prep_stmt_nomRecup = con.prepareStatement(nomRecupSql);
        prep_stmt_nomRecup.setString(1, this.email);
        ResultSet rs = prep_stmt_nomRecup.executeQuery();
        while (rs.next()) {
            this.id = rs.getInt("id_usager");
            this.nom = rs.getString("nom");
            this.prenom = rs.getString("prenom");
            this.categorie = new Categorie(rs.getString("categorie"));
            this.categorie.updateWithNom(con);
        }
    }
    public void updateWithId (Connection con) throws SQLException {
        String nomRecupSql = "SELECT email, prenom, nom, categorie FROM usagers WHERE id_usager = ?";
        PreparedStatement prep_stmt_nomRecup = con.prepareStatement(nomRecupSql);
        prep_stmt_nomRecup.setInt(1, this.id);
        ResultSet rs = prep_stmt_nomRecup.executeQuery();
        while (rs.next()) {
            this.email = rs.getString("email");
            this.nom = rs.getString("nom");
            this.prenom = rs.getString("prenom");
            this.categorie = new Categorie(rs.getString("categorie"));
            this.categorie.updateWithNom(con);
        }
    }
    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getEmail() {
        return email;
    }
    public Categorie getCategorie() {
        return categorie;
    }
    @Override
    public String toString() {
        return id + " - " + prenom + " " + nom;
    }
}
