package edu.ycp.cs320.project.model;
import java.util.*;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class Puzzle extends Task {
	private IDatabase db = DatabaseProvider.getInstance();

	public Puzzle(Task task) {
		super.setTaskID(task.getTaskID());
		super.setObjectiveID(task.getObjectiveID());
		super.setName(task.getName());
		super.setIsStarted(task.getIsStarted());
		super.setIsComplete(task.getIsComplete());
	}

	@Override	
	public String validateComplete(int userID) {
		System.out.println("Puzzle Task being checked");
		List<Item> items = db.getUsedItemsByTaskId(super.getTaskID());
		int roomID = db.findRoomIDByUserID(userID);
		if(items.size() >= 4) {
			//check if the items are in the right order
			if(items.get(0).getName().equals("Puzzle Piece 1") && items.get(1).getName().equals("Puzzle Piece 4")
					&& items.get(2).getName().equals("Puzzle Piece 3") && items.get(3).getName().equals("Puzzle Piece 2")) {
				Item board = db.findItemByNameAndIDInRoom("Puzzle Board", roomID);
				//create a full cauldron to swap into room
				board.setName("Complete Puzzle Board");
				db.addItemToRoom(board, roomID);
				db.changeTaskIsComplete(super.getTaskID(), true);
				db.addItemToRoom(new Item("Hammer", true, 162,408, 0), roomID);
				return "You completed the puzzle! You heard something crash in the rooms";
			}else {

				//remove all the items if the wrong ingredients were used
				for(Item item : items) {
					System.out.println("Adding item back to inv");
					db.addItemToInventory(item, userID);
				}
				db.removeItemFromUsedItems(super.getTaskID());
				
				return "The pieces don't seem to go this way";
				
			}
		}else {
			//The task is still in progress. return nothing
			return "";
		}
	}
}
