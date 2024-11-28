package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private static final int PORT = 12345;
    private static final List<Socket> clients = new ArrayList<>(); // 클라이언트 소켓 저장
    private static final GameState gameState = new GameState();    // 게임 상태 관리

    public static void main(String[] args) {
        System.out.println("게임 서버가 시작되었습니다...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (clients.size() < 2) { // 두 클라이언트만 허용
                System.out.println("클라이언트 대기 중...");
                Socket client = serverSocket.accept();
                clients.add(client);

                String playerRole = (clients.size() == 1) ? "LEFT" : "RIGHT"; // 첫 번째는 LEFT, 두 번째는 RIGHT
                System.out.println("새로운 클라이언트 연결: " + playerRole);

                new Thread(new ClientHandler(client, playerRole)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final String playerRole;

        public ClientHandler(Socket clientSocket, String playerRole) {
            this.clientSocket = clientSocket;
            this.playerRole = playerRole;
        }

        @Override
        public void run() {
            try (
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            ) {
                out.writeUTF(playerRole); // 플레이어 역할 전송 (LEFT 또는 RIGHT)

                while (true) {
                    String command = in.readUTF(); // 클라이언트 명령 수신
                    synchronized (gameState) {
                        gameState.processCommand(command, playerRole); // 게임 상태 업데이트
                    }
                    broadcastGameState(); // 모든 클라이언트에 게임 상태 전송
                }
            } catch (IOException e) {
                System.out.println("클라이언트 연결 끊김: " + playerRole);
            }
        }

        private void broadcastGameState() {
            synchronized (gameState) {
                for (Socket client : clients) {
                    try {
                        DataOutputStream out = new DataOutputStream(client.getOutputStream());
                        out.writeUTF(gameState.toString()); // 게임 상태를 문자열로 전송
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

// 게임 상태 관리 클래스
class GameState {
    private int leftFortressHp = 1500;
    private int rightFortressHp = 1500;
    private final List<Unit> units = new ArrayList<>();

    public void processCommand(String command, String playerRole) {
        if (command.equals("SPAWN")) {
            // 플레이어 역할에 따라 유닛 생성
            if (playerRole.equals("LEFT")) {
                units.add(new Unit(100, 370, "LEFT")); // 왼쪽 유닛
            } else {
                units.add(new Unit(800, 370, "RIGHT")); // 오른쪽 유닛
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();
        state.append("LeftFortressHp:").append(leftFortressHp).append("\n");
        state.append("RightFortressHp:").append(rightFortressHp).append("\n");
        for (Unit unit : units) {
            state.append(unit).append("\n");
        }
        return state.toString();
    }
}

// 유닛 클래스
class Unit {
    private int x;
    private int y;
    private final String owner;

    public Unit(int x, int y, String owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Unit [x=" + x + ", y=" + y + ", owner=" + owner + "]";
    }
}
