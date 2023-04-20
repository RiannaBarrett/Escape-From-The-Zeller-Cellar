package edu.ycp.cs320.project.model;

public class MainPage {
	
	private User user;
	
	public MainPage() {
		
	}
	
	public MainPage(User user, Room room) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
