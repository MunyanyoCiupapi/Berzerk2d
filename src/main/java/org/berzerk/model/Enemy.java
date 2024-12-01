package org.berzerk.model;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Enemy extends Entity {

    private Random random;


    public Enemy(int id, int health, int x, int y, Color color) {
        super(id, health, x, y, color);
    }


    @Override
    public void move(KeyCode keyCode, int gameFormWidth, int gameFormHeight, int cellWidth, int cellHeight, List<Rectangle> mazeWalls) {

        moveRandomly(gameFormWidth, gameFormHeight, cellWidth, cellHeight, mazeWalls);

        updateRepresentationPosition();
    }

    private void moveRandomly(int gameFormWidth, int gameFormHeight, int cellWidth, int cellHeight, List<Rectangle> mazeWalls) {
        int direction = random.nextInt(4);
        double nextX = getX();
        double nextY = getY();

        switch (direction) {
            case 0:
                nextX -= 5;
                break;
            case 1:
                nextX += 5;
                break;
            case 2:
                nextY -= 5;
                break;
            case 3: 
                nextY += 5;
                break;
            default:
                break;
        }

        if (canMove(nextX, nextY, cellWidth, cellHeight, mazeWalls, gameFormWidth, gameFormHeight)) {
            setX((int) nextX);
            setY((int) nextY);
        }

        updateRepresentationPosition();
    }

}
