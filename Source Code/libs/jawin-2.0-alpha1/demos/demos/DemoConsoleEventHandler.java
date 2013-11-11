/*
 * DemoConsoleEventHandler.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: DemoConsoleEventHandler.java,v 1.3 2004/06/14 20:22:23 arosii_moa Exp $ */

package demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jawin.donated.win32.Console;
import org.jawin.donated.win32.HandlerRoutine;

/**
 * This demo works only in the win32 console, and not in eg. the Cygwin console.
 * 
 * @version     $Revision: 1.3 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class DemoConsoleEventHandler {

	private static boolean exit = false;

	public static void main(String[] args) throws Exception {
	
		System.out.println(
			"\nThis demo installs a console event handler that catches "
				+ "CTRL-C events and gives you the chance to shutdown the app");
		Console.SetConsoleCtrlHandler(new HandlerRoutine() {
			public boolean call(long dwCtrlType) {
				if (CTRL_C_EVENT == dwCtrlType) {
					System.out.println("Your pressed CTRL-C.  Type 'EXIT' to exit");
					BufferedReader br =
						new BufferedReader(new InputStreamReader(System.in));
					try {
						String choice = br.readLine();
						System.out.println("You typed " + choice);
						if ("EXIT".equalsIgnoreCase(choice)) {
							exit = true;
							return false;
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
						return false;
					}
				}
				return true;
			}
		}, true);
		
		int countdown = 5;
		while (countdown-- > 0 && !exit) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ie) {
			}
			System.out.println("Still going " + countdown);
		}
	}
}
