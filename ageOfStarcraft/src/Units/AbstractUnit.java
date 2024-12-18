package Units;

import ageOfStarcraft.Building;
import java.awt.*;

public abstract class AbstractUnit implements Unit {
	protected String id; // 유닛 식별자
    protected int x;
    protected int y;
    protected int hp;
    protected int range;
    protected int attackPower;
    protected boolean isAttacking;
    protected int imageWidth;
    protected String teamSide; // 유닛의 팀을 나타내는 변수 (LEFT 또는 RIGHT)

    public AbstractUnit(int x, int y, int hp, int range, int attackPower, String teamSide) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.range = range;
        this.attackPower = attackPower;
        this.isAttacking = false;
        this.teamSide = teamSide; // 생성 시 팀 정보 초기화
    }
    

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    @Override
    public void setX(int x) { this.x = x; }

    @Override
    public void setY(int y) { this.y = y; }

    @Override
    public void setHp(int hp) { this.hp = hp; }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public int getRange() { return range; }
    
    @Override
    public int getHp() {return hp; }
    
    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    @Override
    public void move(int dx) {
        if (!isAttacking) { // 공격 중이 아닐 때만 이동
            if (teamSide.equals("LEFT")) {
                x += Math.abs(dx); // 오른쪽으로 이동
            } else {
                x -= Math.abs(dx); // 왼쪽으로 이동
            }
        }
    }

    @Override
    public void attack(Unit target) {
        if (!target.isDestroyed()) {
            target.takeDamage(attackPower); // 적 유닛 HP 감소
            isAttacking = true; // 공격 상태로 변경

            // 적 유닛이 파괴되었는지 확인
            if (target.isDestroyed()) {
                System.out.println("Unit " + target.getId() + " destroyed!");
            }
        }
    }

    @Override
    public void attack(Building target) {
        target.takeDamage(attackPower); // 건물의 체력을 줄임
        isAttacking = true;
    }

    @Override
    public void stopAttacking() {
        isAttacking = false;
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0; // 체력을 0으로 설정
            System.out.println("Unit " + id + " is destroyed!");
        }
    }

    @Override
    public boolean isDestroyed() {
        return hp <= 0; // 체력이 0이면 파괴 상태
    }


    @Override
    public boolean isInRange(Building enemyBuilding, String playerside) {
        int unitEdgeX;      // 유닛의 끝 좌표
        int buildingEdgeX;  // 건물의 끝 좌표

        if (playerside.equals("LEFT")) {
            // LEFT 플레이어: 유닛의 오른쪽 끝 좌표와 건물의 왼쪽 끝 좌표 비교
            unitEdgeX = x + imageWidth; // 유닛의 오른쪽 끝 x좌표
            buildingEdgeX = enemyBuilding.getX(); // 건물의 왼쪽 끝 x좌표
        } else {
            // RIGHT 플레이어: 유닛의 왼쪽 끝 좌표와 건물의 오른쪽 끝 좌표 비교
            unitEdgeX = x; // 유닛의 왼쪽 끝 x좌표
            buildingEdgeX = enemyBuilding.getX() + enemyBuilding.getWidth(); // 건물의 오른쪽 끝 x좌표
        }

        // 두 좌표 사이의 거리가 range 이내인지 확인
        int distance = Math.abs(unitEdgeX - buildingEdgeX);
        return distance <= range; // 범위 내에 있으면 true
    }
    @Override
    public boolean isInRange(Unit enemyUnit) {
        int thisCenterX = this.x + this.imageWidth / 2; // 유닛 중심 좌표
        int enemyCenterX = enemyUnit.getX() + enemyUnit.getImageWidth() / 2; // 적 유닛 중심 좌표

        // 두 유닛 간 거리 계산
        int distance = Math.abs(thisCenterX - enemyCenterX);
        return distance <= range; // 사정거리 내인지 확인
    }
    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
    @Override
    public String getTeamSide() {
        return teamSide; // 팀 사이드 반환
    }
    


}
