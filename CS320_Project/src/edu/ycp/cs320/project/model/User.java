package edu.ycp.cs320.project.model;
import java.util.*;

public class User {

	private String username;
	private String password;
	private List<Item> inventory;
	private Room room;
	
	public User() {
		this.username = "Unnamed User";
		this.password = "Password";
		this.inventory = new ArrayList<Item>();
		this.room = new Room();
	}
	
	public User(String username, String password, List<Item> inventory, Room room) {
		this.username = username;
		this.password = password;
		this.inventory = inventory;
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
