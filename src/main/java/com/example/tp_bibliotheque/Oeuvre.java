package com.example.tp_bibliotheque;

import java.sql.*;

public class Oeuvre {
    int id;
    String titre;
    int premiere_parution;
    String mot_cle1;
    String mot_cle2;
    String mot_cle3;
    String mot_cle4;
    String mot_cle5;
    public Oeuvre(String titre, int premiere_parution, String mot_cle1) {
        this.titre = titre;
        this.premiere_parution = premiere_parution;
        this.mot_cle1 = mot_cle1;
    }
    public ResultSet ajouter (Connection con) throws SQLException {
        String oeuvreSql = "INSERT INTO oeuvres (titre, premiere_parution, mot_cle1) VALUES (?, ?, ?)";
        PreparedStatement prep_stmt_oeuvre = con.prepareStatement(oeuvreSql, Statement.RETURN_GENERATED_KEYS);
        prep_stmt_oeuvre.setString(1, this.titre);
        prep_stmt_oeuvre.setInt(2, this.premiere_parution);
        prep_stmt_oeuvre.setString(3, this.mot_cle1);
        prep_stmt_oeuvre.executeUpdate();
        return prep_stmt_oeuvre.getGeneratedKeys();
    }
}
