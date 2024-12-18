package ageOfStarcraft;

import javax.swing.*;

import Units.*;
import server.GameAction;
import server.GameClient;

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
    private final String playerside; // LEFT 또는 RIGHT 플레이어 역할
    private int playerResources = 200; // 초기 재화
    private JLabel resourceLabel; // 재화 표시를 위한 JLabel
    private GameClient client; // 클라이언트 객체 추가


    public GameWindow(String playerside, GameClient client) {
    	this.playerside = playerside; // 역할 초기화
        this.client = client; // 클라이언트 저장
       

        // RIGHT 플레이어일 경우 배경의 시작 위치를 오른쪽 끝으로 설정
        if (playerside.equals("RIGHT")) {
            backgroundX = -(1536)+1024; // 배경 전체 너비에서 창 너비를 뺀 만큼 이동
        }

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
        background = new ImageIcon(getClass().getResource("/drawable/expanded_background_smooth_3072x1024.png"))
                .getImage().getScaledInstance(1536, 512, Image.SCALE_SMOOTH);

        // 건물 객체 생성 (위치와 크기 조정)
        leftFortress = new Building("/drawable/left_fortress.png", 1500, 0, getHeight() - 390, 300, 400);
        rightFortress = new Building("/drawable/right_fortress.png", 1500, 1536 - 300, getHeight() - 390, 300, 400);

        // 메인 게임 패널 추가
        GamePanel panel = new GamePanel(playerside);
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
        
        resourceLabel = new JLabel("Resources: " + playerResources); // 초기 재화 표시
        resourceLabel.setBounds(10, 10, 200, 30);
        resourceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resourceLabel.setForeground(Color.WHITE);
        layeredPane.add(resourceLabel, Integer.valueOf(2));
        
     // 10초마다 재화를 증가시키는 타이머
        Timer resourceTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerResources += 50; // 10초마다 50 재화 증가
                updateResourceLabel();
            }
        });
        resourceTimer.start();
    }
    
    private void updateResourceLabel() {
        resourceLabel.setText("Resources: " + playerResources); // 재화 라벨 업데이트
    }
    
    public List<Unit> getUnits() {
        return units;
    }
    
    public void spawnSCV() {
        if (playerResources >= 50) {
            playerResources -= 50;
            updateResourceLabel();

            // 서버로 SCV 소환 요청 전송
            int spawnX = playerside.equals("LEFT")
                    ? leftFortress.getX() + leftFortress.getWidth() - 50
                    : rightFortress.getX() - 80;

            client.sendAction(new GameAction("spawn_unit", "SCV", playerside, spawnX, 370));
        } else {
            JOptionPane.showMessageDialog(this, "Not enough resources to spawn SCV!");
        }
    }

    public void spawnMarine() {
        if (playerResources >= 100) {
            playerResources -= 100;
            updateResourceLabel();

            // 서버로 Marine 소환 요청 전송
            int spawnX = playerside.equals("LEFT")
                    ? leftFortress.getX() + leftFortress.getWidth()
                    : rightFortress.getX() - 30;

            client.sendAction(new GameAction("spawn_unit", "Marine", playerside, spawnX, 370));
        } else {
            JOptionPane.showMessageDialog(this, "Not enough resources to spawn Marine!");
        }
    }


    /*
    public void spawnSCV() {//로컬에서 소환
        // SCV 스폰 비용 확인
        if (playerResources >= 50) { // SCV 비용: 50
            playerResources -= 50; // 재화 차감
            updateResourceLabel(); // 라벨 업데이트

            int spawnX = playerside.equals("LEFT")
                    ? leftFortress.getX() + leftFortress.getWidth()
                    : rightFortress.getX() - 30;
            SCV scv = new SCV(spawnX, 370, playerside); // teamside 추가
            units.add(scv);

            if (playerside.equals("RIGHT")) {
                scv.flipImage();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Not enough resources to spawn SCV!"); // 재화 부족 알림
        }
    }
    
    
  


    public void spawnMarine() {
        // Marine 스폰 비용 확인
        if (playerResources >= 100) { // Marine 비용: 100
            playerResources -= 100; // 재화 차감
            updateResourceLabel(); // 라벨 업데이트

            int spawnX = playerside.equals("LEFT")
                    ? leftFortress.getX() + leftFortress.getWidth()
                    : rightFortress.getX() - 30;
            Marine marine = new Marine(spawnX, 370, playerside); // teamside 추가
            units.add(marine);
        } else {
            JOptionPane.showMessageDialog(this, "Not enough resources to spawn Marine!"); // 재화 부족 알림
        }
    }
    */
   

    private void updateUnits() {
        List<Unit> destroyedUnits = new ArrayList<>(); // 파괴된 유닛 저장 리스트

        for (Unit unit : units) {
            // 이미 파괴된 유닛은 스킵
            if (unit.isDestroyed()) {
                destroyedUnits.add(unit);
                continue;
            }

            boolean attacked = false;

            // **1. 적 유닛 탐지 및 공격**
            for (Unit enemyUnit : units) {
                if (!enemyUnit.isDestroyed() && !unit.getTeamSide().equals(enemyUnit.getTeamSide())) {
                    if (unit.isInRange(enemyUnit)) {
                        unit.attack(enemyUnit);
                        attacked = true;

                        if (unit instanceof Marine) {
                            // 마린은 공격 상태로 전환
                        	 ((Marine) unit).setAttacking(true);
                        }

                        if (enemyUnit.isDestroyed()) {
                            destroyedUnits.add(enemyUnit);

                            // 보상 지급
                            if (playerside.equals(unit.getTeamSide())) {
                                int reward = enemyUnit.getCost() / 2;
                                playerResources += reward;
                                updateResourceLabel();
                            }
                        }
                        break; // 적 공격 완료 후 루프 종료
                    }
                }
            }

            // **2. 적 건물 탐지 및 공격**
            if (!attacked) {
                if (unit.getTeamSide().equals("LEFT") && unit.isInRange(rightFortress, "LEFT")) {
                    unit.attack(rightFortress);
                    attacked = true;
                    if (unit instanceof Marine) {
                    	 ((Marine) unit).setAttacking(true);
                    }
                } else if (unit.getTeamSide().equals("RIGHT") && unit.isInRange(leftFortress, "RIGHT")) {
                    unit.attack(leftFortress);
                    attacked = true;
                    if (unit instanceof Marine) {
                    	 ((Marine) unit).setAttacking(true);
                    }
                }
            }

            // **3. 이동 로직**
            if (!attacked) {
                unit.stopAttacking(); // 공격 중이 아니므로 이동 상태로 복귀
                unit.move(playerside.equals("LEFT") ? 3 : -3);
            }
        }

        // **4. 파괴된 유닛 처리**
        for (Unit destroyedUnit : destroyedUnits) {
            units.remove(destroyedUnit);

            // 서버와 클라이언트 동기화
            client.sendAction(new GameAction("remove_unit", null, destroyedUnit.getTeamSide(), destroyedUnit.getX(), destroyedUnit.getY()));
        }
    }

   

    
