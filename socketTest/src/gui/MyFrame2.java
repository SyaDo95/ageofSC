package gui;

import javax.swing.*;

public class MyFrame2 extends JFrame{
	public MyFrame2() {
		setTitle("두번째 프레임");
		setSize(300,300);
		JButton bnt = new JButton("버튼1");
		add(bnt);
		setLocation(400,200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public static void main(String [] args) {
		MyFrame2 mf = new MyFrame2();
		
	}

}
