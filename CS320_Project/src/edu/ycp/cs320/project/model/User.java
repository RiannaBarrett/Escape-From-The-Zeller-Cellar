package edu.ycp.cs320.project.model;
import java.util.*;

public class User {
	
	private int userID;
	private String username;
	private String password;
	private List<Item> inventory;
	private Room room;

	public User() {
		this.userID = 0;
		this.username = "Unnamed User";
		this.password = "Password";
		this.inventory = new ArrayList<Item>();
		this.room = new Room();

	}
	
	public User(int userID, String username, String password, List<Item> inventory, Room room) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.inventory = inventory;
		this.room = room;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public List<Item> getInventory() {
		return inventory;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setUserID(int id) {
		this.userID = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public void select() {
		
	}
	
	public void indicate() {
		
	}
	
	public void useItem() {
		
	}
	
	public void enter() {
		
	}
	
	public void navigate() {
		
	}
	
	public void login() {
		
	}
	
	public void logout() {
		
	}
}
