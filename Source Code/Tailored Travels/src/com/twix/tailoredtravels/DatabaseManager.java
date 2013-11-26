package com.twix.tailoredtravels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
//there must be the derby embeddeddriver loaded in plugins
//the database must exist
public class DatabaseManager {
	private LinkedList<Waypoint> db_waypoints;
	private boolean admin;
	private final String newDatabase = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String url = "jdbc:derby:Database;create = true";
	private final String userTable = "db_users";
	Connection _connect = null;
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
		db_waypoints = new LinkedList<Waypoint>();
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
		
		boolean login = false;
		ResultSet result = executeQueryForResult("SELECT * FROM " + userTable + 
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		//while there is another item
		while(result.next())
		{
			//check if the user name and password exist with name being case-insensitive
			//and password being case sensitive
			if(result.getString(3).equals(password))
			{
				//check if the person is a admin
				if(result.getBoolean(4))
					admin = true;
				login = true;
			}
		}
		_connect.close();
		return login;
	}
	private ResultSet executeQueryForResult(String query) {
		Statement statement = null;
		ResultSet result = null;
		try{
			//calls the driver to call the database and query the user table
			_connect = DriverManager.getConnection(url);
			statement = _connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			result = statement.executeQuery(query);
		}catch(SQLException e)
		{
			// Cannot access database
		}
		return result;
	}
	/*
	 * log the user out
	 * switch user to 0 which means no one is logged in
	 * switch admin to false;
	 */
	public void logout()
	{
		admin = false;
		db_waypoints = new LinkedList<Waypoint>();
	}
	/*
	 * add user locations and then update the user table
	 * precondition: name, password, admin is not null
	 * input:	String of user name
	 * 			String of password
	 * 			Boolean of admin
	 * output:	none
	 */
	public boolean addUser(String name, String password, boolean isAdmin) throws SQLException
	{
		boolean success = false;
		if(name == null || password == null || !admin)
			return false;

		//check if the user already exist
		ResultSet _r = executeQueryForResult("SELECT * FROM " + userTable + 
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (_r.isBeforeFirst())
		{
			_connect.close();
			return false;
		}
		
		//if user is unique then add the user
		String query = "";
		//if the new person added is admin then give them admin privileges
		query = "INSERT into " + userTable + " (name, password, admin) " +
				"values ('"+name+"', '"+password+"', "+isAdmin+")";
		success = executeQuery(query);
		return true;
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
		boolean success = false;
		if(latitude == 0 || longitude == 0 || name == null || description == null)
			return false;
		if(latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180)
			return false;
		
		if(!admin)
			return false;

		//calls the driver to call the database and query the location table
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (_r.isBeforeFirst())
		{
			_connect.close();
			return false;
		}
		success = executeQuery("INSERT into db_waypoints " +
				"(name, latitude, longitude, description)  values " +
				"('" + name + "', " + latitude + 
				", " + longitude + ", '" + description + "')");

		return true;
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
		boolean success = false;
		if(name == null || !admin)
			return false;
		
		//check if the user already exist
		ResultSet _r = executeQueryForResult("SELECT * FROM " + userTable + 
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (!_r.isBeforeFirst())
		{
			_connect.close();
			return false;
		}
		//if user is unique then add the user

		//if the new person added is admin then give them admin privileges
		String query = "DELETE FROM " + userTable + 
				" WHERE upper(name) LIKE upper('%"+name+"%')";
		success = executeQuery(query);

		return success;
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
		boolean success = false;
		
		//calls the driver to call the database and query the location table
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (!_r.isBeforeFirst())
		{
			_connect.close();
			return false;
		}
		success = executeQuery("DELETE FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");

		return success;
	}
	
	private boolean executeQuery(String query)
	{
		boolean success = false;
		Connection connect = null;
		try {
			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			success = connect.createStatement().execute(query);
			connect.close();
		}
		catch(SQLException e)
		{
			success = false;
		}
		return success;
	}
	
	public boolean setWaypointName(String oldName, String newName) throws SQLException
	{
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+oldName+"%')");
		if (!_r.isBeforeFirst())
			return false;
		
		String query = "UPDATE db_waypoints SET " +
				"name='"+newName+"' WHERE upper(name) LIKE upper('%"+oldName+"%')";
		
		return executeQuery(query);
	}
	
	public boolean setWaypointDescription(String name, String newDescription) throws SQLException
	{
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (!_r.isBeforeFirst())
		{
			_connect.close();
			return false;
		}
		String query = "UPDATE db_waypoints SET " +
				"description='"+newDescription+"' WHERE upper(name) LIKE upper('%"+name+"%')";
		
		return executeQuery(query);
	}
	
	public boolean setWaypointLatLong(String name, float newLat, float newLong) throws SQLException
	{
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (!_r.isBeforeFirst())
		{
			_connect.close();
			return false;
		}
		String query = "UPDATE db_waypoints SET " +
				"latitude="+newLat+", longitude="+newLong+" WHERE upper(name) LIKE upper('%"+name+"%')";
		
		return executeQuery(query);
	}
	
	public boolean isUserAdmin()
	{
		return admin;
	}
	/*
	 * get the locations from the database and return all the locations
	 * precondition: none
	 * postcondition: a linkedlist of waypoint is generated
	 * input:	none
	 * output:	linked list of location
	 */
	public LinkedList<Waypoint> getWaypoints() throws SQLException
	{
		Connection connect = null;
		Statement statement = null;
		try {
			//calls the driver to call the database and query the location table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet locationResult = statement.executeQuery("SELECT * FROM db_waypoints");
			//add the waypoints to the waypoint linked list for all possible locations
			db_waypoints = new LinkedList<Waypoint>();
			while(locationResult.next())
			{
				Waypoint newWaypoint = new Waypoint(locationResult.getString(2), 
						(float)locationResult.getDouble(3),
						(float)locationResult.getDouble(4),
						locationResult.getString(5));
				db_waypoints.add(newWaypoint);
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
		return db_waypoints;
	}
	
	public void printWaypoints()
	{
		for(int i = 0; i < db_waypoints.size(); i++)
			System.out.println(db_waypoints.get(i).toString());
	}
}