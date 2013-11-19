package com.twix.tailoredtravels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class DatabaseManager {
	LinkedList<Waypoint> location = new LinkedList<Waypoint>();
	LinkedList<User> user = new LinkedList<User>();
	private String userFile = "user.txt";
	private String locationFile = "location.txt";
	private int currentUser;
	private boolean login;
	static Scanner input = new Scanner(System.in);
	private boolean userAdmin;
	/*
	 * Constructor that starts this file reader
	 * sets currentUser to 0 to mean no user at this time
	 * set login to false
	 * reads the location file and user file to have data
	 * input: none
	 * output: none
	 */
	public DatabaseManager()
	{
		currentUser = 0;
		login = false;
		readLocation();
		readUser();
		userAdmin = false;
	}
	/*
	 * allows the user to log in and sets the user
	 * input:	String: name of the person logging in
	 * 			String: password of the person logging in
	 * output:	true if the person is logged in
	 * 			false if there are no one with that username / password
	 */
	public boolean login(String name, String password)
	{

		//loops through all the user in the file
		for(int i = 0; i < user.size(); i++)
		{
			//checks the user
			if(user.get(i).login(name, password))
			{
				currentUser = i;
				login = true;
				if(user.get(currentUser).getAdmin() == true)
					userAdmin = true;
			}
		}
		return login;
	}
	/*
	 * log the user out
	 */
	public void logout()
	{
		currentUser = 0;
		login = false;
		userAdmin = false;
	}
	/*
	 * read all the location from the file
	 * input: none
	 * output: none
	 */
	public void readLocation()
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(locationFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			/* read format is read whole line
			 * double (latitude), double(longitude), name of the location 
			 * readline again for description
			 */
			String line = br.readLine();
			while (line != null) {
				String description = br.readLine();
				String string[] = line.split(" ");
				String name = "";
				//concatnate the string for proper use
				for(int i = 2; i < string.length; i++)
				{
					if(i + 1 != string.length)
						name = name + string[i] + " ";
					else
						name = name + string[i];
				}
				//add the location to the file
				location.add(new Waypoint(name,Float.parseFloat(string[0]), Float.parseFloat(string[1])
						, description));
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * read the user file
	 * input:	none
	 * output:	none
	 */
	public void readUser()
	{
		/*
		 * find the file that is being written from
		 */
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(userFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//read first line to check if admin
			String line = br.readLine();			
			while (line != null) 
			{
				if(line.equalsIgnoreCase("Admin") || line.equalsIgnoreCase("Normal"))
				{
					boolean admin = false;
					if(line.equalsIgnoreCase("Admin"))
						admin = true;
					//read the user
					String userName = br.readLine();
					//read the password
					String password = br.readLine();
					//get a new user
					User newUser = new User(userName, password, admin);
					//then the file reads the location name the user input
					//designate "." as when the user stops selecting location location
					while(!line.equalsIgnoreCase("."))
					{
						line = br.readLine();
						for(int i = 0; i < location.size(); i ++)
						{
							if(line.equalsIgnoreCase(location.get(i).getName()))
							{
								newUser.addLocation(location.get(i));
								break;
							}
						}
					}
					//add the user to the user linked list
					user.add(newUser);
				}
				//set the next line
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * get the user locations from the array and return all the locations
	 * input:	none
	 * output:	linked list of location the user entered
	 */
	public LinkedList<Waypoint> getUserLocations()
	{
		return user.get(currentUser).getLocation();
	}
	/*
	 * add user locations and then update the user file
	 * input:	String of user name
	 * 			String of password
	 * 			Boolean of admin
	 * output:	none
	 */
	public void addUser(String name, String password, boolean admin)
	{
		user.add(new User(name, password, admin));
		printToUserFile();
	}
	/*
	 * add new location to file and update the location file
	 * input:	double latitude	
	 * 			double longitude
	 * 			string name of location
	 * 			string description
	 * output:	true if the user add a location
	 *			false if the user cannot add a location
	 */
	public boolean addLocation(float latitude, float longitude ,String name,  String description)
	{
		if(login == false || !user.get(currentUser).getAdmin())
			return false;
		location.add(new Waypoint(name, latitude, longitude, description));
		printToLocationFile();
		return true;
	}
	/*
	 * add a new location to the user
	 * input:	string of the location being added
	 * output:	false of the user is 0
	 * 			true if the user adds a new location properly
	 */
	public boolean addUserLocation(String addLocation)
	{
		if(login == true)
			return false;
		int locationIndex = 0;
		//loops through the location to add the name
		for(int i = 0;i < location.size(); i++)
		{
			if(location.get(i).getName().equalsIgnoreCase(addLocation))
				locationIndex = i;
		}
		user.get(currentUser).addLocation(location.get(locationIndex));
		printToLocationFile();
		return true;
	}
	/*
	 * remove the user from the database
	 * input:	string of the user being removed
	 * output:	true if user exist and removed
	 * 			false otherwise
	 */
	public boolean removeUser(String name)
	{

		for(int i = 0; i < user.size(); i++)
		{
			String userName = user.get(i).getName();
			if(userName.equalsIgnoreCase(name) && (i + 1 != user.size()))
			{
				user.remove(i);
				printToUserFile();
				return true;
			}
			if(userName.equalsIgnoreCase(name))
			{
				user.removeLast();
				printToUserFile();
				return true;
			}
		}
		return false;
	}
	/*
	 * remove the location being called
	 * input:	name being removed
	 * output:	true if the location exist
	 * 			false otherwise
	 */
	public boolean removeLocation(String name)
	{
		if(login == false || !user.get(currentUser).getAdmin())
			for(int i = 0; i < location.size(); i++)
			{
				if(location.get(i).getName().equalsIgnoreCase(name))
				{
					location.remove(i);
					printToLocationFile();
					return true;
				}
			}
		return false;
	}
	/*
	 * remove the location from the user
	 * input:	String of removeLocation
	 * output:	true if there is a user logged in
	 * 			false if otherwise
	 */
	public boolean removeUserLocation(String removeLocation)
	{
		if(login == false)
			return false;
		user.get(currentUser).removeLocation(removeLocation);
		printToUserFile();
		return true;
	}
	/*
	 * edit the location
	 * input:	double latitude
	 * 			double longitude
	 * 			String name
	 * 			String description
	 * output:	false if the user cannot edit the location
	 * 			true otherwise
	 */
	public boolean editLocation(float latitude, float longitude, String name, String description)
	{
		if(!user.get(currentUser).getAdmin())
			return false;
		for(int i = 0; i < location.size(); i++)
		{
			if(name.equalsIgnoreCase(location.get(i).getName()))
			{
				location.get(i).setwDescription(description);
				location.get(i).setwLatitude(latitude);
				location.get(i).setwLongitude(longitude);
			}

		}
		return true;
	}
	public boolean isUserAdmin()
	{
		return new Boolean(userAdmin);
	}
	/*
	 * update the user file to be correct
	 */
	public void printToUserFile()
	{
		try {			 
			FileWriter fstream = new FileWriter(userFile);
			BufferedWriter bw = new BufferedWriter(fstream);
			for(int i = 0; i < user.size(); i++)
				bw.write(user.get(i).printFile());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * update the location file to be correct
	 */
	public void printToLocationFile()
	{
		try {			 
			FileWriter fstream = new FileWriter(locationFile);
			BufferedWriter bw = new BufferedWriter(fstream);
			for(int i = 0; i < location.size(); i++)
				bw.write(location.get(i).toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args)
	{
		DatabaseManager read = new DatabaseManager();
		String nextInput1 = input.nextLine();
		int nextInput = Integer.parseInt(nextInput1);
		String name = "";
		String password = "";
		String latitude = "";
		String longitude = "";
		String description = "";
		while(nextInput != 0)
		{
			switch(nextInput)
			{
			//test login
			case(1):
				name = input.nextLine();
				password = input.nextLine();
				read.login(name, password);
				break;
			
			//test add location
			case(2):
				longitude = input.nextLine(); 
				latitude = input.nextLine();
				name = input.nextLine();
				description = input.nextLine();
				read.addLocation(Float.parseFloat(longitude), Float.parseFloat(latitude), name, description);
				break;
			//test add user
			case(3):
				name = input.nextLine();
				password = input.nextLine();
				read.addUser(name, password, false);
			}
			nextInput1 = input.nextLine();
			nextInput = Integer.parseInt(nextInput1);
		}
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
	}
}
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
