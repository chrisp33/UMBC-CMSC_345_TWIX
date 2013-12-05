package com.twix.tailoredtravels;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle calculation of shortest distance between given start and end
 * Waypoints.
 * 
 * @author Mariama Barr-Dallas & Michael Tang with contributions from Keith
 *         Cheng
 * @version 2.0
 */

public class DistCalcDriver {
	/**
	 * Calculates the distance of the given path
	 * 
	 * @param List
	 *            <Waypoint> ap
	 * @return
	 */
	public static double dist(List<Waypoint> ap) {
		double distance = 0;
		Waypoint start = ap.get(0);
		int count = 1;
		while (count < ap.size()) {
			distance += start.distance(ap.get(count));
			if (ap.isEmpty() == false) {
				start = ap.get(count++);
			}
		}

		return distance;
	}

	/**
	 * @author - Herman Kontcho
	 * 
	 * @param list
	 * @return
	 */
	public static ArrayList<Waypoint> shortestPath(ArrayList<Waypoint> list) {
		if (list == null || list.size() == 0) {
			return null;
		}

		// Remove the starting Waypoint from the list and get the ending
		// Waypoint
		Waypoint start = list.get(0);
		Waypoint end = list.get(list.size() - 1);

		// Set the curr Waypoint to be the starting Waypoint
		Waypoint curr = list.remove(0);

		// create a temp list that will be holding the path along the iteration
		ArrayList<Waypoint> path = new ArrayList<Waypoint>();

		// adding the starting Waypoint in the path
		path.add(start);

		// curretnce distance
		double curr_dist = 0;

		// loop till the curr Waypoint is the ending Waypoint
		while (!curr.equals(end)) {
			double curr_temp_dist = curr_dist + curr.distance(list.get(0));
			int closest_index = 0;

			for (int i = 0; i < list.size(); i++) {
				double temp = curr_dist + curr.distance(list.get(i));

				if (temp < curr_temp_dist) {
					curr_temp_dist = temp;
					closest_index = i;
				}
			}

			// At this Waypoint closest_index will be the index of the closest
			// Waypoint from
			// curr so we need to remove it from the list and update the
			// distance
			// along with the curr_Waypoint
			curr_dist += curr.distance(list.get(closest_index));
			curr = list.remove(closest_index);
			path.add(curr);
		}
		return path;
	}

	/**
	 * totalDistance calculates the total distance covered by this path
	 * 
	 * @param endlist
	 *            A list containing the sorted path
	 * @return double
	 */

	public static double totalDistance(List<Waypoint> endlist) {
		double dist = 0;
		for (int i = 0; i < endlist.size() - 1; i++) {
			dist += endlist.get(i).distance(endlist.get(i + 1));
		}
		return dist;
	}

	private static ArrayList<Waypoint> shortestWaypoint = new ArrayList<Waypoint>();
	private static double distance = 0;

	/*
	 * ap is the cities not used
	 */
	public static ArrayList<Waypoint> shortDistAlgorithm(
			ArrayList<Waypoint> cities) {
		distance = 0;
		shortestWaypoint = new ArrayList<Waypoint>();
		ArrayList<Waypoint> path = new ArrayList<Waypoint>();
		Waypoint beginning = cities.remove(0);
		Waypoint end = cities.remove(cities.size() - 1);
		ArrayList<Waypoint> shortestRoute = new ArrayList<Waypoint>();
		searchCity(path, cities, beginning, end, shortestRoute);
		return shortestWaypoint;
	}

	@SuppressWarnings("unchecked")
	private static void searchCity(ArrayList<Waypoint> path,
			List<Waypoint> waypointNotSelected, Waypoint beginning,
			Waypoint end, ArrayList<Waypoint> shortestRoute) {
		if (waypointNotSelected.size() == 0) {
			path.add(0, beginning);
			path.add(end);
			double newDistance = totalDistance(path);
			if (distance == 0 || newDistance < distance) {
				distance = newDistance;
				shortestWaypoint = (ArrayList<Waypoint>) path.clone();
			}
			path.remove(0);
			path.remove(path.size() - 1);
		}
		for (int i = 0; i < waypointNotSelected.size(); i++) {
			if (path.size() != 0 && totalDistance(path) > distance
					&& distance != 0) {
				break;
			}
			Waypoint next = waypointNotSelected.remove(i);
			path.add(next);

			searchCity(path, waypointNotSelected, beginning, end, shortestRoute);

			path.remove(next);
			waypointNotSelected.add(i, next);
		}
	}
}
