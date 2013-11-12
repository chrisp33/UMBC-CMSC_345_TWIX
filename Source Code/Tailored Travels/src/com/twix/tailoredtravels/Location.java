package com.twix.tailoredtravels;

public class Location {
	private String name;
	private double longitude;
	private double latitude;
	private String description;
	public Location(double longitude, double latitude, String name, String description)
	{
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.description = description;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public double getLatitude() 
	{
		return latitude;
	}
	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}
	public String toString()
	{
		return new String(longitude + " " + latitude + " " + name + "\n" + description +"\n");
	}
}
