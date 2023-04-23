package edu.ycp.cs320.project.model;
import java.util.*;

public class Room {
	
	private int roomID;
	private int userID;
	private List<Item> items;
	private int userPosition;

	
	public Room() {
		this.items = new ArrayList<Item>();
		this.userPosition = 0;
		this.userID = 0;
	}
	
	public Room(List<Item> items, int position, int userID) {
		this.items = items;
		this.userPosition = position;
		this.userID = userID;
	}
	
	public void movePosition() {
		
	}
	
	public int getRoomID() {
		return roomID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public int getUserPosition() {
		return userPosition;
	}
	
	public void setRoomID(int id) {
		this.roomID = id;
	}
	
	public void setUserID(int id) {
		this.userID = id;
	}
	
	public void setUserPosition(int position) {
		this.userPosition = position;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}

}