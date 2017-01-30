package com.groupseven.cleansweeptests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.groupseven.cleansweeplib.ChargingStation;
import com.groupseven.cleansweeplib.Room;
import com.groupseven.cleansweeplib.Tile;
import com.groupseven.sensorsim.RoomParser;

public class RoomTest {
	//add a tile if existed should throw exception
	@Test(expected = RuntimeException.class)
	public void Exceptiontest() throws IOException  {
		String filename = "rooms/samplefloor.bmp";
		Room room1;
		room1 = RoomParser.parseFile(filename);
		Point point = new Point(7,8);
		Tile tile = new Tile();
		room1.addTile(point, tile);		
	}
	@Test
	public void test(){
		int height = 10;
		int width = 10;
		Room newRoom = new Room(height, width);
		assertEquals(height, newRoom.getHeight());
		assertEquals(height, newRoom.getWidth());
		List<Point> points = new ArrayList<Point>();
		Point startPoint = new Point(0,0);
		points.add(startPoint);
		Point temp1 = new Point(0,1);
		points.add(new Point(temp1));
		Point temp2 = new Point(1,1);
		points.add(temp2);
		int carpetType = 1;
		int dirt = 1;
		int obstacleType =1;
		Tile t = new Tile(carpetType,dirt,obstacleType);
		for(Point p: points){
		newRoom.addTile(p, t);
		}
		//test start point
		assertEquals(startPoint,newRoom.getStartingPosition());
		//test if dirt in point(0,0)
		assertTrue(newRoom.hasDirtAt(startPoint));
		//test clean function
		newRoom.clean(startPoint);
		assertFalse(newRoom.hasDirtAt(startPoint));
		//isobstacle
		assertEquals(obstacleType,newRoom.floorIsObstacle(startPoint));
		//isplaceholder
		assertFalse(newRoom.floorIsPlaceholder(startPoint));
		//floortype
		assertEquals(carpetType, newRoom.getFloorTypeAt(startPoint));
		//add south wall to Point temp1
		newRoom.addWall(temp1, 3, 1);
		//add wall east
		newRoom.addWall(temp2, 2, 2);
		
		int[] sides = new int[4];
		sides = newRoom.wallsSurrounding(temp1);
	    //should be value 1 in sides[3]: south direction there is a wall
		assertEquals(1, sides[3]);
		newRoom.addRotatingDoor(temp1, 1, true, 3);
		newRoom.addRotatingDoor(temp1, 2, true, 3);
		newRoom.addRotatingDoor(temp1, 3, false, 2);
		newRoom.addRotatingDoor(temp1, 0, false, 1);
		newRoom.roomStep();
		//add a charging station on temp2
		newRoom.addChargingStation(temp2);
		ChargingStation cs = newRoom.getChargingStation(temp2);
		Tile tile = cs.getStation();
		//there is no charging station on temp2, retun null
		ChargingStation nullcs = newRoom.getChargingStation(temp1);
		assertTrue(newRoom.chargingStationExist(temp2));
		newRoom.toString();	
	}
	
	

}
