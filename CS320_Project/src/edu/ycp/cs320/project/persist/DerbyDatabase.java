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
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean transferItemFromUserToRoom(User user, String itemName) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean moveUser(User user, int moveTo) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public boolean addUser(User user) {
		throw new UnsupportedOperationException();
	}
	
	private Room findRoomByUserID(int userID) {
		throw new UnsupportedOperationException();

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
	
	private void loadUser(User user, ResultSet result, int index) throws SQLException {
		user.setUserID(result.getInt(index++));
		user.setUsername(result.getString(index++));
		user.setPassword(result.getString(index++));
		user.setInventoryLimit(result.getInt(index++));
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
}
