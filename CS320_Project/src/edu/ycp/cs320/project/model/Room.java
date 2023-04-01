package edu.ycp.cs320.project.model;
import java.util.*;

public class Room {
	
	private List<Item> items;
	private int userPosition;
	
	public Room() {
		this.items = new ArrayList<Item>();
		this.userPosition = 0;
	}
	
	public void movePosition() {
		
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public int getUserPosition() {
		return userPosition;
	}
}
