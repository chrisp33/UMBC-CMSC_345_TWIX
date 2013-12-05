package com.twix.tailoredtravels;

/**
 * @author Keith Cheng, Stephen Moore 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.twix.init.InitDatabase;
//there must be the derby embeddeddriver loaded in plugins
//the database must exist

public class DatabaseManager {
	private LinkedList<Waypoint> db_waypoints;
	private boolean admin;
	private final String newDatabase = Messages.getString("DatabaseManager.0"); //$NON-NLS-1$
	private final String url = Messages.getString("DatabaseManager.1"); //$NON-NLS-1$
	private final String userTable = Messages.getString("DatabaseManager.2"); //$NON-NLS-1$
	Connection _connect = null;

	// private Connection connect = null;

	/*
	 * Constructor that starts this file reader sets currentUser to 0 to mean no
	 * user at this time set login to false reads the location file and user
	 * file to have data precondition: org.apache.derby.jdbc.EmbeddedDriver must
	 * exist in eclipse plugin input: none output: none
	 */
	public DatabaseManager() throws ClassNotFoundException, SQLException {
		Class.forName(newDatabase);
		db_waypoints = new LinkedList<Waypoint>();
		admin = false;
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.3") + userTable); //$NON-NLS-1$
		if (_r == null) {
			_connect.close();
			InitDatabase.CreateUserTable();
		} else
			_connect.close();
		_r = executeQueryForResult(Messages.getString("DatabaseManager.4")); //$NON-NLS-1$
		if (_r == null) {
			_connect.close();
			InitDatabase.CreateWaypointTable();

		} else
			_connect.close();

	}

	/*
	 * allows the user to log in and sets the user precondition: name / password
	 * are not null input: String: name of the person logging in String:
	 * password of the person logging in output: true if the person is logged in
	 * false if there are no one with that username / password
	 */
	public boolean login(String name, String password) throws SQLException {
		if (name == null || password == null)
			return false;

		boolean login = false;
		ResultSet result = executeQueryForResult(Messages
				.getString("DatabaseManager.5") + userTable //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.6") + name + Messages.getString("DatabaseManager.7")); //$NON-NLS-1$ //$NON-NLS-2$
		// while there is another item
		while (result.next()) {
			// check if the user name and password exist with name being
			// case-insensitive
			// and password being case sensitive
			if (result.getString(3).equals(password)) {
				// check if the person is a admin
				if (result.getBoolean(4))
					admin = true;
				login = true;
			}
		}
		_connect.close();
		return login;
	}

	/*
	 * log the user out switch admin to false;
	 */
	public void logout() {
		admin = false;
		db_waypoints = new LinkedList<Waypoint>();
	}

	/*
	 * add user locations and then update the user table precondition: name,
	 * password, admin is not null input: String of user name String of password
	 * Boolean of admin output: none
	 */
	public boolean addUser(String name, String password, boolean isAdmin)
			throws SQLException {
		if (name == null || password == null || !admin)
			return false;

		// check if the user already exist
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.8") + userTable //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.9") + name + Messages.getString("DatabaseManager.10")); //$NON-NLS-1$ //$NON-NLS-2$
		if (_r.isBeforeFirst() || searchUserQuery(_r, name)) {
			_connect.close();
			return false;
		}
		_connect.close();

		// if user is unique then add the user
		String query = Messages.getString("DatabaseManager.11"); //$NON-NLS-1$
		// if the new person added is admin then give them admin privileges
		query = Messages.getString("DatabaseManager.12") + userTable + Messages.getString("DatabaseManager.13") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("DatabaseManager.14") + name + Messages.getString("DatabaseManager.15") + password + Messages.getString("DatabaseManager.16") + isAdmin //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ Messages.getString("DatabaseManager.17"); //$NON-NLS-1$
		executeQuery(query);
		return true;
	}

	/*
	 * add new location to file and update the location file precondition:
	 * latitude / longitude is not 0 name / description is not null input:
	 * double latitude double longitude string name of location string
	 * description output: true if the user add a location false if the user
	 * cannot add a location
	 */
	public boolean addLocation(float latitude, float longitude, String name,
			String description) throws SQLException {
		if (latitude == 0 || longitude == 0 || name == null
				|| description == null)
			return false;
		if (latitude > 90 || latitude < -90 || longitude > 180
				|| longitude < -180)
			return false;

		if (!admin)
			return false;

		// calls the driver to call the database and query the location table
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.18") //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.19") + name + Messages.getString("DatabaseManager.20")); //$NON-NLS-1$ //$NON-NLS-2$
		if (_r.isBeforeFirst() || searchLocationQuery(_r, name)) {
			_connect.close();
			return false;
		}
		_connect.close();
		executeQuery(Messages.getString("DatabaseManager.21") //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.22") + Messages.getString("DatabaseManager.23") //$NON-NLS-1$ //$NON-NLS-2$
				+ name
				+ Messages.getString("DatabaseManager.24") + latitude + Messages.getString("DatabaseManager.25") + longitude + Messages.getString("DatabaseManager.26") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ description + Messages.getString("DatabaseManager.27")); //$NON-NLS-1$

		return true;
	}

	/*
	 * remove the user from the database precondition: name is not null the user
	 * is in the database input: string of the user being removed output: true
	 * if user exist and removed false otherwise
	 */
	public boolean removeUser(String name) throws SQLException {
		if (name == null || !admin)
			return false;

		// check if the user already exist
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.28") + userTable //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.29") + name + Messages.getString("DatabaseManager.30")); //$NON-NLS-1$ //$NON-NLS-2$
		if (!_r.isBeforeFirst() || !searchUserQuery(_r, name)) {
			_connect.close();
			return false;
		}
		_connect.close();
		// if user is unique then add the user

		// if the new person added is admin then give them admin privileges
		String query = Messages.getString("DatabaseManager.31") + userTable //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.32") + name + Messages.getString("DatabaseManager.33"); //$NON-NLS-1$ //$NON-NLS-2$
		executeQuery(query);
		return true;
	}

	/*
	 * precondition: name is not null the person logged in is a admin location
	 * is in the database postcondition: a location is removed from the database
	 * remove the location being called input: name being removed output: true
	 * if the location exist false otherwise
	 */
	public boolean removeLocation(String name) throws SQLException {
		if ((!admin) || name == null)
			return false;

		// calls the driver to call the database and query the location table
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.34") //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.35") + name + Messages.getString("DatabaseManager.36")); //$NON-NLS-1$ //$NON-NLS-2$
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r, name)) {
			_connect.close();
			return false;
		}
		_connect.close();
		// I do not know why but this cannot be changed to follow the other
		// syntax
		// or at least not without the code breaking
		executeQuery(Messages.getString("DatabaseManager.37") + name + Messages.getString("DatabaseManager.38")); //$NON-NLS-1$ //$NON-NLS-2$
		return true;
	}

	/*
	 * Execute a query for the resultSet precondition - a database exist
	 * postcondition - return the resultset input -String of what is being
	 * queried output - ResultSet
	 */
	private ResultSet executeQueryForResult(String query) {
		Statement statement = null;
		ResultSet result = null;
		try {
			// calls the driver to call the database and query the user table
			_connect = DriverManager.getConnection(url);
			statement = _connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			// Cannot access database
		}
		return result;
	}

	/*
	 * execute the query to execute the statement precondition - database exist
	 * postcondition - database is modified input - String of the query output -
	 * none
	 */
	private void executeQuery(String query) {
		Connection connect = null;
		try {
			// calls the driver to call the database and query the user table
			connect = DriverManager.getConnection(url);
			connect.createStatement().execute(query);
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Query the user table to check if it exist precondition - ResultSet exist
	 * / userName is not null postcondition - returns whether the user is
	 * already in the database or not input - ResultSet result / String userName
	 * output - boolean of whether the user exist or not
	 */
	private boolean searchUserQuery(ResultSet result, String userName)
			throws SQLException {
		while (result.next()) {
			if (result.getString(2).equalsIgnoreCase(userName))
				return true;
		}
		return false;
	}

	/*
	 * Query the waypointdb table to check if it exist precondition - ResultSet
	 * exist / locationName is not null postcondition - returns whether the
	 * location is already in the database or not input - ResultSet result /
	 * String locationName output - boolean of whether the location exist or not
	 */
	private boolean searchLocationQuery(ResultSet result, String locationName)
			throws SQLException {
		while (result.next()) {
			if (result.getString(2).equalsIgnoreCase(locationName))
				return true;
		}
		return false;
	}

	/*
	 * change a waypoint name precondition - String oldName / newName is not
	 * null postcondition - waypoint name is changed input - String oldName /
	 * newName output - boolean whether it suceeded or not
	 */
	public boolean setWaypointName(String oldName, String newName)
			throws SQLException {
		if (oldName == null || newName == null)
			return false;
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.39") //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.40") + oldName + Messages.getString("DatabaseManager.41")); //$NON-NLS-1$ //$NON-NLS-2$
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r, oldName)) {
			_connect.close();
			return false;
		}
		_connect.close();

		String query = Messages.getString("DatabaseManager.42") + Messages.getString("DatabaseManager.43") + newName //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("DatabaseManager.44") + oldName + Messages.getString("DatabaseManager.45"); //$NON-NLS-1$ //$NON-NLS-2$

		executeQuery(query);
		// return executeQuery(query);
		return true;
	}

	/*
	 * change a waypoint description precondition - String oldName /
	 * newDescription is not null postcondition - waypoint description is
	 * changed input - String oldName / newDescription output - boolean whether
	 * it suceeded or not
	 */
	public boolean setWaypointDescription(String name, String newDescription)
			throws SQLException {
		if (name == null || newDescription == null)
			return false;
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.46") //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.47") + name + Messages.getString("DatabaseManager.48")); //$NON-NLS-1$ //$NON-NLS-2$
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r, name)) {
			_connect.close();
			return false;
		}
		_connect.close();
		String query = Messages.getString("DatabaseManager.49") + Messages.getString("DatabaseManager.50") //$NON-NLS-1$ //$NON-NLS-2$
				+ newDescription
				+ Messages.getString("DatabaseManager.51") + name //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.52"); //$NON-NLS-1$
		executeQuery(query);
		return true;
	}

	/*
	 * change a waypoint latitude / longitude precondition - String oldName / is
	 * not null postcondition - waypoint latitude / longitude is changed input -
	 * String oldName / float newLat / float newLong output - boolean whether it
	 * suceeded or not
	 */
	public boolean setWaypointLatLong(String name, float newLat, float newLong)
			throws SQLException {
		if (name == null)
			return false;
		ResultSet _r = executeQueryForResult(Messages
				.getString("DatabaseManager.53") //$NON-NLS-1$
				+ Messages.getString("DatabaseManager.54") + name + Messages.getString("DatabaseManager.55")); //$NON-NLS-1$ //$NON-NLS-2$
		if (!_r.isBeforeFirst() || !searchLocationQuery(_r, name)) {
			_connect.close();
			return false;
		}
		_connect.close();
		String query = Messages.getString("DatabaseManager.56") + Messages.getString("DatabaseManager.57") + newLat //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("DatabaseManager.58") + newLong + Messages.getString("DatabaseManager.59") //$NON-NLS-1$ //$NON-NLS-2$
				+ name + Messages.getString("DatabaseManager.60"); //$NON-NLS-1$

		executeQuery(query);
		return true;
	}

	public boolean isUserAdmin() {
		return admin;
	}

	/*
	 * get the locations from the database and return all the locations
	 * precondition: none postcondition: a linkedlist of waypoint is generated
	 * input: none output: linked list of location
	 */
	public LinkedList<Waypoint> getWaypoints() throws SQLException {
		Connection connect = null;
		Statement statement = null;
		try {
			// calls the driver to call the database and query the location
			// table
			connect = DriverManager.getConnection(url);
			statement = connect.createStatement();
			ResultSet locationResult = statement.executeQuery(Messages
					.getString("DatabaseManager.61")); //$NON-NLS-1$
			// add the waypoints to the waypoint linked list for all possible
			// locations
			db_waypoints = new LinkedList<Waypoint>();
			while (locationResult.next()) {
				Waypoint newWaypoint = new Waypoint(
						locationResult.getString(2),
						(float) locationResult.getDouble(3),
						(float) locationResult.getDouble(4),
						locationResult.getString(5));
				db_waypoints.add(newWaypoint);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// cleans up the connection resources
			if (statement != null)
				statement.close();
			if (connect != null)
				connect.close();
		}
		return db_waypoints;
	}

	public void printWaypoints() {
		for (int i = 0; i < db_waypoints.size(); i++)
			System.out.println(db_waypoints.get(i).toString());
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		DatabaseManager data = new DatabaseManager();
		data.login(
				Messages.getString("DatabaseManager.62"), Messages.getString("DatabaseManager.63")); //$NON-NLS-1$ //$NON-NLS-2$
		// data.addLocation((float)10, (float)41.12, "Maryland", "Description");
		// data.removeLocation("YellowStone");
		// data.removeUser("Stephen");
		// data.addLocation((float)13.13246, (float)12, "Heaven",
		// "description1");
		if (data.removeUser(Messages.getString("DatabaseManager.64"))) //$NON-NLS-1$
			System.out.println(Messages.getString("DatabaseManager.65")); //$NON-NLS-1$
		else
			System.out.println(Messages.getString("DatabaseManager.66")); //$NON-NLS-1$
	}
}