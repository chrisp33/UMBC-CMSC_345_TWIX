/**
 * Panel containing the main menu and most administrator and user functions. 
 * Only logged in users will be able to access this window. 
 * 
 * @author Christopher Pagan
 * @version 1.0
 */

package com.twix.tailoredtravels;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.twix.tailoredtravels.DistCalcDriver;
import com.twix.tailoredtravels.GoogleEarthManager;
import com.twix.tailoredtravels.GoogleEarthPath;
import com.twix.tailoredtravels.Waypoint;


public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 3359477065217156534L;

	/**
	 * Components to add to the panel
	 */

	private JButton calcRoute, calcDist, addLocation, removeLocation, addUser,
	removeUser, logout, edit, help;
	private JLabel welcomeMsg,availMsg;
	private JList<String> list;
	private JScrollPane scroller;
	private JPanel welcome, addRmLoc, addRmUser, waypointsList, calculations, edits, exit, 
	adminPanel, helpPanel, progress;
	final int REQUIRED_NUM = 3;
	final int REQUIRED_MAX = 12;
	private JProgressBar bar;
	private JDialog dialog;
	
	/**
	 * Filenames
	 */
	private static File helpFile;
	private static ImageIcon helpimg;

	/**
	 * The current user's username
	 */
	private String currentUser;

	/**
	 * Is the current user an admin
	 */
	private boolean isAdmin;

	/**
	 * Database instance
	 */
	private DatabaseManager dbm;

	/**
	 * Constructor for main menu
	 * 
	 * @param dbm the database manager
	 * @param user the user's username
	 * @param admin whether or not the user is an administrator
	 */
	public MenuPanel(DatabaseManager dbm, String user, boolean admin)
	{
		//Assign variables
		this.dbm = dbm;
		isAdmin = admin;
		currentUser = user;
		Color bgColor = new Color(48,235,71); //Color for menuPanel background
		TitledBorder adminBorder = BorderFactory.createTitledBorder("Administrative Controls");
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		//Create main menu panels
		helpPanel = new JPanel();
		welcome = new JPanel();
		addRmLoc = new JPanel();
		addRmUser = new JPanel();
		edits = new JPanel();
		adminPanel = new JPanel();
		bar = new JProgressBar();
		bar.setIndeterminate(true);
		//Create help button
		helpimg = new ImageIcon("help.png");
		help = new JButton(helpimg);
		help.setToolTipText("Help");
		help.setBorder(BorderFactory.createEmptyBorder());

		//When user interacts with help button, load and open the help pdf file
		help.addActionListener(new ActionListener(){public void actionPerformed (ActionEvent e)
		{
			if (Desktop.isDesktopSupported()) {
				try {
					helpFile = new File("Tailored Travels Help.pdf");
					Desktop.getDesktop().open(helpFile);
				} catch (IOException ex) {
					// no application registered for PDFs
					JOptionPane.showMessageDialog(null, "A PDF viewer is needed to view the help"
							+ " file.", "Cannot Show Help", JOptionPane.ERROR_MESSAGE);
				}
			}
		}});
		helpPanel.add(help);
		helpPanel.setBackground(bgColor);
		help.setBackground(bgColor);



		//Create these components if the user is an administrator
		if (isAdmin)
		{
			//Admin section
			adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
			adminBorder.setBorder(b);
			adminPanel.setBorder(adminBorder);

			//Add/Remove/Edit User/Location buttons
			addLocation = new JButton("Add Location");
			removeLocation = new JButton("Remove Location");			
			addUser = new JButton("Add User");
			removeUser = new JButton("Remove User");
			edit = new JButton("Edit Location");

			//Add all buttons to panel
			addRmLoc.add(addLocation);
			addRmLoc.add(removeLocation);
			addRmUser.add(addUser);
			addRmUser.add(removeUser);
			edits.add(edit);
			adminPanel.add(addRmLoc);
			adminPanel.add(addRmUser);
			adminPanel.add(edits);

			//Add ActionListeners to buttons
			addLocation.addActionListener(new AddLocListener());
			removeLocation.addActionListener(new RemLocListener());
			addUser.addActionListener(new AddUserListener());
			removeUser.addActionListener(new RemUserListener());
			edit.addActionListener(new EditListener());
		}

		//Instantiate GUI components
		welcomeMsg = new JLabel("Welcome back, " + user + "!");
		welcome.add(welcomeMsg);
		availMsg = new JLabel("<html>Available Location<br>Select for info</html>");
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scroller = new JScrollPane(list);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		calcRoute = new JButton("Calculate Route");
		calcDist = new JButton("Calculate Distance");
		logout = new JButton("Log Out");

		//Add ActionListeners for normal user buttons
		list.addListSelectionListener(new ListSelListener());
		calcRoute.addActionListener(new RouteListener());
		calcDist.addActionListener(new DistListener());
		logout.addActionListener(new LogoutListener());

		//Add list of waypoints
		waypointsList = new JPanel();
		waypointsList.add(availMsg);
		waypointsList.add(scroller);
		waypointsList.setBorder(b);
		calculations = new JPanel();
		calculations.add(calcRoute);
		calculations.add(calcDist);
		exit = new JPanel();
		exit.add(logout);

		//Set uniform background color for all components
		setBackground(bgColor);
		adminPanel.setBackground(bgColor);
		welcome.setBackground(bgColor);
		addRmLoc.setBackground(bgColor);
		addRmUser.setBackground(bgColor);
		waypointsList.setBackground(bgColor);
		calculations.setBackground(bgColor);
		edits.setBackground(bgColor);
		exit.setBackground(bgColor);

		updateJList();
	}



	/**
	 * Add appropriate components to the main menu.
	 */
	public void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(welcome);

		if(isAdmin)
		{
			add(adminPanel);
		}

		add(waypointsList);
		add(calculations);
		add(exit);
		add(helpPanel);
	}

	/**
	 * Update list of waypoints for changes with edits, adding and removing
	 */
	public void updateJList()
	{
		try 
		{
			//Update JList with the new waypoint names
			LinkedList<Waypoint> dbWaypoints = dbm.getWaypoints();
			Vector<String> listData = new Vector<String>();
			for (Waypoint wp: dbWaypoints)
			{
				listData.add(wp.getName());
			}
			list.setListData(listData);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Action listener for "Add Location" button
	 */
	private class AddLocListener implements ActionListener
	{

		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for waypoint data and adds it to the system.
		 * 
		 * @param ae An ActionEvent is created when the user clicks the "Add Location" button
		 */
		public void actionPerformed(ActionEvent ae) 
		{
			JTextField name = new JTextField();
			JTextField latitude = new JTextField();
			JTextField longitude = new JTextField();
			JTextField details = new JTextField();

			//Prompt to enter details
			Object[] message = {"Enter the name of the location:", name,
					"Enter the location's latitude position:", latitude, 
					"Enter the location's longitude position:", longitude,
					"Enter some detail about the location:", details};

			//Assign input to variables
			int sel = JOptionPane.showConfirmDialog(null, message,
					"Add Location", JOptionPane.OK_CANCEL_OPTION);
			String wName = name.getText();
			String lat = latitude.getText();
			String lon = longitude.getText();
			String det = details.getText();

			//If cancel is selected, confirm cancellation and exit add new location
			if (sel == JOptionPane.CANCEL_OPTION)
			{
				JOptionPane.showMessageDialog(null,
						"No locations will be added.","Add Location Cancelled",
						JOptionPane.INFORMATION_MESSAGE);				
				return;
			}

			//Check for blank entry
			if (wName.equals("") || lat.equals("") || lon.equals("") || det.equals(""))
			{
				JOptionPane.showMessageDialog(null, "Cannot leave any fields blank.",
						"Location Not Added", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//float variables for latitude and longitude
			float wLat, wLong;
			try
			{
				wLat = Float.parseFloat(lat);
				wLong = Float.parseFloat(lon);
			}
			catch(NumberFormatException e1)
			{
				JOptionPane.showMessageDialog(null, "Latitude and can only be numbers",
						"Invalid Characters", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Latitude and Logitude validation
			if (wLat < -90 || wLat > 90 || wLong < -180 || wLong > 180)
			{
				String msg = "Latitude can only range from -90 degrees to +90 degrees.\n" +
				"Longitude can only range from -180 degrees to +180 degrees.";
				JOptionPane.showMessageDialog(null, msg,
						"Invalid Values", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Add new waypoint to database manager
			try
			{
				boolean added;
				added = dbm.addLocation(wLat, wLong, wName, det);

				if (!added)
				{
					JOptionPane.showMessageDialog(null, "The location \"" + wName 
							+ "\" has not been added successfully.", "Location Not Added", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(null, "The location \"" + wName 
						+ "\" has been added.", "Location Added", 
						JOptionPane.INFORMATION_MESSAGE);

				updateJList();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.",
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Action listener for "Remove Location" button
	 */
	private class RemLocListener implements ActionListener
	{

		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for waypoint data and removes it from the system.
		 * 
		 * @param ae An ActionEvent is created when the user clicks the "Remove Location" button
		 */
		public void actionPerformed(ActionEvent ae) 
		{
			LinkedList<Waypoint> waypoints = null;
			try 
			{
				waypoints = dbm.getWaypoints();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

			//Initialize array of removable waypoint names
			String[] waypointNames = new String[waypoints.size()];

			//Error message for empty list
			if (waypointNames.length == 0)
			{
				JOptionPane.showMessageDialog(null,
						"Unable to remove any locations. Must first have locations available", 
						"Remove Location Cancelled", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				waypointNames[i] = waypoints.get(i).getName();
			}

			String remLoc = (String) JOptionPane.showInputDialog(null, 
					"Select a location to remove:",
					"Remove Location", JOptionPane.QUESTION_MESSAGE, null, waypointNames, 
					waypointNames[0]);

			//Notification if user clicks cancel button
			if (remLoc == null)
			{
				JOptionPane.showMessageDialog(null, "No locations will be removed from the list.",
						"Remove Location Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			//User confirmation of remove
			int sel = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove \"" +
					remLoc + "\"?", "Confirm Deletion",JOptionPane.WARNING_MESSAGE);

			//Message when user clicks "no" button
			if (sel == JOptionPane.CANCEL_OPTION)
			{
				JOptionPane.showMessageDialog(null, "\"" + remLoc +
						"\" will not be removed from the list.", "Remove Cancelled",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			try
			{
				//Remove waypoint
				dbm.removeLocation(remLoc);
				JOptionPane.showMessageDialog(null, "\"" + remLoc + 
						"\" has been removed from the list successfully.", "Location Removed", 
						JOptionPane.INFORMATION_MESSAGE);

			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

			updateJList();
		}
	}

	/**
	 * Action listener for "Add User" button
	 */
	private class AddUserListener implements ActionListener
	{

		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for user data and adds it to the system.
		 * 
		 * @param ae An ActionEvent is created when the user clicks the "Add User" button
		 */
		public void actionPerformed(ActionEvent ae) 
		{

			//Prompt for username and password
			JTextField nameField = new JTextField();
			JPasswordField passField =  new JPasswordField();
			Object[] message = {
					"Enter the New Username",
					nameField, 
					"Enter the New Password", 
					passField};
			int option = JOptionPane.showConfirmDialog(null, message, 
					"Enter Details for New User", JOptionPane.OK_CANCEL_OPTION);
			String newName = nameField.getText();
			char[] passChars = passField.getPassword();

			//Check for valid data
			if ((option == JOptionPane.OK_OPTION) && (!newName.equals(""))&&(passChars.length > 0))
			{
				boolean admin = false;

				//Prompt for new user privlege level
				Object[] opts = {"Administrator", "Normal User"};
				String selection = (String) JOptionPane.showInputDialog(null, 
						"Select User Privilege Level", "User Privilege", 
						JOptionPane.QUESTION_MESSAGE, null, opts, "Administrator");

				if (selection == null)
				{
					//Inform user that the new user has not been added
					JOptionPane.showMessageDialog(null, "No new users have been added.",
							"User Not Added", JOptionPane.ERROR_MESSAGE);
					return;
				}

				String addMsg = "A new user \"" + newName + "\" has been successfully added as ";

				if (selection.equals("Administrator"))
				{
					admin = true;
					addMsg += "an administrator.";
				}
				else
				{
					admin = false;
					addMsg += "a normal user.";
				}

				//Convert password to string
				String password = "";
				for (int i = 0; i < passChars.length; i++)
				{
					password += passChars[i];
					passChars[i] = ' '; // clear array for security purposes
				}

				try 
				{
					//Add user to database
					if (dbm.addUser(newName, password, admin))
						JOptionPane.showMessageDialog(null, addMsg, "Added User",
								JOptionPane.INFORMATION_MESSAGE);
					else 
						JOptionPane.showMessageDialog(null, "User already exists.",
								"User Not Added", JOptionPane.ERROR_MESSAGE);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", 
							"Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					System.exit(0);
				}

			}
			else if (option == JOptionPane.CANCEL_OPTION)
			{
				//User is not added if "cancel" is pressed
				JOptionPane.showMessageDialog(null, "No new users have been added.",
						"Add User Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			else
			{
				JOptionPane.showMessageDialog(null,
						"Both the new user's name and password must be entered.",
						"Add User Cancelled", JOptionPane.ERROR_MESSAGE);
				return;
			}

		}
	}

	/**
	 * Action listener for "Remove User" button
	 */
	private class RemUserListener implements ActionListener
	{

		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for user data and removes it from the system.
		 * 
		 * @param ae An ActionEvent is created when the user clicks the "Remove User" button
		 */
		public void actionPerformed(ActionEvent ae) 
		{

			//Prompt for user name
			String name = JOptionPane.showInputDialog(null, "Enter the name of the user.", 
					"Remove User", JOptionPane.QUESTION_MESSAGE);
			if (name == null)
			{
				JOptionPane.showMessageDialog(null,"No users have been removed from the database.",
						"Remove User Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (name.equals(""))
			{
				JOptionPane.showMessageDialog(null, "Please enter the user's name.",
						"User Not Removed", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validation if removing the current user
			if (currentUser.equals(name))
			{
				JOptionPane.showMessageDialog(null, 
						"Cannot remove current users. Log in as another administrator first.",
						"Cannot Remove User", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int sel = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove user \""
					+ name + "\"?", "Confirm Remove User", JOptionPane.WARNING_MESSAGE);

			// Cancel operation if No option is clicked
			if (sel == JOptionPane.NO_OPTION)
			{
				JOptionPane.showMessageDialog(null, "No users have been removed.", 
						"User Not Removed", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			try 
			{
				boolean removed = dbm.removeUser(name);
				if (removed)
				{
					JOptionPane.showMessageDialog(null, "The user \"" + name + 
							"\" has been successfully removed.", "User Successfully Removed",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					String errmsg = "The user \"" + name + "\" has not been successfully removed."
					+ "\nUsers that are not in the database are never removed.";
					JOptionPane.showMessageDialog(null,errmsg, "User Remove Unsuccessful",
							JOptionPane.ERROR_MESSAGE);
				}
			} 
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Action listener for "Edit Location" button
	 */
	private class EditListener implements ActionListener
	{
		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for which type of data is edited for the waypoint
		 * 
		 * @param ae An ActionEvent is created when the user clicks the "Edit Location" button
		 */
		public void actionPerformed(ActionEvent e)
		{

			LinkedList<Waypoint> waypoints = null;
			try 
			{
				waypoints = dbm.getWaypoints();
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}

			//Initialize array of editable waypoint names
			String[] waypointNames = new String[waypoints.size()];

			//Error message for empty list
			if (waypointNames.length == 0)
			{
				JOptionPane.showMessageDialog(null,
						"Unable to edit any locations. Must first have locations available.",
						"Not Enough Locations", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				waypointNames[i] = waypoints.get(i).getName();
			}

			String editLoc = (String) JOptionPane.showInputDialog(null, 
					"Select a location to edit:",
					"Edit Location", JOptionPane.QUESTION_MESSAGE, null, waypointNames, 
					waypointNames[0]);

			//Notification if user clicks cancel button
			if (editLoc == null)
			{
				JOptionPane.showMessageDialog(null, "No locations from the list will be edited.",
						"Edit Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			//Prompt for edit type
			Object[] options = {"Name", "Coordinates", "Description", "Cancel"};
			int sel = JOptionPane.showOptionDialog(null, "What would you like to edit for \"" +
					editLoc + "\"?", "Edit Options", JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, options, options[3]);

			//Edit Name
			if (sel == 0)
			{
				String newName = JOptionPane.showInputDialog(null,
						"Enter the new name for \"" + editLoc +"\".",
						"Edit Name", JOptionPane.QUESTION_MESSAGE);

				//Exit operation if cancelled
				if (newName == null)
				{
					JOptionPane.showMessageDialog(null, 
							"The edit name operation has been cancelled.",
							"Edit Name Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				//Loop for empty field
				while (newName == null || newName.equals(""))
				{
					JOptionPane.showMessageDialog(null, "Please enter a new name.", "Enter Name",
							JOptionPane.WARNING_MESSAGE);

					newName = JOptionPane.showInputDialog(null,
							"Enter the new name for \"" + editLoc +"\".",
							"Edit Name", JOptionPane.QUESTION_MESSAGE);

					//Exit operation if cancelled
					if (newName == null)
					{
						JOptionPane.showMessageDialog(null, 
								"The edit name operation has been cancelled.",
								"Edit Name Cancelled", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}

				try
				{
					dbm.setWaypointName(editLoc, newName);
					JOptionPane.showMessageDialog(null,
							"\""+ editLoc + "\" has been changed to \"" + newName +
							"\" successfully.", "Name Edited",
							JOptionPane.INFORMATION_MESSAGE);
				} 
				catch (SQLException e1) 
				{
					JOptionPane.showMessageDialog(null, "\"" + editLoc + "\" was not found.",
							"Edit Cancelled", JOptionPane.ERROR_MESSAGE);
				}

				updateJList();
			}
			//Edit coordinates
			else if (sel == 1)
			{
				//Prompt for latitude and longitude
				JTextField latField = new JTextField();
				JTextField longField =  new JTextField();
				Object[] message = {
						"Enter the new latitude",
						latField, 
						"Enter the new longitude", 
						longField};
				int option = JOptionPane.showConfirmDialog(null, message, 
						"Enter New Coordinates", JOptionPane.OK_CANCEL_OPTION);

				if (option == JOptionPane.CANCEL_OPTION)
				{
					JOptionPane.showMessageDialog(null, 
							"The edit coordinates operation has been cancelled.",
							"Edit Coordinates Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				float newLat;
				float newLong;

				try
				{
					newLat = Float.parseFloat(latField.getText());
					newLong = Float.parseFloat(longField.getText());
				}
				catch (NumberFormatException ne)
				{
					JOptionPane.showMessageDialog(null, 
							"Latitude and longitude must only be numeric values.","Invalid Values",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Validation for latitude and longitude values
				while (newLat < -90 || newLat > 90 || newLong < -180 || newLong > 180)
				{

					String msg = "Latitude can only range from -90 degrees to +90 degrees.\n" +
					"Longitude can only range from -180 degrees to +180 degrees.";
					JOptionPane.showMessageDialog(null, msg,
							"Invalid Values", JOptionPane.ERROR_MESSAGE);

					Object[] message1 = {
							"Enter the new latitude",
							latField, 
							"Enter the new longitude", 
							longField};
					int option1 = JOptionPane.showConfirmDialog(null, message1, 
							"Enter New Coordinates", JOptionPane.OK_CANCEL_OPTION);

					if (option1 == JOptionPane.CANCEL_OPTION)
					{
						JOptionPane.showMessageDialog(null, 
								"The edit coordinates operation has been cancelled.",
								"Edit Coordinates Cancelled", JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					try
					{
						newLat = Float.parseFloat(latField.getText());
						newLong = Float.parseFloat(longField.getText());
					}
					catch (NumberFormatException ne)
					{
						JOptionPane.showMessageDialog(null, 
								"Latitude and longitude must only be numeric values.", 
								"Invalid Values", JOptionPane.ERROR_MESSAGE);
						return;
					}

				}

				try
				{
					dbm.setWaypointLatLong(editLoc, newLat, newLong);
					JOptionPane.showMessageDialog(null, "\"" + editLoc
							+ "\"s coordinates have been updated successfully.", 
							"Coordinates Edited", JOptionPane.INFORMATION_MESSAGE);
				} 
				catch (SQLException e1) 
				{
					JOptionPane.showMessageDialog(null, "\"" + editLoc + "\" was not found.",
							"Edit Cancelled", JOptionPane.ERROR_MESSAGE);
				}

				updateJList();
			}
			//Edit Description
			else if (sel == 2)
			{
				String newDesc = JOptionPane.showInputDialog(null, 
						"Enter the new description for \"" + editLoc + "\".",
						"Edit Description", JOptionPane.PLAIN_MESSAGE);

				if (newDesc == null)
				{
					JOptionPane.showMessageDialog(null, 
							"The edit description operation has been cancelled.",
							"Edit Description Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				while (newDesc == null || newDesc.equals(""))
				{

					if (newDesc == null)
					{
						JOptionPane.showMessageDialog(null, 
								"The edit description operation has been cancelled.",
								"Edit Description Cancelled", JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					JOptionPane.showMessageDialog(null, "Enter a new description." ,
							"Enter Description", JOptionPane.ERROR_MESSAGE);

					newDesc = JOptionPane.showInputDialog(null, 
							"Enter the new description for \"" + editLoc + "\".",
							"Edit Description", JOptionPane.PLAIN_MESSAGE);

				}

				try 
				{
					dbm.setWaypointDescription(editLoc, newDesc);
					JOptionPane.showMessageDialog(null,
							"\"" + editLoc + "\"'s description has been edited successfully.",
							"Description Edited", JOptionPane.INFORMATION_MESSAGE);
				} 
				catch (SQLException e1) 
				{
					JOptionPane.showMessageDialog(null, "\"" + editLoc + "\" was not found.",
							"Edit Location Cancelled", JOptionPane.ERROR_MESSAGE);
				}
			}
			//Cancel
			else
			{
				JOptionPane.showMessageDialog(null, "\"" + editLoc + "\" will not be edited.",
						"Edit Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	}

	/**
	 * List selection listener for when an entry in the list of locations is selected
	 */
	private class ListSelListener implements ListSelectionListener
	{

		public void valueChanged(ListSelectionEvent e) 
		{
			//Check if selection is changing due to mouse click
			boolean valAdjusting = e.getValueIsAdjusting();

			if (!valAdjusting)
			{
				//Get the selection
				String selection = list.getSelectedValue();
				Waypoint selectedWaypoint = null;
				LinkedList<Waypoint> points = null;
				try 
				{
					points = dbm.getWaypoints();

					if (points == null)
						throw new SQLException("No waypoints found.");
				} 
				catch (SQLException e1)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program",
							"Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}

				//ASSUMES no duplicate waypoint names
				for (Waypoint wp: points)
				{
					if (wp.getName().equals(selection))
						selectedWaypoint = wp;
				}

				//Error if waypoint cannot be found
				if (selectedWaypoint == null)
				{
					//Exit operation if list has no items selected
					if (list.isSelectionEmpty())
						return;

					JOptionPane.showMessageDialog(null, "Database Error. Exiting Operation",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Split waypoint details into separate sentences for formatting
				String[] wpDetails = selectedWaypoint.getDescription().split("\\.");
				String wpDet = " ";
				for (int i = 0; i < wpDetails.length; i++)
				{
					//Fix for java.lang.String.format percent escape
					if (wpDetails[i].contains("%"))
						wpDet += wpDetails[i].replace("%", "%%") + ".\n\t";
					else
						wpDet += wpDetails[i] + ".\n\t";
				}

				String waypointDetails = String.format(
						"Here are the details of this location:\n\n"+
						"Name:\t " + selectedWaypoint.getName() + "\n" +
						"Latitude:\t %.3f\u00B0 \n" +
						"Longitude:\t %.3f\u00B0 \n" +
						"Additional information:\n\t" + wpDet,
						selectedWaypoint.getLatitude(), selectedWaypoint.getLongitude() 
				);

				JTextArea details = new JTextArea(waypointDetails);
				details.setEditable(false);

				JOptionPane.showMessageDialog(null, details,
						"Location Details", JOptionPane.INFORMATION_MESSAGE);

				list.clearSelection();
				return;
			}
		}

	}

	/**
	 * Action listener for "Calculate Route" button
	 */
	private class RouteListener implements ActionListener
	{

		/**
		 * Overwritten method for ActionListener class. 
		 * Calculates the route given the waypoints the user has selected.
		 * @param ae An ActionEvent is created when the user clicks the "Calculate Route" button
		 */
		public void actionPerformed(ActionEvent arg0) 
		{

			//Get list of points from database
			LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
			try 
			{
				waypoints = dbm.getWaypoints();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", 
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

			//Validation for fewer than two available locations
			if (waypoints.size() < 2)
			{
				JOptionPane.showMessageDialog(null, 
						"There must be two locations available to find the shortest route",
						"Need More Locations", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Prompt for starting point
			String[] firstPts = new String[waypoints.size()];

			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				firstPts[i] = waypoints.get(i).getName();
			}
			String startPoint = (String) JOptionPane.showInputDialog(null, 
					"Select the starting location:","Select Starting Point", 
					JOptionPane.PLAIN_MESSAGE, null, firstPts, firstPts[0]);

			//If cancelled
			if (startPoint == null)
			{
				JOptionPane.showMessageDialog(null,
						"The operation to calculate route has been cancelled.",
						"Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			//Prompt for ending point
			ArrayList<String> secondList = new ArrayList<String>(); 
			String[] secondPts = new String[waypoints.size() - 1];

			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				if (!waypoints.get(i).getName().equals(startPoint))
					secondList.add(waypoints.get(i).getName());
			}

			for (int i = 0; i < secondList.size(); i++)
			{
				secondPts[i] = secondList.get(i);
			}

			String endPoint = (String) JOptionPane.showInputDialog(null,
					"Select the ending location:","Select Ending Point", 
					JOptionPane.PLAIN_MESSAGE, null, secondPts, secondPts[0]);

			//If cancelled when selecting second point
			if (endPoint == null)
			{
				JOptionPane.showMessageDialog(null,
						"The operation to calculate route has been cancelled.", 
						"Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			//Use waypoint names to calculate route
			Waypoint ptA = null;
			Waypoint ptB = null;

			ArrayList<Waypoint> points = new ArrayList<Waypoint>();
			LinkedList<Waypoint> pts = new LinkedList<Waypoint>();
			try
			{
				//Find corresponding Waypoints with names
				pts = dbm.getWaypoints();
				for (int i = 0; i < pts.size(); i++)
				{
					points.add(pts.get(i));
					if (pts.get(i).getName().equals(startPoint))
						ptA = pts.get(i);
					if (pts.get(i).getName().equals(endPoint))
						ptB = pts.get(i);
				}

				if (ptA == null || ptB == null)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Operation.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				ArrayList<Waypoint> selectedPoints = new ArrayList<Waypoint>();

				//Number of required points in route is greater than 2
				if (REQUIRED_NUM >= 2) 
				{
					//Remove the starting and end points for other popup
					ArrayList<Waypoint> remainingPoints = points;
					Vector<String> remainingNames = new Vector<String>();
					JList<String> names;
					int indexA = remainingPoints.indexOf(ptA);
					remainingPoints.remove(indexA);
					int indexB = remainingPoints.indexOf(ptB);
					remainingPoints.remove(indexB);
					if (remainingPoints.size() >= (REQUIRED_NUM - 2)) {
						for (Waypoint wp : remainingPoints)
							remainingNames.add(wp.getName());
						names = new JList<String>();
						JScrollPane sp = new JScrollPane(names);
						names.setListData(remainingNames);
						names.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

						//Set so multiple items are selected without the need to hold other keys.
						//Taken from https://forums.oracle.com/thread/1360864
						names.setSelectionModel(new DefaultListSelectionModel() {
							private static final long serialVersionUID = 1L;

							public void setSelectionInterval(int index0,
									int index1) {
								if (isSelectedIndex(index0))
									super.removeSelectionInterval(index0,
											index1);
								else
									super.addSelectionInterval(index0, index1);
							}
						});

						Object[] msg = { "What other points are in the route?",
								sp };
						int op = JOptionPane.showConfirmDialog(null, msg,
								"Select Other Points",
								JOptionPane.OK_CANCEL_OPTION);

						//If cancelled
						if (op == JOptionPane.CANCEL_OPTION) {
							JOptionPane
							.showMessageDialog(
									null,
									"The operation to calculate the route has been cancelled.",
									"Operation Cancelled",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						int selectedNum = names.getSelectedIndices().length;

						if (selectedNum < (REQUIRED_NUM - 2)|| (selectedNum + 2) > REQUIRED_MAX) {
							JOptionPane
							.showMessageDialog(
									null,
									"There must be between "+(REQUIRED_NUM -2)+" and "+(REQUIRED_MAX-2)+
									"waypoints choosen , besides the start and end, must be selected.",
									"More Selections Required",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					//Error for points < 13
					else {
						JOptionPane
						.showMessageDialog(
								null,
								"There must be a route containing at least "
								+ REQUIRED_NUM
								+ " points.\nAs an administrator, add more locations.",
								"More Locations Needed",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					selectedPoints = new ArrayList<Waypoint>();
					selectedPoints.add(ptA);
					for (String wpName : names.getSelectedValuesList()) {
						for (Waypoint wp : points) {
							if (wp.getName().equals(wpName)) {
								selectedPoints.add(wp);
							}
						}
					}
					selectedPoints.add(ptB);
				}

				//Do calculations
				ArrayList<Waypoint> routeWaypoints = DistCalcDriver.
				shortDistAlgorithm(selectedPoints);
				GoogleEarthPath path = new GoogleEarthPath(routeWaypoints);
				GoogleEarthManager gem = new GoogleEarthManager();
				String result = gem.Path2KML(path);
				JOptionPane.showMessageDialog(null, result, "Route", 
						JOptionPane.INFORMATION_MESSAGE);
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

		}

	}

	/**
	 * Action listener for "Calculate Distance" button
	 */
	private class DistListener implements ActionListener
	{

		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for starting and ending waypoint data and finds the distance between them.
		 * @param ae An ActionEvent is created when the user clicks the "Calculate Distance" button
		 */
		public void actionPerformed(ActionEvent e) 
		{

			//Get list of points from database
			LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
			try 
			{
				waypoints = dbm.getWaypoints();
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", 
						"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}

			//Validation for fewer than two available locations
			if (waypoints.size() < 2)
			{
				JOptionPane.showMessageDialog(null, 
						"There must be two locations available to find the shortest route",
						"Need More Locations", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Prompt for starting point
			String[] firstPts = new String[waypoints.size()];

			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				firstPts[i] = waypoints.get(i).getName();
			}
			String startPoint = (String) JOptionPane.showInputDialog(null, 
					"Select the starting location:","Select Starting Point", 
					JOptionPane.PLAIN_MESSAGE, null, firstPts, firstPts[0]);

			//If cancelled
			if (startPoint == null)
			{
				JOptionPane.showMessageDialog(null,
						"The operation to calculate route has been cancelled.",
						"Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			//Prompt for ending point
			ArrayList<String> secondList = new ArrayList<String>(); 
			String[] secondPts = new String[waypoints.size() - 1];

			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				if (!waypoints.get(i).getName().equals(startPoint))
					secondList.add(waypoints.get(i).getName());
			}

			for (int i = 0; i < secondList.size(); i++)
			{
				secondPts[i] = secondList.get(i);
			}

			String endPoint = (String) JOptionPane.showInputDialog(null,
					"Select the ending location:","Select Ending Point", 
					JOptionPane.PLAIN_MESSAGE, null, secondPts, secondPts[0]);

			//If cancelled when selecting second point
			if (endPoint == null)
			{
				JOptionPane.showMessageDialog(null,
						"The operation to calculate the shortest route distance has been cancelled.",
						"Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			//Use waypoint names to calculate route
			Waypoint ptA = null;
			Waypoint ptB = null;

			ArrayList<Waypoint> points = new ArrayList<Waypoint>();
			LinkedList<Waypoint> pts = new LinkedList<Waypoint>();
			ArrayList<Waypoint> selectedPoints = new ArrayList<Waypoint>();
			try
			{
				//Find corresponding Waypoints with names
				pts = dbm.getWaypoints();
				for (int i = 0; i < pts.size(); i++)
				{
					points.add(pts.get(i));
					if (pts.get(i).getName().equals(startPoint))
						ptA = pts.get(i);
					if (pts.get(i).getName().equals(endPoint))
						ptB = pts.get(i);
				}

				if (ptA == null || ptB == null)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Operation.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (REQUIRED_NUM >= 2) 
				{
					//Remove the starting and end points for other popup
					ArrayList<Waypoint> remainingPoints = points;
					Vector<String> remainingNames = new Vector<String>();
					JList<String> names;
					int indexA = remainingPoints.indexOf(ptA);
					remainingPoints.remove(indexA);
					int indexB = remainingPoints.indexOf(ptB);
					remainingPoints.remove(indexB);
					if (remainingPoints.size() >= (REQUIRED_NUM - 2)) {
						for (Waypoint wp : remainingPoints)
							remainingNames.add(wp.getName());
						names = new JList<String>();
						JScrollPane sp = new JScrollPane(names);
						names.setListData(remainingNames);
						names.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

						//Set so multiple items are selected without the need to hold other keys.
						//Taken from https://forums.oracle.com/thread/1360864
						names.setSelectionModel(new DefaultListSelectionModel() {
							private static final long serialVersionUID = 1L;

							public void setSelectionInterval(int index0,
									int index1) {
								if (isSelectedIndex(index0))
									super.removeSelectionInterval(index0,
											index1);
								else
									super.addSelectionInterval(index0, index1);
							}
						});

						Object[] msg = { "What other points are in the route?",
								sp };
						int op = JOptionPane.showConfirmDialog(null, msg,
								"Select Other Points",
								JOptionPane.OK_CANCEL_OPTION);

						//If cancelled
						if (op == JOptionPane.CANCEL_OPTION) {
							JOptionPane
							.showMessageDialog(
									null,
									"The operation to calculate the shortest route distance has been cancelled.",
									"Operation Cancelled",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						int selectedNum = names.getSelectedIndices().length;

						if (selectedNum < (REQUIRED_NUM - 2) || (selectedNum + 2) > REQUIRED_MAX) {
							JOptionPane
							.showMessageDialog(
									null,
									"There must be between "+(REQUIRED_NUM -2)+" and "+(REQUIRED_MAX-2)+
									"waypoints choosen , besides the start and end, must be selected.",
									"More Selections Required",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					//Error for points < 13
					else {
						JOptionPane
						.showMessageDialog(
								null,
								"There must be a route containing at least "
								+ REQUIRED_NUM
								+ " points.\nAs an administrator, add more locations.",
								"More Locations Needed",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					selectedPoints.add(ptA);
					for (String wpName : names.getSelectedValuesList()) {
						for (Waypoint wp : points) {
							if (wp.getName().equals(wpName)) {
								selectedPoints.add(wp);
							}
						}
					}
					selectedPoints.add(ptB);
					
				}

			}


			catch (SQLException e1)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", 
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}

			dialog = new JDialog();
			progress = new JPanel();
			progress.add(bar);
			dialog.setContentPane(progress);
			dialog.pack();
			dialog.setVisible(true);
			dialog.setTitle("Please Wait. Performing Calculations.");
			DoCalc doCalc = new DoCalc(selectedPoints);
			doCalc.execute();

		}

	}



	/**
	 * Action listener for "Log Out" button
	 */
	private class LogoutListener implements ActionListener
	{
		/**
		 * Logs the user out of the system
		 * @param e the ActionEvent created when the user selects the "Log Out" button.
		 */
		public void actionPerformed(ActionEvent e)
		{
			JOptionPane.showMessageDialog(null,
					"Logging out. Run the program again to log in again.", "Exiting",
					JOptionPane.INFORMATION_MESSAGE);
			dbm.logout();
			System.exit(0);
		}
	}
	/**
	 * Swingworker for calculating the route
	 */
	private class DoCalc extends SwingWorker<Double, Void>
	{

		private ArrayList<Waypoint> selectedPoints;
		private double dist;

		public DoCalc(ArrayList<Waypoint> wp)
		{
			selectedPoints = wp;
		}

		/**
		 * Perform calculation for the distance in the background
		 */
		protected Double doInBackground() throws Exception {

			ArrayList<Waypoint> rt = DistCalcDriver.shortDistAlgorithm(selectedPoints);
			dist = DistCalcDriver.totalDistance(rt);
			return dist;
		}

		/**
		 * Displays message after completed calculations
		 */
		public void done ()
		{
			dialog.dispose();
			String startPoint = selectedPoints.get(0).getName();
			String endPoint = selectedPoints.get(selectedPoints.size() -1).getName();

			String distMsg = String.format(
					"The shortest distance between all points, starting from \"" + startPoint +
					"\" and ending at\n \"" + endPoint + "\", is %.2f miles.", dist);

			JOptionPane.showMessageDialog(null, distMsg, "Total Distance",
					JOptionPane.INFORMATION_MESSAGE);

		}
	}
	/**
	 * Swingworker for calculating the route
	 */
	@SuppressWarnings("unused")
	private class DoRoute extends SwingWorker<String, Void>
	{

		private ArrayList<Waypoint> selectedPoints;
		private String result;

		public DoRoute(ArrayList<Waypoint> wp)
		{
			selectedPoints = wp;
		}

		/**
		 * Perform calculation for the distance in the background
		 */
		protected String doInBackground() throws Exception {

			ArrayList<Waypoint> routeWaypoints = DistCalcDriver.
			shortDistAlgorithm(selectedPoints);
			GoogleEarthPath path = new GoogleEarthPath(routeWaypoints);
			GoogleEarthManager gem = new GoogleEarthManager();
			result = gem.Path2KML(path);
			return result;
		}

		/**
		 * Displays message after completed calculations
		 */
		public void done ()
		{
			dialog.dispose();
			JOptionPane.showMessageDialog(null, result, "Route", 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
