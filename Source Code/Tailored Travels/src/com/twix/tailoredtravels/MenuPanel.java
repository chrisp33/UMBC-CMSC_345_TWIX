/**
 * Panel for main menu
 * @author Christopher Pagan
 * @version 1.0
 */

package com.twix.tailoredtravels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
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
	private ArrayList<String> waypointNames,userWaypoints;
	private Vector<String> locs;
	
	public MenuPanel(DatabaseManager dbm,String user, boolean admin){
		
		this.dbm = dbm;
		isAdmin = admin;
		currentUser = user;
		
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		
		//Create these components if the user is an administrator
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
		
		//Instantiate GUI components
		welcomeMsg = new JLabel("Welcome back, " + user + "!");
		p1.add(welcomeMsg);
		availMsg = new JLabel("Available Locations");
		locations = new JList<String>();
		locations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scroller = new JScrollPane(locations);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
		
		//List of waypoint names
		userWaypoints = new ArrayList<String>();
		waypointNames = new ArrayList<String>();
		waypointNames.add("Acadia");
		waypointNames.add("American Samoa");
		waypointNames.add("Arches");
		waypointNames.add("Badlands");
		waypointNames.add("Big Bend");
		waypointNames.add("Biscayne");
		waypointNames.add("Black Canyon of Guinnson");
		waypointNames.add("Bryce Canyon");
		waypointNames.add("Canyonlands");
		waypointNames.add("Capitol Reef");
		waypointNames.add("Carlsbad Caverns");
		waypointNames.add("Channel Islands");
		waypointNames.add("Congaree");
		waypointNames.add("Crater Lake");
		waypointNames.add("Cuyahoga Valley");
		waypointNames.add("Death Valley");
		waypointNames.add("Denali");
		waypointNames.add("Dry Tortugas");
		waypointNames.add("Everglades");
		waypointNames.add("Gates of the Arctic");
		waypointNames.add("Glacier");
		waypointNames.add("Glacier Bay");
		waypointNames.add("Grand Canyon");
		waypointNames.add("Grand Teton");
		waypointNames.add("Great Basin");
		waypointNames.add("Great Sand Dunes");
		waypointNames.add("Great Smoky Mountains");
		waypointNames.add("Guadalupe Mountains");
		waypointNames.add("Haleakala");
		waypointNames.add("Hawaii Volcanoes");
		waypointNames.add("Hot Springs");
		waypointNames.add("Isle Royale");
		waypointNames.add("Joshua Tree");
		waypointNames.add("Katmai");
		waypointNames.add("Kenai Fjords");
		waypointNames.add("Kings Canyon");
		waypointNames.add("Kobuk Valley");
		waypointNames.add("Lake Clark");
		waypointNames.add("Lassen Vocanic");
		waypointNames.add("Machu Pichu");
		waypointNames.add("Mammoth Cave");
		waypointNames.add("Mesa Verde");
		waypointNames.add("Mount Rainier");
		waypointNames.add("North Cascades");
		waypointNames.add("Olympic");
		waypointNames.add("Petrified Forest");
		waypointNames.add("Pinnacles");
		waypointNames.add("Redwood");
		waypointNames.add("Rocky Mountain");
		waypointNames.add("saguaro");
		waypointNames.add("sequoia");
		waypointNames.add("shenandoah");
		waypointNames.add("Theodore Roosevelt");
		waypointNames.add("Virgin Islands");
		waypointNames.add("Voyageurs");
		waypointNames.add("Wind Cave");
		waypointNames.add("Wrangell -St. Elias");
		waypointNames.add("YellowStone");
		waypointNames.add("Yosemite");
		waypointNames.add("Zion");

	}
	
	/**
	 * Finds the list of locations for the user and uses them to fill the JList
	 * @param locations2 list of Locations containing waypoint information.
	 */
	public void populateJList(LinkedList<Waypoint> locations2)
	{
		locs = new Vector<String>();
		for (Waypoint loc : locations2)
		{	
			locs.add(loc.getName());
			userWaypoints.add(loc.getName());
		}
		
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
	
	private class AddLocListener implements ActionListener
	{
			
			/**
			 * Overwritten method for ActionListener class. 
			 * Prompts for waypoint data and adds it to the system.
			 * @param ae An ActionEvent is created when the user clicks the "Add Location" button
			 */
			public void actionPerformed(ActionEvent ae) 
			{
				
				//Find the locations that have not been added yet
				LinkedList<String> waypointsNotAdded = new LinkedList<String>();
				for (int i = 0 ; i < waypointNames.size(); i++)
				{
					if (!userWaypoints.contains(waypointNames.get(i)))
						waypointsNotAdded.add(waypointNames.get(i));
				}
				
				//Error message if there are no more locations to add
				if (waypointsNotAdded.size() == 0)
				{
					JOptionPane.showMessageDialog(null, "There are no more locations available to add.", "No More Locations", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//Construct array of locations that haven't been added specifically for the dialog box
				String[] newWaypoints = new String[waypointsNotAdded.size()];
				for (int i = 0; i < waypointsNotAdded.size(); i++)
					newWaypoints[i] = waypointsNotAdded.get(i);
				
				//Prompt for selection
				String addLocation = (String) JOptionPane.showInputDialog(null, "Select a location to add:",
						"Add Location", JOptionPane.OK_CANCEL_OPTION, null, newWaypoints, newWaypoints[0]);
				
				try
				{
					//Add location to the user's list of locations
					dbm.addUserLocation(addLocation);
					JOptionPane.showMessageDialog(null, "The location \"" + addLocation + "\" has been successfully added.", "Location Added", JOptionPane.INFORMATION_MESSAGE);
					userWaypoints.add(addLocation);
					locs.add(addLocation);
					locations.setListData(locs);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					System.exit(0);
				}
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
			//Initialize array of removable waypoint names
			String[] waypointNames = new String[userWaypoints.size()];
			
			//Error message for empty list
			if (waypointNames.length == 0)
			{
				JOptionPane.showMessageDialog(null, "Unable to remove any locations. Must first have locations available");
				return;
			}
			
			//Create array for use in JOptionPane
			for (int i = 0; i < userWaypoints.size(); i++)
			{
				waypointNames[i] = userWaypoints.get(i);
			}
			
			String remLoc = (String) JOptionPane.showInputDialog(null, "Select a location to remove:",
					"Remove Location", JOptionPane.QUESTION_MESSAGE, null, waypointNames, waypointNames[0]);
			
			//Notification if user clicks cancel button
			if (remLoc == null)
			{
				JOptionPane.showMessageDialog(null, "No locations will be removed from the list.", "Remove Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			//User confirmation of remove
			int sel = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove \"" + remLoc + "\"?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
			
			//Message when user clicks "no" button
			if (sel == JOptionPane.NO_OPTION)
			{
				JOptionPane.showMessageDialog(null, "\"" + remLoc + "\" will not be removed from the list.", "Remove Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		
			try
			{
				//Remove waypoint
				dbm.removeLocation(remLoc);
				userWaypoints.remove(remLoc);
				locs.remove(remLoc);
				JOptionPane.showMessageDialog(null, "\"" + remLoc + "\" has been removed from the list successfully.", "Location Removed", JOptionPane.INFORMATION_MESSAGE);
				locations.setListData(locs);
			}
			catch (Exception e)
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

			int sel = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove user \"" + name + "\"?", "Confirm Remove User", JOptionPane.YES_NO_OPTION);
			
			// Cancel operation if No option is clicked
			if (sel == JOptionPane.NO_OPTION)
			{
				JOptionPane.showMessageDialog(null, "No users have been removed.", "User Not Removed", JOptionPane.PLAIN_MESSAGE);
				return;
			}
			
			try 
			{
				dbm.removeUser(name);
				JOptionPane.showMessageDialog(null, "The user \"" + name + "\" has been successfully removed.");
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
			
			//Validation for fewer than two available locations
			if (userWaypoints.size() < 2)
			{
				JOptionPane.showMessageDialog(null, "There must be two locations available to find the shortest route", "Need More Locations", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//Prompt for starting point
			String[] firstPts = new String[userWaypoints.size()];
			
			//Create array for use in JOptionPane
			for (int i = 0; i < userWaypoints.size(); i++)
			{
				firstPts[i] = userWaypoints.get(i);
			}
			String startPoint = (String) JOptionPane.showInputDialog(null, "Select the starting location:","Select Starting Point", JOptionPane.PLAIN_MESSAGE, null, firstPts, firstPts[0]);
			
			if (startPoint == null)
			{
				JOptionPane.showMessageDialog(null, "The operation to calculate route has been cancelled.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			//Prompt for ending point
			ArrayList<String> secondList = new ArrayList<String>(); 
			String[] secondPts = new String[userWaypoints.size() - 1];
			
			//Create array for use in JOptionPane
			for (int i = 0; i < userWaypoints.size(); i++)
			{
				if (!userWaypoints.get(i).equals(startPoint))
					secondList.add(userWaypoints.get(i));
			}
			
			for (int i = 0; i < secondList.size(); i++)
			{
				secondPts[i] = secondList.get(i);
			}
			
			String endPoint = (String) JOptionPane.showInputDialog(null, "Select the ending location:","Select Ending Point", JOptionPane.PLAIN_MESSAGE, null, secondPts, secondPts[0]);

			if (endPoint == null)
			{
				JOptionPane.showMessageDialog(null, "The operation to calculate route has been cancelled.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			//Use waypoint names to calculate route
			Waypoint ptA = null;
			Waypoint ptB = null;
			
			ArrayList<Waypoint> points = new ArrayList<Waypoint>();
			LinkedList<Waypoint> pts;
			try
			{
				pts = dbm.getUserLocations();
				for (Waypoint wp : pts)
				{
					points.add(wp);
					if (wp.getName().equals(startPoint))
						ptA = wp;
					if (wp.getName().equals(endPoint))
						ptB = wp;
				}
				
				if (ptA == null || ptB == null)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Operation.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				ArrayList<Waypoint> routeWaypoints = DistCalcDriver.shortDistAlgorithm(points, ptA, ptB);
				GoogleEarthPath path = new GoogleEarthPath(routeWaypoints);
				GoogleEarthManager gem = new GoogleEarthManager();
				gem.Path2KML(path);
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

		}
		
	}
	
	private class DistListener implements ActionListener{

		/**
		 * Overwritten method for ActionListener class. 
		 * Prompts for starting and ending waypoint data and finds the distance between them.
		 * @param ae An ActionEvent is created when the user clicks the "Calculate Distance" button
		 */
		public void actionPerformed(ActionEvent e) {
			
			//Validation for less than two points
			if (userWaypoints.size() < 2)
			{
				JOptionPane.showMessageDialog(null, "There must be two locations available to calculate the distance between them", "Need More Locations", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//Prompt for first point
			String[] firstPts = new String[userWaypoints.size()];
			
			//Create array for use in JOptionPane
			for (int i = 0; i < userWaypoints.size(); i++)
			{
				firstPts[i] = userWaypoints.get(i);
			}
			String startPoint = (String) JOptionPane.showInputDialog(null, "Select the starting location:","Select Starting Point", JOptionPane.PLAIN_MESSAGE, null, firstPts, firstPts[0]);
			
			if (startPoint == null)
			{
				JOptionPane.showMessageDialog(null, "The operation to calculate distance has been cancelled.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			//Prompt for second point
			ArrayList<String> secondList = new ArrayList<String>(); 
			String[] secondPts = new String[userWaypoints.size() - 1];
			
			//Create array for use in JOptionPane
			for (int i = 0; i < userWaypoints.size(); i++)
			{
				if (!userWaypoints.get(i).equals(startPoint))
					secondList.add(userWaypoints.get(i));
			}
			
			for (int i = 0; i < secondList.size(); i++)
			{
				secondPts[i] = secondList.get(i);
			}
			
			String endPoint = (String) JOptionPane.showInputDialog(null, "Select the ending location:","Select Ending Point", JOptionPane.PLAIN_MESSAGE, null, secondPts, secondPts[0]);

			if (endPoint == null)
			{
				JOptionPane.showMessageDialog(null, "The operation to calculate distance has been cancelled.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			//Use waypoint names to calculate route
			Waypoint ptA = null;
			Waypoint ptB = null;
			
			ArrayList<Waypoint> points = new ArrayList<Waypoint>();
			LinkedList<Waypoint> pts;
			try
			{
				pts = dbm.getUserLocations();
				for (Waypoint wp : pts)
				{
					points.add(wp);
					if (wp.getName().equals(startPoint))
						ptA = wp;
					if (wp.getName().equals(endPoint))
						ptB = wp;
				}
			}
				catch (SQLException e1)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}

				if (ptA == null || ptB == null)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Operation.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			
			//Calculate the distance
			ArrayList<Waypoint> rt = DistCalcDriver.shortDistAlgorithm(points, ptA, ptB);
			System.out.println(DistCalcDriver.totalDistance(rt));
			
			//KML Methods here----------------------------------------------------------------
		}
		
	}
}
