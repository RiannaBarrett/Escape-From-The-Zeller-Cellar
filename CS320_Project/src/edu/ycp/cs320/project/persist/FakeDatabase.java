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
		readInitialData();
		
		System.out.println(userList.size() + " Users");
		System.out.println(roomList.size() + " Rooms");
	}

	public void readInitialData() {
		try {
			userList.addAll(InitialData.getUsers());
			roomList.addAll(InitialData.getRooms());
			for (User user : userList) {
				for (Room room : roomList) {
					if(room.getUserID() == user.getUserID()) {
						user.setRoom(room);
					}
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}
	
	@Override
	public User findUserByName(String name) {
		for (User user : userList) {
			if (user.getUsername().equals(name)) {
				return user;
			}
		}
		return null;
	}

	private Room findRoomByUserID(int userID) {
		for (Room room : roomList) {
			if (room.getUserID() == userID) {
				return room;
			}
		}
		return null;
	}
	@Override
	public boolean addUser(User user) {
	    if (userList.add(user)) {
	        return true;
	    }
	    return false;
	}


}
