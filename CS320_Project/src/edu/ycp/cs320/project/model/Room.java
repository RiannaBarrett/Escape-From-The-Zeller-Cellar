package edu.ycp.cs320.project.model;
import java.util.*;

public class Room {
	
	private int roomID;
	private int userID;
	private List<Item> items;
	private List<Objective> objectives; 
	private int userPosition;

	
	public Room() {
		this.items = createInitialItems();
		this.objectives = createInitialObjectives();
		this.userPosition = 0;
		this.userID = 0;
		this.roomID = 0;
	}
	
	public Room(Room other) {
		this.items = other.getItems();
		this.objectives = other.getObjectives();
		this.userPosition = other.getUserPosition();
		this.userID = other.getUserID();
		this.roomID = other.getRoomID();
	}
	
	public Room(List<Item> items, int position, int userID, List<Objective> objectives) {
		this.items = items;
		this.userPosition = position;
		this.userID = userID;
		this.objectives = objectives;
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
	
	public List<Objective> getObjectives() {
		return objectives;
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
	
	public void setObjectives(List<Objective> obj) {
		this.objectives = obj;
	}
	
	private List<Item> createInitialItems() {
		List<Item> iList = new ArrayList<Item>();
		iList.add(new Item("Shelf",false,-875,-820,0));
		iList.add(new Item("Jar of Cat Hairs",false,108,230,0));
		iList.add(new Item("Jar with Hibiscus",false,108,190,0));
		iList.add(new Item("Empty Potion Bottle",false,111,145,0));
		iList.add(new Item("Fire Spinning Book",false,65,405,0));
		iList.add(new Item("Unlit Candle",false,113,195,0));
		iList.add(new Item("Clover",false,113,225,0));
		iList.add(new Item("Wishbone",false,166,145,0));
		iList.add(new Item("Carton of Lime Juice",false,166,225,0));
		iList.add(new Item("Empty Cauldron",false,-450,-635,0));
		
		return iList;
	}
	
	private List<Objective> createInitialObjectives() {
		List<Objective> oList = new ArrayList<Objective>();
		// TODO: Add Objectives (WITH TASKS) here for New Account Creation.
		return oList;
	}

}