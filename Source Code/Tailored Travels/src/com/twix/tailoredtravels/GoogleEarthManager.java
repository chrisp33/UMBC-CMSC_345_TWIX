package com.twix.tailoredtravels;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;

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
		final Kml kml = KmlFactory.createKml();
		final Document document = kml.createAndSetDocument()
		.withName("Test2.kml").withOpen(true);

		document.createAndAddDocument();

		final Placemark placemark = document.createAndAddPlacemark()
				.withName("Your Tailored Travel!")
				.withDescription("The shortest path between all known points.");
		final LineString path = placemark.createAndSetLineString()
				.withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
				.withTessellate(true);
		List<Coordinate> coord = path.createAndSetCoordinates();
		
		LinkedList<Waypoint> wPath = _path.getPath();
		for(Waypoint w : wPath) 
		{
			final Placemark _placemark = document.createAndAddPlacemark()
					.withName(w.getName())
					.withDescription(w.getDescription());
			List<Coordinate> _c = _placemark.createAndSetPoint().createAndSetCoordinates();
			_c.add(new Coordinate(w.getLongitude(),w.getLatitude(),0));
			coord.add(new Coordinate(w.getLongitude(),w.getLatitude(),0));
		}
		
		try {
			kml.marshal(new File("TEST_KML_OUTPUT.kml"));
		} catch (FileNotFoundException e) {
			// output.marshal creates the file TEST_KML_OUTPUT.kml.
			e.printStackTrace();
		}
	}
}
