package com.groupseven.sensorsim;

import java.io.*;

import com.groupseven.cleansweeplib.*;

import java.awt.Point;

public class RoomParser {
	public static int byteLEtoInt(byte[] b){
		int val = 0;
        for (int i=3; i>=0; i--) {
        	val=val*256;
        	val+=b[i];
        }
        return val;
	}
	
	public static Tile intsToTile(int[] is){
		//from a pixel, return a Tile based on red, green, blue channel
		int obstacle, carpet, dirt;
		obstacle=Tile.OBSTACLE_NONE;
		carpet=Tile.CARPET_BARE;
		if (is[0]==0x00) obstacle = Tile.OBSTACLE_BLOCK;
		else if (is[0]==0x55) obstacle = Tile.OBSTACLE_STAIRS;
		else if (is[0]==0xCC) carpet=Tile.CARPET_LOW;
		else if (is[0]==0x99) carpet=Tile.CARPET_HIGH;
		
		if (is[1]<0x65) dirt=(int) is[1];
		else dirt=0;
		
		if (is.length>2){
		if (is[2]==2 || is[2]==3){ //charging station
			Tile t = new Tile(carpet,0,Tile.OBSTACLE_NONE);
			t.setChargingStation(new ChargingStation());
			return t;
		}}
		
		return new Tile(carpet, dirt, obstacle);
	}
	
	public static Wall intsToWall(int[] is){
		if (is[0]==0) 	return new Wall(Wall.WALL_WALL);
		if (is[0]==0x55) return new Wall(Wall.DOOR_CLOSED);
		if (is[0]==0xAA) return new Wall(Wall.DOOR_OPEN);
		if (is[0]==0xFF) return new Wall(Wall.WALL_NONE);
		if (is[0]<0x55) return new Wall(true, is[0]);
		return new Wall(Wall.WALL_NONE);
	}
	
	public static Room parseFile(String filename) throws IOException {
		//takes in a filename in my format, spits out a room object. 
		try{
			InputStream f = RoomParser.class.getClassLoader().getResourceAsStream(filename);
			f.skip(10);
			byte[] buffer = new byte[4];
			f.read(buffer);
			int offset = byteLEtoInt(buffer); 
			//System.out.println(offset);
			f.skip(4);
			f.read(buffer);
			int xSize = byteLEtoInt(buffer);
			f.read(buffer);
			int ySize = byteLEtoInt(buffer);
			f.skip(offset-26);
			
			Room r = new Room((xSize-1)/2,(ySize-1)/2);
			
			buffer = new byte[3];
			int[] pixel = new int[3];
			for (int y=ySize-1; y>=0;y--){
				for(int x=0; x<xSize; x++){
					f.read(buffer);
					for(int i=0;i<3;i++){
						pixel[i] = buffer[2-i];
						if (pixel[i]<0){
							pixel[i]+=256;
						}
					}
					
					if (y%2==0 && (x)%2==1){ //horiz wall
						r.addWall(new Point(x/2, y/2), Room.DIR_N, intsToWall(pixel));
					} else if (y%2==1 && (x)%2==1){ //tile
						r.addTile(new Point(x/2,y/2),intsToTile(pixel));
						if (pixel[2]==1 || pixel[2]==3){ //handle objects (blue channel) here
							r.setStartingPos(new Point(x/2,y/2));
						}
					} else if (y%2==1){ //vert wall
						r.addWall(new Point(x/2, y/2), Room.DIR_W, intsToWall(pixel));
					}
					
				
				}
				f.skip(4-((xSize*3)%4));
				

			}
			f.close();
			return r;
		} catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}
	}
}
