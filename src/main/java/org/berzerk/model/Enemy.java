package org.berzerk.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy extends Entity {
    
    private Circle representation;
    public Enemy(int id, int health, int x, int y) {
        super(id, health, x, y);
        this.representation = new Circle(10);
        this.representation.setFill(Color.RED);
        this.representation.setCenterX(x);
        this.representation.setCenterY(y);
    }

    public Circle getRepresentation() {
        return representation;
    }


}


