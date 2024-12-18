package Units;

import javax.swing.*;
import java.awt.image.BufferedImage;
import ageOfStarcraft.Building;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Marine extends AbstractUnit {
    private ImageIcon moveIcon;
    private ImageIcon attackIcon;

    public Marine(int x, int y, String playerside) {
    	super(x, y, 40, 50, 3, playerside); // 체력 40, 사거리 50, 공격력 6

        if (playerside.equals("LEFT")) {
            this.moveIcon = new ImageIcon(getClass().getResource("/drawable/Marine-walking-unscreen.gif"));
            this.attackIcon = new ImageIcon(getClass().getResource("/drawable/Marine-shooting-right.gif"));
        } else { // RIGHT 플레이어
            this.moveIcon = new ImageIcon(getClass().getResource("/drawable/Marine-walking-left.gif"));
            this.attackIcon = new ImageIcon(getClass().getResource("/drawable/Marine-shooting.gif"));
        }
        this.imageWidth = moveIcon.getIconWidth();

    }
    
    @Override
    public String getTeamSide() {
        return super.getTeamSide(); // 부모 클래스(AbstractUnit)의 getTeamSide() 사용
    }

    @Override
    public void draw(Graphics g, int backgroundX, Component observer) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform(); // 현재 상태 저장

        // 현재 상태에 따라 사용하는 이미지 아이콘 선택
        ImageIcon currentIcon = isAttacking ? attackIcon : moveIcon;

        // 일반 이동과 공격 상태에 따른 스케일과 위치 조정
        double scaleX, scaleY;
        int translateX, translateY;

        if (isAttacking) {
            // 공격 상태일 때의 크기와 위치
            scaleX = 0.3;
            scaleY = 0.3;
            translateX = x + backgroundX + (int) (currentIcon.getIconWidth() * 0.35) - 30;
            translateY = y + 10;
        } else {
            // 이동 상태일 때의 크기와 위치
            scaleX = 0.3;
            scaleY = 0.3;
            translateX = x + backgroundX;
            translateY = y;
        }

        // 위치와 스케일 적용
        g2d.translate(translateX, translateY);
        g2d.scale(scaleX, scaleY);

        // 아이콘 그리기
        currentIcon.paintIcon(observer, g2d, 0, 0);

        // 원래 상태로 복원
        g2d.setTransform(originalTransform);

        // 체력 바 그리기
        g.setColor(Color.RED);
        int barWidth = 30;
        int barHeight = 5;
        int filledWidth = (int) ((double) hp / 40 * barWidth);
        g.fillRect(x + backgroundX, y - barHeight - 5, filledWidth, barHeight);
    }

    
    @Override
    public boolean isInRange(Building enemyBuilding, String playerside) {
        int unitEdgeX;      // 유닛의 끝 좌표
        int buildingEdgeX;  // 건물의 끝 좌표

        if (playerside.equals("LEFT")) {
           
            unitEdgeX = x + imageWidth; // 
            buildingEdgeX = enemyBuilding.getX(); // 건물의 왼쪽 끝 x좌표
        } else {
            unitEdgeX = x; // 
            buildingEdgeX = enemyBuilding.getX() + enemyBuilding.getWidth(); // 건물의 오른쪽 끝 x좌표
        }

        
        int distance = Math.abs(unitEdgeX - buildingEdgeX);
        return distance <= range; 
    }
    
    @Override
    public int getCost() {
        return 100; // Marine 비용
    }
   /* public void flipImage() {
        Image originalMoveImage = moveIcon.getImage();
        Image originalAttackImage = attackIcon.getImage();

        moveIcon = new ImageIcon(createFlippedImage(originalMoveImage));
        attackIcon = new ImageIcon(createFlippedImage(originalAttackImage));
    }

    private Image createFlippedImage(Image img) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();

        g2d.drawImage(img, 0, 0, width, height, width, 0, 0, height, null);
        g2d.dispose();

        return flippedImage;
    }
    */
}
