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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.apache.derby.impl.sql.catalog.SYSPERMSRowFactory;

public class MenuPanel extends JPanel {

	private JButton calcRoute, calcDist, addLocation, removeLocation, addUser, removeUser;
	private JLabel welcomeMsg,availMsg;
	private JTextArea textArea;
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
		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		calcRoute = new JButton("Calculate Route");
		calcDist = new JButton("Calculate Distance");
		
		calcRoute.addActionListener(new RouteListener());
		calcDist.addActionListener(new DistListener());
		
		p4 = new JPanel();
		p4.add(availMsg);
		p4.add(scroller);
		p5 = new JPanel();
		p5.add(calcRoute);
		p5.add(calcDist);
	
		try 
		{
			//Fill textArea with the waypoint names for user to see
			LinkedList<Waypoint> waypoints = dbm.getLocation();
			String locations = "";
			for (Waypoint wp: waypoints)
			{
				locations += wp.getName() + "\n";
			}
			locations = locations.trim();
			textArea.setText(locations);
			textArea.setCaretPosition(0);
		} 
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Database Error. Exiting Program.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
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
				JTextField name = new JTextField();
				JTextField latitude = new JTextField();
				JTextField longitude = new JTextField();
				JTextField details = new JTextField();
				
				//Prompt to enter details
				Object[] message = {"Enter the name of the location:", name,
						"Enter the location's latitude position:", latitude, 
						"Enter the location's longitude position:", longitude,
						"Enter some detail about the location:", details};

				int sel = JOptionPane.showConfirmDialog(null, message,
						"Add Location", JOptionPane.OK_CANCEL_OPTION);
				String wName = name.getText();
				String lat = latitude.getText();
				String lon = longitude.getText();
				String det = details.getText();
				
				if (sel == JOptionPane.CANCEL_OPTION)
				{
					return;
				}
				
				if (wName.equals("") || lat.equals("") || lon.equals("") || det.equals(""))
				{
					JOptionPane.showMessageDialog(null, "Cannot leave any fields blank.",
							"Location Not Added", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
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
				
				if (wLat < -90 || wLat > 90 || wLong < -180 || wLong > 180)
				{
					String msg = "Latitude can only range from -90 degrees to +90 degrees.\n" +
							"Longitude can only range from -180 degrees to +180 degrees.";
					JOptionPane.showMessageDialog(null, msg,
							"Invalid Values", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try
				{
					dbm.addLocation(wLat, wLong, wName, det);
					JOptionPane.showMessageDialog(null, "The location \"" + wName + "\" has been added.", 
							"Location Added", JOptionPane.INFORMATION_MESSAGE);

					//Update textArea with the new waypoint names
					LinkedList<Waypoint> waypoints = dbm.getLocation();
					String locations = "";
					for (Waypoint wp: waypoints)
					{
						locations += wp.getName() + "\n";
					}
					locations = locations.trim();
					textArea.setText(locations);
					textArea.setCaretPosition(0);
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
			LinkedList<Waypoint> waypoints = null;
			try 
			{
				waypoints = dbm.getLocation();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
			
			//Initialize array of removable waypoint names
			String[] waypointNames = new String[waypoints.size()];
			
			//Error message for empty list
			if (waypointNames.length == 0)
			{
				JOptionPane.showMessageDialog(null, "Unable to remove any locations. Must first have locations available");
				return;
			}
			
			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				waypointNames[i] = waypoints.get(i).getName();
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
				JOptionPane.showMessageDialog(null, "\"" + remLoc + "\" has been removed from the list successfully.", "Location Removed", JOptionPane.INFORMATION_MESSAGE);
				
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
			
			try 
			{
				waypoints = dbm.getLocation();
				//Update textArea
				String locations = "";
				for (Waypoint wp: waypoints)
				{
					locations += wp.getName() + "\n";
				}
				locations = locations.trim();
				textArea.setText(locations);
				textArea.setCaretPosition(0);
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
				return;
				//JOptionPane.showMessageDialog(null, "The New User Has Not Been Added.", "User Not Added", JOptionPane.ERROR_MESSAGE);
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
			
			//Get list of points from database
			LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
			try 
			{
				waypoints = dbm.getLocation();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
			
			//Validation for fewer than two available locations
			if (waypoints.size() < 2)
			{
				JOptionPane.showMessageDialog(null, "There must be two locations available to find the shortest route", "Need More Locations", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//Prompt for starting point
			String[] firstPts = new String[waypoints.size()];
			
			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				firstPts[i] = waypoints.get(i).getName();
			}
			String startPoint = (String) JOptionPane.showInputDialog(null, "Select the starting location:","Select Starting Point", JOptionPane.PLAIN_MESSAGE, null, firstPts, firstPts[0]);
			
			if (startPoint == null)
			{
				JOptionPane.showMessageDialog(null, "The operation to calculate route has been cancelled.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
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
			LinkedList<Waypoint> pts = new LinkedList<Waypoint>();
			try
			{
				pts = dbm.getLocation();
				for (int i = 0; i < pts.size() - 1; i++)
				{
					points.add(pts.get(i));
					if (pts.get(i).getName().equals(startPoint))
						ptA = pts.get(i);
					if (pts.get(i).getName().equals(endPoint))
						ptB = pts.get(i);
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
			
			//Get list of points from database
			LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
			try 
			{
				waypoints = dbm.getLocation();
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}
			
			//Validation for fewer than two available locations
			if (waypoints.size() < 2)
			{
				JOptionPane.showMessageDialog(null, "There must be two locations available to find the shortest route", "Need More Locations", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//Prompt for starting point
			String[] firstPts = new String[waypoints.size()];
			
			//Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++)
			{
				firstPts[i] = waypoints.get(i).getName();
			}
			String startPoint = (String) JOptionPane.showInputDialog(null, "Select the starting location:","Select Starting Point", JOptionPane.PLAIN_MESSAGE, null, firstPts, firstPts[0]);
			
			if (startPoint == null)
			{
				JOptionPane.showMessageDialog(null, "The operation to calculate route has been cancelled.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
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
			LinkedList<Waypoint> pts = new LinkedList<Waypoint>();
			try
			{
				pts = dbm.getLocation();
				for (int i = 0; i < pts.size() - 1; i++)
				{
					points.add(pts.get(i));
					if (pts.get(i).getName().equals(startPoint))
						ptA = pts.get(i);
					if (pts.get(i).getName().equals(endPoint))
						ptB = pts.get(i);
				}
				
				if (ptA == null || ptB == null)
				{
					JOptionPane.showMessageDialog(null, "Database Error. Exiting Operation.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(null, "Database Error. Exiting Program", "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}
			
			//Calculate the distance
			ArrayList<Waypoint> rt = DistCalcDriver.shortDistAlgorithm(points, ptA, ptB);
			double dist = DistCalcDriver.totalDistance(rt);
			
			String distMsg = String.format("The total distance between all points, starting from \"" + startPoint +
					"\" and ending at\n \"" + endPoint + "\", is %.2f miles.", dist);
			
			JOptionPane.showMessageDialog(null, distMsg, "Total Distance", JOptionPane.INFORMATION_MESSAGE);
			
			//KML Methods here----------------------------------------------------------------
		}
		
	}
}
