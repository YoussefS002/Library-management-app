package com.example.tp_bibliotheque;

import java.sql.*;

public class Categorie {
    String nom;
    int emprunts_max;
    int duree_max;

    public void update() throws SQLException {
        String updateSql = "SELECT emprunts_max, duree_max FROM categories WHERE nom = ?";
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
        PreparedStatement ps = con.prepareStatement(updateSql);
        ps.setString(1, this.nom);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            this.emprunts_max = rs.getInt("emprunts_max");
            this.duree_max = rs.getInt("duree_max");
        }
    }
}
