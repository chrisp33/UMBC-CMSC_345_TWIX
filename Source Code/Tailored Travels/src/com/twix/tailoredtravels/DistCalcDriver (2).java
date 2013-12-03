package com.twix.tailoredtravels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Handle calculation of shortest distance between given
 * start and end Waypoints. 
 * 
 * @author Mariama Barr-Dallas & Michael Tang
 * @version 2.0
 */

public class DistCalcDriver {
	/**Calculates the distance of the given path
	 * 
	 * @param List<Waypoint> ap
	 * @return
	 */
	public static double dist(List<Waypoint> ap){
		double distance = 0;
		Waypoint  start = ap.get(0);
		int count = 1;
		while(count < ap.size()){
			distance += start.distance(ap.get(count));
			if (ap.isEmpty() == false){
				start = ap.get(count++);
			}
		}

		return distance;
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
	public static double totalDistance(List<Waypoint> endlist)
	{
		double dist = 0;
		for(int i=0; i<endlist.size()-1; i++)
		{
			dist += endlist.get(i).distance(endlist.get(i+1));
		}
		return dist;
	}
	


	public static ArrayList<Waypoint> shortDistAlgorithm(final ArrayList<Waypoint> wpList)
	{

		Random rGen = new Random();
		
		//Randomly swap waypoints leaving the first and last in their place.
			//If the resulting swap creates a shorter path, keep it. Otherwise, throw it out. 
			
		
	
		ArrayList<Waypoint> oldList = new ArrayList<Waypoint>();
		oldList.addAll(wpList); //Copy wpList into oldDist so we can shuffle newList without losing its pre-shuffled order
		ArrayList<Waypoint> newList = new ArrayList<Waypoint>();

		
		double oldDist = totalDistance(oldList); //Distance of the original unoptimized path
		double newDist = 0;
		
		int i = 0;
		//while(i < 2147483647){
		while(i < 1000000){
			
			newList.addAll(oldList);
			
			int rStr = rGen.nextInt(wpList.size()-3) + 1; 			//Random index from arraylist, leaving first and last intact, and leaving at least 2 elements at the tail
																		   //Example, on list size 20, will give index between 1 and 17 inclusive, allowing swap of idx 17 and 18 and preservation of idx 0 and 19 (start/end waypoints)
			int rEnd = rStr + rGen.nextInt(wpList.size()-rStr-2)+1;	//Must be between rStr and index size-2 in order to preserve idx size-1 (ex. 19) but allow at least one swap element (ex. idx 18)'
	
			Collections.shuffle(newList.subList(rStr, rEnd));
			newDist = totalDistance(newList);

			if(newDist < oldDist) //The new shortestPath is more efficient than oldList
			{
				oldList.clear();  //Clear out oldList
				oldList.addAll(newList); //Replace oldList with the new shortestpath
				oldDist = newDist;
			}
			
			newList.clear();
			i++;
				
		}
		return oldList;
	}

}
