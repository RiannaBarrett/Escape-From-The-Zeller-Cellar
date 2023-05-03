package edu.ycp.cs320.project.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.model.*;

public class InitialData {
	
	public static List<User> getUsers() throws IOException {
		List<User> userList = new ArrayList<User>();
		List<Room> roomList = getRooms();
		List<Item> userInventoryList = getUserInventories();
		List<Item> roomInventoryList = getRoomInventories();
		List<Objective> objectiveList = getObjectives();
		List<Task> taskList = getTasks();
		
		ReadCSV readUsers = new ReadCSV("users.csv");
		try {
			// auto-generated primary key users
			Integer userID = 1;
			while (true) {
				List<String> tuple = readUsers.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				User user = new User();
				user.setUserID(userID++);
				user.setUsername(i.next());
				user.setPassword(i.next());
				user.setTime(Integer.parseInt(i.next()));
				
				Room svRoom = new Room();
				for(Room room : roomList) {
					if(room.getUserID() == user.getUserID()) {
						svRoom = room;
					}
				}
				
				List<Item> userInventory = new ArrayList<Item>();
				for(Item item : userInventoryList) {
					if(item.getUserOrRoomID() == user.getUserID()) {
						userInventory.add(item);
					}
				}
				user.setInventory(userInventory);
				
				List<Item> roomInventory = new ArrayList<Item>();
				for(Item item : roomInventoryList) {
					if(item.getUserOrRoomID() == svRoom.getRoomID()) {
						roomInventory.add(item);
					}
				}
				svRoom.setItems(roomInventory);
				
				List<Objective> objectives = new ArrayList<Objective>();
				for(Objective obj : objectiveList) {
					if(obj.getRoomID() == svRoom.getRoomID()) {
						objectives.add(obj);
					}
				}
				
				for(int j=0; j<objectives.size(); j++) {
					List<Task> tasks = new ArrayList<Task>();
					for(Task task : taskList) {
						if(task.getObjectiveID() == objectives.get(j).getObjectiveID()) {
							tasks.add(task);
						}
					}
					objectives.get(j).setTasks(tasks);
				}
				svRoom.setObjectives(objectives);
				user.setRoom(svRoom);
				userList.add(user);
			}
			return userList;
		} finally {
			readUsers.close();
		}
	}
	
	public static List<Room> getRooms() throws IOException {
		List<Room> roomList = new ArrayList<Room>();
		ReadCSV readRooms = new ReadCSV("rooms.csv");
		try {
			// auto-generated primary key for rooms
			Integer roomId = 1;
			while (true) {
				List<String> tuple = readRooms.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Room room = new Room();
				room.setRoomID(roomId++);
				room.setUserID(Integer.parseInt(i.next()));
				room.setUserPosition(Integer.parseInt(i.next()));
				roomList.add(room);
			}
			return roomList;
		} finally {
			readRooms.close();
		}
	}
	
	public static List<Item> getUserInventories() throws IOException {
		List<Item> itemList = new ArrayList<Item>();
		ReadCSV readUserInv = new ReadCSV("userInventories.csv");
		try {
			// auto-generated primary key for rooms
			Integer itemID = 1;
			while (true) {
				List<String> tuple = readUserInv.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Item item = new Item();
				item.setItemID(itemID++);
				item.setUserOrRoomID(Integer.parseInt(i.next()));
				item.setName(i.next());
				item.setCanBePickedUp(Boolean.parseBoolean(i.next()));
				item.setXPosition(Integer.parseInt(i.next()));
				item.setYPosition(Integer.parseInt(i.next()));
				item.setRoomPosition(Integer.parseInt(i.next()));
				itemList.add(item);
			}
			return itemList;
		} finally {
			readUserInv.close();
		}
	}
	
