package edu.ycp.cs320.project.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ycp.cs320.project.model.*;
import edu.ycp.cs320.project.persist.DBUtil;


public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			System.out.println(e);
			throw new IllegalStateException("Could not load Derby driver");
		}
	}

	public interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;

	
	
	// New Methods Go Here
	@Override
	public User findUserByName(String name) {
		System.out.println("Current db: Derby");
		//throw new UnsupportedOperationException();
		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
							"select users.* " +
									"  from users " +
									" where users.username = ? "
							);
					stmt.setString(1, name);

					User result = null;

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						// create new User object
						// retrieve attributes from resultSet starting with index 1
						User user = new User();
						loadUser(user, resultSet, 1);
						// load inventory objects
						user.setRoom(getRoomByUserID(user.getUserID()));
						user.setInventory(getUserInventoryByID(user.getUserID()));
						result = user;
					}

					// check if the user was found
					if (!found) {
						System.out.println("<" + name + "> was not found in the users table");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	//TODO: maybe move to controller
	@Override
	public boolean transferItemFromRoomToUser(User user, Item item) {
		Boolean added = false;
		Boolean removed = false;
		added = addItemToInventory(item, user.getUserID());
		removed = removeItemFromRoom(item, user.getRoom().getRoomID());
		System.out.println(added);
		System.out.println("removed: " + removed);
		if(!added || !removed) {
			return false;
		}else {
			return true;
		}
		
		
	}

	//TODO: implement and maybe move to constroller
	@Override
	public boolean transferItemFromUserToRoom(User user, Item item) {
		Boolean added = false;
		Boolean removed = false;
		added = addItemToRoom(item, user.getRoom().getRoomID());
		removed = removeItemFromInventory(item, user.getUserID());
		System.out.println(added);
		System.out.println("removed: " + removed);
		if(!added || !removed) {
			return false;
		}else {
			return true;
		}

	}

	@Override
	public boolean moveUser(User user, int moveTo) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				int roomID = user.getRoom().getRoomID();
				
						
				
				try {
					//updates user position
					stmt = conn.prepareStatement(
							"update rooms " +
									"set rooms.userPosition = ? " +
									"where rooms.room_id = ?"
							);
					stmt.setInt(1, moveTo);
					stmt.setInt(2, roomID);
					
					
					stmt.execute();
					
					Boolean result = false;
					Boolean success = false;

					//selects the new position to check if it was successful
					stmt2 = conn.prepareStatement(
							"select rooms.userPosition " +
									"  from rooms " +
									" where rooms.room_id = ?"
							);
					stmt2.setInt(1, roomID);
					
						
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
						success = true;
						
						 //retrieve attributes from resultSet starting with index 1
						int finalPos = resultSet.getInt(1);
						System.out.println("Position: " + finalPos);
						if(moveTo == finalPos) {
							result  = true;
						}
					}
					// check if the item was found
					if (!success) {
						System.out.println("<" + user.getUsername() + "> was not moved");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});

	}
	
	@Override
	public int findUserIDByName(String name) {
		//throw new UnsupportedOperationException();
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
							"select users.user_id " +
									"  from users " +
									" where users.username = ? "
							);
					stmt.setString(1, name);

					int result = -1;

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;
						//get the integer for user id
						result = resultSet.getInt(1);
					}

					// check if the user was found
					if (!found) {
						System.out.println("<" + name + "> was not found in the users table");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	
	@Override
	public boolean addUser(User user) {
		Boolean result = false;
		result = executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				ResultSet resultSet = null;
				try {
					//inserts user based on username, password and inventory limit of 5
					stmt = conn.prepareStatement(
							"insert into users (username, password, inventoryLimit, time)" +
									"values (?,?,?,?) "
							);
					stmt.setString(1, user.getUsername());
					stmt.setString(2, user.getPassword());
					stmt.setInt(3, user.getInventoryLimit());
					stmt.setInt(4, user.getTime());
					stmt.execute();
					
					Boolean result = false;
					Boolean success = false;

					//selects all details of the new user
					stmt2 = conn.prepareStatement(
							"select users.* " +
									"  from users " +
									" where users.username = ? and users.password = ? "
							);
					stmt2.setString(1, user.getUsername());
					stmt2.setString(2, user.getPassword());
					resultSet = stmt2.executeQuery();
									
					//gets userID 
					while (resultSet.next()) {
						success = true;
						loadUser(user, resultSet, 1);
					}
					// check if the user was found
					if (!success) {
						System.out.println("<" + user.getUsername() + "> was not inserted in the users table");
						return success;
					}
					
					//adds new room to to user_id
					stmt3 = conn.prepareStatement(
							"insert into rooms(user_id, userPosition) " +
									" values (?,?) "
							);
					stmt3.setInt(1, user.getUserID());
					stmt3.setInt(2, user.getRoom().getUserPosition());
					stmt3.execute();

					stmt4 = conn.prepareStatement(
							"select rooms.* " +
									"  from rooms " +
									" where rooms.user_id = ? "
							);
					stmt4.setInt(1, user.getUserID());
					resultSet = stmt4.executeQuery();				
					
					// check if the room was found
					while (resultSet.next()) {
						Room room = new Room();
						loadRoom(room, resultSet, 1);
						user.setRoom(room);
						
						result = true;
					}					
					if (!result) {
						System.out.println("<" + user.getPassword() + "> was not inserted in the users table");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
		System.out.println("Adding items to user...");
		for(Item item : user.getInventory()) {
			System.out.println("Item: " + item.getName());
			addItemToInventory(item, user.getUserID());
		}
		System.out.println("Adding items to room...");
		for(Item item : user.getRoom().getItems()) {
			System.out.println("Item: " + item.getName());
			addItemToRoom(item, user.getRoom().getRoomID());
		}
		
		System.out.println("Adding objectives to room...");
		for(Objective objective : user.getRoom().getObjectives()) {
			System.out.println("Objective ");
			addObjectiveToRoom(objective, user.getRoom().getRoomID());
		}
		return result;
	}
	
	@Override
	public boolean addObjectiveToRoom(Objective obj, int roomID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;		
				
				try {
					System.out.println("Adding objective...");
					stmt = conn.prepareStatement(
							"insert into objectives (room_id, isStarted, isComplete)" +
									"values (?,?,?) "
							);
					stmt.setInt(1, roomID);
					stmt.setString(2, obj.getIsStarted().toString());
					stmt.setString(3, obj.getIsComplete().toString());
					stmt.execute();
					conn.commit();
					Boolean result = false;
					Boolean success = false;

					//selects all details of the new item
					stmt2 = conn.prepareStatement(
							"select objectives.* " +
									"  from objectives " +
									" where objectives.room_id = ? "
							);
					stmt2.setInt(1, roomID);
						
					resultSet = stmt2.executeQuery();
					
					Objective resultObj = new Objective();
					while (resultSet.next()) {
						success = true;
						// create new item object
						// retrieve attributes from resultSet starting with index 1
						loadObjective(resultObj, resultSet, 1);
						System.out.println(resultObj.getObjectiveID());
					}
					
					for(Task task : obj.getTasks()) {
						result = addTaskToObjective(task, resultObj.getObjectiveID());
					}
					
					// check if the item was found
					if (!success) {
						System.out.println("<Objective> was not inserted in the objectives table");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	@Override
	public boolean addTaskToObjective(Task task, int objID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;		
				
				try {
					System.out.println("Adding tasks...");
					stmt = conn.prepareStatement(
							"insert into tasks (objective_id, name, isStarted, isComplete)" +
									"values (?,?,?,?) "
							);
					stmt.setInt(1, objID);
					stmt.setString(2, task.getName());
					stmt.setString(3, task.getIsStarted().toString());
					stmt.setString(4, task.getIsComplete().toString());
					stmt.execute();
					
					Boolean result = false;
					Boolean success = false;

					//selects all details of the new item
					stmt2 = conn.prepareStatement(
							"select tasks.* " +
									"  from tasks " +
									" where tasks.objective_id = ? "
							);
					stmt2.setInt(1, objID);
						
					resultSet = stmt2.executeQuery();
					
					
					while (resultSet.next()) {
						success = true;
						// create new item object
						// retrieve attributes from resultSet starting with index 1
						Task resultTask = new Task();
						loadTask(resultTask, resultSet, 1);
						System.out.println(resultTask.getTaskID());
					}
					
					// check if the item was found
					if (!success) {
						System.out.println("<Task> was not inserted in the objectives table");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	@Override
	public boolean addItemToTask(Item item, int taskID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"insert into usedItems (task_id, name, canBePickedUp, xPosition, yPosition, roomPosition)" +
									" values (?,?,?,?,?,?) "
							);
					stmt.setInt(1, taskID);
					stmt.setString(2, item.getName());
					stmt.setString(3,  item.getCanBePickedUp().toString());
					stmt.setInt(4, item.getXPosition());
					stmt.setInt(5, item.getYPosition());
					stmt.setInt(6, item.getRoomPosition());
					stmt.execute();
					Boolean result = false;
					Boolean success = false;

					//selects all details of the new user
					stmt2 = conn.prepareStatement(
							"select usedItems.* " +
									"  from usedItems " +
									" where usedItems.name = ? and usedItems.task_id = ?"
							);
					stmt2.setString(1, item.getName());
					stmt2.setInt(2, taskID);
						
					
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
						success = true;
						// create new item object
						// retrieve attributes from resultSet starting with index 1
						Item resultItem = new Item();
						loadItem(resultItem, resultSet, 1);
						System.out.println(resultItem.getName());
						result = true;
					}
					// check if the item was found
					if (!success) {
						System.out.println("<" + item.getName() + "> was not inserted in the roomInventories table");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
		
	@Override
	public boolean addItemToRoom(Item item, int roomID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				List<Item> currentItems = new ArrayList<Item>();
				currentItems = findRoomInventoryByID(roomID);
				for(Item roomItem : currentItems) {
					if(item.getName().equals(roomItem.getName())) {
						return false;
					}
				}
				
				try {
					stmt = conn.prepareStatement(
							"insert into roomInventories (room_id, name, canBePickedUp, xPosition, yPosition, roomPosition)" +
									" values (?,?,?,?,?,?) "
							);
					stmt.setInt(1, roomID);
					stmt.setString(2, item.getName());
					stmt.setString(3,  item.getCanBePickedUp().toString());
					stmt.setInt(4, item.getXPosition());
					stmt.setInt(5, item.getYPosition());
					stmt.setInt(6, item.getRoomPosition());
					stmt.execute();
					
					Boolean result = false;
					Boolean success = false;

					//selects all details of the new user
					stmt2 = conn.prepareStatement(
							"select roomInventories.* " +
									"  from roomInventories " +
									" where roomInventories.name = ? and roomInventories.room_id = ?"
							);
					stmt2.setString(1, item.getName());
					stmt2.setInt(2, roomID);
						
					
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
						success = true;
						// create new item object
						// retrieve attributes from resultSet starting with index 1
						Item resultItem = new Item();
						loadItem(resultItem, resultSet, 1);
						System.out.println(resultItem.getName());
						result = true;
					}
					// check if the item was found
					if (!success) {
						System.out.println("<" + item.getName() + "> was not inserted in the roomInventories table");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	
	@Override
	public boolean removeItemFromRoom(Item item, int roomID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				List<Item> currentItems = new ArrayList<Item>();
				currentItems = findRoomInventoryByID(roomID);
				Boolean itemExistsInRoom = false;
				for(Item roomItem : currentItems) {
					if(item.getName().equals(roomItem.getName())) {
						itemExistsInRoom = true;
					}
				}
				
				if(!itemExistsInRoom) {
					return false;
				}
						
				
				int itemID = item.getItemID();
				try {
					//deletes item from userInventories
					stmt = conn.prepareStatement(
							"delete from roomInventories " +
									" where roomInventories.item_id = ?"
							);
					stmt.setInt(1, itemID);
					

					
					stmt.execute();
					
					

					//sees if the item is still in the inventory
					stmt2 = conn.prepareStatement(
							"select roomInventories.* " +
									"  from roomInventories " +
									" where roomInventories.name = ? and roomInventories.room_id = ?"
							);
					stmt2.setString(1, item.getName());
					stmt2.setInt(2, roomID);
						
					
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
						System.out.println("<" + item.getName() + "> was not removed from the roomInventories table");

						return false;
					}
				
					return true;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	
	@Override
	public boolean removeItemFromInventory(Item item, int userID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				List<Item> currentItems = new ArrayList<Item>();
				currentItems = getUserInventoryByID(userID);
				Boolean itemExistsInRoom = false;
				for(Item invItem : currentItems) {
					if(item.getName().equals(invItem.getName())) {
						itemExistsInRoom = true;
					}
				}
				
				if(!itemExistsInRoom) {
					return false;
				}
						
				
				int itemID = item.getItemID();
				try {
					//deletes item from userInventories
					stmt = conn.prepareStatement(
							"delete from userInventories " +
									" where userInventories.item_id = ?"
							);
					stmt.setInt(1, itemID);
					
					stmt.execute();

					//sees if the item is still in the inventory
					stmt2 = conn.prepareStatement(
							"select userInventories.* " +
									"  from userInventories " +
									" where userInventories.name = ? and userInventories.user_id = ?"
							);
					stmt2.setString(1, item.getName());
					stmt2.setInt(2, userID);
						
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
				
						System.out.println("<" + item.getName() + "> was not removed from the userInventories table");
						return false;
					}
					
					return true;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	@Override
	public boolean removeItemFromUsedItems(int taskId) {
	    return executeTransaction(new Transaction<Boolean>() {
	        @Override
	        public Boolean execute(Connection conn) throws SQLException {
	            PreparedStatement stmt = null;
	            PreparedStatement stmt2 = null;
	            ResultSet resultSet = null;
	            try {
	                stmt = conn.prepareStatement(
	                        "delete from usedItems " +
	                			"where usedItems.task_id = ? ");
	                stmt.setInt(1, taskId);
	                stmt.execute();

	                // Check if the item was actually removed
	                stmt2 = conn.prepareStatement(
	                        "select usedItems.* "+
	                        		"from usedItems "
	                        		+ "where usedItems.task_id = ?");
	                stmt2.setInt(1, taskId);
	                resultSet = stmt2.executeQuery();

	                if (resultSet.next()) {
	                    System.out.println("Item" + taskId + " was not removed");
	                    return false;
	                }

	                return true;

	            } finally {
	                DBUtil.closeQuietly(resultSet);
	                DBUtil.closeQuietly(stmt);
	                DBUtil.closeQuietly(stmt2);
	            }
	        }
	    });
	}



	@Override
	public boolean addItemToInventory(Item item, int userID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				List<Item> currentItems = new ArrayList<Item>();
				currentItems = getUserInventoryByID(userID);
				for(Item invItem : currentItems) {
					if(item.getName().equals(invItem.getName())) {
						return false;
					}
				}
				
						
				
				try {
					//inserts item into userInventory
					stmt = conn.prepareStatement(
							"insert into userInventories (user_id, name, canBePickedUp, xPosition, yPosition, roomPosition) values (?, ?, ?, ?, ?, ?)"
							);
					stmt.setInt(1, userID);
					stmt.setString(2, item.getName());
					stmt.setString(3,  item.getCanBePickedUp().toString());
					stmt.setInt(4, item.getXPosition());
					stmt.setInt(5, item.getYPosition());
					stmt.setInt(6, item.getRoomPosition());

					
					stmt.execute();
					
					Boolean result = false;
					Boolean success = false;

					//selects all details of the new user
					stmt2 = conn.prepareStatement(
							"select userInventories.* " +
									"  from userInventories " +
									" where userInventories.name = ? and userInventories.user_id = ?"
							);
					stmt2.setString(1, item.getName());
					stmt2.setInt(2, userID);
						
					
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
						success = true;
						// create new User object
						// retrieve attributes from resultSet starting with index 1
						Item resultItem = new Item();
						loadItem(resultItem, resultSet, 1);
						
						result = true;
					}
					// check if the user was found
					if (!success) {
						System.out.println("<" + item.getName() + "> was not inserted in the userInventories");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	private Room getRoomByUserID(int userID) {
		//throw new UnsupportedOperationException();
		return executeTransaction(new Transaction<Room>() {
			@Override
			public Room execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select rooms.* " +
							"  from rooms " +
							" where rooms.user_id = ? "
					);
					stmt.setInt(1, userID);
					
					Room result = null;
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						// create new User object
						// retrieve attributes from resultSet starting with index 1
						Room room = new Room();
						loadRoom(room, resultSet, 1);
						// load inventory objects
						room.setItems(findRoomInventoryByID(room.getRoomID()));
						result = room;
					}
					
					// check if the user was found
					if (!found) {
						System.out.println("<" + userID + "> was not found in the rooms table");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	private List<Item> getUserInventoryByID(int userID) {
		return executeTransaction(new Transaction<List<Item>>() {
			@Override
			public List<Item> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
							"select userInventories.* " +
									"  from userInventories " +
									" where userInventories.user_id = ? "
							);
					stmt.setInt(1, userID);

					List<Item> result = new ArrayList<Item>();

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						// create new User object
						// retrieve attributes from resultSet starting with index 1
						Item item = new Item();
						loadItem(item, resultSet, 1);
						// load inventory objects
						result.add(new Item(item));
					}

					// check if the user was found
					if (!found) {
						System.out.println("<No items found for userID: " + userID + ">");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	@Override
	public List<Item> findRoomInventoryByID(int roomID) {
		return executeTransaction(new Transaction<List<Item>>() {
			@Override
			public List<Item> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select roomInventories.* " +
							"  from roomInventories " +
							" where roomInventories.room_id = ? "
					);
					stmt.setInt(1, roomID);
					List<Item> result = new ArrayList<Item>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						// create new User object
						// retrieve attributes from resultSet starting with index 1
						Item item = new Item();
						loadItem(item, resultSet, 1);
						// load inventory objects
						result.add(new Item(item));
					}
					
					// check if the user was found
					if (!found) {
						System.out.println("<No items found for roomID: " + roomID + ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	@Override
	public List<Item> findItemsInPositionByID(int roomID, int position){
		return executeTransaction(new Transaction<List<Item>>() {
			@Override
			public List<Item> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select roomInventories.* " +
							"  from roomInventories " +
							" where roomInventories.room_id = ? " +
							"and roomInventories.roomPosition = ? "
					);
					stmt.setInt(1, roomID);
					stmt.setInt(2, position);
					
					List<Item> result = new ArrayList<Item>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						// create new Item object
						// retrieve attributes from resultSet starting with index 1
						Item item = new Item();
						loadItem(item, resultSet, 1);
						// load inventory objects
						result.add(new Item(item));
					}
					
					// check if the user was found
					if (!found) {
						System.out.println("<No items found for roomID: " + roomID + " in position: " + position + ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}
	@Override
	public List<Item> getUsedItemsByTaskId(int taskId) {
	    List<Item> usedItems = new ArrayList<>();
	    return executeTransaction(new Transaction<List<Item>>() {
	        @Override
	        public List<Item> execute(Connection conn) throws SQLException {
	            PreparedStatement stmt = null;
	            ResultSet resultSet = null;

	            try {
	                stmt = conn.prepareStatement(
	                		"select usedItems.* " +
	                				"from usedItems " +
	                				" where usedItems.task_id = ? "
	                );
	                stmt.setInt(1, taskId);

	                List<Item> result = new ArrayList<Item>();

	                resultSet = stmt.executeQuery();

	                // for testing that a result was returned
	                Boolean found = false;

	                while (resultSet.next()) {
	                    found = true;

	                    // create new Item object
	                    // retrieve attributes from resultSet starting with index 1
	                    Item item = new Item();
	                    loadItem(item, resultSet, 1);
	                    // load inventory objects
	                    result.add(new Item(item));
	                }

	                // check if the user was found
	                if (!found) {
	                    System.out.println("<No items found for taskId: " + taskId + ">");
	                } else {
	                    usedItems.addAll(result);
	                }

	                return usedItems;
	            } finally {
	                DBUtil.closeQuietly(resultSet);
	                DBUtil.closeQuietly(stmt);
	            }
	        }
	    });
	}

	
	@Override
	public Item findItemByNameAndIDInRoom(String name, int roomID) {
		return executeTransaction(new Transaction<Item>() {
			@Override
			public Item execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select roomInventories.* " +
							"  from roomInventories " +
							" where roomInventories.room_id = ? " +
							"and roomInventories.name = ? "
					);
					stmt.setInt(1, roomID);
					stmt.setString(2, name);
					
					Item result = new Item();
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						// create new Item object
						// retrieve attributes from resultSet starting with index 1
						Item item = new Item();
						loadItem(item, resultSet, 1);
						// load inventory objects
						result = item;
					}
					
					// check if the item was found
					if (!found) {
						System.out.println("<No items found for roomID: " + roomID + " by the name: " + name + ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}
	
	@Override
	public Item findItemByNameAndIDInInv(String name, int userID) {
		return executeTransaction(new Transaction<Item>() {
			@Override
			public Item execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select userInventories.* " +
							"  from userInventories " +
							" where userInventories.user_id = ? " +
							"and userInventories.name = ? "
					);
					stmt.setInt(1, userID);
					stmt.setString(2, name);
					
					Item result = new Item();
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						// create new Item object
						// retrieve attributes from resultSet starting with index 1
						Item item = new Item();
						loadItem(item, resultSet, 1);
						// load inventory objects
						result = item;
					}
					
					// check if the item was found
					if (!found) {
						System.out.println("<No items found for roomID: " + userID + " by the name: " + name + ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}
	
	private void loadUser(User user, ResultSet result, int index) throws SQLException {
		user.setUserID(result.getInt(index++));
		user.setUsername(result.getString(index++));
		user.setPassword(result.getString(index++));
		user.setInventoryLimit(result.getInt(index++));
		user.setTime(result.getInt(index++));
	}
	
	private void loadRoom(Room room, ResultSet result, int index) throws SQLException {
		room.setRoomID(result.getInt(index++));
		room.setUserID(result.getInt(index++));
		room.setUserPosition(result.getInt(index++));
	}
	
	private void loadItem(Item item, ResultSet result, int index) throws SQLException {
		item.setItemID(result.getInt(index++));
		item.setSecondaryID(result.getInt(index++));
		item.setName(result.getString(index++));
		item.setCanBePickedUp(Boolean.parseBoolean(result.getString(index++)));
		item.setXPosition(result.getInt(index++));
		item.setYPosition(result.getInt(index++));
		item.setRoomPosition(result.getInt(index++));
	}
	
	private void loadTask(Task task, ResultSet result, int index) throws SQLException {
		task.setTaskID(result.getInt(index++));
		task.setObjectiveID(result.getInt(index++));
		task.setName(result.getString(index++));
		task.setIsStarted(Boolean.parseBoolean(result.getString(index++)));
		task.setIsComplete(Boolean.parseBoolean(result.getString(index++)));
	}
	
	private void loadObjective(Objective objective, ResultSet result, int index) throws SQLException {
		objective.setObjectiveID(result.getInt(index++));
		objective.setRoomID(result.getInt(index++));
		objective.setIsStarted(Boolean.parseBoolean(result.getString(index++)));
		objective.setIsComplete(Boolean.parseBoolean(result.getString(index++)));
	}

	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}

	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();

		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;

			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}

			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}

			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	public Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:database.db;create=true");

		// Set autocommit to false to allow execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
	}

	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;
				PreparedStatement stmt7 = null;

				try {
					stmt1 = conn.prepareStatement(
							"create table users (" +
									"	user_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +									
									"	username varchar(32)," +
									"	password varchar(32)," +
									"	inventoryLimit integer, " +
									"	time integer " +
									")"
							);	
					stmt1.executeUpdate();

					stmt2 = conn.prepareStatement(
							"create table rooms (" +
									"	room_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	user_id integer constraint fk_room_userid references users(user_id), " +
									"	userPosition integer " +
									")"
							);
					stmt2.executeUpdate();

					stmt3 = conn.prepareStatement(
							"create table userInventories (" +
									"	item_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	user_id integer constraint fk_inv_userid references users(user_id), " +
									"	name varchar(40)," +
									"	canBePickedUp varchar(10)," + 
									"	xPosition integer," +
									"	yPosition integer," +
									"	roomPosition integer " +
									")"
							);
					stmt3.executeUpdate();

					stmt4 = conn.prepareStatement(
							"create table roomInventories (" +
									"	item_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	room_id integer constraint fk_inv_roomid references rooms(room_id), " +
									"	name varchar(40)," +
									"	canBePickedUp varchar(10)," + 
									"	xPosition integer," +
									"	yPosition integer," +
									"	roomPosition integer " +
									")"
							);
					stmt4.executeUpdate();
					
					stmt5 = conn.prepareStatement(
							"create table objectives (" +
									"	objective_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	room_id integer constraint fk_obj_roomid references rooms(room_id), " +
									"	isStarted varchar(10)," +
									"	isComplete varchar(10) " +
									")"
							);
					stmt5.executeUpdate();
					
					stmt6 = conn.prepareStatement(
							"create table tasks (" +
									"	task_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	objective_id integer constraint fk_task_objid references objectives(objective_id), " +
									"	name varchar(40)," +
									"	isStarted varchar(10)," + 
									"	isComplete varchar(10) " +
									")"
							);
					stmt6.executeUpdate();
					
					stmt7 = conn.prepareStatement(
							"create table usedItems (" +
									"	item_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +
									"	task_id integer constraint fk_inv_taskid references tasks(task_id), " +
									"	name varchar(40)," +
									"	canBePickedUp varchar(10)," + 
									"	xPosition integer," +
									"	yPosition integer," +
									"	roomPosition integer " +
									")"
							);
					stmt7.executeUpdate();

					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt6);
					DBUtil.closeQuietly(stmt7);
				}
			}
		});
	}

	@Override
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<User> userList;

				try {
					userList = InitialData.getUsers();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertUser = null;
				PreparedStatement insertRoom   = null;
				PreparedStatement insertUserInventory = null;
				PreparedStatement insertRoomInventory = null;
				PreparedStatement insertObjective = null;
				PreparedStatement insertTask = null;
				PreparedStatement insertUsedItem = null;


				try {
					// populate users table
					insertUser = conn.prepareStatement("insert into users (username, password, inventoryLimit, time) values (?, ?, ?, ?)");
					for (User user : userList) {
						//						//insertAuthor.setInt(1, author.getAuthorId());	// auto-generated primary key, don't insert this
						insertUser.setString(1, user.getUsername());
						insertUser.setString(2, user.getPassword());
						insertUser.setInt(3, user.getInventoryLimit());
						insertUser.setInt(4, user.getTime());
						insertUser.addBatch();
					}
					insertUser.executeBatch();

					// populate rooms table
					insertRoom = conn.prepareStatement("insert into rooms (user_id, userPosition) values (?, ?)");
					for (int i=0; i < userList.size(); i++) {
						insertRoom.setInt(1, userList.get(i).getUserID());
						insertRoom.setInt(2, userList.get(i).getRoom().getUserPosition());
						insertRoom.addBatch();
					}
					insertRoom.executeBatch();

					insertUserInventory = conn.prepareStatement("insert into userInventories (user_id, name, canBePickedUp, xPosition, yPosition, roomPosition) values (?, ?, ?, ?, ?, ?)");
					for (User user : userList) {
						for (Item item : user.getInventory()) {
							insertUserInventory.setInt(1, user.getUserID());
							insertUserInventory.setString(2, item.getName());
							insertUserInventory.setString(3, item.getCanBePickedUp().toString());
							insertUserInventory.setInt(4, item.getXPosition());
							insertUserInventory.setInt(5, item.getYPosition());
							insertUserInventory.setInt(6, item.getRoomPosition());
							insertUserInventory.addBatch();
						}
					}
					insertUserInventory.executeBatch();

					insertRoomInventory = conn.prepareStatement("insert into roomInventories (room_id, name, canBePickedUp, xPosition, yPosition, roomPosition) values (?, ?, ?, ?, ?, ?)");
					for (int i=0; i < userList.size(); i++) {
						Room room = userList.get(i).getRoom();
						for (Item item : room.getItems()) {
							insertRoomInventory.setInt(1, room.getRoomID());
							insertRoomInventory.setString(2, item.getName());
							insertRoomInventory.setString(3, item.getCanBePickedUp().toString());
							insertRoomInventory.setInt(4, item.getXPosition());
							insertRoomInventory.setInt(5, item.getYPosition());
							insertRoomInventory.setInt(6, item.getRoomPosition());
							insertRoomInventory.addBatch();
						}
					}
					insertRoomInventory.executeBatch();
					
					insertObjective = conn.prepareStatement("insert into objectives (room_id, isStarted, isComplete) values (?, ?, ?)");
					for (int i=0; i < userList.size(); i++) {
						Room room = userList.get(i).getRoom();
						for (Objective obj : room.getObjectives()) {
							insertObjective.setInt(1, room.getRoomID());
							insertObjective.setString(2, obj.getIsStarted().toString());
							insertObjective.setString(3, obj.getIsComplete().toString());
							insertObjective.addBatch();
						}
					}
					insertObjective.executeBatch();
					
					insertTask = conn.prepareStatement("insert into tasks (objective_id, name, isStarted, isComplete) values (?, ?, ?, ?)");
					for (int i=0; i < userList.size(); i++) {
						Room room = userList.get(i).getRoom();
						for (Objective obj : room.getObjectives()) {
							for(Task task : obj.getTasks()) {
								insertTask.setInt(1, obj.getObjectiveID());
								insertTask.setString(2, task.getName());
								insertTask.setString(3, task.getIsStarted().toString());
								insertTask.setString(4, task.getIsComplete().toString());
								insertTask.addBatch();
							}
						}
					}
					insertTask.executeBatch();
					
					insertUsedItem = conn.prepareStatement("insert into usedItems (task_id, name, canBePickedUp, xPosition, yPosition, roomPosition) values (?, ?, ?, ?, ?, ?)");
					for (int i=0; i < userList.size(); i++) {
						Room room = userList.get(i).getRoom();
						for (Objective obj : room.getObjectives()) {
							for(Task task : obj.getTasks()) {
								for(Item item : task.getItems()) {
									insertUsedItem.setInt(1, task.getTaskID());
									insertUsedItem.setString(2, item.getName());
									insertUsedItem.setString(3, item.getCanBePickedUp().toString());
									insertUsedItem.setInt(4, item.getXPosition());
									insertUsedItem.setInt(5, item.getYPosition());
									insertUsedItem.setInt(6, item.getRoomPosition());
									insertUsedItem.addBatch();
								}
							}
						}
					}
					insertUsedItem.executeBatch();


					return true;
				} finally {
					DBUtil.closeQuietly(insertUser);
					DBUtil.closeQuietly(insertRoom);
					DBUtil.closeQuietly(insertUserInventory);
					DBUtil.closeQuietly(insertRoomInventory);
					DBUtil.closeQuietly(insertTask);
					DBUtil.closeQuietly(insertObjective);
				}
			}
		});
	}

	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();

		System.out.println("Loading initial data...");
		db.loadInitialData();

		System.out.println("Success!");
	}

	@Override
	public String useEmptyPotion(Item bottle, Item selected, User user) {
		String message = "Nothing Happened";
		System.out.println(selected.getName() + "is selected");
		if(selected.getName().equals("Cauldron with Potion")) {
			message = "You filled the bottle with a potion";
			Item fullPotion = new Item("Full Potion Bottle", false, 0,0,0);
			swapItemInInventory(bottle, fullPotion, user);
		}
		return message;
	}

	@Override
	public void swapItemInInventory(Item itemToRemove, Item itemToAdd, User user) {
		removeItemFromInventory(itemToRemove, user.getUserID());
		addItemToInventory(itemToAdd, user.getUserID());
	}

	@Override
	public void swapItemInRoom(Item itemToRemove, Item itemToAdd, User user) {
		int roomID = user.getRoom().getRoomID();
		removeItemFromRoom(itemToRemove, roomID);
		addItemToRoom(itemToAdd, roomID);
		
	}

	@Override
	public String useMatches(Item matches, Item selected, User user) {
		String message = "Nothing Happened";
		System.out.println(selected.getName() + "is selected");
		if(selected.getName().equals("Unlit Candle")) {
			message = "You lit the candle";
			Item litCandle = new Item("Lit Candle", true, 8,556,0);
			swapItemInRoom(selected, litCandle, user);
			removeItemFromInventory(matches, user.getUserID());
			
			//change which items in the room can be picked up
			Item itemToChange = new Item();
			List<Item> roomInv = user.getRoom().getItems();
			for(Item item : roomInv) {
				if(item.getName().equals("Jar of Cat Hairs") ||
						item.getName().equals("Clover") ||
						item.getName().equals("Jar with Hibiscus") ||
						item.getName().equals("Carton of Lime Juice") ||
						item.getName().equals("Wishbone")) {
					itemToChange = item;
					changeCanBePickedUp(user.getUserID(), itemToChange.getName(), true);
					System.out.println(changeCanBePickedUp(user.getUserID(), itemToChange.getName(), true));
				}
			}
		}
		return message;
	}

	@Override
	public boolean changeCanBePickedUp(int userID, String itemName, Boolean canBePickedUp) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;	
				
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"update roomInventories " +
									"set roomInventories.canBePickedUp = ? " +
									"where roomInventories.room_id = ? " +
									"and roomInventories.name = ?"
							);
					stmt.setString(1, canBePickedUp.toString());
					stmt.setInt(2, userID);
					stmt.setString(3, itemName);
				

					
					stmt.execute();
					
					Boolean result = false;
					Boolean success = false;

					//selects to see if it was changed
					stmt2 = conn.prepareStatement(
							"select roomInventories.canBePickedUp " +
									"  from roomInventories " +
									"where roomInventories.room_id = ? " +
									"and roomInventories.name = ?"
							);
			
					stmt2.setInt(1, userID);
					stmt2.setString(2, itemName);
						
					
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
						
						result = true;
						// see if result matches desired result
						
						String queryResult = resultSet.getString(1);
						if(queryResult.equals(canBePickedUp.toString())) {
							success = true;
						}
						
					}
					if (!success) {
						System.out.println("<" + itemName + "> was not updated");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});

	}

	
	@Override
	public int findRoomIDByUsername(String username) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				int userID = -1;
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"select users.user_id " +
									"  from users " +
									" where users.username = ? "
							);
					stmt.setString(1, username);
					stmt.execute();
					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;
						//get the integer for user id
						userID = resultSet.getInt(1);
					}
					
					if(found == false) {
						System.out.println("user id for <" + username + "> was not found");
					}

					//selects to see if it was changed
					stmt2 = conn.prepareStatement(
							"select rooms.room_id " +
									"  from rooms " +
									" where rooms.user_id = ?"
							);
			
					stmt2.setInt(1, userID);
						
					resultSet2 = stmt2.executeQuery();
					int result = -1;
					while (resultSet.next()) {
						result = resultSet.getInt(1);
					}
					
					return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	@Override
	public int findRoomIDByUserID(int userID) {
		return executeTransaction(new Transaction<Integer>() {
		@Override
		public Integer execute(Connection conn) throws SQLException {
			PreparedStatement stmt = null;
			PreparedStatement stmt2 = null;
			ResultSet resultSet = null;
				int roomID = -1;
			try {
				//changes canBePickedUp for the requested item
				stmt = conn.prepareStatement(
						"select rooms.room_id " +
								"  from rooms " +
								" where rooms.user_id = ? "
						);
				stmt.setInt(1, userID);
				stmt.execute();
				resultSet = stmt.executeQuery();

				// for testing that a result was returned
				Boolean found = false;

				while (resultSet.next()) {
					found = true;
					//get the integer for user id
					roomID = resultSet.getInt(1);
				}
				
				if(found == false) {
					System.out.println("room id for <" + userID + "> was not found");
				}
				//return result
				return roomID;
			} finally {
				DBUtil.closeQuietly(resultSet);
				DBUtil.closeQuietly(stmt);
				DBUtil.closeQuietly(stmt2);
			}
		}
	});
	}
	
	
	@Override
	public List<Item> findItemsInInventory(int userID){
		return executeTransaction(new Transaction<List<Item>>() {
			@Override
			public List<Item> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select userInventories.* " +
							"  from userInventories " +
							" where userInventories.user_id = ? " 
					);
					stmt.setInt(1, userID);

					
					List<Item> result = new ArrayList<Item>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						// create new Item object
						// retrieve attributes from resultSet starting with index 1
						Item item = new Item();
						loadItem(item, resultSet, 1);
						// load inventory objects
						result.add(new Item(item));
					}
					
					// check if the user was found
					if (!found) {
						System.out.println("<No items found for userID: " + userID + ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}
	
	
	@Override
	public boolean getCanBePickedUp(int userID, String itemName) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;	
				
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"select roomInventories.canBePickedUp " +
									"from roomInventories " +
									"where roomInventories.room_id = ? " +
									"and roomInventories.name = ?"
							);
					stmt.setInt(1, userID);
					stmt.setString(2, itemName);
					stmt.execute();
					Boolean result = false;
					Boolean success = false;
					
					resultSet = stmt.executeQuery();
					
					while (resultSet.next()) {
						success = true;
						result = resultSet.getBoolean(1);
						
					}
					if (!success) {
						System.out.println("<" + itemName + "> canBePickedUp was found");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});

	}

	@Override
	public Boolean changeObjectiveIsStarted(int objectiveID, Boolean desiredResult) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;	
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"update objectives " +
									"set objectives.isStarted = ? " +
									"where objectives.objective_id = ?" 
							);
					stmt.setString(1, desiredResult.toString());
					stmt.setInt(2, objectiveID);
					stmt.execute();
					Boolean result = false;
					Boolean success = false;

					//selects to see if it was changed
					stmt2 = conn.prepareStatement(
							"select objectives.isStarted " +
									"  from objectives " +
									"where objectives.objective_id = ?"
							);
					stmt2.setInt(1, objectiveID);
					resultSet = stmt2.executeQuery();
					while (resultSet.next()) {
						
						result = true;
						// see if result matches desired result
						
						String queryResult = resultSet.getString(1);
						if(queryResult.equals(desiredResult.toString())) {
							success = true;
						}
					}
					if (!success) {
						System.out.println("<Objective isStarted> was not updated");
					}
					return result;
					//return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	@Override
	public Boolean changeObjectiveIsComplete(int objectiveID, Boolean desiredResult) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;	
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"update objectives " +
									"set objectives.isComplete = ? " +
									"where objectives.objective_id = ?" 
							);
					stmt.setString(1, desiredResult.toString());
					stmt.setInt(2, objectiveID);
					stmt.execute();
					Boolean result = false;
					Boolean success = false;
					//selects to see if it was changed
					stmt2 = conn.prepareStatement(
							"select objectives.isComplete " +
									"  from objectives " +
									"where objectives.objective_id = ?"
							);
			
					stmt2.setInt(1, objectiveID);
					resultSet = stmt2.executeQuery();
					while (resultSet.next()) {
						result = true;
						// see if result matches desired result
						
						String queryResult = resultSet.getString(1);
						if(queryResult.equals(desiredResult.toString())) {
							success = true;
						}
					}
					if (!success) {
						System.out.println("<Objective isComplete> was not updated");
					}
					return result;
					//return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	@Override
	public Boolean changeTaskIsComplete(int taskID, Boolean desiredResult) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;	
				
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"update tasks " +
									"set tasks.isComplete = ? " +
									"where tasks.task_id = ?" 
							);
					stmt.setString(1, desiredResult.toString());
					stmt.setInt(2, taskID);
					stmt.execute();
					Boolean result = false;
					Boolean success = false;
					//selects to see if it was changed
					stmt2 = conn.prepareStatement(
							"select tasks.isComplete " +
									"  from tasks " +
									"where tasks.task_id = ?"
							);
			
					stmt2.setInt(1, taskID);
					resultSet = stmt2.executeQuery();
					while (resultSet.next()) {
						
						result = true;
						// see if result matches desired result
						
						String queryResult = resultSet.getString(1);
						if(queryResult.equals(desiredResult.toString())) {
							success = true;
						}
					}
					if (!success) {
						System.out.println("<Task isComplete> was not updated");
					}
					return result;
					//return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	@Override
	public Boolean changeTaskIsStarted(int taskID, Boolean desiredResult) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;	
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"update tasks " +
									"set tasks.isStarted = ? " +
									"where tasks.task_id = ?" 
							);
					stmt.setString(1, desiredResult.toString());
					stmt.setInt(2, taskID);
					stmt.execute();
					Boolean result = false;
					Boolean success = false;
					//selects to see if it was changed
					stmt2 = conn.prepareStatement(
							"select tasks.isStarted " +
									"  from tasks " +
									"where tasks.task_id = ?"
							);
			
					stmt2.setInt(1, taskID);
					resultSet = stmt2.executeQuery();
					
					while (resultSet.next()) {
						
						result = true;
						// see if result matches desired result
						
						String queryResult = resultSet.getString(1);
						if(queryResult.equals(desiredResult.toString())) {
							success = true;
						}
						
					}
					if (!success) {
						System.out.println("<Task isStarted> was not updated");
					}
					return result;
					//return result;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	@Override
	public List<Task> getTasksByObjID(int objectiveID) {
		return executeTransaction(new Transaction<List<Task>>() {
			@Override
			public List<Task> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select tasks.* " +
							"  from tasks " +
							" where tasks.objective_id = ? " 
					);
					stmt.setInt(1, objectiveID);

					
					List<Task> result = new ArrayList<Task>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						// create new Item object
						// retrieve attributes from resultSet starting with index 1
						Task task = new Task();
						loadTask(task, resultSet, 1);
						// load inventory objects
						if(task.getName().equals("PotionMachine")) {
							PotionMachine newTask = new PotionMachine(task);
							result.add(newTask);
						}else if(task.getName().equals("Cat")) {
							Cat newTask = new Cat(task);
							result.add(newTask);
						}else if(task.getName().equals("Passcode")) {
							Passcode newTask = new Passcode(task);
							result.add(newTask);
						}else if(task.getName().equals("Bookshelf")) {
							Bookshelf newTask = new Bookshelf(task);
							result.add(newTask);
						}else if(task.getName().equals("Puzzle")) {
							Puzzle newTask = new Puzzle(task);
							result.add(newTask);
						}else if(task.getName().equals("Window")) {
							Window newTask = new Window(task);
							result.add(newTask);
						}else {
							result.add(task);
						}
					}
					
					// check if the user was found
					if (!found) {
						System.out.println("<No tasks found for objectiveID: " + objectiveID + ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}
	
	@Override
	public List<Objective> getObjectivesByRoomID(int roomID) {
		return executeTransaction(new Transaction<List<Objective>>() {
			@Override
			public List<Objective> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select objectives.* " +
							"  from objectives " +
							" where objectives.room_id = ? " 
					);
					stmt.setInt(1, roomID);

					
					List<Objective> result = new ArrayList<Objective>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						// create new Item object
						// retrieve attributes from resultSet starting with index 1
						Objective objective = new Objective();
						loadObjective(objective, resultSet, 1);
						result.add(objective);
					}
					
					// check if the user was found
					if (!found) {
						System.out.println("<No objectives found for roomID: " + roomID + ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}

	@Override
	public int getTaskIDByNameAndObjectiveID(String taskName, int objectiveID) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select tasks.task_id " +
							"  from tasks " +
							" where tasks.name = ? " +
							"and tasks.objective_id = ?"
					);
					stmt.setString(1, taskName);
					stmt.setInt(2, objectiveID);

					
					int result = -1;
					
					resultSet = stmt.executeQuery();
					

					
					while (resultSet.next()) {
						result = resultSet.getInt(1);
					}
					
					// check if the user was found
					if (result == -1) {
						System.out.println("<No tasks id was found for name: " + taskName+ ">");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}

	@Override
	public Objective getObjectiveByObjectiveID(int objectiveID) {
		return executeTransaction(new Transaction<Objective>() {
			@Override
			public Objective execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select objectives.* " +
							"  from objectives " +
							" where objectives.objective_id = ? "
					);
					stmt.setInt(1, objectiveID);

					
					Objective result = new Objective();
					Boolean found = false;
					
					resultSet = stmt.executeQuery();
					

					
					while (resultSet.next()) {
						loadObjective(result, resultSet, 1);
						found = true;
					}
					
					// check if the user was found
					if (found) {
						System.out.println("<No objective id was found for id: <" + objectiveID + ">");
						return result;
					} else {
						return null;
					}
					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			
		
			}
		});
	}

	@Override
	public Task getTaskByTaskID(int taskID) {
		return executeTransaction(new Transaction<Task>() {
			@Override
			public Task execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select tasks.* " +
							"  from tasks " +
							" where tasks.task_id = ? "
					);
					stmt.setInt(1, taskID);

					
					Task result = new Task();
					Boolean found = false;
					
					resultSet = stmt.executeQuery();
					

					
					while (resultSet.next()) {
						loadTask(result, resultSet, 1);
						found = true;
					}
					
					// check if the user was found
					if (found) {
						System.out.println("<No task id was found for id: <" + taskID + ">");
						return result;
					} else {
						return null;
					}
					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
}
