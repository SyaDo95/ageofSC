package Units;

import ageOfStarcraft.Building;
import java.awt.*;

public abstract class AbstractUnit implements Unit {
    protected int x;
    protected int y;
    protected int hp;
    protected int range;
    protected int attackPower;
    protected boolean isAttacking;

    public AbstractUnit(int x, int y, int hp, int range, int attackPower) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.range = range;
        this.attackPower = attackPower;
        this.isAttacking = false;
    }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public int getRange() { return range; }

    @Override
    public void move(int dx) {
        if (!isAttacking) x += dx;
    }

    @Override
    public void attack(Building target) {
        target.takeDamage(attackPower);
        isAttacking = true;
    }

    @Override
    public void stopAttacking() {
        isAttacking = false;
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    @Override
    public boolean isDestroyed() { return hp <= 0; }

    @Override
    public boolean isInRange(Building enemyBuilding) {
        int distance = Math.abs(x - (enemyBuilding.getX()));
        return distance <= range;
    }
}
