package Units;

import ageOfStarcraft.Building;
import java.awt.*;

public interface Unit {
    int getX();
    int getY();
    int getRange();
    int getHp();
    int getImageWidth(); // 유닛의 이미지 너비 반환
    void setX(int x);
    void setY(int y);
    void setHp(int hp);
    void setId(String id);
    String getId(); // getId() 메서드 추가
    void move(int dx);
    void attack(Building target);
    void stopAttacking();
    void takeDamage(int damage);
    boolean isDestroyed();
    boolean isInRange(Building enemyBuilding, String playerside);
    void draw(Graphics g, int backgroundX, Component observer);
    boolean isInRange(Unit enemyUnit);
    void attack(Unit target);
    String getTeamSide(); // 팀 정보를 반환하는 메서드 추가
    int getCost(); // 유닛 비용 반환 메서드 추가

}
