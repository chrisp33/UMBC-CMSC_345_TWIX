package com.twix.tailoredtravels;

/**
 * A class that acts as the template for KML objects correlating to locations.
 * 
 * @author Stephen Moore & Justin Tavares with distance function by Mariama
 *         Barr-Dallas, Michael Tang
 * 
 */
public class Waypoint {

	private String wName; // Waypoint name
	private String wDescription; // Waypoint description
	private float wLongitude;
	private float wLatitude;

	/**
	 * Float constructor, creates waypoint with name, latitude, longitude,
	 * description given in input
	 * 
	 * @param _name
	 *            name of waypoint
	 * @param _lat
	 *            latitude of waypoint
	 * @param _long
	 *            longitude of waypoint
	 * @param _desc
	 *            description of waypoint
	 */
	public Waypoint(String _name, float _lat, float _long, String _desc) {
		this.wLatitude = _lat;
		this.wLongitude = _long;
		this.wDescription = _desc;
		this.wName = _name;
	}

	/**
	 * No name/desc constructor for Waypoint, creates a nameless waypoint with
	 * given latitude/longitude
	 * 
	 * @param _lat
	 *            latitude of waypoint
	 * @param _long
	 *            longitude of waypoint
	 */
	public Waypoint(float _lat, float _long) {

		this.wLongitude = _long;
		this.wLatitude = _lat;

	}

	/**
	 * Noarg constructor
	 */
	public Waypoint() {
	}

	// Accessors
	/**
	 * getLongitude
	 * 
	 * @return wLongitude
	 */
	public float getLongitude() {
		return this.wLongitude;
	}

	/**
	 * getLatitude
	 * 
	 * @return wLatitude
	 */
	public float getLatitude() {
		return wLatitude;
	}

	/**
	 * getName
	 * 
	 * @return wName
	 */
	public String getName() {
		return this.wName;
	}

	/**
	 * getDescription
	 * 
	 * @return wDescription
	 */
	public String getDescription() {
		return this.wDescription;
	}

	// Mutators for local variables
	/**
	 * setwName
	 * 
	 * @param wName
	 *            new name
	 */
	public void setwName(String wName) {
		this.wName = wName;
	}

	/**
	 * setwDescription
	 * 
	 * @param wDescription
	 *            new description
	 */
	public void setwDescription(String wDescription) {
		this.wDescription = wDescription;
	}

	/**
	 * setwLongitude
	 * 
	 * @param wLongitude
	 *            new longitude
	 */
	public void setwLongitude(float wLongitude) {
		this.wLongitude = wLongitude;
	}

	/**
	 * setwLatitude
	 * 
	 * @param wLatitude
	 *            new latitude
	 */
	public void setwLatitude(float wLatitude) {
		this.wLatitude = wLatitude;
	}

	/**
	 * Calculates the distance between 2 coordinates in miles Equation for
	 * calculation: dlon = lon2 - lon1 dlat = lat2 - lat1 a = (sin(dlat/2))^2 +
	 * cos(lat1) * cos(lat2) * (sin(dlon/2))^2 c = 2 * atan2( sqrt(a), sqrt(1-a)
	 * ) d = R * c (where R is the radius of the Earth)
	 * 
	 * @param other
	 *            other waypoint for distance calculation
	 * 
	 * @return double containing distance between 2 coordinates
	 * 
	 */
	public double distance(Waypoint other) {
		double dlon = Math
				.toRadians(other.getLongitude() - this.getLongitude());
		double dlat = Math.toRadians(other.getLatitude() - this.getLatitude());
		double a = Math.pow(Math.sin(dlat / 2), 2)
				+ Math.cos(Math.toRadians(this.getLatitude()))
				* Math.cos(Math.toRadians(other.getLatitude()))
				* Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return 3961 * c;
	}

	/**
	 * Determines if this waypoint is the same as a given waypoint
	 * 
	 * @param other
	 *            other waypoint for distance calculation
	 * @return true if waypoints are the same, false otherwise
	 */
	public boolean equals(Waypoint other) {

		if (other == null) {
			return false;
		}

		return (other.getLatitude() == this.getLatitude() && other
				.getLongitude() == this.getLongitude());
	}

	/**
	 * toString - returns wName, wLatitude:wLongitude, wDescription
	 */
	@Override
	public String toString() {
		return new String(
				this.wLongitude
						+ Messages.getString("Waypoint.0") + this.wLongitude + Messages.getString("Waypoint.1") //$NON-NLS-1$ //$NON-NLS-2$
						+ this.wName
						+ Messages.getString("Waypoint.2") + this.wDescription + Messages.getString("Waypoint.3")); //$NON-NLS-1$ //$NON-NLS-2$

	}

	public String printDatabase() {
		return toString();
	}
}
