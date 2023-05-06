package edu.ycp.cs320.project.persist;

import edu.ycp.cs320.project.model.*;
import java.util.List;

public interface IDatabase {

	public void loadInitialData();
	
	public User findUserByName(String name);
	
	public boolean transferItemFromRoomToUser(User user, Item item);
	
	public boolean transferItemFromUserToRoom(User user, Item item);
	
	public boolean moveUser(User user, int moveTo);

	public boolean addUser(User user);

	public String useEmptyPotion(Item bottle, Item selected, User user);

	public boolean addItemToRoom(Item item, int roomID);

	public boolean addItemToInventory(Item item, int userID);
	
	public boolean addObjectiveToRoom(Objective obj, int roomID);
	
	public boolean addTaskToObjective(Task task, int objectiveID);

	public boolean removeItemFromRoom(Item item, int roomID);

	public boolean removeItemFromInventory(Item item, int userID);

	public void swapItemInInventory(Item itemToRemove, Item itemToAdd, User user);

	public void swapItemInRoom(Item itemToRemove, Item itemToAdd, User user);

	public String useMatches(Item matches, Item selected, User user);

	public List<Item> getRoomInventoryByID(int roomID);
	
	public int findUserIDByName(String name);

	public List<Item> findItemsInPositionByID(int roomID, int position);

	public Item findItemByNameAndIDInRoom(String name, int roomID);

	public Item findItemByNameAndIDInInv(String name, int userID);

	public int findRoomIDByUsername(String username);

	public int findRoomIDByUserID(int userID);

	public List<Item> findItemsInInventory(int userID);

	public boolean changeCanBePickedUp(int userID, String itemName, Boolean canBePickedUp);

	public boolean getCanBePickedUp(int userID, String itemName);

	boolean addItemToTask(Item item, int taskID);

	public Boolean changeObjectiveIsStarted(int objectiveID, Boolean desiredResult);

	public Boolean changeObjectiveIsComplete(int objectiveID, Boolean desiredResult);

	public Boolean changeTaskIsComplete(int taskID, Boolean desiredResult);

	public Boolean changeTaskIsStarted(int taskID, Boolean desiredResult);

	public List<Item> getUsedItemsByTaskId(int taskId);

	public boolean removeItemFromUsedItems(int taskId);

	public List<Task> getTasksByObjID(int objectiveID);

}
