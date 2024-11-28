package server;

import java.io.*;
import java.net.*;

public class GameClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            String playerRole = in.readUTF(); // 서버에서 역할 수신
            System.out.println("플레이어 역할: " + playerRole);

            new Thread(() -> { // 서버에서 게임 상태 수신
                try {
                    while (true) {
                        String gameState = in.readUTF();
                        System.out.println("게임 상태:\n" + gameState);
                    }
                } catch (IOException e) {
                    System.out.println("서버 연결 끊김");
                }
            }).start();

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("유닛 생성 명령(SPAWN): ");
                String command = userInput.readLine();
                out.writeUTF(command); // 서버로 명령 전송
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
