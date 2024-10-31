package com.example.tp_bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.*;

public class categoriesController {
    @FXML
    private ComboBox<Categorie> cbCategories;
    @FXML
    private TextField empruntsMaxTF;
    @FXML
    private TextField dureeMaxTF;
    @FXML
    private AnchorPane mainContent;


    @FXML
    private void newView (String FXMLpath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpath));
        Pane newLoadedPane = loader.load();
        mainContent.getChildren().setAll(newLoadedPane);
    }
    @FXML
    private void initialize() {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String categories_query = "SELECT nom FROM categories";
        try (Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(categories_query)) {
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                Categorie categorie = new Categorie(nom);

                categorie.updateWithNom(con);
                categories.add(categorie);
            }
        } catch (Exception e) {
             throw new RuntimeException(e);
        }
        cbCategories.setItems(categories);
    }
    @FXML
    private void majCategorie() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","0000");
        Categorie categorie = cbCategories.getValue();
        int empruntsMax = Integer.parseInt(empruntsMaxTF.getText());
        int dureeMax = Integer.parseInt(dureeMaxTF.getText());
        String query = "UPDATE categories SET emprunts_max = ? AND duree_max = ? WHERE nom = ?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, empruntsMax);
        preparedStatement.setInt(2, dureeMax);
        preparedStatement.setString(3, categorie.nom);
        preparedStatement.execute();
        con.close();
        Notifications.create()
                .title("Catégorie mise à jour")
                .text("La catégorie " + categorie.nom + " a bien été modifiée.")
                .showInformation();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                return null;
            }
            @Override
            protected void succeeded() {
                try {
                    retour();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(task).start();
    }

    @FXML
    private void retour() throws IOException {
        newView("main.fxml");
    }
}
