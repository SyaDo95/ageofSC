package mythread;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ThreadTimerEx2 extends JFrame{
	public ThreadTimerEx2() {
		setTitle("Thread를 상속받은 타이머 스레드 예제");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		JLabel timerLabel = new JLabel();
		timerLabel.setFont(new Font("Gothic", Font.ITALIC, 80));
		c.add(timerLabel);
		Runnable th = new TimerThread2(timerLabel);
		Thread t = new Thread(th);
		setSize(300,170);
		setVisible(true);
		t.start();
		}
		public static void main(String[] args) {
		new ThreadTimerEx2();
		}

}
