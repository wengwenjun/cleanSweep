package com.groupseven.cleansweeplib;

public class Wall {

	public static final int WALL_NONE = 0;
	public static final int WALL_WALL = 1;
	public static final int DOOR_CLOSED = 2; //permanent
	public static final int DOOR_OPEN = 3; //permanent
	
	public static final int WALL_UNKNOWN = -1;
	
	private static final int DOOR_CHANGING=4;

	private int wallType;
	private boolean canPass; //for doors of changing state, currently open/closed (true=open)
	private int timer; //for doors of changing state, which step currently on
	private int changeTime; //for doors of changing state, how many steps before closing/opening (can be overridden)
	
	public Wall(){
		this.wallType = Wall.WALL_UNKNOWN;
		this.canPass=false;
	}
	
	public Wall(int type){
		if (type==Wall.DOOR_CHANGING) throw new IllegalArgumentException("Use the other constructor");
		this.wallType=type;
		if (type==WALL_WALL || type==Wall.DOOR_CLOSED) canPass=false;
	}
	
	public Wall(boolean initialStatus, int changeTime){
		this.wallType=Wall.DOOR_CHANGING;
		this.canPass=initialStatus;
		this.timer=0;
		this.changeTime=changeTime;
	}
	
	public void open(){
		if (this.canPass==false && this.wallType==Wall.DOOR_CHANGING) canPass=true;
		else throw new RuntimeException("Tried to open a door that can't be opened");
	}
	
	public void close(){
		if (this.canPass==true && this.wallType==Wall.DOOR_CHANGING) canPass=false;
		else throw new RuntimeException("Tried to close a door that can't be closed");
	}
	
	public void step(){
		if (wallType ==Wall.DOOR_CHANGING){
			timer+=1;
			if (timer>=changeTime){
				timer=0;
				canPass = !canPass;
			}
		}
	}
	
	public int getStatus(){
		if (wallType==Wall.DOOR_OPEN || (wallType==Wall.DOOR_CHANGING && canPass)){
			return Wall.DOOR_OPEN;
		} else if (wallType==Wall.DOOR_CLOSED || (wallType==Wall.DOOR_CHANGING && !canPass)){
			return Wall.DOOR_CLOSED;
		} else return wallType;
	}
}
