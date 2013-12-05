package com.twix.tailoredtravels;

/**
 * @author Keith Cheng and Stephen Moore
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;


/**
 * There MUST be a derby embeddeddriver loaded in plugins
 * The database must exist
 */
public class DatabaseManager {
	private LinkedList<Waypoint> db_waypoints;
	private boolean admin;
	private final String newDatabase = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String url = "jdbc:derby:Database;create = true";
	private final String userTable = "db_users";
	Connection _connect = null;

	/**
	 * Constructor that starts this file reader
	 * reads the location file and user file to have data
	 * 
	 * precondition: org.apache.derby.jdbc.EmbeddedDriver must exist in eclipse plugin
	 * postcondition: sets currentUser to 0 to mean no user at this time
	 * 				  sets login to false
	 * 
	 * @throws ClassNotFountException
	 * @throws SQLException
	 */
	public DatabaseManager() throws ClassNotFoundException, SQLException
	{
		Class.forName(newDatabase);
		db_waypoints = new LinkedList<Waypoint>();
		admin = false;
	}
	
	/**
	 * allows the user to log in and sets the user
	 * 
	 * precondition: name / password are not null
	 * 
	 * @param name of the person logging in
	 * @param password of the person logging in
	 * @return	login
	 * 			--true if the person is logged in
	 * 			--false if there are no one with that username / password
	 * 
	 * @throws SQLException
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
			//check if the user name and password exist (username not case sensitive)
			if(result.getString(3).equals(password))
			{
				//check if the person is an admin
				if(result.getBoolean(4))
					admin = true;
				login = true;
			}
		}
		_connect.close();
		return login;
	}
	
	/**
	 * logs the user out
	 * 
	 * postcondition: admin status set to false
	 * 				  waypoint list cleared
	 */
	public void logout()
	{
		admin = false;
		db_waypoints = new LinkedList<Waypoint>();
	}
	
	/**
	 * add user locations and then update the user table
	 * 
	 * precondition: admin is not null
	 * 
	 * @param name of user
	 * @param password of user
	 * @param isAdmin - is the user an admin
	 * 
	 * @return true
	 * 
	 * @throws SQLException
	 */
	public boolean addUser(String name, String password, boolean isAdmin) throws SQLException
	{
		if(name == null || password == null || !admin)
			return false;

		//check if the user already exist
		ResultSet _r = executeQueryForResult("SELECT * FROM " + userTable + 
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (_r.isBeforeFirst() || searchUserQuery(_r,name))
		{
			_connect.close();
			return false;
		}
		_connect.close();
		
		//if user is unique then add the user
		String query = "";
		//if the new person added is admin then give them admin privileges
		query = "INSERT into " + userTable + " (name, password, admin) " +
				"values ('"+name+"', '"+password+"', "+isAdmin+")";
		executeQuery(query);
		return true;
	}
	
	/**
	 * add new location to file and update the location file
	 * 
	 * @param latitude	
	 * @param longitude
	 * @param name of location
	 * @param description for location
	 * 
	 * @return true if the user add a location, false if the user cannot add a location
	 *
	 * @throws SQLException
	 */
	public boolean addLocation(float latitude, float longitude ,String name,  String description) throws SQLException
	{
		if(latitude == 0 || longitude == 0 || name == null || description == null)
			return false;
		if(latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180)
			return false;
		
		if(!admin)
			return false;

		//calls the driver to call the database and query the location table
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (_r.isBeforeFirst() || searchLocationQuery(_r, name))
		{
			_connect.close();
			return false;
		}
		_connect.close();
		executeQuery("INSERT into db_waypoints " +
				"(name, latitude, longitude, description)  values " +
				"('" + name + "', " + latitude + 
				", " + longitude + ", '" + description + "')");

		return true;
	}
	
	/**
	 * Remove a user from the database
	 * 
	 * precondition: the user is in the database
	 * 
	 * @param	name of user being removed
	 * 
	 * @return true if user exists and is removed, false otherwise
	 * 
	 * @throws SQLException
	 */
	public boolean removeUser(String name) throws SQLException
	{
		if(name == null || !admin)
			return false;
		
		//check if the user already exist
		ResultSet _r = executeQueryForResult("SELECT * FROM " + userTable + 
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (!_r.isBeforeFirst() || !searchUserQuery(_r, name))
		{
			_connect.close();
			return false;
		}
		_connect.close();
		//if user is unique then add the user

		//if the new person added is admin then give them admin privileges
		String query = "DELETE FROM " + userTable + 
				" WHERE upper(name) LIKE upper('%"+name+"%')";
		executeQuery(query);
		return true;
	}
	
	/**
	 * Remove a location from the database
	 * 
	 * precondition:	location is in the database
	 * postcondition:	a location is removed from the database
	 * 
	 * @param	name being removed
	 * 
	 * @return true if the location exists and is removed, false otherwise
	 * 
	 * @throws SQLException
	 */
	public boolean removeLocation(String name) throws SQLException
	{
		if((!admin) || name == null)
			return false;
		
		//calls the driver to call the database and query the location table
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r, name))
		{
			_connect.close();
			return false;
		}
		_connect.close();
		//I do not know why but this cannot be changed to follow the other syntax
		//or at least not without the code breaking
		executeQuery("delete from db_waypoints where name = '"+name+"'");
		return true;
	}
	
