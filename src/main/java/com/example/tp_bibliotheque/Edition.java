package com.example.tp_bibliotheque;

import java.sql.*;

public class Edition {
    long isbn;
    String Editeur;
    int anneeEdition;
    Oeuvre oeuvre;
    int nbExemplaires;
    int nbDispos;
    public Edition(long isbn) {
        this.isbn = isbn;
    }
    public void updateWithIsbn (Connection con) throws SQLException {
        String recupererOeuvre ="Select id_oeuvre, editeur, nb_exemplaires, annee_edition from editions where isbn = ?";
        PreparedStatement ps = con.prepareStatement(recupererOeuvre);
        ps.setLong(1, isbn);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Editeur = rs.getString("editeur");
            anneeEdition = rs.getInt("annee_edition");
            oeuvre = new Oeuvre("?", 0, "?");
            oeuvre.id = rs.getInt("id_oeuvre");
            oeuvre.updateWithId(con);
            nbExemplaires = rs.getInt("nb_exemplaires");
        }

        String recupererDispos = "SELECT COUNT(*) FROM exemplaires WHERE isbn = ? AND emprunte = false";
        ps = con.prepareStatement(recupererDispos);
        ps.setLong(1, isbn);
        rs = ps.executeQuery();
        while (rs.next()) {
            nbDispos = rs.getInt(1);
        }
    }

    public void ajouter (Connection con) throws SQLException {
        String editionSql = "INSERT INTO editions (isbn, id_oeuvre, editeur, annee_edition, nb_exemplaires) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement prep_stmt_edition = con.prepareStatement(editionSql, Statement.RETURN_GENERATED_KEYS);
        prep_stmt_edition.setLong(1, this.isbn);
        prep_stmt_edition.setInt(2, this.oeuvre.id);
        prep_stmt_edition.setString(3, this.Editeur);
        prep_stmt_edition.setInt(4, this.anneeEdition);
        prep_stmt_edition.setInt(5, this.nbExemplaires);
        prep_stmt_edition.executeUpdate();

        String creationExemplairesSql = "INSERT INTO exemplaires(isbn) VALUES (?)";
        PreparedStatement prep_stmt_exemplaires = con.prepareStatement(creationExemplairesSql);
        prep_stmt_exemplaires.setLong(1, this.isbn);
        for (int i = 0; i < this.nbExemplaires; i++) {
            prep_stmt_exemplaires.executeUpdate();
        }
    }
    public void ajouterExp (Connection con, int nombre) throws SQLException {
        String recupNbSql = "SELECT nb_exemplaires FROM editions WHERE isbn = ?";
        PreparedStatement prep_stmt_recup = con.prepareStatement(recupNbSql);
        prep_stmt_recup.setLong(1, this.isbn);

        ResultSet resultSet = prep_stmt_recup.executeQuery();
        while (resultSet.next()) {
            this.nbExemplaires = resultSet.getInt("nb_exemplaires");
        }
        this.nbExemplaires+=nombre;

        String updateSql = "UPDATE editions SET nb_exemplaires = ? WHERE isbn = ?";
        PreparedStatement prep_stmt_edition = con.prepareStatement(updateSql);
        prep_stmt_edition.setInt(1, this.nbExemplaires);
        prep_stmt_edition.setLong(2, this.isbn);
        prep_stmt_edition.executeUpdate();

        String creationExemplairesSql = "INSERT INTO exemplaires(isbn) VALUES (?)";
        PreparedStatement prep_stmt_exemplaires = con.prepareStatement(creationExemplairesSql);
        prep_stmt_exemplaires.setLong(1, this.isbn);
        for (int i = 0; i < nombre; i++) {
            prep_stmt_exemplaires.executeUpdate();
        }
    }
    @Override
    public String toString() {
        return Long.toString(isbn);
    }
    public int getNbExemplaires() {
        return nbExemplaires;
    }
    public int getAnneeEdition() {
        return anneeEdition;
    }
    public String getEditeur() {
        return Editeur;
    }
    public long getIsbn() {
        return isbn;
    }
    public int getNbDispos() {
        return nbDispos;
    }
    public Oeuvre getOeuvre() {
        return oeuvre;
    }
}
