package edu.ycp.cs320.project.model;

public class Item {
	
	private int itemID;
	private int userOrRoomID;
	private String name;
	private Boolean canBePickedUp;
	private int xPositionInRoom;
	private int yPositionInRoom;
	private int roomPosition;
	
	public Item() {
		this.itemID = 0;
		this.userOrRoomID = 0;
		this.name = "Unnamed Item";
		this.canBePickedUp = true;
		this.xPositionInRoom = 0;
		this.yPositionInRoom = 0;
		this.roomPosition = 0;
	}
	public Item(String name) {
		this.itemID = 0;
		this.userOrRoomID = 0;
		this.name = name;
		this.canBePickedUp = true;
		this.xPositionInRoom = 0;
		this.yPositionInRoom = 0;
		this.roomPosition = 0;
	}
	
	public Item(String name, Boolean canBePickedUp, int xPositionInRoom, int yPositionInRoom, int roomPosition) {
		this.name = name;
		this.canBePickedUp = canBePickedUp;
		this.xPositionInRoom = xPositionInRoom;
		this.yPositionInRoom = yPositionInRoom;
		this.roomPosition = roomPosition;
	}
	
	public Item(Item other) {
		this.itemID = other.getItemID();
		this.name = other.getName();
		this.canBePickedUp = other.getCanBePickedUp();
		this.xPositionInRoom = other.getXPosition();
		this.yPositionInRoom = other.getYPosition();
		this.roomPosition = other.getRoomPosition();
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public int getUserOrRoomID() {
		return userOrRoomID;
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
	
	public int getRoomPosition() {
		return roomPosition;
	}
	
	public void setItemID(int id) {
		this.itemID = id;
	}
	
	public void setUserOrRoomID(int id) {
		this.userOrRoomID = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCanBePickedUp(Boolean canBePickedUp) {
		this.canBePickedUp = canBePickedUp;
	}
	
	public void setXPosition(int x) {
		this.xPositionInRoom = x;
	}
	
	public void setYPosition(int y) {
		this.yPositionInRoom = y;
	}
	
	public void setRoomPosition(int pos) {
		this.roomPosition = pos;
	}
}
