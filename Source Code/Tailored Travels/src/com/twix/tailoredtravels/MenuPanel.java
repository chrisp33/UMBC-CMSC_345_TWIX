/**
 * Panel for main menu
 * @author Christopher Pagan
 * @version 1.0
 */

package com.twix.tailoredtravels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MenuPanel extends JPanel {

	private JButton calcRoute, calcDist, addLocation, removeLocation, addUser, removeUser;
	private JLabel welcomeMsg,availMsg;
	private JList<String> locations;
	private JScrollPane scroller;
	private boolean isAdmin;
	private JPanel p1, p2, p3, p4, p5;
	private DatabaseManager dbm;
	private String currentUser;
	
	public MenuPanel(DatabaseManager dbm,String user, boolean admin){
		
		this.dbm = dbm;
		isAdmin = admin;
		currentUser = user;
		
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		
		if (isAdmin)
		{
			addLocation = new JButton("Add Location");
			removeLocation = new JButton("Remove Location");			
			addUser = new JButton("Add User");
			removeUser = new JButton("Remove User");
			
			p2.add(addLocation);
			p2.add(removeLocation);
			p3.add(addUser);
			p3.add(removeUser);
			
			addLocation.addActionListener(new AddLocListener());
			removeLocation.addActionListener(new RemLocListener());
			addUser.addActionListener(new AddUserListener());
			removeUser.addActionListener(new RemUserListener());
			
		}
		
		welcomeMsg = new JLabel("Welcome back, " + user + "!");
		p1.add(welcomeMsg);
		availMsg = new JLabel("Available Locations");
		locations = new JList<String>();
		locations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scroller = new JScrollPane(locations);
		calcRoute = new JButton("Calculate Route");
		calcDist = new JButton("Calculate Distance");
		
		calcRoute.addActionListener(new RouteListener());
		calcDist.addActionListener(new DistListener());
		
		p4 = new JPanel();
		p4.add(availMsg);
		p4.add(locations);
		p4.add(scroller);
		p5 = new JPanel();
		p5.add(calcRoute);
		p5.add(calcDist);
		
	}
	
	/**
	 * Finds the list of locations for the user and uses them to fill the JList
	 * @param locations2 list of Locations containing waypoint information.
	 */
	public void populateJList(LinkedList<Waypoint> locations2)
	{
		Vector<String> locs = new Vector<String>();
		for (Waypoint loc : locations2)
			locs.add(loc.getName());
		this.locations.setListData(locs);
	}
	
	/**
	 * Add appropriate components to the main menu.
	 */
	public void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(p1);
		
		if(isAdmin)
		{
			add(p2);
			add(p3);
		}
		
		add(p4);
		add(p5);
	}
	
	private class AddLocListener implements ActionListener{
			
			/**
			 * Overwritten method for ActionListener class. 
			 * Prompts for waypoint data and adds it to the system.
			 * @param ae An ActionEvent is created when the user clicks the "Add Location" button
			 */
			public void actionPerformed(ActionEvent ae) {
				
				//dbm.addUserLocation(addLocation)
			}
	}
	
	private class RemLocListener implements ActionListener
	{
		
		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for waypoint data and removes it from the system.
		 * @param ae An ActionEvent is created when the user clicks the "Remove Location" button
		 */
		public void actionPerformed(ActionEvent ae) 
		{
			try
			{
				LinkedList<Waypoint> waypoints = dbm.getUserLocations();
				String[] waypointNames = new String[waypoints.size()];
				for (int i = 0; i < waypoints.size(); i++)
				{
					waypointNames[i] = waypoints.get(i).getName();
				}
				String remLoc = (String) JOptionPane.showInputDialog(null, "Which location would you like to remove?",
						"Remove Location", JOptionPane.PLAIN_MESSAGE, null, waypointNames, waypointNames[0]);
				System.out.println(remLoc);
				dbm.removeLocation(remLoc);
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	private class AddUserListener implements ActionListener{
		
		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for user data and adds it to the system.
		 * @param ae An ActionEvent is created when the user clicks the "Add User" button
		 */
		public void actionPerformed(ActionEvent ae) {
			
			//Prompt for username and password
			JTextField nameField = new JTextField();
			JPasswordField passField =  new JPasswordField();
			Object[] message = {"Enter the New Username", nameField, "Enter the New Password", passField};
			int option = JOptionPane.showConfirmDialog(null, message, "Enter Details for New User", JOptionPane.OK_CANCEL_OPTION);
			String newName = nameField.getText();
			char[] passChars = passField.getPassword();
			
			//Check for valid data
			if ((option == JOptionPane.OK_OPTION) && (!newName.equals("")) && (passChars.length > 0))
			{
				boolean admin = false;
				
				//Prompt for new user privlege level
				Object[] opts = {"Administrator", "Normal User"};
				String selection = (String) JOptionPane.showInputDialog(null, "Select User Privilege Level", "User Privilege", JOptionPane.QUESTION_MESSAGE, null, opts, "Administrator");
				
				if (selection == null)
				{
					//Inform user that the new user has not been added
					JOptionPane.showMessageDialog(null, "The New User Has Not Been Added.", "User Not Added", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String addMsg = "A new user \"" + newName + "\" has been added as ";

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
					dbm.addUser(newName, password, admin);
					JOptionPane.showMessageDialog(null, addMsg, "Added User", JOptionPane.INFORMATION_MESSAGE);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					System.exit(0);
				}

			}
			else
			{
				//User is not added if "cancel" is pressed
				JOptionPane.showMessageDialog(null, "The New User Has Not Been Added.", "User Not Added", JOptionPane.ERROR_MESSAGE);
			}
		
		}
	}
	
	private class RemUserListener implements ActionListener{
		
		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for user data and removes it from the system.
		 * @param ae An ActionEvent is created when the user clicks the "Remove User" button
		 */
		public void actionPerformed(ActionEvent ae) {
			
			//Prompt for user name
			String name = JOptionPane.showInputDialog(null, "Enter the name of the user.", "Remove User", JOptionPane.QUESTION_MESSAGE);
			if (name == null)
			{
				JOptionPane.showMessageDialog(null, "No users have been removed from the database.", "User Not Removed", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Validation if removing the current user
			if (currentUser.equals(name))
			{
				JOptionPane.showMessageDialog(null, "Cannot remove current users. Log in as another administrator first.", "Cannot Remove User", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int sel = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove user ,\"" + name + "\"?", "Confirm Remove User", JOptionPane.YES_NO_OPTION);
			
			// Cancel operation if No option is clicked
			if (sel == JOptionPane.NO_OPTION)
			{
				JOptionPane.showMessageDialog(null, "No users have been removed.", "User Not Removed", JOptionPane.PLAIN_MESSAGE);
				return;
			}
			
			try 
			{
				dbm.removeUser(name);
				JOptionPane.showMessageDialog(null, "The user \"" + name + "\" has been removed.");
			} 
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	private class RouteListener implements ActionListener{

		
		/**
		 * Overwritten method for ActionListener class. 
		 * Calculates the route given the waypoints the user has selected.
		 * @param ae An ActionEvent is created when the user clicks the "Calculate Route" button
		 */
		public void actionPerformed(ActionEvent arg0) {
			
		}
		
	}
	
	public class DistListener implements ActionListener{

		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for starting and ending waypoint data and finds the distance between them.
		 * @param ae An ActionEvent is created when the user clicks the "Calculate Distance" button
		 */
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
}
