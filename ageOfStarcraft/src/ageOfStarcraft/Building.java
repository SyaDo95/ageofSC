package ageOfStarcraft;

import javax.swing.*;
import java.awt.*;

public class Building {
    private int hp;
    private final int maxHp = 1500;
    private Image image;
    private int x, y;
    private int width, height; // 이미지 크기

    public Building(String imagePath, int hp, int x, int y, int width, int height) {
        this.hp = hp;
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getHp() {
        return hp;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public void draw(Graphics g, int backgroundX) {
        g.drawImage(image, x + backgroundX, y, null);
        // 체력 바 그리기
        g.setColor(Color.RED);
        int barWidth = 200;
        int barHeight = 10;
        int filledWidth = (int) ((double) hp / maxHp * barWidth); // 비율에 맞춰 체력 바 줄이기
        g.fillRect(x + backgroundX, y - barHeight - 5, filledWidth, barHeight);
    }
}
