package org.berzerk.model;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Projectile {

    private double x;
    private double y;
    private double speed;
    private double directionX;
    private double directionY;
    private Rectangle projectileRep;

    public Projectile(double startX, double startY, double targetX, double targetY) {
        this.x = startX;
        this.y = startY;
        this.speed = 10;

        double deltaX = targetX - startX;
        double deltaY = targetY - startY;
        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        this.directionX = (magnitude != 0) ? (deltaX / magnitude) : 0;
        this.directionY = (magnitude != 0) ? (deltaY / magnitude) : 0;

        this.projectileRep = new Rectangle(5,5);
        this.projectileRep.setFill(Color.RED);
        this.projectileRep.setX(x);
        this.projectileRep.setY(y);
    }
    public Rectangle getRepresentation() {
        return projectileRep;
    }

    public boolean isOffScreen(double sceneWidth, double sceneHeight) {
        return (x < 0 || x > sceneWidth || y < 0 || y > sceneHeight);
    }

    public boolean collidesWithWalls(List<Rectangle> walls) {
        for (Rectangle wall : walls) {
            if (projectileRep.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }
    public void update(List<Rectangle> walls) {
        this.x += directionX * speed;
        this.y += directionY * speed;
        projectileRep.setX(this.x);
        projectileRep.setY(this.y);

        if (collidesWithWalls(walls)) {
            projectileRep.setVisible(false);
        }
    }
}
