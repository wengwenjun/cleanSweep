package com.groupseven.cleansweeptests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.groupseven.cleansweeplib.Wall;

public class WallTest {
	//WALL_NONE = 0; 
	//WALL_WALL = 1;
	//DOOR_CLOSED = 2;
	//DOOR_OPEN = 3; 	
	//DOOR_CHANGING=4;

	boolean canpass;
	@Test(expected = IllegalArgumentException.class)
	public void Exceptiontest() {
		int type = 4;
		Wall wall = new Wall(type);
	}
	@Test(expected = RuntimeException.class)
	public void wallOpenTest(){
		Wall wall1 = new Wall(false, 0);
		wall1.open();//open the door
		int wallStatus= wall1.getStatus();
		assertEquals(3, wallStatus);
		wall1.open();
		
	}
	@Test(expected = RuntimeException.class)
	public void wallCloseTest(){
		Wall wall2 = new Wall(true,0);
		//wall2.open();
		wall2.close();
		wall2.close();
	}
	@Test
	public void wallTest(){
		//todo a missing codition
		Wall wall3 = new Wall(true, 2);
		wall3.step();	
	}
}
