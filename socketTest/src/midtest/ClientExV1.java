package midtest;

import java.io.*;
import java.util.*;
import java.net.*;

public class ClientExV1 {
	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedWriter out = null;
		Scanner scanner = new Scanner(System.in);
		Socket socket = null;
		try {
			socket = new Socket("localhost", 9999);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while(true) {
				System.out.println("보내기>>");
				String outputMessage = scanner.nextLine();
				if(outputMessage.equals("bye")) {
					out.write(outputMessage+"\n");
					out.flush();
					break;
				}
				out.write(outputMessage+"\n");
				out.flush();
				String inputMessage = in.readLine();
				System.out.println("서버: " + inputMessage);
			}
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				scanner.close();
				if(socket != null) socket.close(); // 클라이언트 소켓 닫기
				
			}catch(IOException e) {
				System.out.println("서버와 채팅 중 오류가 발생했습니다.");
			}
		}
	}

}
