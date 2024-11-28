package myguiserver;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

public class GUIServer extends JFrame{
	public GUIServer() {
		setTitle("서버창");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("New label");
		getContentPane().add(lblNewLabel, BorderLayout.SOUTH);
		
	}
	class MyFrame extends JFrame {
		public MyFrame() {
		setTitle("첫번째 프레임");
		setSize(300, 300);
		setLocation(100, 200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}

	
	public static void main(String [] args) {
		
	}

}



