package edu.ycp.cs320.project.model;

public class Item {
	
	private String name;
	private Boolean canBePickedUp;
	private int xPositionInRoom;
	private int yPositionInRoom;
	
	public Item() {
		this.name = "Unnamed Item";
		this.canBePickedUp = true;
		this.xPositionInRoom = 0;
		this.yPositionInRoom = 0;
	}
	
	public Item(String name, Boolean canBePickedUp, int xPositionInRoom, int yPositionInRoom) {
		this.name = name;
		this.canBePickedUp = canBePickedUp;
		this.xPositionInRoom = xPositionInRoom;
		this.yPositionInRoom = yPositionInRoom;
	}
	
	public String getName() {
		return name;
	}
	
	public int getXPosition() {
		return xPositionInRoom;
	}
	
	public int getYPosition() {
		return yPositionInRoom;
	}
	
	public Boolean getCanBePickedUp() {
		return canBePickedUp;
	}
	
	public void setCanBePickedUp(Boolean canBePickedUp) {
		this.canBePickedUp = canBePickedUp;
	}
	
	public void setPosition(int x, int y) {
		this.xPositionInRoom = x;
		this.yPositionInRoom = y;
	}
}
