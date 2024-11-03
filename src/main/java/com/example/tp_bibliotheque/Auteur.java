package com.example.tp_bibliotheque;

import java.sql.*;
import java.time.LocalDate;

public class Auteur {
    int id;
    String nom;
    String prenom;
    LocalDate dateNaissance;
    public int recupererId (Connection con) throws SQLException {
        String idRecupSql = "SELECT id_auteur FROM auteurs WHERE nom = ? AND prenom = ? AND date_naissance = ?";
        PreparedStatement prep_stmt_idRecup = con.prepareStatement(idRecupSql);
        prep_stmt_idRecup.setString(1, this.nom);
        prep_stmt_idRecup.setString(2, this.prenom);
        prep_stmt_idRecup.setDate(3, Date.valueOf(this.dateNaissance));
        ResultSet ids = prep_stmt_idRecup.executeQuery();
        while (ids.next()) {
            this.id = ids.getInt("id_auteur");
        }
        return this.id;
    }
}
