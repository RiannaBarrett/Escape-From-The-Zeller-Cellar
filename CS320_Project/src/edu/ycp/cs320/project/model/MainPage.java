package edu.ycp.cs320.project.model;

public class MainPage {
	
	private Room room;
	private User user;
	
	public MainPage() {
		
	}
	
	public MainPage(User user, Room room) {
		this.user = user;
		this.room = room;
	}
	
	public User getUser() {
		return user;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
}
