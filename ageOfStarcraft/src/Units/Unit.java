package Units;

import ageOfStarcraft.Building;
import java.awt.*;

public interface Unit {
    int getX();
    int getY();
    int getRange();
    void move(int dx);
    void attack(Building target);
    void stopAttacking();
    void takeDamage(int damage);
    boolean isDestroyed();
    boolean isInRange(Building enemyBuilding);
    void draw(Graphics g, int backgroundX, Component observer);
}
