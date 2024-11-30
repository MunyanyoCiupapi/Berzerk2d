package org.berzerk.model;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public abstract class Entity {
    private int id;
    private int health = 100;
    private int x;
    private int y;
    private Circle representation;

    public Entity(int id, int health, int x, int y, Color color) {
        this.id = id;
        this.health = health;
        this.x = x;
        this.y = y;
        this.representation = new Circle(10);
        this.representation.setFill(color);
        updateRepresentationPosition();
    }

    public void updateRepresentationPosition() {
        representation.setCenterX(x);
        representation.setCenterY(y);
    }

    public boolean collidesWith(Entity other) {
        return this.representation.getBoundsInParent().intersects(other.representation.getBoundsInParent());
    }

    public abstract void move(KeyCode keyCode, int gameFormWidth, int gameFormHeight, int cellWidth, int cellHeight, List<Rectangle> mazeWalls);


    boolean canMove(double nextX, double nextY, int cellWidth, int cellHeight, List<Rectangle> mazeWalls, int gameFormWidth, int gameFormHeight) {
        double playerRadius = representation.getRadius();

        if (nextX - playerRadius < 0 || nextX + playerRadius > gameFormWidth || nextY - playerRadius < 0 || nextY + playerRadius > gameFormHeight) {
            return false;
        }

        for (Rectangle wall : mazeWalls) {
            if (wall.getBoundsInParent().intersects(nextX - playerRadius, nextY - playerRadius, playerRadius * 2, playerRadius * 2)) {
                return false;
            }
        }

        return true;
    }

}
