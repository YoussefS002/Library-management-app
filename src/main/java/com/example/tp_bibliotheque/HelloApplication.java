package com.example.tp_bibliotheque;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[ ] args) throws Exception {
        launch();

    }
}


/*try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/biblio","root","0000");
// con est un objet de type connection
            System.out.println("Vous etes connectes !");

            Statement stmt=con.createStatement();
            String sql0 = "DROP TABLE IF EXISTS Athletes ;";
            String sql00 = "CREATE TABLE IF NOT EXISTS Athletes (Name VARCHAR(255), Country VARCHAR(255), Discipline VARCHAR(255))";
            stmt.executeUpdate(sql0);
            stmt.executeUpdate(sql00);
            String sql1 = "INSERT INTO Athletes VALUES ('Jesse Owens', 'USA', 'Athletics')";
            stmt.executeUpdate(sql1);

            Scanner sc=new Scanner(System.in);
            System.out.println("Enter a line for the database");
            String nameUsr=sc.nextLine();
            String countryUsr=sc.nextLine();
            String disciplineUsr=sc.nextLine();

            String sql3 = "INSERT INTO Athletes VALUES (?, ?, ?)";
            PreparedStatement prep_stmt = con.prepareStatement(sql3);
            prep_stmt.setString(1,nameUsr);
            prep_stmt.setString(2,countryUsr);
            prep_stmt.setString(3,disciplineUsr);
            prep_stmt.executeUpdate();



            String lineText = null;
            BufferedReader lineReader = new BufferedReader(new FileReader("src/Athletes.csv"));
            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(";");
                String col1 = data[0];
                String col2 = data[1];
                String col3 = data[2];
                prep_stmt.setString(1,col1);
                prep_stmt.setString(2,col2);
                prep_stmt.setString(3,col3);
                prep_stmt.executeUpdate();
            }


            String sql2 = "SELECT * FROM Athletes";
            ResultSet rs2 = stmt.executeQuery(sql2) ;
            while(rs2.next()){
                System.out.println(rs2.getString(1)+" "+rs2.getString(2)+" "+rs2.getString(3));
            }

            con.close();

        } catch (Exception e){
            System.out.println(e);
        }*/