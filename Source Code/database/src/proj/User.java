package proj;

import java.util.LinkedList;

public class User {
	private LinkedList<Location> location = new LinkedList<Location>();
	private String name;
	private String password;
	private boolean admin;
	public User(String name, String password, boolean admin)
	{
		this.name = name;
		this.password = password;
		this.admin = admin;
	}
	public LinkedList<Location> getLocation()
	{
		return location;
	}
	public boolean getAdmin()
	{
		return admin;
	}
	public void addLocation(Location newLocation)
	{
		location.add(newLocation);
	}
	public void removeLocation(String removeLocation)
	{
		for(int i = 0; i < location.size(); i++)
		{
			String name = location.get(i).getName();
			
			if(name.equalsIgnoreCase(removeLocation))
				location.remove(i);
			if(name.equalsIgnoreCase(removeLocation))
				location.removeFirst();
			if(name.equalsIgnoreCase(removeLocation))
				location.removeLast();
		}
	}
	public boolean correct(String name, String password)
	{
		if(this.name.equalsIgnoreCase(name) && this.password.equalsIgnoreCase(password))
			return true;
		return false;
	}
	public String getName()
	{
		return new String(name);
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
