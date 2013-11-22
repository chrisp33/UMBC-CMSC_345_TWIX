package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateUserTable {
	public static final String newDatabase = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String url = "jdbc:derby:Database;create = true";
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		Class.forName(newDatabase);
		//create the database if it doesn't exist
		Connection connect = DriverManager.getConnection(url);
		//creates the location table in database
		connect.createStatement().execute("create table UserPassword (" +
				"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
				"PRIMARY KEY (id)," +
				"name varchar(20) not null," +
				"password varchar(20) not null," +
				"admin boolean not null)");
		//add default columns for user
		connect.createStatement().execute("alter table UserPassword add column \"Acadia\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"American Samoa\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Arches\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Badlands\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Big Bend\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Biscayne\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Black Canyon of Guinnson\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Bryce Canyon\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Canyonlands\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Capitol Reef\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Carlsbad Caverns\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Channel Islands\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Congaree\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Crater Lake\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Cuyahoga Valley\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Death Valley\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Denali\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Dry Tortugas\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Everglades\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Gates of the Arctic\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Glacier\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Glacier Bay\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Grand Canyon\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Grand Teton\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Great Basin\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Great Sand Dunes\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Great Smoky Mountains\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Guadalupe Mountains\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Haleakala\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Hawaii Volcanoes\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Hot Springs\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Isle Royale\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Joshua Tree\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Katmai\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Kenai Fjords\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Kings Canyon\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Kobuk Valley\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Lake Clark\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Lassen Vocanic\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Machu Pichu\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Mammoth Cave\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Mesa Verde\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Mount Rainier\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"North Cascades\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Olympic\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Petrified Forest\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Pinnacles\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Redwood\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Rocky Mountain\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Saguaro\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Sequoia\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Shenandoah\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Theodore Roosevelt\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Virgin Islands\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Voyageurs\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Wind Cave\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Wrangell -St. Elias\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"YellowStone\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Yosemite\" boolean default false");
		connect.createStatement().execute("alter table UserPassword add column \"Zion\" boolean default false");
		connect.createStatement().execute("insert into UserPassword (name, password, admin) values ('Keith', 'password1', true)"); 

		connect.createStatement().execute("insert into UserPassword (name, password, admin) values ('Chris', 'password3',false)");
		//connect.createStatement().execute("insert into UserPassword (name, password, admin) values('Steven' , 'password3', false)"); 
		//connect.createStatement().execute("alter table UserPassword add column region3 boolean default false");		
		//connect.createStatement().execute("alter table UserPassword drop column region1");
//		connect.close();
		System.out.println("Code finished");
	}
}
