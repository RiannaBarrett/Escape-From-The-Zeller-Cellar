package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.project.model.*;

public class MainPageController {

	private MainPage model;
	private IDatabase db;
	
	public MainPageController() {
		model = new MainPage();
	}
	
	public MainPageController(MainPage model) {
		this.model = model;
	}
	
	public void setModel(MainPage model) {
		this.model = model;
	}
	public MainPage getModel() {
		return model;
	}
	
	public void PopulateModel(String username) {
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
		User user = db.findUserByName(username);
		if(user != null) {
			model.setUser(user);
		}
	}
	

	
	public boolean transferItemFromRoomToUser(String itemName) {

		// Does the user have inventory space?
		// MOVE TO CONTROLLER
		User user = model.getUser();
		if(user.getInventory().size() >= user.getInventoryLimit()) {
			return false;
		}
		
		Item itemToBeTransferred = findItemByName(itemName, user.getRoom().getItems());
		// Does the item exist in the room?
		if(itemToBeTransferred != null) {
			// Is the user able to interact with the item?
			//if(itemToBeTransferred.getRoomPosition() == user.getRoom().getUserPosition()) {
				// Can the item be picked up?
				if(itemToBeTransferred.getCanBePickedUp() == true) {
					
					return db.transferItemFromRoomToUser(user, itemToBeTransferred);
				}
			//}
		}
		return false;
	}
	
	// 3 sides, 1 up. 
	// 0, 1, 2 for sides
	// 3 for up
	public boolean moveUserLeft() {
		// Can't look left if looking up
		int currentPos = model.getUser().getRoom().getUserPosition();
		if(currentPos == 3) {
			return false;
		}
		// Max left at 0, loop back to 2
		if(currentPos == 0) {
			return db.moveUser(model.getUser(), 2);
		}
		else {
			return db.moveUser(model.getUser(), currentPos - 1);
		}
	}
	
	public boolean moveUserRight() {
		// Can't look left if looking up
		int currentPos = model.getUser().getRoom().getUserPosition();
		if(model.getUser().getRoom().getUserPosition() == 3) {
			return false;
		}
		// Max right at 2, loop back to 0
		if(currentPos == 2) {
			return db.moveUser(model.getUser(), 0);
		}
		else {
			return db.moveUser(model.getUser(), currentPos + 1);
		}
	}
	
	public boolean moveUserUp() {
		// Can't look up twice
		if(model.getUser().getRoom().getUserPosition() == 3) {
			return false;
		}
		else {
			return db.moveUser(model.getUser(), 3);
		}
	}
	
	public boolean moveUserDown() {
		// Can only look down if looking up
		if(model.getUser().getRoom().getUserPosition() != 3) {
			return false;
		}
		else {
			return db.moveUser(model.getUser(), 0);
		}
	}
	
	
	
	// Helper functions
	public Item findItemByName(String itemName, List<Item> itemList) {
		for (Item item : itemList) {
			if(item.getName().equals(itemName)) {
				return item;
			}
		}
		return null;
	}
	

	
	
	public String useItem(Item item, Item selected) {
		String message = "Nothing happened";
		if(item.getName().equals("Empty Potion Bottle")) {
			message = db.useEmptyPotion(item, selected, model.getUser());
		}
		return message;
	}
	
	
	
}