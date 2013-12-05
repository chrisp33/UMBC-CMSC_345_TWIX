/**
 * Progress bad runnable JPanel
 * 
 * @author Keith Cheng with Christopher Pagan
 */

package com.twix.tailoredtravels;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JPanel implements Runnable{
	private static final long serialVersionUID = -3474647204199764046L;
	
	JProgressBar aJProgressBar;
	boolean start = true;
	
	//When ProgressBar is run, call runbar() and sleep for 1000 as long as the progress bar is set to start
	public void run() {
		runBar();
		while(start)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.setVisible(false);
	}
	public void runBar()
	{
		//Set the size of this panel and add the progress bar
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
