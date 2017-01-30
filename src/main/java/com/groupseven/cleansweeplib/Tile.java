package com.groupseven.cleansweeplib;


public class Tile {
	//handles carpet type, dirt level, and obstacle on the floor.
	public static final int CARPET_BARE = 0;
	public static final int CARPET_LOW = 1;
	public static final int CARPET_HIGH = 2;
	
	public static final int OBSTACLE_NONE = 0;
	public static final int OBSTACLE_BLOCK = 1;
	public static final int OBSTACLE_STAIRS = 2;
	
	private ChargingStation chargingStation;
	private boolean hasChargingStation;
	
	private int carpet;
	private int dirtLevel;
	private int obstacleType;
	private boolean placeholder;
	
	public Tile(){
		this.carpet=-1;
		this.dirtLevel=-1;
		this.obstacleType=-1;
		this.placeholder=true;
		this.setChargingStation(chargingStation);
		this.hasChargingStation=false;
	}
	
	public Tile(int carpetType, int dirt, int obstacle){
		this.carpet=carpetType;
		this.dirtLevel=dirt;
		this.obstacleType=obstacle;
		this.placeholder=false;
		this.setChargingStation(chargingStation);
		this.hasChargingStation=false;
	}
	
	public boolean isPlaceholder(){return placeholder;}
	
	public void cleanTile(){
		dirtLevel--;
	}
	
	public int getCarpetType(){return carpet;}
	public boolean hasDirt(){return dirtLevel>0;}
	public int getObstacleType(){return obstacleType;}
	
	public ChargingStation getChargingStation() {
		return chargingStation;
	}

	public void setChargingStation(ChargingStation cs) {
		this.chargingStation = cs;
		this.hasChargingStation=true;
	}
	
	public boolean isChargingStation() {
		return this.hasChargingStation; 
	}
	
}