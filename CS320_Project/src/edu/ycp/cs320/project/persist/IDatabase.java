package edu.ycp.cs320.project.persist;

import edu.ycp.cs320.project.model.*;
import java.util.List;

public interface IDatabase {
	// Returns either the user in question or Null if no user found.
	public User findUserByName(String name);
	
	public boolean transferItemFromRoomToUser(User user, Item item);
	
	public boolean transferItemFromUserToRoom(User user, String itemName);
	
	public boolean moveUser(User user, int moveTo);

	public boolean addUser(User user);

	public String useEmptyPotion(Item bottle, Item selected, User user);

	public boolean addItemToRoom(Item item, int roomID);

	public boolean addItemToInventory(Item item, int userID);

	public boolean removeItemFromRoom(Item item, int roomID);

	public boolean removeItemFromInventory(Item item, int userID);

	public void swapItemInInventory(Item itemToRemove, Item itemToAdd, User user);

	public void swapItemInRoom(Item itemToRemove, Item itemToAdd, User user);

	public String useMatches(Item matches, Item selected, User user);

	public boolean changeCanBePickedUp(User user, Item item, Boolean canBePickedUp);

	public String usePotionIngredient(Item item, Item selected, User user);

	public int findUserIDByName(String name);

	public List<Item> findItemsInPositionByID(int roomID, int position);

	public Item findItemByNameAndIDInRoom(String name, int roomID);

	public Item findItemByNameAndIDInInv(String name, int userID);
	

}
