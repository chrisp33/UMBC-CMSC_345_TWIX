package proj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class ReadText {
	LinkedList<Location> location = new LinkedList<Location>();
	LinkedList<User> user = new LinkedList<User>();
	private String userFile = "user.txt";
	private String locationFile = "location.txt";
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
			String line = br.readLine();
			while (line != null) {
				String description = br.readLine();
				String string[] = line.split(" ");
				String name = "";
				for(int i = 2; i < string.length; i++)
				{
					if(i + 1 != string.length)
						name = name + string[i] + " ";
					else
						name = name + string[i];
				}
				location.add(new Location(Double.parseDouble(string[0]), Double.parseDouble(string[1])
						, name, description));
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
	public void readText()
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(userFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String line = br.readLine();			
			while (line != null) 
			{
				if(line.equalsIgnoreCase("Admin") || line.equalsIgnoreCase("Normal"))
				{
					boolean admin = false;
					if(line.equalsIgnoreCase("Admin"))
						admin = true;
					String userName = br.readLine();
					String password = br.readLine();
					User newUser = new User(userName, password, admin);
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
					user.add(newUser);
				}
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

	public LinkedList<User> getUser()
	{
		return user;
	}
	public void addUser(String name, String password, boolean admin)
	{
		user.add(new User(name, password, admin));
	}
	public void addLocation(double longitude, double latitude, String name, String description)
	{
		location.add(new Location(longitude, latitude, name, description));
	}
	public void addUserLocation(String UserName, String addLocation)
	{
		int userIndex = 0;
		int locationIndex = 0;
		for(int i = 0;i < user.size(); i++)
		{
			if(user.get(i).getName().equalsIgnoreCase(UserName))
				userIndex = i;
		}
		for(int i = 0;i < location.size(); i++)
		{
			if(location.get(i).getName().equalsIgnoreCase(addLocation))
				locationIndex = i;
		}
		user.get(userIndex).addLocation(location.get(locationIndex));
	}
	public void removeUser(String name)
	{
		for(int i = 0; i < user.size(); i++)
		{
			String userName = user.get(i).getName();
			if(userName.equalsIgnoreCase(name))
				user.removeFirst();
			if(userName.equalsIgnoreCase(name))
				user.remove(i);
			if(userName.equalsIgnoreCase(name))
				user.removeLast();
		}
	}
	public void removeLocation(String name)
	{
		for(int i = 0; i < location.size(); i++)
		{
			if(location.get(i).getName().equalsIgnoreCase(name))
				location.remove(i);
		}
	}
	public void removeUserLocation(String userName, String removeLocation)
	{
		for(int i = 0; i < user.size(); i++)
		{
			if(user.get(i).getName().equalsIgnoreCase(userName))
				user.get(i).removeLocation(removeLocation);
		}
	}
	public void editLocation(double longitude, double latitude, String name, String description)
	{
		for(int i = 0; i < location.size(); i++)
		{
			if(name.equalsIgnoreCase(location.get(i).getName()))
			{
				location.get(i).setLatitude(latitude);
				location.get(i).setLongitude(longitude);
				location.get(i).setDescription(description);
			}

		}
	}
	public void printToUserFile()
	{
		try {			 
			FileWriter fstream = new FileWriter(userFile);

			// if file doesnt exists, then create it

			BufferedWriter bw = new BufferedWriter(fstream);
			for(int i = 0; i < user.size(); i++)
				bw.write(user.get(i).printFile());
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void printToLocationFile()
	{
		try {			 
			FileWriter fstream = new FileWriter(locationFile);

			// if file doesnt exists, then create it

			BufferedWriter bw = new BufferedWriter(fstream);
			for(int i = 0; i < location.size(); i++)
				bw.write(location.get(i).toString());
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args)
	{
		ReadText read = new ReadText();
		read.readLocation();
		//		System.out.println(read);
		read.readText();
		//		System.out.println(read);
		//read.addUser("Russ", "password2", false);
		//test to make sure remove location works
		//read.addUserLocation("Steven", "machu pichu");
		//read.removeUser("Russ");
		//read.addLocation(123.01, 97, "Adding location", "description of city");
		read.removeLocation("Adding location");
		//test to make sure it prints user file properly
		read.printToUserFile();
		//test to make sure it prints location file properly
		read.printToLocationFile();
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
