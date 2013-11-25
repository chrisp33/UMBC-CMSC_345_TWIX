/**
 * Where the program starts. Users are prompted for user names and passwords
 * then Google Earth opens.
 * 
 * @author Christopher Pagan
 * @version 1.0
 */

package com.twix.tailoredtravels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Client{

	private static JPanel panel, p, p1, p2, p3, p4;
	private static JFrame frame;
	private static JButton login;
	private static JTextField nameField;
	private static JPasswordField passField;
	private static MenuPanel mainMenu;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		frame = new JFrame("Tailored Travels");
		
		// cleanup/make better later
		panel = new JPanel();
		p = new JPanel();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		login = new JButton("Login");
		
		p.setBackground(new Color(0,91,255));
		p1.setBackground(new Color(0,91,255));
		p2.setBackground(new Color(0,91,255));
		p3.setBackground(new Color(0,91,255));
		p4.setBackground(new Color(0,91,255));

		p.add(new JLabel("Tailored Travels"));
		p1.add(new JLabel ("Enter your username and password"));
		p2.add(new JLabel("Username"));
		nameField = new JTextField(25);
		p2.add(nameField);
		
		p3.add(new JLabel("Password"));
		passField = new JPasswordField(25);
		String bullet = "\u2022";
		passField.setEchoChar(bullet.charAt(0));
		p3.add(passField);
		
		p4.add(login);
		
		panel.add(p);
		panel.add(p1);
		panel.add(p2);
		panel.add(p3);
		panel.add(p4);
		
		panel.setPreferredSize(new Dimension(600,300));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setContentPane(panel);
		frame.pack();
		
// check login credentials here ------------------------------------------------------
		login.addActionListener(new ActionListener()
		{
			
			/**
			 * Overwritten method for ActionListener class. Verifies the user is
			 * authorized to use the system.
			 * @param e An ActionEvent is created when the user clicks the login button
			 */
			public void actionPerformed(ActionEvent e)
			{
				
				String userName = nameField.getText();
				char[] password = passField.getPassword();
				
				nameField.setText(null);
				passField.setText(null);
				
				DatabaseManager dbm = null;
				
				try 
				{
					dbm = new DatabaseManager();
				} 
				catch (ClassNotFoundException e1) 
				{
					e1.printStackTrace();
				} 
				catch (SQLException e1)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}
				
				String passwd = "";
				
				//convert password to string
				for (int i = 0; i < password.length; i++)
					passwd += password[i];
					
				try 
				{
					boolean validUser = dbm.login(userName, passwd);
					// Java documentation recommends clearing password array after use
					for (int i = 0; i < password.length; i++)
						password[i] = ' ';
					boolean admin = dbm.isUserAdmin();
					if (!validUser) 
					{
						JOptionPane
								.showMessageDialog(
										null,
										"Username and password are incorrect, please try again.",
										"Invalid User",
										JOptionPane.ERROR_MESSAGE);
						return;
					} 
					else 
					{
						openGE(dbm, userName, admin);
					}
				} 
				catch (SQLException e1)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}
				
			}
		});
	}
	
	/**
	 * Opens up the Google Earth application and displays menu options for the user
	 * @param dbm the dbm that contains user and location information
	 * @param userName the user's username
	 * @param admin whether or not a user is an administrator
	 * @param locations the list of waypoints
	 */
	public static void openGE(DatabaseManager dbm, String userName, boolean admin)
	{

		frame.dispose();
		mainMenu = new MenuPanel(dbm, userName, admin);
		/*
		try
		{
			Process googleEarth = Runtime.getRuntime().exec("C:\\Program Files (x86)\\Google\\Google Earth\\client\\googleearth.exe");
		}
		catch (IOException e1) //Not sure if necessary for requirements (feature creep?)
		{
			//Install Google Earth or verify location of googleearth.exe
			String[] options = {"Install", "Browse", "Exit"};
			int option = JOptionPane.showOptionDialog(null, "Google Earth could not be found. Would you like locate or install it?", 
					"Can't Find Google Earth", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
			if (option == 2)
			{
				System.exit(0);
			}
			else if (option == 1)
			{
				//open JFileChooser for googleearth.exe
			}
			else
			{
				//Install GE
			}
		}
		*/
		
		JFrame frame2 = new JFrame("Tailored Travels");
		mainMenu.addComponents();
		frame2.setContentPane(mainMenu);
		frame2.setVisible(true);
		frame2.pack();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
