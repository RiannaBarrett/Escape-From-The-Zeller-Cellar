package edu.ycp.cs320.project.model;
import java.util.*;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class Window extends Task {
	private IDatabase db = DatabaseProvider.getInstance();

	public Window(Task task) {
		super.setTaskID(task.getTaskID());
		super.setObjectiveID(task.getObjectiveID());
		super.setName(task.getName());
		super.setIsStarted(task.getIsStarted());
		super.setIsComplete(task.getIsComplete());
	}

	@Override	
	public String validateComplete(int userID) {
		System.out.println("Hammer Task being checked");
		//if the fire alarm is not in the room, the task is complete
		Item window = db.findItemByNameAndIDInRoom("Window", db.findRoomIDByUserID(userID));
		if(window==null) {
			db.changeTaskIsComplete(super.getTaskID(),true);
		}
		return "";
		}
}
