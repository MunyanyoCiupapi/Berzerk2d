package org.berzerk.controllers;

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

public class BerzerkController {

    @FXML
    public AnchorPane gameForm;
    private Player player;
    private AnimationTimer gameLoop;
    private Circle playerRepresentation;
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private List<Projectile> projectiles;
    private List<Enemy> enemies;

    private int[][] maze;
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
        playerRepresentation = new Circle(10);
        playerRepresentation.setFill(Color.GREEN);
        gameForm.getChildren().add(playerRepresentation);

        playerRepresentation.setCenterX(player.getX());
        playerRepresentation.setCenterY(player.getY());

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

        gameForm.widthProperty().addListener((obs, oldVal, newVal) -> drawMaze(map));
        gameForm.heightProperty().addListener((obs, oldVal, newVal) -> drawMaze(map));

        spawnEnemies(10);
    }

    private void spawnEnemies(int numberOfEnemies) {
        Random random = new Random();

        for (int i = 0; i < numberOfEnemies; i++) {
            int x, y;
            x = random.nextInt(map.getHeight());
            y = random.nextInt(map.getWidth());

            if(map.isWall(x,y))
            {
                do {
                    x = random.nextInt(map.getHeight());
                    y = random.nextInt(map.getWidth());
                } while (map.isWall(x, y));
            }


            Enemy enemy = new Enemy(i, 100, y*40, x*40);
            enemies.add(enemy);


            Circle enemyRepresentation = new Circle(10);
            enemyRepresentation.setFill(Color.RED);
            enemyRepresentation.setCenterX(enemy.getX());
            enemyRepresentation.setCenterY(enemy.getY());
            gameForm.getChildren().add(enemyRepresentation);
            enemy.setRepresentation(enemyRepresentation);
        }
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
        int row, col;

        for (KeyCode key : keysPressed) {
            switch (key) {
                case W:
                    row = (player.getY() - 5) / cellHeight;
                    col = player.getX() / cellWidth;
                    if (row >= 0 && row < map.getHeight() && !map.isWall(row, col)) {
                        player.moveUp();
                    }
                    break;
                case S:
                    row = (player.getY() + 5) / cellHeight;
                    col = player.getX() / cellWidth;
                    if (row >= 0 && row < map.getHeight() && !map.isWall(row, col)) {
                        player.moveDown(gameForm.getHeight());
                    }
                    break;
                case A:
                    row = player.getY() / cellHeight;
                    col = (player.getX() - 5) / cellWidth;
                    if (col >= 0 && col < map.getWidth() && !map.isWall(row, col)) {
                        player.moveLeft();
                    }
                    break;
                case D:
                    row = player.getY() / cellHeight;
                    col = (player.getX() + 5) / cellWidth;
                    if (col >= 0 && col < map.getWidth() && !map.isWall(row, col)) {
                        player.moveRight(gameForm.getWidth());
                    }
                    break;
                default:
                    break;
            }
        }

        playerRepresentation.setCenterX(player.getX());
        playerRepresentation.setCenterY(player.getY());

        for (Enemy enemy : enemies) {
            enemy.getRepresentation().setCenterX(enemy.getX());
            enemy.getRepresentation().setCenterY(enemy.getY());

            if (playerRepresentation.getBoundsInParent().intersects(enemy.getRepresentation().getBoundsInParent())) {
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
