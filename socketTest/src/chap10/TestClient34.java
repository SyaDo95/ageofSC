package chap10;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestClient34 {
    public static void main(String[] args) {

        try {
        	Socket socket = new Socket("localhost", 9999); // 클라이언트 소켓 생성. 서버에 바로 접속
        	
        	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        	
        	String inputMessage = in.readLine(); // 서버로부터 한 행의 텍스트 받음
			System.out.println("받은 메시지: " + inputMessage); // 서버가 보낸 메시지 화면에 출력
			
            socket.close();
            
        } catch (IOException e) {
            System.out.println("오류가 발생했습니다.");
        }
    }
}

