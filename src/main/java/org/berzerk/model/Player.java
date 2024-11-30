package org.berzerk.model;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Player extends Entity {
    private int score = 0;

    public Player(int id, int health, int x, int y, int score) {
        super(id, health, x, y, Color.GREEN);
        this.score = score;
    }

    @Override
    public void move(KeyCode keyCode, int gameFormWidth, int gameFormHeight, int cellWidth, int cellHeight, List<Rectangle> mazeWalls) {
        double nextX = getX();
        double nextY = getY();

        switch (keyCode) {
            case D:
                nextX += 5;
                break;
            case A:
                nextX -= 5;
                break;
            case W:
                nextY -= 5;
                break;
            case S: 
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
