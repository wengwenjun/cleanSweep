/**
 * test file for room object
 */
package com.groupseven.cleansweeptests;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import com.groupseven.cleansweeplib.Tile;
import com.groupseven.cleansweeplib.Wall;
import com.groupseven.sensorsim.RoomParser;


public class RoomParserTest {
	//takes in a filename in my format, spits out a room object. 
	private FileInputStream f;
	
	@Test
	public void byteLEtoIntTest() throws IOException {
		File file =  new File("src/main/resources/rooms/samplefloor.bmp");
	    f = new FileInputStream(file);
	    f.skip(10);
	    byte[] buffer = new byte[4];
		f.read(buffer);
		int offset = RoomParser.byteLEtoInt(buffer); 
		int test = 122;
		assertEquals(test, offset);
	}
	@Test
	public void intsToTileTest(){
		int[] is = new int[2];	
		is[0] = 0x00;//obstacle_block = 1 
		is[1] = 0x50; // dirt level > 0
		Tile tile1 = RoomParser.intsToTile(is);
		assertTrue(tile1.hasDirt());
		assertEquals(tile1.getObstacleType(), 1);
		is[0] =0x55;//obstacle_stairs = 2
		Tile tile2 = RoomParser.intsToTile(is);
		assertEquals(tile2.getObstacleType(),2);
		is[0] = 0xCC;//carpet_low = 1
		Tile tile3 = RoomParser.intsToTile(is);
		assertEquals(tile3.getCarpetType(),1);
		is[0] = 0x99;//carpet_high = 2
		Tile tile4 = RoomParser.intsToTile(is);
		assertEquals(tile4.getCarpetType(),2);
		is[1] = 0x70;// dirt =0
		Tile tile5 = RoomParser.intsToTile(is);
		assertFalse(tile5.hasDirt());		
	}
	@Test
	public void intsToWallTest(){
		int[] is = new int[2];
		is[0] = 0;//wall wall	
		Wall walltype1 = RoomParser.intsToWall(is);
		
		assertSame(walltype1.getStatus(), Wall.WALL_WALL);
		is[0] = 0x55;//wall door closed
		Wall walltype2 = RoomParser.intsToWall(is);
		assertEquals(walltype2.getStatus(), Wall.DOOR_CLOSED);
		is[0] = 0xAA;//wall DOOR OPEN
		Wall walltype3 = RoomParser.intsToWall(is);
		assertEquals(walltype3.getStatus(), Wall.DOOR_OPEN);
		is[0] = 0xFF;//wall none
		Wall walltype4 = RoomParser.intsToWall(is);
		assertEquals(walltype4.getStatus(), Wall.WALL_NONE);
	}
	//test create a room, not sure if should be add more detail
	@Test
	public void parseFileTest () {
		
	}
}
