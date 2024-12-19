// --- 클라이언트 ---
package server;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import ageOfStarcraft.GameWindow;

public class GameClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameWindow gameWindow;
    private String playerSide; // 플레이어 역할 추가

    public GameClient(String host, int port, String playerSide) { // 역할을 생성자 매개변수로 추가
        this.playerSide = playerSide; // 플레이어 역할 저장
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            // 게임 창 생성 및 저장
            SwingUtilities.invokeLater(() -> {
                gameWindow = new GameWindow(playerSide, this); // playerSide 사용
                gameWindow.setVisible(true);
            });

            // Start listening to server messages
            new Thread(() -> {
                try {
                    Object receivedObject;
                    while ((receivedObject = in.readObject()) != null) {
                        if (receivedObject instanceof GameAction) {
                            GameAction action = (GameAction) receivedObject;

                            // 유닛 소환 처리
                            if (action.getActionType().equals("spawn_unit")) {
                                SwingUtilities.invokeLater(() -> {
                                    gameWindow.addUnit(action.getUnitType(), action.getTeam(), action.getX(), action.getY());
                                });
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public void sendAction(GameAction action) {
        try {
            out.writeObject(action);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 플레이어 역할 선택
        String[] options = {"LEFT", "RIGHT"};
        String role = (String) JOptionPane.showInputDialog(
            null,
            "Choose your role:",
            "Player Role Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (role == null) {
            System.out.println("No role selected. Exiting...");
            System.exit(0);
        }

        // 서버 주소 및 포트 설정
        String serverHost = "localhost"; // 서버 호스트명 (또는 IP 주소)
        int serverPort = 5000; // 서버 포트 번호

        // 클라이언트 시작
        GameClient client = new GameClient(serverHost, serverPort, role); // 역할 전달

        // 역할 전송
        client.sendAction(new GameAction("set_role", null, role, 0, 0));
    }
}
