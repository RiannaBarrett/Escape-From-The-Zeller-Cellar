package edu.ycp.cs320.project.model;
import java.util.*;

public class Puzzle extends Task {

	public Puzzle(Task task) {
		super.setTaskID(task.getTaskID());
		super.setObjectiveID(task.getObjectiveID());
		super.setName(task.getName());
		super.setIsStarted(task.getIsStarted());
		super.setIsComplete(task.getIsComplete());
	}

	@Override	
	public String validateComplete(int userID) {
		throw new UnsupportedOperationException();
	}
}
