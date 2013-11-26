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
	
	/**@author Herman Kontcho
	 * 
	 * dist calculates the distance of the path
	 *@param ArrayList<Waypoint> list
	 *@return distance 
	 */
	public static double dist(ArrayList<Waypoint> list){
		double distance = 0;
		Point  start = list.remove(0);
		
		while(!list.isEmpty()){
			distance += start.distance(list.get(0));
			if (list.isEmpty() == false){
				start = list.remove(0);
			}
		}
		
		return distance;
	}
	
	/**@author Herman Kontcho
	 * 
	 * shortDistAlgorithm calculates the shortest route
	 * based on the given list and the start and end points
	 * returns an arraylist of coordinates sorted for shortest distance
	 * list should come with the start point as the first index, and the last index as the end point
	 * 
	 * @param list	list of parks that the user wants to go to
	 * @return ArrayList<Waypoint> sorted arraylist of all waypoints
	 */
	public static ArrayList<Waypoint> shortDistAlgorithm(ArrayList<Waypoint> list){
		if (list == null || list.size() == 0){
			return null;
		}
		
		//Remove the starting point from the list and get the ending point
		Waypoint start = list.get(0);
		Waypoint end = list.get(list.size() - 1);
		
		
		//Set the curr point to be the starting point
		Waypoint curr = list.remove(0);
		
		//create a temp list that will be holding the path along the iteration
		ArrayList<Waypoint> path = new ArrayList<Waypoint>();
		
		//adding the starting point in the path
		path.add(start);
		
		//curretnce distance
		double curr_dist = 0;
		
		
		//loop till the curr point is the ending point
		while (!curr.equals(end)){
			double curr_temp_dist = curr_dist + curr.distance(list.get(0));
			int closest_index = 0;
			
			
			for (int i = 0; i < list.size(); i++){
				double temp  = curr_dist + curr.distance(list.get(i));
				
				if (temp < curr_temp_dist){
					curr_temp_dist = temp;
					closest_index = i;
				}
			}
			
			//At this point closest_index will be the index of the closest point from 
			//curr so we need to remove it from the list and update the distance 
			//along with the curr_point
			curr_dist += curr.distance(list.get(closest_index));
			curr = list.remove(closest_index);
			path.add(curr);
		}
		return path;	
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Waypoint p1 = new Waypoint(30,30);
		Waypoint p2 = new Waypoint(200,45);
		Waypoint p3 = new Waypoint(34,55);
		Waypoint p4 = new Waypoint(111,676);
		Waypoint p5 = new Waypoint(1,34);
		
		ArrayList<Waypoint> list = new ArrayList<Waypoint>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		
		ArrayList<Waypoint> path = shortDistAlgorithm(list);
		for (Waypoint p: path){
			System.out.println(p);
		}
		System.out.println("And the distance is " + dist(path));
	
	}
	/**
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
	**/
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
