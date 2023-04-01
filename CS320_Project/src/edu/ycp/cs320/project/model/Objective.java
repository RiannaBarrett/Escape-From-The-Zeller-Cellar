package edu.ycp.cs320.project.model;
import java.util.*;

public class Objective {
	
	private List<Task> tasks;
	private Boolean isComplete;
	private Boolean isStarted;
	
	public Objective() {
		this.isStarted = true;
		this.isComplete = false;
		this.tasks = new ArrayList<Task>();
	}
	
	public Objective(Boolean isStarted, Boolean isComplete, List<Task> tasks) {
		this.isStarted = isStarted;
		this.isComplete = isComplete;
		this.tasks = tasks;
	}
	
	public Boolean getIsStarted() {
		return isStarted;
	}
	
	public Boolean getIsComplete() {
		return isComplete;
	}
	
	public List<Task> getTasks() {
		return tasks;
	}
	
	public void setIsStarted(Boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
}
