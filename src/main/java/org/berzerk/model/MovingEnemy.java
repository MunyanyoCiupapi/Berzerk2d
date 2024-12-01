package org.berzerk.model;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Random;

public class MovingEnemy extends Enemy {
    private Random random = new Random();

    public MovingEnemy(int id, int health, int x, int y) {
        super(id, health, x, y, Color.RED);
    }

    @Override
    public void move(KeyCode keyCode, int gameFormWidth, int gameFormHeight, int cellWidth, int cellHeight, List<Rectangle> mazeWalls) {
        int direction = random.nextInt(4);
        double nextX = getX();
        double nextY = getY();

        switch (direction) {
            case 0 -> nextX -= 10;
            case 1 -> nextX += 10;
            case 2 -> nextY -= 10;
            case 3 -> nextY += 10;
        }

        if (canMove(nextX, nextY, cellWidth, cellHeight, mazeWalls, gameFormWidth, gameFormHeight)) {
            setX((int) nextX);
            setY((int) nextY);
        }
        updateRepresentationPosition();
    }
}
