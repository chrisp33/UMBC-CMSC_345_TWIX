package com.twix.tailoredtravels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

	/**@author - Herman Kontcho
	 * Generates all the permutations of paths
	 * 
	 * @param original
	 * @return
	 */
	public static List<List<Waypoint>> generatePermP(ArrayList<Waypoint> original) {

		if (original.size() == 0) { 
			List<List<Waypoint>> result = new ArrayList<List<Waypoint>>();
			result.add(new ArrayList<Waypoint>());
			return result;
		}
		Waypoint firstElement = original.remove(0);
		List<List<Waypoint>> returnValue = new ArrayList<List<Waypoint>>();
		List<List<Waypoint>> permutations = generatePermP(original);


		for (List<Waypoint> smallerPermutated : permutations) {
			for (int index=0; index <= smallerPermutated.size(); index++) {
				ArrayList<Waypoint> temp = new ArrayList<Waypoint>(smallerPermutated);
				temp.add(index, firstElement);
				returnValue.add(temp);
			}
		}
		return returnValue;
	}

	/**
	 * @author - Herman Kontcho
	 * Calculates the shortest path
	 * 
	 * @param list
	 * @return
	 */
	//	public static ArrayList<Waypoint> shortDistAlgorithm(ArrayList<Waypoint> list){
	//		if (list == null || list.size() == 0){
	//			return null;
	//		}
	//
	//		//remove the start and ending Waypoint
	//		Waypoint start = list.remove(0);
	//		Waypoint end = list.remove(list.size() - 1);
	//
	//		//get all possible permutation of the remaining list
	//		List<List<Waypoint>> temp = generatePermP(list);
	//
	//		//get the first one
	//		List<Waypoint> path1 = temp.get(0);
	//		path1.add(end);
	//		path1.add(0, start);
	//
	//		//get the current distance
	//		double curr_dis = dist(path1);
	//
	//		for (int i = 1; i < temp.size(); i++){
	//
	//			List<Waypoint> curr = temp.get(i);
	//			curr.add(end);
	//			curr.add(0, start);
	//
	//			double d = dist(curr);
	//
	//			if (curr_dis > d){
	//				path1 = curr;
	//				curr_dis = d;
	//			}
	//		}
	//		ArrayList<Waypoint> lis = new ArrayList<Waypoint>();
	//
	//		for(int i = 0; i < path1.size(); i++)
	//			lis.add(path1.get(i));
	//		return lis;
	//	}

	/**
	 * @author - Herman Kontcho
	 * @param list
	 * @return
	 */
	public static ArrayList<Waypoint> shortestPath(ArrayList<Waypoint> list){
		if (list == null || list.size() == 0){
			return null;
		}

		//Remove the starting Waypoint from the list and get the ending Waypoint
		Waypoint start = list.get(0);
		Waypoint end = list.get(list.size() - 1);


		//Set the curr Waypoint to be the starting Waypoint
		Waypoint curr = list.remove(0);

		//create a temp list that will be holding the path along the iteration
		ArrayList<Waypoint> path = new ArrayList<Waypoint>();

		//adding the starting Waypoint in the path
		path.add(start);

		//curretnce distance
		double curr_dist = 0;


		//loop till the curr Waypoint is the ending Waypoint
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

			//At this Waypoint closest_index will be the index of the closest Waypoint from 
			//curr so we need to remove it from the list and update the distance 
			//along with the curr_Waypoint
			curr_dist += curr.distance(list.get(closest_index));
			curr = list.remove(closest_index);
			path.add(curr);
		}
		return path;	
	}

	/**
	 * Testing algorithm
	 * @param args
	 */
	public static void main(String[] args) {


		Waypoint p1 = new Waypoint(30,30);
		Waypoint p2 = new Waypoint(200,45);
		Waypoint p3 = new Waypoint(34,55);
		Waypoint p4 = new Waypoint(111,676);
		Waypoint p5 = new Waypoint(1,34);
		Waypoint p6 = new Waypoint(11,76);
		Waypoint p7 = new Waypoint(1,1);
		Waypoint p8 = new Waypoint(18,46);
		Waypoint p9 = new Waypoint(70,44);
		Waypoint p10 = new Waypoint(96,676);
		Waypoint p11 = new Waypoint(3,4);
		Waypoint p12 = new Waypoint(11,60);
		Waypoint p13 = new Waypoint(6,16);

		ArrayList<Waypoint> list = new ArrayList<Waypoint>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		list.add(p6);
		list.add(p7);
		list.add(p8);
		list.add(p9);
		list.add(p10);
		list.add(p11);
		list.add(p12);
		list.add(p13);

		ArrayList<Waypoint> path = shortDistAlgorithm(list);
		//		ArrayList<Waypoint> path = searchCity(list);
		for (Waypoint p: path){
			System.out.println(p);
		}
		System.out.println("And the distance is " + dist(path));

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
	private static ArrayList<Waypoint> shortestWaypoint = new ArrayList<Waypoint>();
	private static double distance = 0;
	/*
	 * ap is the cities not used
	 */
	public static ArrayList<Waypoint> shortDistAlgorithm(ArrayList<Waypoint> cities)
	{
		distance = 0;
		shortestWaypoint = new ArrayList<Waypoint>();
		ArrayList<Waypoint> path = new ArrayList<Waypoint>();
		Waypoint beginning = cities.remove(0);
		Waypoint end = cities.remove(cities.size() - 1);
		ArrayList<Waypoint> shortestRoute = new ArrayList<Waypoint>();
		searchCity(path, cities, beginning, end, shortestRoute);
		return shortestWaypoint;
	}

	private static void searchCity(ArrayList<Waypoint> path,List<Waypoint> waypointNotSelected,
			Waypoint beginning, Waypoint end, ArrayList<Waypoint> shortestRoute)
	{
		if(waypointNotSelected.size() == 0)
		{
			path.add(0, beginning);
			path.add(end);
			double newDistance = totalDistance(path);
			if(distance == 0 || newDistance < distance)
			{
				distance = newDistance;
				shortestWaypoint = (ArrayList<Waypoint>) path.clone();
			}			
			path.remove(0);
			path.remove(path.size()-1);
		}
		for(int i = 0; i < waypointNotSelected.size(); i++)
		{
			if(path.size() != 0 &&totalDistance(path) > distance && distance != 0)
			{
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
