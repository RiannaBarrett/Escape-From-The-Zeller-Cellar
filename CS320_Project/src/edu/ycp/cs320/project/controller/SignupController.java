package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.model.Item;
import edu.ycp.cs320.project.model.User;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;

public class SignupController {
	private IDatabase db = null;
	
	public SignupController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		this.db = DatabaseProvider.getInstance();
	}

	public boolean validateSignup(String username, String password) {
	    // check if the user name and password meet your criteria for validity
	    if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
	        // return false if the user name or password is null or empty
	        return false;
	    } else {
	        // create a new User object
	        User newUser = new User(username, password);
	        IDatabase db = DatabaseProvider.getInstance();
	        
	        boolean success = false;
	        if(username.contains("|") || username.contains(";") || username.contains("^") || 
					username.contains("\"") || username.contains("'") || password.contains("|") ||
					password.contains(";") || password.contains("^") || password.contains("\"") || 
					password.contains("'")) {
				return false;
			}
	        User existingUser = db.findUserByName(username);

	        if(existingUser == null) {
	        	success = db.addUser(newUser);
	        	
	        }
	        if(success) {
	        	User user = db.findUserByName(username);
	        	int userID = user.getUserID();
	        	int roomID = user.getRoom().getRoomID();
	        	Item matches = new Item("Matches", true,23,544,0);
	        	db.addItemToInventory(matches, userID);
	        	
	        	Item shelf = new Item("Shelf",false,-875,-820,0);
	        	db.addItemToRoom(shelf, roomID);
	        	Item hairs = new Item("Jar of Cat Hairs",false,108,230,0);
	        	db.addItemToRoom(hairs, roomID);
	        	Item hibiscus = new Item("Jar with Hibiscus",false,108,190,0);
	        	db.addItemToRoom(hibiscus, roomID);
	        	Item emptyBottle = new Item("Empty Potion Bottle",false,111,145,0);
	        	db.addItemToRoom(emptyBottle, roomID);
	        	Item untitledBook = new Item("Untitled Book",false,75,385,0);
	        	db.addItemToRoom(untitledBook, roomID);
	        	Item fireBook = new Item("Fire Spinning Book",false,65,405,0);
	        	db.addItemToRoom(fireBook, roomID);
	        	Item candle = new Item("Unlit Candle",false,113,195,0);
	        	db.addItemToRoom(candle, roomID);
	        	Item clover = new Item("Clover",false,113,225,0);
	        	db.addItemToRoom(clover, roomID);
	        	Item bone = new Item("Wishbone",false,166,145,0);
	        	db.addItemToRoom(bone, roomID);
	        	Item juice = new Item("Carton of Lime Juice",false,166,225,0);
	        	db.addItemToRoom(juice, roomID);
	        	Item cauldron = new Item("Empty Cauldron",false,-450,-635,0);
	        	db.addItemToRoom(cauldron, roomID);
	        	
	        	System.out.println(newUser.getUsername() +" & " + newUser.getPassword());
	        }
	        
	        // return true if the sign-up was successful
	        return success;
	    }
	}


}