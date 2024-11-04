package org.berzerk.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends Entity {

    int score = 0;

    public Player(int id, int health, int x, int y, int score) {
        super(id, health, x, y);
        this.score = score;
    }

    public void moveUp() {
        if (this.y > 0) {
            this.y -= 5;
        }
    }

    public void moveDown(double sceneHeight) {
        if (this.y < sceneHeight ) {
            this.y += 5;
        }
    }

    public void moveLeft() {
        if (this.x > 0) {
            this.x -= 5;
        }
    }

    public void moveRight(double sceneWidth) {
        if (this.x < sceneWidth ) {
            this.x += 5;
        }
    }

}


