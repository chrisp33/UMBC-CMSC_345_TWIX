package com.twix.test;

import java.util.ArrayList;

import com.twix.tailoredtravels.DistCalcDriver;
import com.twix.tailoredtravels.GoogleEarthManager;
import com.twix.tailoredtravels.GoogleEarthPath;
import com.twix.tailoredtravels.Waypoint;

public class UnitTestKML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Waypoint wayA = new Waypoint("Destination A",282,269,"descA");
		Waypoint wayB = new Waypoint("Destination B",366,331,"descB");
		Waypoint wayC = new Waypoint("Destination C",189,455,"descC");
		Waypoint wayD = new Waypoint("Destination D",56,34,"descD");
		Waypoint wayE = new Waypoint("Destination E",375,238,"descE");
		Waypoint wayF = new Waypoint("Destination F",206,282,"descF");
		Waypoint wayG = new Waypoint("Destination G",1,418,"descG");
		
		ArrayList<Waypoint> list = new ArrayList<Waypoint>();
		
		list.add(wayA);
		list.add(wayB);
		list.add(wayC);
		list.add(wayD);
		list.add(wayE);
		list.add(wayF);
		list.add(wayG);
		

		ArrayList<Waypoint> endList = new ArrayList<Waypoint>();
		endList = DistCalcDriver.shortDistAlgorithm(list);
		
		GoogleEarthPath GEP = new GoogleEarthPath(endList);
		GoogleEarthManager GEM = new GoogleEarthManager();
		
		GEM.Path2KML(GEP);

	}

}
