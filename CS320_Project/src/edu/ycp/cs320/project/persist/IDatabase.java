package edu.ycp.cs320.project.persist;

import edu.ycp.cs320.project.model.*;
import java.util.List;

public interface IDatabase {
	public User findUserByName(String name);
	
	public boolean transferItemFromRoomToUser(User user, String itemName);
	
	public boolean transferItemFromUserToRoom(User user, String itemName);

	public boolean addUser(User user);
}
