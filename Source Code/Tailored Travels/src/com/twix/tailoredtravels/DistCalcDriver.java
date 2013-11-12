package com.twix.tailoredtravels;

import java.util.ArrayList;

/**
 * Handle calculation of shortest distance between given
 * start and end points. 
 * 
 * @author Mariama Barr-Dallas & Michael Tang
 * @version 1.0
 */

public class DistCalcDriver {

	//Must know the arraylist <type> that will be recieved from
	//Google Earth Data.
	
	/**
	 * shortDistAlgorithm calculates the shortest route
	 * based on the given list and the start and end points
	 * returns an arraylist of coordinates sorted for shortest distance
	 * 
	 * @param list	list of parks that the user wants to go to
	 * @param destA 	the coordinate the user starts at
	 * @param destG		the coordinate the user ends at
	 * @return ArrayList<Waypoint> sorted arraylist of all waypoints
	 */
	public static ArrayList<Waypoint> shortDistAlgorithm(ArrayList<Waypoint> list, Waypoint destA, Waypoint destG)
	{
		
		ArrayList<Waypoint> path = new ArrayList<Waypoint>();
		Waypoint current = destA;
		Waypoint next = null;
		double smallest = Double.MAX_VALUE;
		double distance;
		path.add(current);
		
		for(int i=0; i<(list.size()-2); i++)
		{
			for(int j=0; j<list.size(); j++)
			{
				//determines the next nearest destination not searching previous targets or the end value 
				if(!path.contains(list.get(j)) && list.get(j) != destG)
				{
					distance = current.distance(list.get(j));
					if(smallest > distance)
					{
						smallest = distance;
						next = list.get(j);
					}
				}
			}
			current = next;
			path.add(current);
			smallest = Double.MAX_VALUE;
		}
		path.add(destG);		
		return path;
	}
	
	/**
	 * totalDistance calculates the total distance covered by this path
	 * 
	 * @param endlist	A list containing the sorted path
	 * @return double
	 */
	public static double totalDistance(ArrayList<Waypoint> endlist)
	{
		double dist = 0;
		for(int i=0; i<endlist.size()-1; i++)
		{
			dist += endlist.get(i).distance(endlist.get(i+1));
		}
		return dist;
	}
	
	
}
