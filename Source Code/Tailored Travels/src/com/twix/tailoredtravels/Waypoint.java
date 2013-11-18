package com.twix.tailoredtravels;

/**
 * A class that acts as the template for KML 
 * objects correlating to locations.
 * 
 * @author Stephen Moore
 *
 */
public class Waypoint {

	private String wName;
	private String wDescription;
	
	public void setwName(String wName) {
		this.wName = wName;
	}

	public void setwDescription(String wDescription) {
		this.wDescription = wDescription;
	}

	public void setwLongitude(float wLongitude) {
		this.wLongitude = wLongitude;
	}

	public void setwLatitude(float wLatitude) {
		this.wLatitude = wLatitude;
	}
	private float wLongitude;
	private float wLatitude;
	
	public Waypoint(String _name, float _lat, float _long, String _desc)
	{
		this.wLatitude = _lat;
		this.wLongitude = _long;
		this.wDescription = _desc;
		this.wName = _name;
	}
	
	/**
	 * Noarg constructor
	 */
	public Waypoint()
	{
		
	}
	
	//Accessors (comments added for automated documentation)-------------
	/**
	 * getLongitude 
	 * @return wLongitude
	 */
	public float getLongitude()
	{
		return this.wLongitude;
	}
	
	/**
	 * getLatitude
	 * @return wLatitude
	 */
	public float getLatitude()
	{
		return wLatitude;
	}
	
	/**
	 * getName
	 * @return wName
	 */
	public String getName()
	{
		return this.wName;
	}
	
	/**
	 * getDescription
	 * @return wDescription
	 */
	public String getDescription()
	{
		return this.wDescription;
	}
	
	/**
	 * @category function
	 * @param other
	 * @return double containing distance between 2 coordinates
	 * 
	 * This function calculates the distance between 2 coordinates in miles
	 * dlon = lon2 - lon1
	 * dlat = lat2 - lat1
	 * a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2
	 * c = 2 * atan2( sqrt(a), sqrt(1-a) )
	 * d = R * c (where R is the radius of the Earth) 
	 */
	public double distance(Waypoint other)
	{
		double dlon = Math.toRadians(other.getLongitude() - this.getLongitude());
		double dlat = Math.toRadians(other.getLatitude() - this.getLatitude());
		double a = Math.pow(Math.sin(dlat/2),2) +  Math.cos(Math.toRadians(this.getLatitude())) *  Math.cos(Math.toRadians(other.getLatitude())) *  Math.pow(Math.sin(dlon/2),2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return 3961 * c;
	}
	
	
	/**
	 * toString - returns wName, wLatitude:wLongitude, wDescription
	 */
	public String toString()
	{
		return this.wName + ", " + this.wLatitude + ":" + 
				this.wLongitude + ", " + this.wDescription;
	}
	public String printDatabase()
	{
		return new String(this.wLongitude + " " + this.wLongitude + " " + this.wName + "\n" + this.wDescription + "\n");
	}
}
