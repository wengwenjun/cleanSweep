package com.groupseven.cleansweeptests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.groupseven.cleansweeplib.Tile;


public class TileTest {
	Tile tile = new Tile();
	@Test
	public void isPlaceHolder() {
		
	   assertTrue(tile.isPlaceholder());
		
	}
	@Test
	public void testConstructor(){
		int carpet = 1;
		int dirt = 1;
		int obstacleType = 1;	
		Tile tile = new Tile(carpet, dirt, obstacleType); 
		assertEquals(carpet,tile.getCarpetType());
		assertTrue(tile.hasDirt());
		tile.cleanTile();
		assertFalse(tile.hasDirt());
		assertEquals(obstacleType, tile.getObstacleType());
	}
	
	

}
