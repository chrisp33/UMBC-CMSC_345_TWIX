package com.twix.tailoredtravels;

import java.util.LinkedList;

public class User {
	private LinkedList<Location> location = new LinkedList<Location>();
	private String name;
	private String password;
	private boolean admin;
	
	/**
	 * constructor for making a user
	 * input:	String name of the person
	 * 			String password of the person
	 * 			boolean whether he or she is a admin
	 */
	public User(String name, String password, boolean admin)
	{
		this.name = name;
		this.password = password;
		this.admin = admin;
	}
	
	/**
	 * return the locatin the user has selected
	 * input:	none
	 * output:	linkedlist of Location objects
	 */
	public LinkedList<Location> getLocation()
	{
		return location;
	}
	
	/**
	 * checks if the admin is true
	 */
	public boolean getAdmin()
	{
		return admin;
	}
	
	/**
	 * add a location to the user
	 * input:	location object of what is being added
	 * output:	none
	 */
	public void addLocation(Location newLocation)
	{
		location.add(newLocation);
	}
	
	/**
	 * removing a location from the user
	 * input:	string of the location being removed
	 * output:	none
	 */
	public void removeLocation(String removeLocation)
	{
		for(int i = 0; i < location.size(); i++)
		{
			String name = location.get(i).getName();
			
			if(name.equalsIgnoreCase(removeLocation))
				location.remove(i);
			if(name.equalsIgnoreCase(removeLocation))
				location.removeLast();
		}
	}
	
	/**
	 * checks if the person is log in
	 * input:	String of the name
	 * 			String of the password
	 * output:	true if it is the user
	 * 			false otherwise
	 */
	public boolean login(String name, String password)
	{
		if(this.name.equalsIgnoreCase(name) && this.password.equalsIgnoreCase(password))
			return true;
		return false;
	}
	
	/**
	 * return the name
	 */
	public String getName()
	{
		return new String(name);
	}
	
	/**
	 * return a linked list of locations
	 */
	public LinkedList<Location> getLocations()
	{
		return location;
	}
	
	public String toString()
	{
		String string = "";
		string = string + "name: " + name + "\n" +  "password: " + password +"\n";
		for(int i = 0; i < location.size(); i++)
		{
			string = string + location.get(i).toString();
		}
		return string;
	}
	
	/**
	 * print the user's information to a file
	 * input:	none
	 * output:	string of what is being printed
	 */
	public String printFile()
	{
		String string = "";
		if(admin == true)
			string = "Admin\n";
		else
			string = "Normal\n";
		string = string + name + "\n" + password + "\n";
		for(int i = 0; i < location.size(); i++)
			string = string + location.get(i).getName() +"\n";
		string = string +".\n";
		return string;
	}
}
