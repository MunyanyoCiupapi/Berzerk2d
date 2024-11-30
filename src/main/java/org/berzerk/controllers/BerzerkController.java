package org.berzerk.controllers;

import lombok.Getter;
import lombok.Setter;
import org.berzerk.Berzerk;
import org.berzerk.model.Enemy;
import org.berzerk.model.Map;
import org.berzerk.model.Player;
import org.berzerk.model.Projectile;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class BerzerkController {

    @FXML
    public AnchorPane gameForm;
    private Player player;
    private AnimationTimer gameLoop;
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private List<Projectile> projectiles;
    private List<Enemy> enemies;
    private Stage stage;
    private Integer numOfEnemies = 10;

    private List<Rectangle> mazeWalls;
    private Map map;

    public BerzerkController() {
        map = new Map();
        player = new Player(1, 100, 100, 100, 0);
        projectiles = new ArrayList<>();
        mazeWalls = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    public void initialize() {

        gameForm.getChildren().add(player.getRepresentation());
        player.updateRepresentationPosition();

        gameForm.setStyle("-fx-background-color: black;");


        gameForm.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                getInputs();
                drawMaze(new Map());
            }
        });

        gameForm.setOnMouseClicked(this::mouseShoot);

        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                updateGame();
            }
        };
        gameLoop.start();

        gameForm.widthProperty().addListener((obs, oldVal, newVal) -> {
            drawMaze(map);
            if (gameForm.getWidth() > 0 && gameForm.getHeight() > 0) {
                int cellWidth = (int)(gameForm.getWidth() / map.getWidth());
                int cellHeight = (int)(gameForm.getHeight() / map.getHeight());
                spawnEnemies(numOfEnemies, cellWidth, cellHeight);
            }
        });

        gameForm.heightProperty().addListener((obs, oldVal, newVal) -> {
            drawMaze(map);
            if (gameForm.getWidth() > 0 && gameForm.getHeight() > 0) {
                int cellWidth = (int)(gameForm.getWidth() / map.getWidth());
                int cellHeight = (int)(gameForm.getHeight() / map.getHeight());
                spawnEnemies(numOfEnemies, cellWidth, cellHeight);
            }
        });
    }

    private void drawMaze(Map map) {

        mazeWalls.clear();
        gameForm.getChildren().removeIf(node -> node instanceof Rectangle);


        int cellWidth = (int)(gameForm.getWidth() / map.getWidth());
        int cellHeight = (int)(gameForm.getHeight() / map.getHeight());

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                if (map.isWall(row, col)) {
                    Rectangle wall = new Rectangle(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                    wall.setFill(Color.BLUE);
                    mazeWalls.add(wall);
                    gameForm.getChildren().add(wall);
                }
            }
        }
    }


    private void spawnEnemies(int numberOfEnemies, int cellWidth, int cellHeight) {
        Random random = new Random();

        for (int i = 0; i < numberOfEnemies; i++) {
            int x, y;

            do {
                x = random.nextInt(map.getWidth());
                y = random.nextInt(map.getHeight());
            } while (map.isWall(y, x));

            Enemy enemy = new Enemy(i, 100, x * cellWidth, y * cellHeight);
            enemies.add(enemy);
            gameForm.getChildren().add(enemy.getRepresentation());
        }
    }


    public void getInputs() {
        Scene scene = gameForm.getScene();
        scene.setOnKeyPressed(event -> keysPressed.add(event.getCode()));
        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
    }

    private void mouseShoot(MouseEvent event) {
        double mouseX = event.getSceneX();
        double mouseY = event.getSceneY();
        Projectile projectile = new Projectile(player.getX(), player.getY(), mouseX, mouseY);
        gameForm.getChildren().add(projectile.getRepresentation());
        projectiles.add(projectile);
    }
    private void updateGame() {

        int cellWidth = (int)(gameForm.getWidth() / map.getWidth());
        int cellHeight = (int)(gameForm.getHeight() / map.getHeight());

        for (KeyCode key : keysPressed) {
            player.move(key, (int) gameForm.getWidth(), (int) gameForm.getHeight(), cellWidth, cellHeight, mazeWalls);
        }
        for (Enemy enemy : enemies) {
            enemy.move(null, (int) gameForm.getWidth(), (int) gameForm.getHeight(), cellWidth, cellHeight, mazeWalls);
        }

        player.updateRepresentationPosition();

        for (Enemy enemy : enemies) {
            enemy.updateRepresentationPosition();

            if (player.collidesWith(enemy)) {
                restartGame();
                return;
            }
        }


        projectiles.removeIf(projectile -> {
            boolean projectileHit = false;

            for (Enemy enemy : enemies) {
                if (projectile.getRepresentation().getBoundsInParent().intersects(enemy.getRepresentation().getBoundsInParent())) {
                    enemy.getRepresentation().setVisible(false);
                    gameForm.getChildren().remove(enemy.getRepresentation());
                    enemies.remove(enemy);
                    projectileHit = true;
                    break;
                }
            }

            projectile.update(mazeWalls);
            return projectile.isOffScreen(gameForm.getWidth(), gameForm.getHeight()) || !projectile.getRepresentation().isVisible() || projectileHit;
        });
    }
    private void restartGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Berzerk.class.getResource("/menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) gameForm.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
