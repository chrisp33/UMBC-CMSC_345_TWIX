package com.twix.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InitDatabase {
	public static final String newDatabase = Messages
			.getString("InitDatabase.0"); //$NON-NLS-1$
	public static final String url = Messages.getString("InitDatabase.1"); //$NON-NLS-1$

	/**
	 * @param args
	 *            none
	 */
	public static void main(String[] args) {
		try {
			CreateWaypointTable();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// Already created
		}
		try {
			CreateUserTable();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// Already Created
		}
	}

	public static void CreateWaypointTable() throws SQLException,
			ClassNotFoundException {
		Class.forName(newDatabase);
		// creates the database if it doesn't exist
		Connection connect = DriverManager.getConnection(url);
		// creates the db_waypoints table in database
		connect.createStatement().execute(Messages.getString("InitDatabase.2") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.3") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.4") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.5") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.6") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.7") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.8")); //$NON-NLS-1$

		// adds default db_waypointss
		connect.createStatement().execute(Messages.getString("InitDatabase.9")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.10")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.11")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.12")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.13")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.14")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.15")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.16")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.17")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.18")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.19")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.20")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.21")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.22")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.23")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.24")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.25")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.26")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.27")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.28")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.29")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.30")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.31")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.32")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.33")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.34")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.35")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.36")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.37")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.38")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.39")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.40")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.41")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.42")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.43")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.44")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.45")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.46")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.47")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.48")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.49")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.50")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.51")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.52")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.53")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.54")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.55")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.56")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.57")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.58")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.59")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.60")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.61")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.62")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.63")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.64")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.65")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.66")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.67")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.68")); //$NON-NLS-1$

		connect.close();
		System.out.println(Messages.getString("InitDatabase.69")); //$NON-NLS-1$
	}

	public static void CreateUserTable() throws SQLException,
			ClassNotFoundException {
		Class.forName(newDatabase);
		// create the database if it doesn't exist
		Connection connect = DriverManager.getConnection(url);
		// creates the location table in database
		connect.createStatement().execute(Messages.getString("InitDatabase.70") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.71") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.72") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.73") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.74") //$NON-NLS-1$
				+ Messages.getString("InitDatabase.75")); //$NON-NLS-1$

		connect.createStatement()
				.execute(Messages.getString("InitDatabase.76")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.77")); //$NON-NLS-1$
		connect.createStatement()
				.execute(Messages.getString("InitDatabase.78")); //$NON-NLS-1$
		connect.close();
		System.out.println(Messages.getString("InitDatabase.79")); //$NON-NLS-1$
	}

}
