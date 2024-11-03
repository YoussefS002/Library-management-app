package com.example.tp_bibliotheque;

import java.sql.*;
import java.time.LocalDate;

public class Auteur {
    int id;
    String nom;
    String prenom;
    LocalDate dateNaissance;

    public void updateWithId(Connection con) throws SQLException {
        String sql = "select nom, prenom, date_naissance from auteurs WHERE id_auteur = " + this.id;
        Statement statement1 = con.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(sql);
        while (resultSet1.next()) {
            this.nom = resultSet1.getString("nom");
            this.prenom = resultSet1.getString("prenom");
            this.dateNaissance = resultSet1.getDate("date_naissance").toLocalDate();
        }
    }
    @Override
    public String toString() {
        return id + " - " + nom + " " + prenom;
    }
}
