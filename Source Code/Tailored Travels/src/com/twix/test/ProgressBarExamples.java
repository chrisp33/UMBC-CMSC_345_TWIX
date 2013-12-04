package test;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressBarExamples extends Thread{
	private boolean start = true;
	private JFrame frame = new JFrame("Stepping Progress");
	JProgressBar aJProgressBar;
	public void run()
	{
		runBar();
		while(!start)
		{
			aJProgressBar.setVisible(false);
		}
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
		frame.add(aJProgressBar, BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);

	}
}