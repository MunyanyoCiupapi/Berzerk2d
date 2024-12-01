package org.berzerk.model.Strategy;

import javafx.scene.input.KeyCode;
import org.berzerk.controllers.BerzerkController;
import org.berzerk.model.Enemy;


public class DefaultGameUpdateStrategy implements GameUpdateStrategy {

    @Override
    public void update(BerzerkController controller) {
        int cellWidth = (int)(controller.gameForm.getWidth() / controller.getMap().getWidth());
        int cellHeight = (int)(controller.gameForm.getHeight() / controller.getMap().getHeight());


        for (KeyCode key : controller.getKeysPressed()) {
            controller.getPlayer().move(key, (int) controller.gameForm.getWidth(), (int) controller.gameForm.getHeight(), cellWidth, cellHeight, controller.getMazeWalls());
        }

        for (Enemy enemy : controller.getEnemies()) {
            enemy.move(null, (int) controller.gameForm.getWidth(), (int) controller.gameForm.getHeight(), cellWidth, cellHeight, controller.getMazeWalls());
        }

        for (Enemy enemy : controller.getEnemies()) {
            if (controller.getPlayer().collidesWith(enemy)) {
                controller.restartGame();
                return;
            }
        }

        controller.getProjectiles().removeIf(projectile -> {
            boolean projectileHit = false;

            for (Enemy enemy : controller.getEnemies()) {
                if (projectile.getRepresentation().getBoundsInParent().intersects(enemy.getRepresentation().getBoundsInParent())) {
                    enemy.getRepresentation().setVisible(false);
                    controller.gameForm.getChildren().remove(enemy.getRepresentation());
                    controller.getEnemies().remove(enemy);
                    projectileHit = true;
                    break;
                }
            }

            projectile.update(controller.getMazeWalls());
            return projectile.isOffScreen(controller.gameForm.getWidth(), controller.gameForm.getHeight()) || !projectile.getRepresentation().isVisible() || projectileHit;
        });

        controller.getPlayer().updateRepresentationPosition();
        for (Enemy enemy : controller.getEnemies()) {
            enemy.updateRepresentationPosition();
        }
    }
}
