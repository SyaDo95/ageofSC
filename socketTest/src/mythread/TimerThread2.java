package mythread;

import javax.swing.JLabel;

public class TimerThread2 implements Runnable{
	private JLabel timerLabel;
	public TimerThread2(JLabel timerLabel) {
	this.timerLabel = timerLabel;
	}
	@Override
	public void run() {
	int n=0;
	while(true) {
	timerLabel.setText(Integer.toString(n));
	n++;
	try {
	Thread.sleep(1000);
	}
	catch(InterruptedException e) {
	return;
	}
	}
	}


}
