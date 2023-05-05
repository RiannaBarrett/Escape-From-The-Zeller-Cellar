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
		iList.add(new Item("Shelf",false,-978,-450,0));
		iList.add(new Item("Jar of Cat Hairs",false,3,613,0));
		iList.add(new Item("Jar with Hibiscus",false,3,573,0));
		iList.add(new Item("Empty Potion Bottle",false,8,516,0));
		iList.add(new Item("Untitled Book",false,-29,773,0));
		iList.add(new Item("Fire Spinning Book",false,-39,763,0));
		iList.add(new Item("Unlit Candle",false,8,556,0));
		iList.add(new Item("Clover",false,61,588,0));
		iList.add(new Item("Wishbone",false,7,598,0));
		iList.add(new Item("Carton of Lime Juice",false,60,525,0));
		iList.add(new Item("Empty Cauldron",false,-405,-400,2));
		iList.add(new Item("Cat Tree",false, -1457, -460, 1));
		iList.add(new Item("Messy", false, -83, 181, 1));
		iList.add(new Item("Zeller Poster", false, -1000,-400,3));
		iList.add(new Item("Fire Alarm", false, -176, 408, 2));
		iList.add(new Item("Locked Comic Stand",false, -1018,-309, 1));
		iList.add(new Item("Bag of Meow Mix",true, -108,225, 0));
		
		/*these are items that will be added to the comic stand as individual items once
		 * the comic book stand is unlocked. I am putting them here for now to test
		 * x and y values
		 */
		
		iList.add(new Item("X-Men 1 Comic", false, 18, 698, 1));
		iList.add(new Item("Avengers 1 Comic", false, 6, 782, 1));
		iList.add(new Item("Avengers 4 Comic", false, 7, 867, 1));
		iList.add(new Item("Superman 18 Comic", false, 90, 698, 1));
		iList.add(new Item("Fantastic Four 48 Comic", false, 63, 783, 1));
		iList.add(new Item("Fantastic Four 9 Comic", false, 70, 866, 1));
		iList.add(new Item("X-Men 94 Comic", false, 128, 702, 1));
		iList.add(new Item("Giant Size X-Men 1 Comic", false, 61, 588, 1));
		iList.add(new Item("Amazing Spiderman 300 Comic", false, 131, 869, 1));

		return iList;
	}
	
	private List<Objective> createInitialObjectives() {
		List<Objective> oList = new ArrayList<Objective>();
		// TODO: Add Objectives (WITH TASKS) here for New Account Creation.
		return oList;
	}

}