package edu.ycp.cs320.project.model;
import java.util.*;

public class Task {
	
	private int taskID;
	private int objectiveID;
	private List<Integer> itemIDs;
	private Boolean isStarted;
	private Boolean isComplete;
	
	public Task() {
		this.taskID = 0;
		this.objectiveID = 0;
		this.isStarted = true;
		this.isComplete = false;
		this.itemIDs = new ArrayList<Integer>();
	}
	
	public Task(int id, int objID, Boolean isStarted, Boolean isComplete, List<Integer> correctItems) {
		this.taskID = id;
		this.objectiveID = objID;
		this.isStarted = isStarted;
		this.isComplete = isComplete;
		this.itemIDs = correctItems;
	}
	
	public Boolean getIsStarted() {
		return isStarted;
	}
	
	public Boolean getIsComplete() {
		return isComplete;
	}
	
	public List<Integer> getCorrectItems() {
		return itemIDs;
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
	
	public void setCorrectItems(List<Integer> items) {
		this.itemIDs = items;
	}
	
	public void setTaskID(int id) {
		this.taskID = id;
	}
	
	public void setObjectiveID(int id) {
		this.objectiveID = id;
	}
}
