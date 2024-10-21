package com.example.tp_bibliotheque;

import java.sql.*;

public class Edition {
    int isbn;
    String Editeur;
    int anneeEdition;
    Oeuvre oeuvre;
    int nbExemplaires;

    public void ajouter (Connection con) throws SQLException {
        String editionSql = "INSERT INTO editions (isbn, id_oeuvre, editeur, annee_edition, nb_exemplaires) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement prep_stmt_edition = con.prepareStatement(editionSql, Statement.RETURN_GENERATED_KEYS);
        prep_stmt_edition.setInt(1, this.isbn);
        prep_stmt_edition.setInt(2, this.oeuvre.id);
        prep_stmt_edition.setString(3, this.Editeur);
        prep_stmt_edition.setInt(4, this.anneeEdition);
        prep_stmt_edition.setInt(5, this.nbExemplaires);
        prep_stmt_edition.executeUpdate();
    }
}
