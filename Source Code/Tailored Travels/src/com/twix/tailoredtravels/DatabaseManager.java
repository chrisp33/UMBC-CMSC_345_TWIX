package com.twix.tailoredtravels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
//there must be the derby embeddeddriver loaded in plugins
//the database must exist
public class DatabaseManager {
	private int userId;
	private LinkedList<Waypoint> location;
	private LinkedList<Waypoint> locationChoice;
	private boolean admin;
	private final String newDatabase = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String url = "jdbc:derby:Database;create = true";
	private final String queryLocation = "select * from Location";
	private final String queryUser = "select * from UserPassword";
	//	private Connection connect = null;

	/*
	 * Constructor that starts this file reader
	 * sets currentUser to 0 to mean no user at this time
	 * set login to false
	 * reads the location file and user file to have data
	 * precondition: org.apache.derby.jdbc.EmbeddedDriver must exist in eclipse plugin
	 * input: none
	 * output: none
	 */
	public DatabaseManager() throws ClassNotFoundException, SQLException
	{
		Class.forName(newDatabase);
		location = new LinkedList<Waypoint>();
		locationChoice = new LinkedList<Waypoint>();
		userId = 0;
		admin = false;
	}
	/*
	 * allows the user to log in and sets the user
	 * precondition: name / password are not null
	 * input:	String: name of the person logging in
	 * 			String: password of the person logging in
	 * output:	true if the person is logged in
	 * 			false if there are no one with that username / password
	 */
	public boolean login(String name, String password) throws SQLException
	{
		if(name == null || password == null)
			return false;
		Connection connect = null;
		Statement statement = null;
		boolean login = false;
		try{
			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet result = statement.executeQuery(queryUser);
			//while there is another item
			while(result.next())
			{
				//check if the user name and password exist with name being case-insensitive
				//and password being case sensitive
				if(result.getString(2).equalsIgnoreCase(name) && result.getString(3).equals(password))
				{
					//if the user exist then get their id
					userId = result.getInt(1);
					//check if the person is a admin
					if(result.getBoolean(4))
						admin = true;
					login = true;
				}
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(statement != null)
				connect.close();
		}
		return login;
	}
	/*
	 * log the user out
	 * switch user to 0 which means no one is logged in
	 * switch admin to false;
	 */
	public void logout()
	{
		userId = 0;
		admin = false;
		location = new LinkedList<Waypoint>();
	}
	/*
	 * add user locations and then update the user table
	 * precondition: name, password, admin is not null
	 * input:	String of user name
	 * 			String of password
	 * 			Boolean of admin
	 * output:	none
	 */
	public boolean addUser(String name, String password, boolean admin) throws SQLException
	{
		if(name == null || password == null)
			return false;
		Connection connect = null;
		Statement statement = null;
		boolean addUser = true;
		try {
			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet result = statement.executeQuery(queryUser);
			//check if the user already exist
			while(result.next())
				if(result.getString(2).equals(name))
					//if the user exist then addUser is false
					addUser = false;
			//if user is unique then add the user
			result.close();
			if(addUser)
			{
				//if the new person added is admin then give them admin privileges
				if(admin)				
					//add a admin to the user table
					connect.createStatement().execute("insert into UserPassword (name, password, admin) " +
							"values ('" + name + "', '" + password+"', true)");
				//else make a new person a regular person
				else
					//add a regular person to the user table
					connect.createStatement().execute("insert into UserPassword (name, password, admin) " +
							"values ('" + name + "', '" + password+"', false)");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return addUser;
	}
	/*
	 * add new location to file and update the location file
	 * precondition:	latitude / longitude is not 0
	 * 					name / description is not null
	 * input:	double latitude	
	 * 			double longitude
	 * 			string name of location
	 * 			string description
	 * output:	true if the user add a location
	 *			false if the user cannot add a location
	 */
	public boolean addLocation(float latitude, float longitude ,String name,  String description) throws SQLException
	{
		if(latitude == 0 || longitude == 0 || name == null || description == null)
			return false;
		if(latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180)
			return false;
		Connection connect = null;
		Statement statement = null;
		ResultSet result = null;
		boolean locationAdded = true;
		if(!admin)
			return false;
		try {
			//calls the driver to call the database and query the location table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			result = statement.executeQuery(queryLocation);
			//while there is a another location
			while(result.next())
			{
				//if the location already exist then don't add the location
				if(name.equals(result.getString(2)))
					locationAdded =  false;
			}
			result.close();
			//if the location added is true then add a new location to location table and column to user table
			if(locationAdded)
			{
				connect.createStatement().execute("insert into Location (name, latitude, longitude, description)" +
						" values ('" + name + "', " + latitude + ", " + longitude + ", '" + description + "')");
				connect.createStatement().execute("alter table UserPassword add column \"" + name + "\" boolean default false");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return locationAdded;
	}
	/*
	 * add a new location to the user
	 * precondition:	a user is logged in
	 * 					the name of the locations it not null
	 * 					the name typed in is in the database
	 * postcondition:	a location on the user table is switched to true
	 * input:	string of the location being added
	 * output:	false of the user is 0
	 * 			true if the user adds a new location properly
	 */
	public boolean addUserLocation(String addLocation) throws SQLException
	{
		Connection connect = null;
		Statement statement = null;
		if( userId == 0 || addLocation == null)
			return false;
		boolean added = false;
		try {
			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet result = statement.executeQuery(queryUser);
			ResultSetMetaData data = result.getMetaData();
			int columns = data.getColumnCount();
			for(int i = 5; i <= columns; i++)
			{
				if(addLocation.equalsIgnoreCase(data.getColumnName(i)))
				{
					addLocation = data.getColumnName(i);
					added = true;
				}
			}
			addLocation = addLocation.replaceAll("'", "''");
			result.close();
			if(added)
				connect.createStatement().execute("Update UserPassword set \"" + addLocation + "\" = true " +
						"where id = " + userId);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			added = false;
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return added;
	}
	/*
	 * remove the user from the database
	 * precondition:	name is not null
	 * 					the user is in the database
	 * input:	string of the user being removed
	 * output:	true if user exist and removed
	 * 			false otherwise
	 */
	public boolean removeUser(String name) throws SQLException
	{
		if(name == null)
			return false;
		Connection connect = null;
		Statement statement = null;
		boolean removed = false;
		try {

			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet result = statement.executeQuery(queryUser);
			//while there are users remaining
			while(result.next())
			{
				//check if the user exist
				if(name.equalsIgnoreCase(result.getString(2)))
				{
					//then get their name from user table and change removed to true
					name = result.getString(2);
					removed = true;
				}
			}
			result.close();
			//if the removed boolean is true then remove the person from user table
			if(removed)
				connect.createStatement().execute("delete from UserPassword where name = '" + name+"'");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}

		return removed;
	}
	/*
	 * precondition:	name is not null
	 * 					the person logged in is a admin
	 * 					location is in the database
	 * postcondition:	a location is removed from the database
	 * remove the location being called
	 * input:	name being removed
	 * output:	true if the location exist
	 * 			false otherwise
	 */
	public boolean removeLocation(String name) throws SQLException
	{
		if((!admin) || name == null)
			return false;
		Connection connect = null;
		Statement statement = null;
		boolean removed = false;
		try {

			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet result = statement.executeQuery(queryUser);
			ResultSetMetaData data = result.getMetaData();
			int columns = data.getColumnCount();
			//check all the columns in the table
			for(int i = 5; i <= columns; i++)
			{
				//get the column's name
				if(name.equalsIgnoreCase(data.getColumnName(i)))
				{	
					name = data.getColumnName(i);
					removed = true;
				}
			}
			//makes sure the name doesn't have illegal quotes
			name = name.replaceAll("'", "''");
			result.close();
			//remove the location from the user table and location table
			if(removed){
				connect.createStatement().execute("alter table UserPassword drop column \"" + name+ "\"");
				connect.createStatement().execute("delete from Location where name = '" + name+ "'");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return removed;
	}
	/*
	 * precondition:	removeLocation is not null
	 * 					a user is logged in
	 * 					the location exist in the database
	 * remove the location from the user
	 * input:	String of removeLocation
	 * output:	true if there is a user logged in
	 * 			false if otherwise
	 */
	public boolean removeUserLocation(String removeLocation) throws SQLException
	{
		if(userId == 0 || removeLocation == null)
			return false;
		Connection connect = null;
		Statement statement = null;
		boolean removed = false;
		try {
			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet result = statement.executeQuery(queryUser);
			ResultSetMetaData data = result.getMetaData();
			int columns = data.getColumnCount();
			//check all the columns
			for(int i = 5; i <= columns; i++)
			{
				//check if the column name exist and then get the exact spelling of the column
				if(removeLocation.equalsIgnoreCase(data.getColumnName(i)))
				{	
					removeLocation = data.getColumnName(i);
					removed = true;
				}
			}
			result.close();
			//remove the location if it exist from the user table choice
			if(removed)
				connect.createStatement().execute("Update UserPassword set \"" + removeLocation + "\" = false " +
						"where id = " + userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return removed;
	}
	/*
	 * edit the location
	 * precondition:	latitude / longitude is not 0
	 * 					name / description is not null
	 * 					person is admin
	 * postcondition:	a location is modified
	 * input:	double latitude
	 * 			double longitude
	 * 			String name
	 * 			String description
	 * output:	false if the user cannot edit the location
	 * 			true otherwise
	 */
	public boolean editLocation(float latitude, float longitude, String name, String description, String newName) throws SQLException
	{
		if(latitude == 0 || longitude == 0 || name == null || description == null || admin == false)
			return false;
		if(latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180)
			return false;
		Connection connect = null;
		Statement statement = null;
		boolean editted = false;
		try {
			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet result = statement.executeQuery(queryUser);
			ResultSetMetaData data = result.getMetaData();
			int columns = data.getColumnCount();
			//check all the columns
			for(int i = 5; i <= columns; i++)
			{
				//check if the name of the location exist in the database
				if(name.equalsIgnoreCase(data.getColumnName(i)))
				{	
					name = data.getColumnName(i);
					editted = true;
				}
			}
			result.close();
			description = description.replaceAll("'", "''");
			//check if the location exist
			if(editted)
			{
				connect.createStatement().execute("update Location set LATITUDE = " + latitude + ", " +
						"LONGITUDE = " +longitude + ", DESCRIPTION = '" + description + "', NAME = '" + 
						newName  +"' where NAME = '" + name + "'");
				connect.createStatement().execute("RENAME COLUMN UserPassword.\"" + name + "\" TO \"" + newName +"\"");
			}
//				connect.createStatement().execute("ALTER TABLE UserPassword ALTER COLUMN \"" + name + "\" RENAME TO \"" + newName +"\"");
//			connect.createStatement().execute("ALTER TABLE UserPassword ADD COLUMN \""  + newName +"\" boolean default false");
//			connect.createStatement().execute("UPDATE  UserPassword set \"" + newName + "\" = \"" + name + "\"");
			
				} catch (SQLException e) {
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return editted;
	}
	public boolean isUserAdmin()
	{
		return new Boolean(admin);
	}
	/*
	 * get the locations from the database and return all the locations
	 * precondition: none
	 * postcondition: a linkedlist of waypoint is generated
	 * input:	none
	 * output:	linked list of location
	 */
	public LinkedList<Waypoint> getLocation() throws SQLException
	{
		Connection connect = null;
		Statement statement = null;
		try {
			//calls the driver to call the database and query the location table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet locationResult = statement.executeQuery(queryLocation);
			//add the waypoints to the waypoint linked list for all possible locations
			locationChoice = new LinkedList<Waypoint>();
			while(locationResult.next())
			{
				Waypoint newWaypoint = new Waypoint(locationResult.getString(2), 
						(float)locationResult.getDouble(3),
						(float)locationResult.getDouble(4),
						locationResult.getString(5));
				locationChoice.add(newWaypoint);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return locationChoice;
		
	}
	/*
	 * get the user locations from the array and return all the locations
	 * precondition: a user is logged in so userId != 0
	 * postcondition: a linkedlist of waypoint is generated
	 * input:	none
	 * output:	linked list of location the user entered
	 */
	public LinkedList<Waypoint> getUserLocations() throws SQLException
	{
		if(userId == 0)
			return location;
		Connection connect = null;
		Statement statement = null;
		LinkedList<Waypoint> news = getLocation();
		try {
			//calls the driver to call the database and query the location table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet personResult = statement.executeQuery(queryUser);
			//check if the id matches the database
			while(personResult.next())
			{
				//if the person is the one searched for then don't move the cursor
				if(personResult.getInt(1) == userId)
					break;
			}
			for(int i = 0; i < news.size(); i++)
			{	
				//check all their location and add them to the user's location
				if(personResult.getBoolean(i + 5) == true)
					location.add(news.get(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			//cleans up the connection resources
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return location;
	}
	public void printLocations()
	{
		for(int i = 0; i < location.size(); i++)
			System.out.println(location.get(i).toString());
	}
	public void printLocationsChoice()
	{
		for(int i = 0; i < locationChoice.size(); i++)
			System.out.println(locationChoice.get(i).toString());
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		DatabaseManager read = new DatabaseManager();
		//read.addUser("Kevin", "password3", false);
		//		read.login("Keith", "password1");
		read.login("Keith", "password1");
		read.addUser("Kevin", "password4", true);
		read.addUser("Kevin", "password5", true);
		read.addUser("Time", "password5", true);
		read.addLocation((float)3.213, (float)94.0126, "Maryland", "This is a new place");
		read.removeLocation("Maryland");
		read.removeUser("Beware");
		//		read.addUserLocation("yellowstone");
		//		read.addUserLocation("wind cave");
		read.addUserLocation("zion");
		read.addUserLocation("acadia");
		read.removeUserLocation("acadia");
		read.addLocation((float)64.21, (float)97.54, "Heaven", "This is another test");
		read.addLocation((float)4516.461, (float)46.1543, "Bryce canyon", "Another test");
		read.addUserLocation("Heaven");
		read.editLocation((float)464, (float)312, "YellowStone", "New description", "Bull");
		read.editLocation((float) 54, (float)113.112, "Zion", "This is the end", "new beginning");
//		read.editLocation((float) 46.1, (float)465.23, "This does not exist", "Hello hell", "not exist");
//		read.editLocation((float)63.145, longitude, name, description, newName)
		read.removeUser("Kevin");
		read.removeUser("Beware");
		//		read.addUserLocation("acadia");
		//		read.addUserLocation("American Samoa");
		//		read.addUser("Steven", "password5", false);
		//		read.addUser("Mariama", "password9", false);
		//		read.removeUser("Steven");
		//		read.removeUser("mariama");

		//		read.addUserLocation("Big Bend");
		//		read.addUserLocation("badlands");
		//		read.removeUserLocation("big bend");
		//		read.removeUserLocation("badlands");


		//		read.addLocation((float)-90.3214, (float)41.12365, "Maryland", "This is our home town");
		//		read.addLocation((float)634.01234, (float)42.1457, "Acadfdafdfiafd", "descriptiodaf sdfn1fadsf");
		//		read.removeLocation("Maryland");
		//		read.editLocation((float)93.7815, (float)-40.14521, "american samoa", "New description");
		read.getUserLocations();
		read.printLocations();
		read.printLocationsChoice();
		read.logout();
		System.out.println("Code finished");

	}
}
//		read.printToLocationFile();
//		read.printToUserFile();
//		String nextInput1 = input.nextLine();
//		int nextInput = Integer.parseInt(nextInput1);
//		String name = "";
//		String password = "";
//		String latitude = "";
//		String longitude = "";
//		String description = "";
//		while(nextInput != 0)
//		{
//			switch(nextInput)
//			{
//			//test login
//			case(1):
//				name = input.nextLine();
//				password = input.nextLine();
//				read.login(name, password);
//				break;
//			
//			//test add location
//			case(2):
//				longitude = input.nextLine(); 
//				latitude = input.nextLine();
//				name = input.nextLine();
//				description = input.nextLine();
//				read.addLocation(Float.parseFloat(longitude), Float.parseFloat(latitude), name, description);
//				break;
//			//test add user
//			case(3):
//				name = input.nextLine();
//				password = input.nextLine();
//				read.addUser(name, password, false);
//			}
//			nextInput1 = input.nextLine();
//			nextInput = Integer.parseInt(nextInput1);
//		}
//read.readLocation();
//		System.out.println(read);
//read.readUser();
//		System.out.println(read);
//read.addUser("Russ", "password2", false);
//test to make sure remove location works
//read.addUserLocation("Steven", "machu pichu");
//read.removeUser("Russ");
//read.addLocation(123.01, 97, "Adding location", "description of city");
//read.removeLocation("Adding location");
//test to make sure it prints user file properly
//read.printToUserFile();
//test to make sure it prints location file properly
//read.printToLocationFile();
//Mean to test to see if readLocation works properly	
//public String toString()
//{
//	String string = "";
//	for(int i = 0; i < location.size(); i++)
//		string = string + location.get(i).toString();
//	return string;
//}
//Meant to test to see if reading location is working properly
//public String toString()
//{
//	String string = "";
//	for(int i = 0; i < user.size(); i++)
//	{
//		string = string + user.get(i).toString();
//	}
//	return string;
//}
