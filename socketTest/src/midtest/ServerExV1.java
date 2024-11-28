package midtest;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerExV1 {
	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedWriter out = null;
		ServerSocket listener = null;
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		try {
			listener = new ServerSocket(9999);
			System.out.println("연결을 기다리고 있습니다....");
			socket = listener.accept();
			System.out.println("연결 되었습니다.");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 소켓 입력 스트림
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while(true) {
				String inputMessage = in.readLine();
				if(inputMessage.equals("Bye")) {
					System.out.println("클라이언트에서 bye로 연결 종료");
					break;
				}
				System.out.println("클라이언트:" + inputMessage);
				System.out.println("보내기>>");
				String outputMessage = scanner.nextLine();
				out.write(outputMessage + "\n");
				out.flush();
			}

		}catch(IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				scanner.close(); // scanner 닫기
				socket.close(); // 통신용 소켓 닫기
				listener.close(); // 서버 소켓 닫기
				
			}catch(IOException e) {
				System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
			}
		}
		
		
		
	}

}
