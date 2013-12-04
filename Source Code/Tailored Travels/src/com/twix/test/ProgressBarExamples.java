package com.twix.test;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarExamples extends Thread{
	private boolean start = true;
	private JFrame frame = new JFrame("Stepping Progress");
	JProgressBar aJProgressBar;
	JPanel panel = new JPanel();
	public void run()
	{
		runBar();
		while(start)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		panel.setVisible(false);
		frame.setVisible(false);
		frame.dispose();
	}
	public void setStart(boolean start)
	{
		this.start = start;
	}
	public void runBar()
	{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JProgressBar aJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		aJProgressBar.setString("");
		aJProgressBar.setStringPainted(true);
		aJProgressBar.setIndeterminate(true);
		panel.add(aJProgressBar);
		frame.add(panel, BorderLayout.NORTH);
		panel.setSize(200, 60);
		frame.pack();
		frame.setVisible(true);

	}
}