package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;

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
	
	public void PopulateModel(String username) {
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
		User user = db.findUserByName(username);
		if(user != null) {
			model.setUser(user);
			model.setRoom(user.getRoom());
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
			if(itemToBeTransferred.getRoomPosition() == user.getRoom().getUserPosition()) {
				// Can the item be picked up?
				if(itemToBeTransferred.getCanBePickedUp() == true) {
					db.transferItemFromRoomToUser(user, itemToBeTransferred);
				}
			}
		}
		return false;
	}
	
	
	// Helper functions
	private Item findItemByName(String itemName, List<Item> itemList) {
		for (Item item : itemList) {
			if(item.getName() == itemName) {
				return item;
			}
		}
		return null;
	}
}

