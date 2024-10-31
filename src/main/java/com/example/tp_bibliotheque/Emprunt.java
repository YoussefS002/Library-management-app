package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

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

}
