package com.example.tp_bibliotheque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Oeuvre {
    int id;
    String titre;
    int premiere_parution;
    String mot_cle1;
    String mot_cle2 = "";
    String mot_cle3 = "";
    String mot_cle4 = "";
    String mot_cle5 = "";
    String mots_cles;
    String auteurs;


    public ResultSet ajouter (Connection con) throws SQLException {
        String oeuvreSql = "INSERT INTO oeuvres (titre, premiere_parution, mot_cle1, mot_cle2, mot_cle3, mot_cle4, mot_cle5) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement prep_stmt_oeuvre = con.prepareStatement(oeuvreSql, Statement.RETURN_GENERATED_KEYS);
        prep_stmt_oeuvre.setString(1, this.titre);
        prep_stmt_oeuvre.setInt(2, this.premiere_parution);
        prep_stmt_oeuvre.setString(3, this.mot_cle1);
        prep_stmt_oeuvre.setString(4, this.mot_cle2);
        prep_stmt_oeuvre.setString(5, this.mot_cle3);
        prep_stmt_oeuvre.setString(6, this.mot_cle4);
        prep_stmt_oeuvre.setString(7, this.mot_cle5);
        prep_stmt_oeuvre.executeUpdate();
        return prep_stmt_oeuvre.getGeneratedKeys();
    }

    public String getTitre() {
        return titre;
    }
    public int getPremiere_parution() {
        return premiere_parution;
    }
    public String getMot_cle1() {
        return mot_cle1;
    }
    public String getAuteurs() {
        return auteurs;
    }
    public String getMots_cles() {
        return mots_cles;
    }
    public void updateAuteursWithId(Connection con) throws SQLException {
        String recupererAuteurs = "SELECT auteurs.nom, auteurs.prenom from auteurs JOIN oeuvres_auteurs ON auteurs.id_auteur=oeuvres_auteurs.id_auteur WHERE oeuvres_auteurs.id_oeuvre = " + id;
        Statement statement1 = con.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(recupererAuteurs);
        List<String> listeAuteurs = new ArrayList<>();
        while (resultSet1.next()) {
            String auteur = resultSet1.getString("prenom") + " " + resultSet1.getString("nom");
            listeAuteurs.add(auteur);
        }
        auteurs = String.join(", ", listeAuteurs);
    }

    public void updateMotsCles() {
        List<String> motsList = new ArrayList<>();
        if (mot_cle1!=null && !mot_cle1.isEmpty())
            motsList.add(mot_cle1);
        if (mot_cle2!=null && !mot_cle2.isEmpty())
            motsList.add(mot_cle2);
        if (mot_cle3!=null && !mot_cle3.isEmpty())
            motsList.add(mot_cle3);
        if (mot_cle4!=null && !mot_cle4.isEmpty())
            motsList.add(mot_cle4);
        if (mot_cle5!=null && !mot_cle5.isEmpty())
            motsList.add(mot_cle5);
        mots_cles = String.join(", ", motsList);
    }
    public void updateWithId (Connection con) throws SQLException {
        String sql = "select titre, premiere_parution, mot_cle1, mot_cle2, mot_cle3, mot_cle4, mot_cle5 from oeuvres WHERE id_oeuvre = " + this.id;
        Statement statement1 = con.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(sql);
        while (resultSet1.next()) {
            this.titre = resultSet1.getString("titre");
            this.premiere_parution = resultSet1.getInt("premiere_parution");
            this.mot_cle1 = resultSet1.getString("mot_cle1");
            this.mot_cle2 = resultSet1.getString("mot_cle2");
            this.mot_cle3 = resultSet1.getString("mot_cle3");
            this.mot_cle4 = resultSet1.getString("mot_cle4");
            this.mot_cle5 = resultSet1.getString("mot_cle5");
        }
        updateAuteursWithId(con);
        updateMotsCles();
    }
    @Override
    public String toString() {
        return id + " - " + titre;
    }
}
