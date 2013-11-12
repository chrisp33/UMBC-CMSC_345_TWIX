package com.twix.tailoredtravels;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * A class used in handling all calls to 
 * Google Earth and all KML manipulation.
 * 
 * @author Stephen Moore
 *
 */
public class GoogleEarthManager {

	public GoogleEarthManager()
	{
		
		
		
	}
	
	/**
	 * Transforms a given path into a series of connected KML points.
	 * 
	 * @param _path The created path of Waypoints in LinkedList<Waypoint> format
	 * @return the generated KML
	 */
	public void Path2KML(GoogleEarthPath _path)
	{
		Kml output = new Kml();
		
		LinkedList<Waypoint> wPath = _path.getPath();
		for(Waypoint w : wPath)
		{
			output.createAndSetPlacemark().withName(w.getName())
			.withOpen(true).createAndSetPoint()
			.addToCoordinates(w.getLatitude(), w.getLongitude());
		}
		
		try {
			output.marshal(new File("TEST_KML_OUTPUT.kml"));
		} catch (FileNotFoundException e) {
			// output.marshal creates the file TEST_KML_OUTPUT.kml.
			e.printStackTrace();
		}
		
		
		
	}
}
