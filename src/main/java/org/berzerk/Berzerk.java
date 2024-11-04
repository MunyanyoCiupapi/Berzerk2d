package org.berzerk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.berzerk.controllers.MenuController;


import java.io.IOException;

public class Berzerk extends Application {


    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Berzerk.class.getResource("/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Berzerk");
        stage.setScene(scene);
        stage.show();
        MenuController controller = fxmlLoader.getController();
    }
    public static void main(String[] args) {
        launch();
    }





}