	/**
	 * Execute a query for the resultSet
	 * 
	 * precondition: the database exists
	 * 
	 * @param query string
	 * 
	 * @return resultset populated with results from query
	 */
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
	
	/**
	 * Execute the query to execute the statement
	 * 
	 * precondition - database exists
	 * postcondition - database is modified by given statement
	 * 
	 * @param	query string
	 */
	private void executeQuery(String query)
	{
		Connection connect = null;
		try {
			//calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			connect.createStatement().execute(query);
			connect.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Query the user table to check if it exist
	 * 
	 * precondition - ResultSet exists / userName is not null
	 * 
	 * @param result (ResultSet)
	 * @param userName name of user
	 * 
	 * @return true if user exists, false otherwise
	 * 
	 * @throws SQLException
	 */
	private boolean searchUserQuery(ResultSet result, String userName) throws SQLException
	{
		while(result.next())
		{
			if(result.getString(2).equalsIgnoreCase(userName))
				return true;
		}
		return false;
	}
	
	/**
	 * Query the waypointdb table to check if it exists
	 * 
	 * precondition - ResultSet exist / locationName is not null
	 * 
	 * @param result (ResultSet)
	 * @param userName name of user
	 * 
	 * @return - boolean of whether the location exist or not
	 * 
	 * @throws SQLException
	 */
	private boolean searchLocationQuery(ResultSet result, String locationName) throws SQLException
	{
		while(result.next())
		{
			if(result.getString(2).equalsIgnoreCase(locationName))
				return true;
		}
		return false;
	}
	
	/**
	 * Change a waypoint's name
	 * 
	 * postcondition - waypoint name is changed
	 * 
	 * @param oldName for waypoint
	 * @param newName for waypoint
	 * 
	 * @return true if name change was successful, false otherwise
	 * 
	 * @throws SQLException
	 */
	public boolean setWaypointName(String oldName, String newName) throws SQLException
	{
		if(oldName == null || newName == null)
			return false;
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+oldName+"%')");
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r,oldName))
		{
			_connect.close();
			return false;
		}
		_connect.close();
		
		String query = "UPDATE db_waypoints SET " +
				"name='"+newName+"' WHERE upper(name) LIKE upper('%"+oldName+"%')";
		
		executeQuery(query);
		return true;
	}
	
	/**
	 * Change a waypoint's description
	 * 
	 * postcondition - waypoint description is changed
	 * 
	 * @param oldDescription for waypoint
	 * @param newDescription for waypoint
	 * 
	 * @return true if description change was successful, false otherwise
	 * 
	 * @throws SQLException
	 */
	public boolean setWaypointDescription(String oldDescription, String newDescription) throws SQLException
	{
		if(oldDescription == null || newDescription == null)
			return false;
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+oldDescription+"%')");
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r, oldDescription))
		{
			_connect.close();
			return false;
		}
		_connect.close();
		String query = "UPDATE db_waypoints SET " +
				"description='"+newDescription+"' WHERE upper(name) LIKE upper('%"+oldDescription+"%')";
		executeQuery(query);
		return true;
	}
	

	/**
	 * Change a waypoint's latitude/longitude, by name
	 * 
	 * postcondition - waypoint latitude and longitude is changed
	 * 
	 * @param name of waypoint
	 * @param oldDescription for waypoint
	 * @param newDescription for waypoint
	 * 
	 * @return true if description change was successful, false otherwise
	 * 
	 * @throws SQLException
	 */
	public boolean setWaypointLatLong(String name, float newLat, float newLong) throws SQLException
	{
		if(name == null)
			return false;
		ResultSet _r = executeQueryForResult("SELECT * FROM db_waypoints " +
				" WHERE upper(name) LIKE upper('%"+name+"%')");
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r, name))
		{
			_connect.close();
			return false;
		}
		_connect.close();
		String query = "UPDATE db_waypoints SET " +
				"latitude="+newLat+", longitude="+newLong+" WHERE upper(name) LIKE upper('%"+name+"%')";
		
		executeQuery(query);
		return true;
	}
	
	/**
	 * Check if the user is an admin
	 * 
	 * precondition: admin contains the appropriate value
	 * 
	 * @return admin - true if user is admin, false otherwise
	 */
	public boolean isUserAdmin()
	{
		return admin;
	}
	
	/**
	 * Get and return a LinkedList containing all waypoints
	 * 
	 * precondition: database exists
	 * 
	 * @return linked list of all waypoints
	 * 
	 * @throws SQLException
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
			System.err.println("Could not access database");
			e.printStackTrace();
		} finally
		{
			if(statement != null)
				statement.close();
			if(connect != null)
				connect.close();
		}
		return db_waypoints;
	}
	
	/**
	 * Print all waypoints
	 * 
	 * precondition: db_waypoints is not null
	 * 
	 */
	public void printWaypoints()
	{
		for(int i = 0; i < db_waypoints.size(); i++)
			System.out.println(db_waypoints.get(i).toString());
	}
	
}