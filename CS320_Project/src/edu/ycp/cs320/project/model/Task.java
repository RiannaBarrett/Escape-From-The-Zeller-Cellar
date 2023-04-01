package edu.ycp.cs320.project.model;
import java.util.*;

public class Task {
	
	private List<Item> correctItems;
	private Boolean isStarted;
	private Boolean isComplete;
	
	public Task() {
		this.isStarted = true;
		this.isComplete = false;
		this.correctItems = new ArrayList<Item>();
	}
	
	public Task(Boolean isStarted, Boolean isComplete, List<Item> correctItems) {
		this.isStarted = isStarted;
		this.isComplete = isComplete;
		this.correctItems = correctItems;
	}
	
	public Boolean getIsStarted() {
		return isStarted;
	}
	
	public Boolean getIsComplete() {
		return isComplete;
	}
	
	public List<Item> getCorrectItems() {
		return correctItems;
	}
	
	public void setIsStarted(Boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
}
