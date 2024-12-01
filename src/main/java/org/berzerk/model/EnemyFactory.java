package org.berzerk.model;

public class EnemyFactory {

    public static Enemy createEnemy(EnemyType type, int id, int health, int x, int y)
    {
        switch (type) {
            case MOVING:
                return new MovingEnemy(id, health, x, y);
            case STATIONARY:
                return new StationaryEnemy(id, health, x, y);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }
}
