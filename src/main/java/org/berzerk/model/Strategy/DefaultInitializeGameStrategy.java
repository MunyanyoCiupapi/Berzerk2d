package org.berzerk.model.Strategy;

import javafx.animation.AnimationTimer;
import org.berzerk.controllers.BerzerkController;
import org.berzerk.model.Map;

public class DefaultInitializeGameStrategy implements InitializationStrategy{

    @Override
    public void initialize(BerzerkController controller) {

        controller.gameForm.setStyle("-fx-background-color: black;");

        controller.gameForm.getChildren().add(controller.getPlayer().getRepresentation());
        controller.getPlayer().updateRepresentationPosition();

        controller.gameForm.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                controller.getInputs();
                controller.drawMaze(new Map());
            }
        });

        controller.gameForm.setOnMouseClicked(controller::mouseShoot);

        controller.setGameLoop(new AnimationTimer() {
            public void handle(long now) {
                controller.updateGame();
            }
        });
        controller.getGameLoop().start();

        controller.gameForm.widthProperty().addListener((obs, oldVal, newVal) -> {
            controller.drawMaze(controller.getMap());
            if (controller.gameForm.getWidth() > 0 && controller.gameForm.getHeight() > 0) {
                int cellWidth = (int)(controller.gameForm.getWidth() / controller.getMap().getWidth());
                int cellHeight = (int)(controller.gameForm.getHeight() / controller.getMap().getHeight());
                controller.spawnEnemies(controller.getNumOfEnemies(), cellWidth, cellHeight);
            }
        });

        controller.gameForm.heightProperty().addListener((obs, oldVal, newVal) -> {
            controller.drawMaze(controller.getMap());
            if (controller.gameForm.getWidth() > 0 && controller.gameForm.getHeight() > 0) {
                int cellWidth = (int)(controller.gameForm.getWidth() / controller.getMap().getWidth());
                int cellHeight = (int)(controller.gameForm.getHeight() / controller.getMap().getHeight());
                controller.spawnEnemies(controller.getNumOfEnemies(), cellWidth, cellHeight);
            }
        });
    }
}
