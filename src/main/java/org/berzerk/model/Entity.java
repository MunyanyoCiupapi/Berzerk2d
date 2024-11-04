package org.berzerk.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {

    int id;
    int health = 100;
    int x;
    int y;

    public Entity(int id, int health, int x, int y) {
        this.id = id;
        this.health = health;
        this.x = x;
        this.y = y;
    }
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }
}
