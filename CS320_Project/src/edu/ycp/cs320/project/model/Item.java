package edu.ycp.cs320.project.model;

public class Item {
	
	private String name;
	private Boolean canBePickedUp;
	
	public Item() {
		this.name = "Unnamed Item";
		this.canBePickedUp = true;
	}
	
	public Item(String name, Boolean canBePickedUp) {
		this.name = name;
		this.canBePickedUp = canBePickedUp;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCanBePickedUp(Boolean canBePickedUp) {
		this.canBePickedUp = canBePickedUp;
	}
	
	public Boolean getCanBePickedUp() {
		return canBePickedUp;
	}
}
