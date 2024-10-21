package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class nouvelEmpruntController {
    @FXML
    private ComboBox<String> cbGestionnaireEmprunt;
    @FXML
    private ComboBox<String> cbUsager;
    @FXML
    private ComboBox<String> cbOeuvre;
    @FXML
    private ComboBox<String> cbEdition;
    @FXML
    private TextField numeroExemplaire;
    @FXML
    private Label dateEmprunt;
    @FXML
    private Label deadlineEmprunt;

}
