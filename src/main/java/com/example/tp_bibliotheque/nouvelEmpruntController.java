package com.example.tp_bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class nouvelEmpruntController {
    @FXML
    private ComboBox<String> ccbGestionnaireEmprunt;
    @FXML
    private ComboBox<String> ccbUsager;
    @FXML
    private ComboBox<String> ccbOeuvre;
    @FXML
    private ComboBox<String> ccbEdition;
    @FXML
    private TextField numeroExemplaire;
    @FXML
    private Label dateEmprunt;
    @FXML
    private Label deadlineEmprunt;

}
