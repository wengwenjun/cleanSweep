package com.groupseven.cleansweeplib;

import java.util.List;
import java.util.ArrayList;

import java.awt.Point;

public class Room {
	
	public static final int DIR_N = 0;
	public static final int DIR_W = 1;
	public static final int DIR_E = 2;
	public static final int DIR_S = 3;
	
	private List<List<Tile>> floor; /* h*w */
	private List<List<Wall>> walls; /* walls is 2*h+1 by w+1 */
	private int w;
	private int h;
	
	private Point startingPos;

	
	
	public Room(int width, int height){
		this.w=width;
		this.h=height;
		this.startingPos=new Point(0,0);
		floor = new ArrayList<List<Tile>>();
		walls = new ArrayList<List<Wall>>();
		for (int y = 0; y<height; y++){
			floor.add(new ArrayList<Tile>());

			walls.add(new ArrayList<Wall>());
			walls.add(new ArrayList<Wall>());
			for (int x = 0; x<width; x++){
				floor.get(y).add(new Tile());
				walls.get(y*2).add(new Wall(Wall.WALL_NONE));
				walls.get(y*2+1).add(new Wall(Wall.WALL_NONE));
			}
			walls.get(y*2).add(new Wall(Wall.WALL_NONE));
			walls.get(y*2+1).add(new Wall(Wall.WALL_NONE));
		}
		
		
		walls.add(new ArrayList<Wall>());
		for (int x = 0; x<width+1; x++){
			walls.get(height*2).add(new Wall(Wall.WALL_NONE));
		}

	}
	
	public void addTile(Point p, Tile t){
		//todo a bit more error handling?
		int tempX = p.x; 
		int tempY = p.y;
		List<Tile> row = floor.get(tempY);
		while (row.size() < tempX+1){
			row.add(new Tile());
		}
		if (row.get(tempX).isPlaceholder()){
			row.remove(tempX);
			row.add(tempX, t);
		} else {
			System.out.println(row.get(tempX).isPlaceholder());
			System.out.println(floor);
			throw new RuntimeException("Tile already on floor " + p.x + " " + p.y);
		}
	}

	public Point getStartingPosition(){
		/*returns the starting position of the robot*/
		return startingPos;
	}
	
	public void setStartingPos(Point p){
		//TODO error handle
		startingPos = p;
	}
	
	public void addWall(Point p, int direction, Wall w){
		int tempX=p.x;
		int tempY=p.y;
		if (direction==DIR_N){
			walls.get(tempY*2).set(tempX,w);
		} else if (direction==DIR_S) {
			walls.get((tempY*2)+2).set(tempX,w);
		} else if (direction==DIR_W){
			walls.get((tempY*2)+1).set(tempX,w);
		} else{
			walls.get((tempY*2)+1).set(tempX+1,w);
		}
	}
	
	public void addWall(Point p, int direction, int wType){
		//TODO error handle
		int tempX=p.x;
		int tempY=p.y;
		if (direction==DIR_N){
			walls.get(tempY*2).set(tempX,new Wall(wType));
		} else if (direction==DIR_S) {
			walls.get((tempY*2)+2).set(tempX,new Wall(wType));
		} else if (direction==DIR_W){
			walls.get((tempY*2)+1).set(tempX,new Wall(wType));
		} else{
			walls.get((tempY*2)+1).set(tempX+1,new Wall(wType));
		}
	}
	
	public void addRotatingDoor(Point p, int direction, boolean openAtStart, int changeTime){
		//TODO error handle
		int tempX=p.x;
		int tempY=p.y;
		if (direction==DIR_N){
			walls.get(tempY*2).set(tempX,new Wall(openAtStart,changeTime));
		} else if (direction==DIR_S) {
			walls.get((tempY*2)+2).set(tempX,new Wall(openAtStart,changeTime));
		} else if (direction==DIR_W){
			walls.get((tempY*2)+1).set(tempX,new Wall(openAtStart,changeTime));
		} else{
			walls.get((tempY*2)+1).set(tempX+1,new Wall(openAtStart,changeTime));
		}
	}
	
	private String wallToString(int y, int x,String s1,String s2){
		String s = "";
		if (walls.get(y).get(x).getStatus()>0){
			s=s1; //TODO doors
		}
		else{
			s=s2;
		}
		return s;
	}
	
	
	public String toString(){
		//simple output
		String s = "";
		for(int y = 0; y<floor.size(); y++){
			List<Tile> row = floor.get(y);

			for(int x=0; x<row.size(); x++){
				if (new Point(x,y).equals(this.getStartingPosition())){
					s+="P";
					s+=wallToString(y*2,x,"_",".");
				}
				else s+=wallToString(y*2,x," _"," .");
			}
			s+="\n";
			for(int x=0; x<row.size(); x++){
				s+=wallToString(y*2+1,x,"|",".");
				s+= row.get(x).getCarpetType()+1;
			}
			s+=wallToString(y*2+1,row.size(),"|",".");
			s += "\n";
		}
		for(int x=0; x<w+1; x++){
			s+=wallToString(h*2,x,"_",".");
		}
		s+="\n";
		return s;
	}
	
	public int[] wallsSurrounding(Point p){
		/* Returns the wall types surrounding the tile at p
		 * ORDER: N,W,E,S
		 * See constants (WALL_NONE, WALL_WALL, etc)
		 */
		int tX = p.x;
		int tY = p.y;
		int[] sides = {walls.get(tY*2).get(tX).getStatus(),
					   walls.get((tY*2)+1).get(tX).getStatus(),
					   walls.get((tY*2)+1).get(tX+1).getStatus(),
					   walls.get((tY*2)+2).get(tX).getStatus()};
		return sides; //all four directions
	}
	
	public void roomStep(){
		for(List<Wall> l : walls){
			for(Wall w: l){
				w.step();
			}
		}
	}
	
	public boolean hasDirtAt(Point p){
		if (p.x<0 || p.x>this.getWidth() || p.y<0 || p.y>this.getHeight()) return false;
		return floor.get(p.y).get(p.x).hasDirt();
	}
	
	public void clean(Point p){
		if (p.x<0 || p.x>this.getWidth() || p.y<0 || p.y>this.getHeight()) return;
		floor.get(p.y).get(p.x).cleanTile();
	}
	
	public int getFloorTypeAt(Point p){
		if (p.x<0 || p.x>this.getWidth() || p.y<0 || p.y>this.getHeight()) return 0;
		return floor.get(p.y).get(p.x).getCarpetType();
	}
	
	public int floorIsObstacle(Point p){
		if (p.x<0 || p.x>this.getWidth() || p.y<0 || p.y>this.getHeight()) return Tile.OBSTACLE_BLOCK; //can't go outside room
		return floor.get(p.y).get(p.x).getObstacleType();
	}
	
	public boolean floorIsPlaceholder(Point p){
		return floor.get(p.y).get(p.x).isPlaceholder();
	}
	
	public int getWidth(){return w;}
	public int getHeight(){return h;}
	
	// Added Charging Station to room.
	public void addChargingStation(Point p) {
		ChargingStation cs = new ChargingStation();
		floor.get(p.y).get(p.x).setChargingStation(cs);	
	}
	
	public ChargingStation getChargingStation(Point p){
		
		if(this.floor.get(p.y).get(p.x).isChargingStation()){
			return this.floor.get(p.y).get(p.x).getChargingStation();
		}
		else
			return null;
	}
	
	public boolean chargingStationExist(Point p) {
		if(this.floor.get(p.y).get(p.x).isChargingStation()) {
			return true;			
		}	
		
		return false;
			
	}
}