package edu.ycp.cs320.project.model;
import java.util.*;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class PotionMachine extends Task {
	private IDatabase db = DatabaseProvider.getInstance();
	public PotionMachine(Task task) {
		super.setTaskID(task.getTaskID());
		super.setObjectiveID(task.getObjectiveID());
		super.setName(task.getName());
		super.setIsStarted(task.getIsStarted());
		super.setIsComplete(task.getIsComplete());
	}
	@Override	
	public String validateComplete(int userID) {
		System.out.println("PotionMachine Task being checked");
		//NOTE: this is currently incomplete
		List<Item> items = super.getItems();
		int roomID = db.findRoomIDByUserID(userID);
		if(items.size() >= 4) {
			//check if the items are in the right order
			if(items.get(0).getName().equals("Jar of Cat Hairs") && items.get(1).getName().equals("Clover")
					&& items.get(2).getName().equals("Wishbone") && items.get(3).getName().equals("Carton of Lime Juice")) {
				Item cauldron = db.findItemByNameAndIDInRoom("Empty Cauldron", roomID);
				//create a full cauldron to swap into room
				cauldron.setName("Cauldron with Potion");
				db.addItemToRoom(cauldron, roomID);
				
				return "You created a potion";
			}else {
				//TODO: remove the items from the usedItems and move them to the inventory
				return "The ingredients added did not seem to do anything";
			}
		}else {
			//The task is still in progress. return nothing
			return "";
		}
	}
}
