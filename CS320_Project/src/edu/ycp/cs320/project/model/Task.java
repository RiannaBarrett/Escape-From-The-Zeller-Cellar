package edu.ycp.cs320.project.model;
import java.util.*;

public class Task {
	
	private int taskID;
	private int objectiveID;
	private List<Item> items;
	private String name;
	private Boolean isStarted;
	private Boolean isComplete;
	
	public Task() {
		this.taskID = 0;
		this.objectiveID = 0;
		this.isStarted = true;
		this.isComplete = false;
		this.items = new ArrayList<Item>();
		this.name = "Unnamed Task";
	}
	
	public Task(int id, int objID, String name, Boolean isStarted, Boolean isComplete, List<Item> correctItems) {
		this.taskID = id;
		this.objectiveID = objID;
		this.isStarted = isStarted;
		this.isComplete = isComplete;
		this.items = correctItems;
		this.name = name;
	}
	
	public Task(String name, Boolean isStarted, Boolean isComplete) {
		this.taskID = 0;
		this.objectiveID = 0;
		this.isStarted = isStarted;
		this.isComplete = isComplete;
		this.name = name;
		this.items = new ArrayList<Item>();
	}
	
	public String getName() {
		return name;
	}
	
	public Boolean getIsStarted() {
		return isStarted;
	}
	
	public Boolean getIsComplete() {
		return isComplete;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public int getTaskID() {
		return taskID;
	}
	
	public int getObjectiveID() {
		return objectiveID;
	}
	
	public void setIsStarted(Boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void setTaskID(int id) {
		this.taskID = id;
	}
	
	public void setObjectiveID(int id) {
		this.objectiveID = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void validateComplete() {
		throw new UnsupportedOperationException();
	}
}
