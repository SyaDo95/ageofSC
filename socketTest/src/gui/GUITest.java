package gui;

import javax.swing.JFrame;

public class GUITest {
	public static void main(String [] args) {
		MyFrame fa = new MyFrame();

		}
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
