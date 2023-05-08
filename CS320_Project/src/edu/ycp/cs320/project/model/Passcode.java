package edu.ycp.cs320.project.model;
import java.util.*;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class Passcode extends Task {
	private IDatabase db = DatabaseProvider.getInstance();
	public Passcode(Task task) {
		super.setTaskID(task.getTaskID());
		super.setObjectiveID(task.getObjectiveID());
		super.setName(task.getName());
		super.setIsStarted(task.getIsStarted());
		super.setIsComplete(task.getIsComplete());
	}

	@Override	
	public String validateComplete(int userID) {
		System.out.println("Passcode task being checked");
		Item comicStand = db.findItemByNameAndIDInRoom("Comic Stand", db.findRoomIDByUserID(userID));
		if(comicStand!=null) {
			db.changeTaskIsComplete(super.getTaskID(),true);
		}
		return "";
	}
}
