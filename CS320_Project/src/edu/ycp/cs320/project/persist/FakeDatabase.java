package edu.ycp.cs320.project.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ycp.cs320.project.model.*;
import edu.ycp.cs320.project.persist.IDatabase;


public class FakeDatabase implements IDatabase {

	private List<User> userList;
	private List<Room> roomList;

	public FakeDatabase() {
		userList = new ArrayList<User>();
		roomList = new ArrayList<Room>();

		// Add initial data
		loadInitialData();

		System.out.println(userList.size() + " Users");
		System.out.println(roomList.size() + " Rooms");
	}

	public void loadInitialData() {
		try {
			// Gets all users and rooms
			userList.addAll(InitialData.getUsers());
			for(User user : userList) {
				roomList.add(user.getRoom());
			}
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}

	@Override
	public User findUserByName(String name) {
		System.out.println("Current db: fake");
		// Searches userList for a username, returns the user or null
		for (User user : userList) {
			if (user.getUsername().equals(name)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public boolean addUser(User user) {
		user.setUserID(userList.size() + 1);
		user.getRoom().setRoomID(roomList.size() + 1);
		System.out.println("\n\n\n" + user.getRoom().getRoomID());
		System.out.println(user.getRoom().getRoomID());
		if (userList.add(user) && roomList.add(user.getRoom())) {
			return true;
		}
		System.out.println("Failed to create new user: " + user.getUsername());
		return false;
	}

	@Override
	public boolean transferItemFromRoomToUser(User user, Item item) {
		List<Item> inv = user.getRoom().getItems();
		for(int i = 0; i < inv.size(); i++) {
			if(item.getName().equals(inv.get(i).getName())) {
				inv.remove(i);
			}
		}
		user.getRoom().setItems(inv);
		user.getInventory().add(item);
		return true;
	}

	@Override
	public boolean transferItemFromUserToRoom(User user, String itemName) {
		Item itemToBeTransferred = findItemByName(itemName, user.getInventory());
		// Does the item exist in the inventory?
		// MOVE TO CONTROLLER
		System.out.println("db: itemToBeTransferred exist: " + itemToBeTransferred!=null);
		if(itemToBeTransferred != null) {
			itemToBeTransferred.setRoomPosition(user.getRoom().getUserPosition());
			user.getRoom().getItems().add(itemToBeTransferred);
			List<Item> inv = user.getInventory();
			for(int i = 0; i < inv.size(); i++) {
				if(itemName.equals(inv.get(i).getName())) {
					inv.remove(i);
				}
			}
			user.setInventory(inv);
			return true;
		}
		return false;
	}

	@Override
	public boolean moveUser(User user, int moveTo) {
		user.getRoom().setUserPosition(moveTo);
		return true;
	}

	@Override
	public int findUserIDByName(String username) {
		int find = -1;
		for(int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getUsername().equals(username)) {
				find = userList.get(i).getUserID();
			}
		}
		return find;
	}

	public Room findRoomByUserID(int userID) {
		for (User user : userList) {
			if (user.getUserID() == userID) {
				return user.getRoom();
			}
		}
		return null;
	}

	public Item findItemByName(String itemName, List<Item> itemList) {
		for (Item item : itemList) {
			if(item.getName() == itemName) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void swapItemInRoom(Item itemToRemove, Item itemToAdd, User user) {
		List<Item> inv = user.getRoom().getItems();
		for(int i = 0; i < inv.size(); i++) {
			if(itemToRemove.getName().equals(inv.get(i).getName())) {
				inv.remove(i);
			}
		}
		inv.add(itemToAdd);
		user.getRoom().setItems(inv);
	}

	@Override
	public void swapItemInInventory(Item itemToRemove, Item itemToAdd, User user) {
		List<Item> inv = user.getInventory();
		for(int i = 0; i < inv.size(); i++) {
			if(itemToRemove.getName().equals(inv.get(i).getName())) {
				inv.remove(i);
			}
		}
		inv.add(itemToAdd);
		user.setInventory(inv);
	}
	//TODO: move to controller and change parameter to select item names instead of items
	@Override
	public String useEmptyPotion(Item bottle, Item selected, User user) {
		String message = "Nothing Happened";
		System.out.println(selected.getName() + " is selected");
		if(selected.getName().equals("Cauldron with Potion")) {
			message = "You filled the bottle with a potion";
			Item fullPotion = new Item("Full Potion Bottle", false, 0, 0, user.getRoom().getUserPosition());
			swapItemInInventory(bottle, fullPotion, user);
		}
		return message;
	}

	//TODO: move to controller and change parameter to select item names instead of items
	@Override
	public String useMatches(Item matches, Item selected, User user) {
		String message = "Nothing Happened";
		System.out.println(selected.getName() + " is selected");
		if(selected.getName().equals("Unlit Candle")) {
			message = "You lit the candle";
			Item litCandle = new Item("Lit Candle", true,8,556, user.getRoom().getUserPosition());
			swapItemInRoom(selected, litCandle, user);
			removeItemFromInventory(matches, user.getUserID());
		}
		return message;
	}

	@Override
	public boolean addItemToRoom(Item item, int roomID) {
		List<Item> invList = roomList.get(roomID-1).getItems();
		item.setRoomPosition(roomList.get(roomID-1).getUserPosition());
		if(invList.add(item)) {
			roomList.get(roomID-1).setItems(invList);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean addItemToInventory(Item item, int userID) {
		List<Item> invList = userList.get(userID-1).getInventory();
		if(invList.add(item)) {
			userList.get(userID-1).setInventory(invList);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean removeItemFromRoom(Item item, int roomID) {
		List<Item> invList = roomList.get(roomID-1).getItems();
		if(invList.remove(item)) {
			roomList.get(roomID-1).setItems(invList);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean removeItemFromInventory(Item item, int userID) {
		List<Item> invList = userList.get(userID-1).getInventory();
		for(int i=0; i < invList.size(); i++) {
			if(invList.get(i).getName().equals(item.getName())) {
				invList.remove(i);
				userList.get(userID-1).setInventory(invList);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean changeCanBePickedUp(int roomID, String itemName, Boolean canBePickedUp) {
		for(Room room : roomList) {
			if(room.getRoomID() == roomID) {
				List<Item> items = room.getItems();
				for(int i = 0; i< items.size(); i++) {
					if(items.get(i).getName().equals(itemName)) {
						room.getItems().get(i).setCanBePickedUp(canBePickedUp);
						System.out.println("New value set to: " + room.getItems().get(i).getCanBePickedUp());
					}
				}
			}
		}
		if(findItemByNameAndIDInRoom(itemName, roomID).getCanBePickedUp() == canBePickedUp) {
			return true;
		}else {
			return false;
		}

	}



	@Override
	public List<Item> findItemsInPositionByID(int roomID, int position) {
		List<Item> result = new ArrayList<Item>();
		List<Item> items = new ArrayList<Item>();
		for(Room room : roomList) {
			if(room.getRoomID()==roomID) {
				items = room.getItems();
			}
		}

		for(Item item : items) {
			if(item.getRoomPosition()==position) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public Item findItemByNameAndIDInInv(String name, int userID) {
		for(User user : userList) {
			if(user.getUserID() == userID) {
				List<Item> items = user.getInventory();
				for(Item item : items) {
					System.out.println(name);
					if(item.getName().equals(name)) {
						return item;
					}
				}
			}
		}
		return null;
	}

	@Override
	public Item findItemByNameAndIDInRoom(String name, int roomID) {
		for(Room room : roomList) {
			if(room.getRoomID() == roomID) {
				List<Item> items = room.getItems();
				for(Item item : items) {
					System.out.println(name);
					if(item.getName().equals(name)) {
						return item;
					}
				}
			}
		}
		return null;
	}

	@Override
	public int findRoomIDByUsername(String username) {
		int userID = findUserIDByName(username);
		int roomID = -1;
		System.out.println("user id: " + userID);
		for(Room room : roomList) {
			System.out.println("Current id: " + room.getUserID());
			if(room.getUserID() == userID) {
				roomID = room.getRoomID();
			}
		}
		System.out.println("room id: "  + roomID);
		return roomID;
	}

	@Override
	public int findRoomIDByUserID(int userID) {
		int roomID = -1;
		for(Room room : roomList) {
			if(room.getUserID() == userID) {
				return room.getRoomID();
			}
		}
		return roomID;
	}

	@Override
	public List<Item> findItemsInInventory(int userID) {
		List<Item> result = new ArrayList<Item>();
		for(User user : userList) {
			if(user.getUserID() == userID) {
				result = user.getInventory();
				return result;
			}
		}
		return result;
	}

	@Override
	public boolean getCanBePickedUp(int userID, String itemName) {
		Boolean result = false;
		for(User user : userList) {
			if(user.getUserID()==userID) {
				List<Item> items = user.getRoom().getItems();
				for(Item item : items) {
					if(item.getName().equals(itemName)) {
						result = item.getCanBePickedUp();
						return result;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean addObjectiveToRoom(Objective obj, int roomID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addTaskToObjective(Task task, int objectiveID) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Boolean changeObjectiveIsStarted(int objectiveID, Boolean desiredResult) {
		Boolean result = false;
		int roomListID = -1;
		int objectiveListID = -1;
		for(int i = 0; i < roomList.size();i++) {
			for(int j = 0; j < roomList.get(i).getObjectives().size();j++) {
				if(roomList.get(i).getObjectives().get(j).getObjectiveID() == objectiveID) {
					roomListID = i;
					objectiveListID = j;
					Objective currentObj = roomList.get(i).getObjectives().get(j);
					//change isStarted
					currentObj.setIsStarted(desiredResult);
					//set result in roomList
					roomList.get(i).getObjectives().set(j, currentObj);
				}
			}
		}
		//check if correct
		if(roomListID != -1 && objectiveListID != -1) {
			if(roomList.get(roomListID).getObjectives().get(objectiveListID).getIsStarted() == desiredResult);
				result = true;
		}		
		
		return result;
	}
	
	@Override
	public Boolean changeObjectiveIsComplete(int objectiveID, Boolean desiredResult) {
		Boolean result = false;
		int roomListID = -1;
		int objectiveListID = -1;
		//find the objective based on ID and make change in roomList
		for(int i = 0; i < roomList.size();i++) {
			for(int j = 0; j < roomList.get(i).getObjectives().size();j++) {
				if(roomList.get(i).getObjectives().get(j).getObjectiveID() == objectiveID) {
					roomListID = i;
					objectiveListID = j;
					Objective currentObj = roomList.get(i).getObjectives().get(j);
					//change isComplete
					currentObj.setIsComplete(desiredResult);
					//set the result in roomlist
					roomList.get(i).getObjectives().set(j, currentObj);
				}
			}
		}
		
		if(roomListID != -1 && objectiveListID != -1) {
			if(roomList.get(roomListID).getObjectives().get(objectiveListID).getIsComplete() == desiredResult);
				result = true;
		}		
		
		return result;
	}
	
	@Override
	public Boolean changeTaskIsComplete(int taskID, Boolean desiredResult) {
		Boolean result = false;
		int roomListID = -1;
		int objectiveListID = -1;
		int taskListID = -1;
		for(int i = 0; i < roomList.size();i++) {
			for(int j = 0; j < roomList.get(i).getObjectives().size();j++) {
				for(int r = 0; r < roomList.get(i).getObjectives().get(j).getTasks().size();r++) {
					if(roomList.get(i).getObjectives().get(j).getTasks().get(r).getTaskID() == taskID) {
						roomListID = i;
						objectiveListID = j;
						taskListID = r;
						Task currentTask = roomList.get(i).getObjectives().get(j).getTasks().get(r);
						//change isComplete
						currentTask.setIsComplete(desiredResult);
						//set the result in roomlist
						roomList.get(i).getObjectives().get(j).getTasks().set(r, currentTask);
					}
				}
			}
		}
		//check if it is correct
		if(roomListID != -1 && objectiveListID != -1 && taskListID != -1) {
			if(roomList.get(roomListID).getObjectives().get(objectiveListID).getTasks().get(taskListID).getIsComplete() == desiredResult);
				result = true;
		}		
		
		return result;
	}
	
	@Override
		public Boolean changeTaskIsStarted(int taskID, Boolean desiredResult) {
			Boolean result = false;
			int roomListID = -1;
			int objectiveListID = -1;
			int taskListID = -1;
			for(int i = 0; i < roomList.size();i++) {
				for(int j = 0; j < roomList.get(i).getObjectives().size();j++) {
					for(int r = 0; r < roomList.get(i).getObjectives().get(j).getTasks().size();r++) {
						if(roomList.get(i).getObjectives().get(j).getTasks().get(r).getTaskID() == taskID) {
							roomListID = i;
							objectiveListID = j;
							taskListID = r;
							Task currentTask = roomList.get(i).getObjectives().get(j).getTasks().get(r);
							//change isComplete
							currentTask.setIsStarted(desiredResult);
							//set the result in roomlist
							roomList.get(i).getObjectives().get(j).getTasks().set(r, currentTask);
						}
					}
				}
			}
			//check if it is correct
			if(roomListID != -1 && objectiveListID != -1 && taskListID != -1) {
				if(roomList.get(roomListID).getObjectives().get(objectiveListID).getTasks().get(taskListID).getIsStarted() == desiredResult);
					result = true;
			}		
			
			return result;
		}
}
