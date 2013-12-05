package com.twix.test;

public class RunThread {
public static void main(String[] args)
{
	ProgressBarExamples bar = new ProgressBarExamples();
	bar.run();
	try {
		Thread.sleep(3000);
		bar.setStart(false);
		
		System.out.println("Hello world once");
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Hello world Twice");

}
}
