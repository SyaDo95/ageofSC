package ageOfStarcraft;

import javax.swing.*;

import Units.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {

    private Image background;
    private Building leftFortress;
    private Building rightFortress;
    private List<Unit> units = new ArrayList<>(); // SCV 유닛들을 저장할 리스트

    private int backgroundX = 0; // 배경의 X 위치
    private final int slideSpeed = 10; // 배경이 움직이는 속도
    private int mouseX = 0; // 현재 마우스의 X 좌표

    public GameWindow() {
        // 창 설정
        setTitle("2D Game Background with Fortresses");
        setSize(1024, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // JLayeredPane 설정
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight());
        add(layeredPane);

        // 상단 패널 생성 및 위치 설정
        TopPanel topPanel = new TopPanel(this); // 현재 GameWindow 인스턴스를 전달
        topPanel.setBounds(700, 10, 300, 50);
        layeredPane.add(topPanel, Integer.valueOf(2));

        // 배경 이미지 로드 및 축소
        background = new ImageIcon(getClass().getResource("/drawable/expanded_background_smooth_3072x1024.png")).getImage()
                .getScaledInstance(1536, 512, Image.SCALE_SMOOTH);

        // 건물 객체 생성 (위치와 크기 조정)
        leftFortress = new Building("/drawable/left_fortress.png", 1500, 0, getHeight() - 390, 300, 400);
        rightFortress = new Building("/drawable/right_fortress.png", 1500, 1536 - 300, getHeight() - 390, 300, 400);

        // 메인 게임 패널 추가
        GamePanel panel = new GamePanel();
        panel.setBounds(0, 0, 1024, 512);
        layeredPane.add(panel, Integer.valueOf(0));

        // 마우스 움직임 감지 리스너 추가
        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        });

        // 타이머 설정: 20ms마다 마우스 위치에 따라 배경 슬라이드 및 유닛 이동 업데이트
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mouseX < 50 && backgroundX < 0) {
                    backgroundX += slideSpeed;
                } else if (mouseX > getWidth() - 50 && backgroundX > -(background.getWidth(null) - getWidth())) {
                    backgroundX -= slideSpeed;
                }

                // 유닛 이동 및 충돌 검사 업데이트
                updateUnits();

                // 게임 종료 조건 확인
                checkGameOver();

                // 화면을 다시 그려 슬라이드 효과를 적용
                panel.repaint();
            }
        });
        timer.start();
    }

    public void spawnSCV() {
        Unit scv = new SCV(leftFortress.getX() + leftFortress.getWidth(), 370); // 새로운 생성자 형식으로 수정
        units.add(scv);
    }
    
    public void spawnMarine() {
        Unit marine = new Marine(leftFortress.getX() + leftFortress.getWidth(), 370);
        units.add(marine);
    }

 // 유닛 이동 및 충돌 검사 메서드
    private void updateUnits() {
        for (Unit unit : units) {
            if (unit.isInRange(rightFortress)) {
                // 공격 범위 내에 있을 경우 공격하고 멈춤
                unit.attack(rightFortress);
            } else {
                // 공격 범위 밖이면 이동하고 공격을 멈춤
                unit.stopAttacking();
                unit.move(3); // 오른쪽으로 이동
            }
        }

        // 파괴된 유닛 제거
        units.removeIf(Unit::isDestroyed);
    }


    private void checkGameOver() {
        if (leftFortress.isDestroyed()) {
            JOptionPane.showMessageDialog(this, "Right Player Wins!");
            System.exit(0);
        } else if (rightFortress.isDestroyed()) {
            JOptionPane.showMessageDialog(this, "Left Player Wins!");
            System.exit(0);
        }
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 배경 이미지 그리기
            g.drawImage(background, backgroundX, 0, null);

            // 좌측 성 그리기
            leftFortress.draw(g, backgroundX);

            // 우측 성 그리기
            rightFortress.draw(g, backgroundX);

            for (Unit unit : units) {
                unit.draw(g, backgroundX, this); // GamePanel의 this 전달
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}
