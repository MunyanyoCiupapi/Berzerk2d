package org.berzerk.controllers;

import org.berzerk.Berzerk;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    public Button startButton;
    @FXML
    public Button quitButton;
    public AnchorPane menuForm;

    public void initialize() {
        menuForm.setStyle("-fx-background-color: black;");
    }

    public void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Berzerk.class.getResource("/gameboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.BLACK);
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.setTitle("Berzerk");
        stage.setScene(scene);
        stage.show();

        BerzerkController controller = fxmlLoader.getController();
        controller.setStage(stage);
    }

    public void quitGame() throws IOException {
        System.exit(0);
    }
}