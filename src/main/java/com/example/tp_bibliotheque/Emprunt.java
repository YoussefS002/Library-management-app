package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.Date;

public class Emprunt {
    Usager usager;
    Oeuvre oeuvre;
    long isbn;
    int numero;
    LocalDate dateEmprunt;
    LocalDate deadline;
    LocalDate dateRetour;
}
