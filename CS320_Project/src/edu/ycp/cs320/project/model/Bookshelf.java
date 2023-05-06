package edu.ycp.cs320.project.model;
import java.util.*;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class Bookshelf extends Task {
	private IDatabase db = DatabaseProvider.getInstance();
	public Bookshelf(Task task) {
		super.setTaskID(task.getTaskID());
		super.setObjectiveID(task.getObjectiveID());
		super.setName(task.getName());
		super.setIsStarted(task.getIsStarted());
		super.setIsComplete(task.getIsComplete());
	}

	@Override	
	public String validateComplete(int userID) {
		System.out.println("Bookshelf Task being checked");
		List<Item> items = db.getUsedItemsByTaskId(super.getTaskID());
		if(items.size()>=4) {
			//check if the comics were selected in the right order
			if(items.get(0).getName().equals("X-Men 1 Comic") && items.get(1).getName().equals("Fantastic Four 9 Comic")
					&& items.get(2).getName().equals("Fantastic Four 48 Comic") &&
					items.get(3).getName().equals("Amazing Spiderman 300 Comic")) {
				db.changeTaskIsComplete(super.getTaskID(), true);
				//TODO: maybe drop a puzzle piece. Just do something to change the room to show that they are correct
				return "You heard a strange sounds. It seems like something appeared in the room";
			}else {
				//let the player know they did the task incorrectly and remove the items so they can try again
				db.removeItemFromUsedItems(super.getTaskID());
				return "You heard a strange sound, but it doesn't seem like anything happened.";
			}
			
		}else {
			//task is still in progress, return nothing
			return "";
		}
	}
}
