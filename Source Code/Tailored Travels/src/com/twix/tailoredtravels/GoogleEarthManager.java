/**
 * A class used in handling all calls to 
 * Google Earth and all KML manipulation.
 * 
 * @author Stephen Moore
 *
 */

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

public class GoogleEarthManager {

	public GoogleEarthManager() {
	}

	/**
	 * Transforms a given path into a series of connected KML points.
	 * 
	 * @param _path
	 *            The created path of Waypoints in LinkedList<Waypoint> format
	 * @return the generated KML
	 */
	public String Path2KML(GoogleEarthPath _path) {
		LinkedList<Waypoint> wPath = _path.getPath();
		String filename = wPath.getFirst().getName()
				+ Messages.getString("GoogleEarthManager.0") //$NON-NLS-1$
				+ wPath.getLast().getName()
				+ Messages.getString("GoogleEarthManager.1"); //$NON-NLS-1$
		final Kml kml = KmlFactory.createKml();
		final Document document = kml
				.createAndSetDocument()
				.withName(
						Messages.getString("GoogleEarthManager.2") + wPath.getFirst().getName() //$NON-NLS-1$
								+ Messages.getString("GoogleEarthManager.3") + wPath.getLast().getName()) //$NON-NLS-1$
				.withOpen(true);

		document.createAndAddDocument();

		final Placemark placemark = document.createAndAddPlacemark()
				.withName(Messages.getString("GoogleEarthManager.4")) //$NON-NLS-1$
				.withDescription(Messages.getString("GoogleEarthManager.5")); //$NON-NLS-1$
		final LineString path = placemark.createAndSetLineString()
				.withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
				.withTessellate(true);
		List<Coordinate> coord = path.createAndSetCoordinates();

		for (Waypoint w : wPath) {
			final Placemark _placemark = document.createAndAddPlacemark()
					.withName(w.getName()).withDescription(w.getDescription());
			List<Coordinate> _c = _placemark.createAndSetPoint()
					.createAndSetCoordinates();
			_c.add(new Coordinate(w.getLongitude(), w.getLatitude(), 0));
			coord.add(new Coordinate(w.getLongitude(), w.getLatitude(), 0));
		}

		try {
			kml.marshal(new File(filename));
			return Messages.getString("GoogleEarthManager.6") + filename; //$NON-NLS-1$
		} catch (FileNotFoundException e) {
			return Messages.getString("GoogleEarthManager.7"); //$NON-NLS-1$
		}
	}
}