/*
    private void updateUnits() {
        List<Unit> destroyedUnits = new ArrayList<>(); // 파괴된 유닛 저장 리스트

        System.out.println("=== Update Units ===");
        System.out.println("Total Units Before Update: " + units.size());

        for (int i = 0; i < units.size(); i++) {
            Unit unit = units.get(i);

            if (unit.isDestroyed()) {
                destroyedUnits.add(unit);

                // 적 유닛 처치 시 보상 지급 로직
                if (!unit.getTeamSide().equals(playerside)) { // 적 유닛인지 확인
                    int reward = unit.getCost() / 2; // 유닛 비용의 50% 보상
                    playerResources += reward; // 플레이어 재화 증가
                    updateResourceLabel(); // 재화 라벨 업데이트
                    System.out.println("Earned " + reward + " resources for destroying an enemy unit!");
                }

                System.out.println("Destroyed Unit: " + unit + " at X=" + unit.getX());
                continue; // 파괴된 유닛은 더 이상 처리하지 않음
            }

            boolean attacked = false;

            // 다른 유닛들과의 공격 여부 검사 (팀 확인 추가)
            for (Unit enemyUnit : units) {
                if (enemyUnit != unit && !enemyUnit.isDestroyed() &&
                    !unit.getTeamSide().equals(enemyUnit.getTeamSide()) && // 같은 팀인지 확인
                    unit.isInRange(enemyUnit)) {

                    System.out.println("Unit " + unit + " attacking " + enemyUnit);
                    unit.attack(enemyUnit);
                    attacked = true;
                    break; // 공격하면 루프 종료
                }
            }

            // 공격하지 않은 경우, 건물을 공격하거나 이동
            if (!attacked) {
                if (playerside.equals("LEFT")) {
                    if (unit.isInRange(rightFortress, "LEFT")) {
                        unit.attack(rightFortress);
                        System.out.println("Unit " + unit + " attacking Right Fortress");
                    } else {
                        unit.stopAttacking();
                        unit.move(3);
                    }
                } else { // RIGHT 플레이어
                    if (unit.isInRange(leftFortress, "RIGHT")) {
                        unit.attack(leftFortress);
                        System.out.println("Unit " + unit + " attacking Left Fortress");
                    } else {
                        unit.stopAttacking();
                        unit.move(-3);
                    }
                }
            }
        }

        // 파괴된 유닛들을 루프가 끝난 뒤 제거
        for (Unit destroyedUnit : destroyedUnits) {
            units.remove(destroyedUnit);
            System.out.println("Removing Destroyed Unit: " + destroyedUnit);
        }

        System.out.println("Total Units After Update: " + units.size());
        System.out.println("=== End Update ===");
    }
    */

    public void addUnit(String unitType, String team, int x, int y) {
        Unit unit;

        if (unitType.equals("SCV")) {
            unit = new SCV(x, y, team); // SCV 생성 시 소속 전달
            if (team.equals("RIGHT")) {
                ((SCV) unit).flipImage();
            }
        } else if (unitType.equals("Marine")) {
            unit = new Marine(x, y, team);
        } else {
            return;
        }

        units.add(unit);
        repaint();
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
    	private String playerside; // GamePanel에 playerside 추가

        public GamePanel(String playerside) { // 생성자에서 playerside 전달
            this.playerside = playerside;
        }
    	
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 배경 이미지 그리기
            g.drawImage(background, backgroundX, 0, null);

            // 좌측 성 그리기
            leftFortress.draw(g, backgroundX);

            // 우측 성 그리기
            rightFortress.draw(g, backgroundX);
            
            // 유닛이 제대로 추가되었는지 로그 출력
            System.out.println("현재 유닛 수: " + units.size());
            for (Unit unit : units) {
                System.out.println("유닛: ID=" + unit.getId() + ", X=" + unit.getX() + ", Y=" + unit.getY());
                unit.draw(g, backgroundX, this);
            }
            
        }
               

    }
    
    

   




    public void setPlayerRole(String role) {
        System.out.println("Player role set to: " + role);
    }
   



    

    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow("LEFT", null); // 기본적으로 SINGLE-PLAYER 모드
            gameWindow.setVisible(true);
        });
    }
}

