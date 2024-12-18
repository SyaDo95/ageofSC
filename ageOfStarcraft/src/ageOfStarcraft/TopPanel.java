package ageOfStarcraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends JPanel {

    private JButton unitButton1;
    private JButton unitButton2;
    private JButton unitButton3;
    private JButton unitButton4;
    private Image background; // 배경 이미지를 캐싱할 필드
    private GameWindow gameWindow; // GameWindow 참조를 저장할 필드

    public TopPanel(GameWindow gameWindow) { // GameWindow 인스턴스를 생성자에서 전달받음
        this.gameWindow = gameWindow; // 전달받은 인스턴스를 필드에 저장

        // 패널 크기와 위치 설정
        setLayout(new GridLayout(1, 4, 1, 0)); // 1행 4열 그리드 레이아웃, 수평 간격 1, 수직 간격 0
        setPreferredSize(new Dimension(300, 50)); // 패널의 선호 크기 설정
        setOpaque(false); // 배경을 투명하게 설정

        // 배경 이미지 로드 및 크기 조정
        background = new ImageIcon(getClass().getResource("/drawable/panel_background.png"))
                .getImage().getScaledInstance(300, 50, Image.SCALE_SMOOTH);

        // 버튼에 사용할 이미지 아이콘 생성
        ImageIcon buttonIcon = new ImageIcon(new ImageIcon(getClass().getResource("/drawable/button-Photoroom.png"))
                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        // 버튼 생성 및 이미지 아이콘 적용
        unitButton1 = new JButton(buttonIcon);
        unitButton2 = new JButton(buttonIcon);
        unitButton3 = new JButton(buttonIcon);
        unitButton4 = new JButton(buttonIcon);

        // 첫 번째 버튼에 ActionListener 추가
        unitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gameWindow.spawnSCV();
            }
        });
        
        // 두 번째 버튼에 ActionListener 추가
        unitButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.spawnMarine(); // Marine 유닛 생성 메서드 호출
            }
        });

        // 버튼의 테두리와 배경을 투명하게 설정
        for (JButton button : new JButton[]{unitButton1, unitButton2, unitButton3, unitButton4}) {
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            add(button); // 패널에 버튼 추가
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지를 그리기
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.out.println("배경 이미지를 찾을 수 없습니다.");
        }
    }
}
