/**
 * Where the program starts. Users are prompted for user names and passwords
 * then Google Earth opens.
 * 
 * @author Christopher Pagan
 * @version 1.0
 */

package com.twix.tailoredtravels;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;

public class Client {

	/**
	 * Components for login screen
	 */
	private static JPanel helpPanel, mainPanel, welcome, lmsg, lName,
			lPassword, enter, imgPanel;
	private static JFrame frame;
	private static JButton login;
	private static JTextField nameField;
	private static JPasswordField passField;
	private static MenuPanel mainMenu;

	/**
	 * Filenames
	 */
	private static String logoFileName;
	private static File helpFile;
	private static ImageIcon helpimg;

	/**
	 * Main method - Creates login panel, handles login information
	 * 
	 * @param args
	 *            no arguments used
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) {

		frame = new JFrame(Messages.getString("Client.0")); //$NON-NLS-1$
		Color bgcolor = new Color(74, 138, 255); // Background color for login
													// screen

		// Create panels for login screen
		mainPanel = new JPanel();
		helpPanel = new JPanel();
		welcome = new JPanel();
		lmsg = new JPanel();
		lName = new JPanel();
		lPassword = new JPanel();
		enter = new JPanel();
		imgPanel = new JPanel();
		login = new JButton(Messages.getString("Client.1")); //$NON-NLS-1$

		// Set background of all panels in login screen to the same color
		mainPanel.setBackground(bgcolor);
		imgPanel.setBackground(bgcolor);
		welcome.setBackground(bgcolor);
		lmsg.setBackground(bgcolor);
		lName.setBackground(bgcolor);
		lPassword.setBackground(bgcolor);
		enter.setBackground(bgcolor);

		// Add label to top of login screen
		welcome.add(new JLabel(Messages.getString("Client.2"))); //$NON-NLS-1$

		// Add logo, then resize and realign
		logoFileName = Messages.getString("Client.3"); //$NON-NLS-1$
		ImageIcon teamLogo = new ImageIcon(logoFileName);
		Image img = teamLogo.getImage();
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		int resizeW = width / 3;
		int resizeH = height / 3;
		Image resizedImg = img.getScaledInstance(resizeW, resizeH,
				Image.SCALE_SMOOTH);
		teamLogo = new ImageIcon(resizedImg);
		frame.setIconImage(img);
		JLabel logo = new JLabel(teamLogo);
		imgPanel.add(logo);

		// Add username field and label
		lmsg.add(new JLabel(Messages.getString("Client.4"))); //$NON-NLS-1$
		lName.add(new JLabel(Messages.getString("Client.5"))); //$NON-NLS-1$
		nameField = new JTextField(25);
		lName.add(nameField);

		// Add password field and label
		lPassword.add(new JLabel(Messages.getString("Client.6"))); //$NON-NLS-1$
		passField = new JPasswordField(25);
		String bullet = Messages.getString("Client.7"); //$NON-NLS-1$
		passField.setEchoChar(bullet.charAt(0)); // Blank out password field
		lPassword.add(passField);
		enter.add(login);

		// Create the help button
		helpimg = new ImageIcon(Messages.getString("Client.8")); //$NON-NLS-1$
		JButton help = new JButton(helpimg);
		help.setToolTipText(Messages.getString("Client.9")); //$NON-NLS-1$
		help.setBorder(BorderFactory.createEmptyBorder());

		// When user interacts with help button, load and open the help pdf file
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						helpFile = new File(Messages.getString("Client.10")); //$NON-NLS-1$
						Desktop.getDesktop().open(helpFile);
					} catch (IOException ex) {
						// no application registered for PDFs
						JOptionPane.showMessageDialog(
								null,
								Messages.getString("Client.11") //$NON-NLS-1$
										+ Messages.getString("Client.12"), Messages.getString("Client.13"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		// Add help button to the main login screen
		helpPanel.add(help);
		helpPanel.setBackground(bgcolor);
		help.setBackground(bgcolor);

		// Add all panels to the mainPanel in proper format
		mainPanel.add(welcome);
		mainPanel.add(imgPanel);
		mainPanel.add(lmsg);
		mainPanel.add(lName);
		mainPanel.add(lPassword);
		mainPanel.add(enter);
		mainPanel.add(helpPanel);

		// Set mainPanel and frame attributes
		mainPanel.setPreferredSize(new Dimension(500, 425));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setContentPane(mainPanel);
		frame.pack();

		// Check login credentials when user interacts with login button
		login.addActionListener(new ActionListener() {

			/**
			 * Overwritten method for ActionListener class. Verifies the user is
			 * authorized to use the system.
			 * 
			 * @param e
			 *            An ActionEvent is created when the user clicks the
			 *            login button
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				// Get username and password, immediately blank out both fields
				String userName = nameField.getText();
				char[] password = passField.getPassword();

				nameField.setText(null);
				passField.setText(null);

				// Try to instantiate DatabaseManager
				DatabaseManager dbm = null;
				try {
					dbm = new DatabaseManager();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(
							null,
							Messages.getString("Client.14"), Messages.getString("Client.15"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}

				// convert password to string
				String passwd = Messages.getString("Client.16"); //$NON-NLS-1$
				for (int i = 0; i < password.length; i++)
					passwd += password[i];

				try {
					// Is the user valid and in the database
					boolean validUser = dbm.login(userName, passwd);

					// Java documentation recommends clearing password array
					// after use
					for (int i = 0; i < password.length; i++)
						password[i] = ' ';

					// Is the user an admin
					boolean admin = dbm.isUserAdmin();
					if (!validUser) {
						JOptionPane.showMessageDialog(null,
								Messages.getString("Client.17"), //$NON-NLS-1$
								Messages.getString("Client.18"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						showMainMenu(dbm, userName, admin);
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(
							null,
							Messages.getString("Client.19"), Messages.getString("Client.20"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}

			}
		});
	}

	/**
	 * Opens up the main menu screen
	 * 
	 * @param dbm
	 *            the dbm that contains user and location information
	 * @param userName
	 *            the user's username
	 * @param admin
	 *            whether or not a user is an administrator
	 */
	public static void showMainMenu(DatabaseManager dbm, String userName,
			boolean admin) {

		// Get rid of the login frame and create new menuPanel
		frame.dispose();
		mainMenu = new MenuPanel(dbm, userName, admin);

		// Create new frame for menuPanel and add components
		JFrame frame2 = new JFrame(Messages.getString("Client.21")); //$NON-NLS-1$
		frame2.setIconImage(new ImageIcon(logoFileName).getImage());
		mainMenu.addComponents();
		frame2.setContentPane(mainMenu);
		frame2.setVisible(true);
		frame2.pack();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
