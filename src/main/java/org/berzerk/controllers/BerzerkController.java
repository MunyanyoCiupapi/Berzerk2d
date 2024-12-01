package org.berzerk.controllers;

import lombok.Getter;
import lombok.Setter;
import org.berzerk.Berzerk;
import org.berzerk.model.*;
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
import org.berzerk.model.Map;
import org.berzerk.model.Strategy.DefaultGameUpdateStrategy;
import org.berzerk.model.Strategy.DefaultInitializeGameStrategy;
import org.berzerk.model.Strategy.GameUpdateStrategy;
import org.berzerk.model.Strategy.InitializationStrategy;

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

    private InitializationStrategy initializationStrategy;
    private GameUpdateStrategy gameUpdateStrategy;


    public BerzerkController() {
        map = new Map();
        player = new Player(1, 100, 100, 100, 0);
        projectiles = new ArrayList<>();
        mazeWalls = new ArrayList<>();
        enemies = new ArrayList<>();
        this.initializationStrategy = new DefaultInitializeGameStrategy();
        this.gameUpdateStrategy = new DefaultGameUpdateStrategy();
    }

    public void initialize() {
        initializationStrategy.initialize(this);
    }

    public void drawMaze(Map map) {

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


    public void spawnEnemies(int numberOfEnemies, int cellWidth, int cellHeight) {
        Random random = new Random();

        for (int i = 0; i < numberOfEnemies; i++) {
            int x, y;

            do {
                x = random.nextInt(map.getWidth());
                y = random.nextInt(map.getHeight());
            } while (map.isWall(y, x));

            EnemyType type = (i % 2 == 0) ? EnemyType.MOVING : EnemyType.STATIONARY;
            Enemy enemy = EnemyFactory.createEnemy(type, i, 100, x * cellWidth, y * cellHeight);
            enemies.add(enemy);
            gameForm.getChildren().add(enemy.getRepresentation());
        }
    }


    public void getInputs() {
        Scene scene = gameForm.getScene();
        scene.setOnKeyPressed(event -> keysPressed.add(event.getCode()));
        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
    }

    public void mouseShoot(MouseEvent event) {
        double mouseX = event.getSceneX();
        double mouseY = event.getSceneY();
        Projectile projectile = new Projectile(player.getX(), player.getY(), mouseX, mouseY);
        gameForm.getChildren().add(projectile.getRepresentation());
        projectiles.add(projectile);
    }
    public void updateGame() {

        gameUpdateStrategy.update(this);
    }
    public void restartGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Berzerk.class.getResource("/menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) gameForm.getScene().getWindow();

            if(stage != null)
            {
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
