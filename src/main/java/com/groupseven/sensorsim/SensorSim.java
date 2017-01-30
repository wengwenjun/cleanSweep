package com.groupseven.sensorsim;

import java.awt.Point;

import com.groupseven.cleansweeplib.*;

public class SensorSim {
	//This class provides read-only access to a Room object (e.g. generated from a file)
	//In real use, this would provide read-only access to actual sensor data from the real world
	private Room r;
	
	private Point position;
	

	public SensorSim(Room r){
		this.r=r;
		this.position=r.getStartingPosition();
	}
	
	public void move(Point pos){
		this.position=pos;
	}
	
	public Point getStartingPosition(){return r.getStartingPosition();}
	
	public String toString(){return r.toString();}
	
	public int[] wallsSurrounding(){return r.wallsSurrounding(position);}
	
	public void roomStep(){r.roomStep();}
	
	public boolean hasDirtHere(){return r.hasDirtAt(position);}
	
	public void clean(){r.clean(position);} 
	
	public int floorIsObstacle(Point p){return r.floorIsObstacle(p);}
	
	public int getWidth(){return r.getWidth();}
	public int getHeight(){return r.getHeight();}
	
	public boolean chargingStationExist(Point p){return r.chargingStationExist(p);}

	public int getFloorTypeAt(Point p) {return r.getFloorTypeAt(p);}
	
}
