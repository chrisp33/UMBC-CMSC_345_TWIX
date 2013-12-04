package com.twix.tailoredtravels;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JPanel implements Runnable{
	JProgressBar aJProgressBar;
	boolean start = true;
	@Override
	public void run() {
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
		this.setVisible(false);
	}
	public void runBar()
	{
		this.setSize(400, 100);
		aJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		aJProgressBar.setString("");
		aJProgressBar.setStringPainted(true);
		aJProgressBar.setIndeterminate(true);
		add(aJProgressBar);
		setSize(200, 60);

	}
	public void setStart(boolean start)
	{
		this.start = start;
	}
}
