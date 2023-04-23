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

	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;

	
	
	// New Methods Go Here
	@Override
	public User findUserByName(String name) {
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

	@Override
	public boolean transferItemFromUserToRoom(User user, String itemName) {
		throw new UnsupportedOperationException();
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
	public boolean addUser(User user) {
		return executeTransaction(new Transaction<Boolean>() {
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
							"insert into users (username, password, inventoryLimit)" +
									"values (?,?,?) "
							);
					stmt.setString(1, user.getUsername());
					stmt.setString(2, user.getPassword());
					stmt.setInt(3, user.getInventoryLimit());
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
					
					// check if the user was found
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
				currentItems = getRoomInventoryByID(roomID);
				for(Item roomItem : currentItems) {
					if(item.getName().equals(roomItem.getName())) {
						return false;
					}
				}
				
						
				
				try {
					//inserts item into roomInventory
					stmt = conn.prepareStatement(
							"insert into roomInventories (room_id, name, canBePickedUp, xPosition, yPosition, roomPosition)" +
									"values (?,?,?,?,?,?) "
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

					//selects all details of the new item
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
				currentItems = getRoomInventoryByID(roomID);
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
							"insert into userInventories (user_id, name, canBePickedUp, xPosition, yPosition, roomPosition)" +
									"values (?,?,?,?,?,?) "
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
						room.setItems(getRoomInventoryByID(room.getRoomID()));
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
	
	private List<Item> getRoomInventoryByID(int roomID) {
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
	
	private void loadUser(User user, ResultSet result, int index) throws SQLException {
		user.setUserID(result.getInt(index++));
		user.setUsername(result.getString(index++));
		user.setPassword(result.getString(index++));
		user.setInventoryLimit(result.getInt(index++));
	}
	
	private void loadRoom(Room room, ResultSet result, int index) throws SQLException {
		room.setRoomID(result.getInt(index++));
		room.setUserID(result.getInt(index++));
		room.setUserPosition(result.getInt(index++));
	}
	
	private void loadItem(Item item, ResultSet result, int index) throws SQLException {
		item.setItemID(result.getInt(index++));
		// Skip User ID
		index++;
		item.setName(result.getString(index++));
		item.setCanBePickedUp(Boolean.parseBoolean(result.getString(index++)));
		item.setXPosition(result.getInt(index++));
		item.setYPosition(result.getInt(index++));
		item.setRoomPosition(result.getInt(index++));
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

	private Connection connect() throws SQLException {
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

				try {
					stmt1 = conn.prepareStatement(
							"create table users (" +
									"	user_id integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +									
									"	username varchar(32)," +
									"	password varchar(32)," +
									"	inventoryLimit integer " +
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
									"	canBePickedUp varchar(10)," + // INCORRECT FIELD TYPE
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
									"	canBePickedUp varchar(10)," + // INCORRECT FIELD TYPE
									"	xPosition integer," +
									"	yPosition integer," +
									"	roomPosition integer " +
									")"
							);
					stmt4.executeUpdate();

					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
	}

	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<User> userList;
				List<Room> roomList;

				try {
					userList = InitialData.getUsers();
					roomList = InitialData.getRooms();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertUser = null;
				PreparedStatement insertRoom   = null;
				PreparedStatement insertUserInventory = null;
				PreparedStatement insertRoomInventory = null;

				try {
					// populate authors table (do authors first, since author_id is foreign key in books table)
					insertUser = conn.prepareStatement("insert into users (username, password, inventoryLimit) values (?, ?, ?)");
					for (User user : userList) {
						//						//insertAuthor.setInt(1, author.getAuthorId());	// auto-generated primary key, don't insert this
						insertUser.setString(1, user.getUsername());
						insertUser.setString(2, user.getPassword());
						insertUser.setInt(3, user.getInventoryLimit());
						insertUser.addBatch();
					}
					insertUser.executeBatch();

					// populate books table (do this after authors table,
					// since author_id must exist in authors table before inserting book)
					insertRoom = conn.prepareStatement("insert into rooms (user_id, userPosition) values (?, ?)");
					for (Room room : roomList) {
						//						insertBook.setInt(1, book.getBookId());		// auto-generated primary key, don't insert this
						insertRoom.setInt(1, room.getUserID());
						insertRoom.setInt(2, room.getUserPosition());
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
					for (Room room : roomList) {
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

					return true;
				} finally {
					DBUtil.closeQuietly(insertUser);
					DBUtil.closeQuietly(insertRoom);
					DBUtil.closeQuietly(insertUserInventory);
					DBUtil.closeQuietly(insertRoomInventory);
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
			Item litCandle = new Item("Lit Candle", false, -200,-305,1);
			swapItemInRoom(selected, litCandle, user);
			removeItemFromInventory(matches, user.getUserID());
		}
		return message;
	}

	@Override
	public boolean changeCanBePickedUp(User user, Item item, Boolean canBePickedUp) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				
				
					int itemID = item.getItemID();	
				
				try {
					//changes canBePickedUp for the requested item
					stmt = conn.prepareStatement(
							"update roomInventories " +
									"set roomInventories.canBePickedUp = ? " +
									"where roomInventories.item_id = ?"
							);
					stmt.setString(1, canBePickedUp.toString());
					stmt.setInt(2, itemID);
				

					
					stmt.execute();
					
					Boolean result = false;
					Boolean success = false;

					//selects to see if it was changed
					stmt2 = conn.prepareStatement(
							"select roomInventories.canBePickedUp " +
									"  from roomInventories " +
									" where roomInventories.item_id = ?"
							);
			
					stmt2.setInt(1, itemID);
						
					
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
						System.out.println("<" + item.getName() + "> was not updated");
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
	public String usePotionIngredient(Item item, Item selected, User user) {
		//message telling the user they successfully added an item
				String message = "You put the item in the cauldron";
				List<Item> items = user.getRoom().getItems();
				List<Item> ingredients = new ArrayList<Item>();
				
				//items with position 4 are items that were used on the empty cauldron. get these items
				for(int i = 0; i<items.size();i++) {
					if(items.get(i).getRoomPosition() == 4) {
						ingredients.add(items.get(i));
						System.out.println("Ingredient added: " + items.get(i).getName());
					}
				}
				
				
				user = findUserByName(user.getUsername());
				Item itemToAdd = item;
				//set the position to 4 (because it is being added to the cauldron
				itemToAdd.setRoomPosition(4);
				System.out.println("Number of ingredients before adding: " + ingredients.size());
				//2 represents the number of ingredients needed. Change this later when all ingredients are added
				if(ingredients.size() < 2) {
					//add it to the room and the current list of ingredients
					addItemToRoom(itemToAdd, user.getRoom().getRoomID());
					//refresh user
					user = findUserByName(user.getUsername());
					//add to ingredients to keep track of the number of ingredients
					ingredients.add(itemToAdd);
					//remove the item that is used
					removeItemFromInventory(item, user.getUserID());
				}
				
				System.out.println("Number of ingredients after adding: " + ingredients.size());
				//check if correct number of ingredients were added
				if(ingredients.size() >= 2) {
					//check if the ingredients are correct and in the right order
					if(ingredients.get(0).getName().equals("Jar of Cat Hairs") &&
							ingredients.get(1).getName().equals("Jar with Hibiscus")) {
							//make a full cauldron item by changing the name 
						Item emptyCauldron = selected;
						Item fullCauldron = selected;
						fullCauldron.setName("Cauldron with potion");
						//if the potion was made swap the empty cauldron with the full cauldron into the room
						swapItemInRoom(emptyCauldron, fullCauldron, user);
						//message telling the user they were successful
						message = "You created a potion";
						Item firstItem = ingredients.get(0);
						Item secondItem = ingredients.get(1);
						//remove the items (they do not need to be used anymore)
						removeItemFromRoom(firstItem, user.getRoom().getRoomID());
						user = findUserByName(user.getUsername());
						secondItem = user.getRoom().getItems().get(user.getRoom().getItems().size()-2);
						System.out.println("removing: " + secondItem.getName());
						removeItemFromRoom(secondItem, user.getRoom().getRoomID());
					}else {
						//if they are incorrect return the items to inventory and remove them from ingredient list
						Item firstItem = ingredients.get(0);
						Item secondItem = ingredients.get(1);
						
						//return the incorrect items so the user can try again
						addItemToInventory(firstItem, user.getUserID());
						addItemToInventory(secondItem, user.getUserID());
						
						//remove items from cauldron
						removeItemFromRoom(firstItem, user.getRoom().getRoomID());
						//refresh user
						user = findUserByName(user.getUsername());
						secondItem = user.getRoom().getItems().get(user.getRoom().getItems().size()-1);
						removeItemFromRoom(secondItem, user.getRoom().getRoomID());;
						//message telling the user they were not successful
						message = "The ingredients added did not seem to do anything";
					}
				}
				
		return message;
	}
}
