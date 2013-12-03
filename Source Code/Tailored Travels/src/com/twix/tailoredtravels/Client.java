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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Client{

	/**
	 * Components for login
	 */
	private static JPanel panel, welcome, lmsg, lName, lPassword, enter, imgPanel;
	private static JFrame frame;
	private static JButton login;
	private static JTextField nameField;
	private static JPasswordField passField;
	private static MenuPanel mainMenu;
	private static String logoName;
	/**
	 * Main method
	 * @param args no arguments used
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args){
		
		// To get the same look and feel of the system the program is run on
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frame = new JFrame("Tailored Travels");
		Color bgcolor = new Color(74,138,255); //Background color for login screen

		
		panel = new JPanel();
		welcome = new JPanel();
		lmsg = new JPanel();
		lName = new JPanel();
		lPassword = new JPanel();
		enter = new JPanel();
		imgPanel = new JPanel();
		login = new JButton("Login");

		panel.setBackground(bgcolor);
		imgPanel.setBackground(bgcolor);
		welcome.setBackground(bgcolor);
		lmsg.setBackground(bgcolor);
		lName.setBackground(bgcolor);
		lPassword.setBackground(bgcolor);
		enter.setBackground(bgcolor);

		welcome.add(new JLabel("Welcome to Tailored Travels!"));
		
		//Add logo, then resize and realign
		logoName = "Final Draft of Logo.jpg";
		ImageIcon teamLogo = new ImageIcon(logoName);
		Image img = teamLogo.getImage();
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		int resizeW = width/3;
		int resizeH = height/3;
		Image resizedImg = img.getScaledInstance(resizeW, resizeH, Image.SCALE_SMOOTH);
		teamLogo = new ImageIcon(resizedImg);
		
		frame.setIconImage(img);
		JLabel logo = new JLabel(teamLogo);
		imgPanel.add(logo);
		lmsg.add(new JLabel ("Enter your username and password"));
		lName.add(new JLabel("Username"));
		nameField = new JTextField(25);
		lName.add(nameField);
		
		lPassword.add(new JLabel("Password"));
		passField = new JPasswordField(25);
		String bullet = "\u2022";
		passField.setEchoChar(bullet.charAt(0));
		lPassword.add(passField);
		enter.add(login);
		
		panel.add(welcome);
		panel.add(imgPanel);
		panel.add(lmsg);
		panel.add(lName);
		panel.add(lPassword);
		panel.add(enter);
		
		panel.setPreferredSize(new Dimension(500,400));
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
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program",
							"Error", JOptionPane.ERROR_MESSAGE);
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
						showMainMenu(dbm, userName, admin);
					}
				} 
				catch (SQLException e1)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", 
												  "Error", JOptionPane.ERROR_MESSAGE);
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
	public static void showMainMenu(DatabaseManager dbm, String userName, boolean admin)
	{

		frame.dispose();
		mainMenu = new MenuPanel(dbm, userName, admin);
		
		JFrame frame2 = new JFrame("Tailored Travels");
		frame2.setIconImage(new ImageIcon(logoName).getImage());
		mainMenu.addComponents();
		frame2.setContentPane(mainMenu);
		frame2.setVisible(true);
		frame2.pack();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