	public static List<Item> getRoomInventories() throws IOException {
		List<Item> itemList = new ArrayList<Item>();
		ReadCSV readRoomInv = new ReadCSV("roomInventories.csv");
		try {
			// auto-generated primary key for rooms
			Integer itemID = 1;
			while (true) {
				List<String> tuple = readRoomInv.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Item item = new Item();
				item.setItemID(itemID++);
				item.setUserOrRoomID(Integer.parseInt(i.next()));
				item.setName(i.next());
				item.setCanBePickedUp(Boolean.parseBoolean(i.next()));
				item.setXPosition(Integer.parseInt(i.next()));
				item.setYPosition(Integer.parseInt(i.next()));
				item.setRoomPosition(Integer.parseInt(i.next()));
				itemList.add(item);
			}
			return itemList;
		} finally {
			readRoomInv.close();
		}
	}
	
	public static List<Objective> getObjectives() throws IOException {
		List<Objective> objList = new ArrayList<Objective>();
		ReadCSV readObj = new ReadCSV("objectives.csv");
		try {
			// auto-generated primary key for rooms
			Integer objID = 1;
			while (true) {
				List<String> tuple = readObj.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Objective obj = new Objective();
				obj.setObjectiveID(objID++);
				obj.setRoomID(Integer.parseInt(i.next()));
				obj.setIsStarted(Boolean.parseBoolean(i.next()));
				obj.setIsComplete(Boolean.parseBoolean(i.next()));
				objList.add(obj);
			}
			return objList;
		} finally {
			readObj.close();
		}
	}
	
	public static List<Task> getTasks() throws IOException {
		List<Task> taskList = new ArrayList<Task>();
		ReadCSV readTasks = new ReadCSV("tasks.csv");
		try {
			// auto-generated primary key for rooms
			Integer taskID = 1;
			while (true) {
				List<String> tuple = readTasks.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Task task = new Task();
				task.setTaskID(taskID++);
				task.setObjectiveID(Integer.parseInt(i.next()));
				task.setIsStarted(Boolean.parseBoolean(i.next()));
				task.setIsComplete(Boolean.parseBoolean(i.next()));
				task.setCorrectItems(intParser(i.next()));
				taskList.add(task);
			}
			return taskList;
		} finally {
			readTasks.close();
		}
	}
	
	// Takes in a list of items in string form, such as an inventory, and returns a list of items.
	// Items are separated by ; and item values are separated by ^
	public static List<Item> itemParser(String itemString) {
		List<Item> itemList = new ArrayList<Item>();
		if(itemString == " " || itemString == null || itemString == "") {
			return itemList;
		}
		else {
			StringTokenizer tok = new StringTokenizer(itemString, ";");
			List<String> tuple = new ArrayList<String>();
			while (tok.hasMoreTokens()) {
				tuple.add(tok.nextToken().trim());
			}
			for(int i=0; i < tuple.size(); i++) {
				StringTokenizer tokInterior = new StringTokenizer(tuple.get(i), "^");
				List<String> unparsedItemList = new ArrayList<String>();
				while (tokInterior.hasMoreTokens()) {
					unparsedItemList.add(tokInterior.nextToken().trim());
				}
				Iterator<String> unparsedItemIterator = unparsedItemList.iterator();
				Item item = new Item();
				item.setName(unparsedItemIterator.next());
				item.setCanBePickedUp(Boolean.valueOf(unparsedItemIterator.next()));
				item.setXPosition(Integer.parseInt(unparsedItemIterator.next()));
				item.setYPosition(Integer.parseInt(unparsedItemIterator.next()));
				item.setRoomPosition(Integer.parseInt(unparsedItemIterator.next()));
				itemList.add(item);
			}
			return itemList;
		}
	}
	
	public static List<Integer> intParser (String itemString) {
		List<Integer> itemList = new ArrayList<Integer>();
		if(itemString == " " || itemString == null || itemString == "") {
			return itemList;
		}
		else {
			for(int i=0; i<itemString.length();i++) {
				itemList.add(Integer.parseInt(itemString.substring(i, i+1)));
			}
			return itemList;
		}
	}
}
