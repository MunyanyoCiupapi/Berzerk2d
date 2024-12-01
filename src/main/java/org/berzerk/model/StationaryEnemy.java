package org.berzerk.model;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class StationaryEnemy extends Enemy {
    public StationaryEnemy(int id, int health, int x, int y) {
        super(id, health, x, y, Color.YELLOW);
    }

    @Override
    public void move(KeyCode keyCode, int gameFormWidth, int gameFormHeight, int cellWidth, int cellHeight, List<Rectangle> mazeWalls) {

    }
}
