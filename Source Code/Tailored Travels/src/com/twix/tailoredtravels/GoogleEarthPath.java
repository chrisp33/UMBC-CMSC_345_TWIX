package com.twix.tailoredtravels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * A class used to maintain an ordered list of 
 * Waypoints to send to Google Earth.
 * 
 * @author Stephen Moore
 *
 */
public class GoogleEarthPath {

	/**
	 * LinkedList associated with the path of Waypoints.
	 */
	private LinkedList<Waypoint> wPath = new LinkedList<Waypoint>();
	
	/**
	 * Initiates a new Google Earth path to be translated into KML.
	 * 
	 * @param _start Thw beginning Waypoint
	 * @param _end The final Waypoint
	 */
	public GoogleEarthPath(Waypoint _start, Waypoint _end)
	{
		this.wPath.add(_start);
		this.wPath.add(_end);
	}
	
	/**
	 * Initiates a new Google Earth path to be translated into KML
	 * given a sorted ArrayList<Waypoint> as returned by DistCalcDriver
	 * 
	 * preconditions: sortedList is actually sorted to be a shortest path
	 *  list of all waypoints
	 * 
	 * @param _start Thw beginning Waypoint
	 * @param _end The final Waypoint
	 */
	public GoogleEarthPath(ArrayList<Waypoint> sortedList)
	{
		//Is this really the best way to handle the list
		for(Waypoint w : sortedList)
		{
			wPath.add(w);
		}
		
	}
	
	
	/**
	 * Adds a new Waypoint to the path, immediately before the end.
	 * 
	 * @param _new The Waypoint to add
	 */
	public void addWaypoint(Waypoint _new)
	{
		Waypoint _temp = this.wPath.pollLast();
		this.wPath.add(_new);
		this.wPath.add(_temp);
	}
	
	
	/**
	 * getPath returns the linkedList of waypoints for use
	 * in creating KML file
	 * 
	 * @return wPath
	 */
	public LinkedList<Waypoint> getPath()
	{
		//create deep copy of wPath
		LinkedList<Waypoint> rtnPath = new LinkedList<Waypoint>();
		
		for(Waypoint w : wPath)
		{
			rtnPath.add(w);
		}
		
		return rtnPath;
		
	}
}
