package edu.ycp.cs320.project.model;
import java.util.*;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class Cat extends Task {
	private IDatabase db = DatabaseProvider.getInstance();
	public Cat(Task task) {
		super.setTaskID(task.getTaskID());
		super.setObjectiveID(task.getObjectiveID());
		super.setName(task.getName());
		super.setIsStarted(task.getIsStarted());
		super.setIsComplete(task.getIsComplete());
	}

	@Override	
	public String validateComplete(int userID) {
		System.out.println("Cat Task being checked");
		List<Item> items = db.getUsedItemsByTaskId(super.getTaskID());
		int roomID = db.findRoomIDByUserID(userID);
		if(items.size() >= 2) {
			//check if the the food and potion was used
			Boolean isFed = false;
			Boolean canTalk = false;
			for(Item item : items) {
				if(item.getName().equals("Bag of Meow Mix")) {
					isFed = true;
				}
				if(item.getName().equals("Full Potion Bottle")) {
					canTalk = true;
				}
			}
				
			if(isFed && canTalk) {
				db.changeTaskIsComplete(super.getTaskID(), true);
			}
			
		}
		return "";
	}
}
