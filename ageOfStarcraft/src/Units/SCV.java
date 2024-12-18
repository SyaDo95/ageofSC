package Units;

import javax.swing.*;
import java.awt.image.BufferedImage;
import ageOfStarcraft.Building;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class SCV extends AbstractUnit {
    private ImageIcon imageIcon;

    public SCV(int x, int y,  String teamSide) {
    	super(x, y, 60, 2, 1, teamSide); // 체력 60, 사거리 5, 공격력 5
        this.imageIcon = new ImageIcon(getClass().getResource("/drawable/scv.png"));
        this.imageWidth = imageIcon.getIconWidth();
    }
    
    @Override
    public String getTeamSide() {
        return super.getTeamSide(); // 부모 클래스(AbstractUnit)의 getTeamSide() 사용
    }

    @Override
    public void draw(Graphics g, int backgroundX, Component observer) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform(); // 현재 상태 저장

        double scaleX = 0.3;
        double scaleY = 0.3;

        // 이동 및 스케일 적용
        g2d.translate(x + backgroundX, y);
        g2d.scale(scaleX, scaleY);
        imageIcon.paintIcon(observer, g2d, 0, 0);

        // 원래 상태로 복원
        g2d.setTransform(originalTransform);

        // 체력 바 그리기
        g.setColor(Color.RED);
        int barWidth = 30;
        int barHeight = 5;
        int filledWidth = (int) ((double) hp / 60 * barWidth);
        g.fillRect(x + backgroundX, y - barHeight - 5, filledWidth, barHeight);
    }
    
    public void flipImage() {
        Image originalImage = imageIcon.getImage();
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();

        g2d.drawImage(originalImage, 0, 0, width, height, width, 0, 0, height, null);
        g2d.dispose();

        imageIcon = new ImageIcon(flippedImage);
    }
    
    @Override
    public boolean isInRange(Building enemyBuilding, String playerside) {
        int unitEdgeX;      // 유닛의 끝 좌표
        int buildingEdgeX;  // 건물의 끝 좌표

        if (playerside.equals("LEFT")) {
            // LEFT 플레이어: SCV의 오른쪽 끝 좌표와 건물의 왼쪽 끝 좌표 비교
            unitEdgeX = x + imageWidth; // SCV의 오른쪽 끝 x좌표
            buildingEdgeX = enemyBuilding.getX()  + enemyBuilding.getWidth() + 150; // 건물의 왼쪽 끝 x좌표
        } else {
            // RIGHT 플레이어: SCV의 왼쪽 끝 좌표와 건물의 오른쪽 끝 좌표 비교
            unitEdgeX = x; // SCV의 왼쪽 끝 x좌표
            buildingEdgeX = enemyBuilding.getX() + enemyBuilding.getWidth() - 100; // 건물의 오른쪽 끝 x좌표
        }

        // 오차 허용: 유닛과 건물의 끝이 일정 거리 이내에 있는 경우 공격 범위로 간주
        int distance = Math.abs(unitEdgeX - buildingEdgeX);
        return distance <= range; // 오차 허용 범위를 5 픽셀로 설정
    }
    @Override
    public int getCost() {
        return 50; // SCV 비용
    }
}
