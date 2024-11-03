package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

public class Emprunt {
    int id;
    Usager usager;
    Oeuvre oeuvre;
    Edition edition;
    int numero;
    LocalDate dateEmprunt;
    LocalDate deadline;
    LocalDate dateRetour;

    public int getId() {
        return id;
    }

    public Usager getUsager() {

        return usager;
    }
    public Oeuvre getOeuvre() {
        return oeuvre;
    }
    public Edition getEdition() {
        return edition;
    }
    public int getNumero() {
        return numero;
    }
    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public LocalDate getDateRetour() {
        return dateRetour;
    }
    public void updateWithId (Connection con) throws SQLException {
        String sql = "select id_usager, isbn, numero_exemplaire, date_emprunt, deadline, date_retour from emprunts WHERE id_emprunt = " + this.id;
        Statement statement1 = con.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(sql);
        while (resultSet1.next()) {
            int id_usager = resultSet1.getInt("id_usager");
            Usager usager = new Usager("?");
            usager.id=id_usager;
            usager.updateWithId(con);
            this.usager=usager;

            long isbn = resultSet1.getLong("isbn");
            Edition edition = new Edition(isbn);
            edition.updateWithIsbn(con);
            this.edition=edition;

            Oeuvre oeuvre = edition.oeuvre;
            this.oeuvre = oeuvre;

            numero = resultSet1.getInt("numero_exemplaire");
            dateEmprunt = resultSet1.getDate("date_emprunt").toLocalDate();
            dateRetour = resultSet1.getDate("date_retour") != null ? resultSet1.getDate("date_retour").toLocalDate() : null;
            deadline = resultSet1.getDate("deadline").toLocalDate();
        }
    }
}
