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
		ReadCSV readUsers = new ReadCSV("users.csv");
		try {
			// auto-generated primary key users
			Integer userID = 0;
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
				user.setInventory(itemParser(i.next()));
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
			Integer roomId = 0;
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
				room.setItems(itemParser(i.next()));
				roomList.add(room);
			}
			return roomList;
		} finally {
			readRooms.close();
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
}
