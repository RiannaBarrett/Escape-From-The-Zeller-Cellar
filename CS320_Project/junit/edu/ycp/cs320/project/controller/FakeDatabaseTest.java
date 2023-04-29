package edu.ycp.cs320.project.controller;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.model.*;

public class FakeDatabaseTest {
	private IDatabase db;
	private User user;

	@Before
	public void setUp() {
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
		user = db.findUserByName("Screamer");
	}

	@Test
	public void testFindUserByName() {
		User user = db.findUserByName("Screamer");
		assertTrue(user.getUsername().equals("Screamer"));
		assertTrue(user.getPassword().equals("letsGoYCP!"));
		assertTrue(user.getInventory().size() == 1);
		System.out.println("fdsafdsa"+user.getInventory().size());
	}

	@Test
	public void testAddUser() {
		user = db.findUserByName("Devon");
		user = new User("Devon", "Barrack");
		assertTrue(db.addUser(user));
		assertTrue(user.getUsername().equals("Devon"));
		assertTrue(user.getPassword().equals("Barrack"));
		assertTrue(user.getUserID() == 7);
		assertTrue(user.getInventory().size() == 0);
		assertTrue(user.getRoom().getRoomID() == 7);
	}

	
	@Test
	public void testTransferItemFromRoomToUser() {
		Item item = user.getRoom().getItems().get(3);
		assertTrue(db.transferItemFromRoomToUser(user, item));
		boolean itemMoved = false;
		boolean itemRemoved = true;
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(user.getInventory().get(i).getName().equals(item.getName())) {
				itemMoved = true;
			}
		}
		for(int i = 0; i < user.getRoom().getItems().size(); i++) {
			if(user.getRoom().getItems().get(i).getName().equals(item.getName())) {
				itemRemoved = false;
			}
		}
		assertTrue(itemRemoved);
		assertTrue(itemMoved);
	}

	@Test
	public void testTransferItemFromUserToRoom() {
		Item item = user.getInventory().get(0);
		assertTrue(db.transferItemFromUserToRoom(user, item.getName()));
		boolean itemMoved = false;
		boolean itemRemoved = true;
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(user.getInventory().get(i).getName().equals(item.getName())) {
				itemRemoved = false;
			}
		}
		for(int i = 0; i < user.getRoom().getItems().size(); i++) {
			if(user.getRoom().getItems().get(i).getName().equals(item.getName())) {
				itemMoved = true;
			}
		}
		assertTrue(itemRemoved);
		assertTrue(itemMoved);
	}

	@Test
	public void testMoveUser() {
		assertFalse(user.getRoom().getUserPosition() == 3);
		assertTrue(db.moveUser(user, 3));
		assertTrue(user.getRoom().getUserPosition() == 3);
	}

	@Test
	public void testAddItemToRoom() {
		Item item = new Item("Matches", true, 240, 235, 0);
		assertTrue(db.addItemToRoom(item, user.getRoom().getRoomID()));
	}

	@Test
	public void testAddItemToInventory() {
		Boolean isInInventory = false;
		Item item = new Item();
		int userID = user.getUserID();
		db.addItemToInventory(item, userID);
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(user.getInventory().get(i).getName().equals(item.getName())) {
				isInInventory = true;
			}
		}
		assertTrue(isInInventory);


	}

	@Test
	public void testFindUserIDByName() {
		User user = db.findUserByName("Screamer");
		assertTrue(user.getUserID() == (5));
	}

	@Test
	public void testUseEmptyPotion() {
		Item item = new Item("Empty Potion Bottle");
		user.getInventory().add(item);
		Item seletedItem = new Item("Cauldron with Potion");
		String message = db.useEmptyPotion(item, seletedItem, user);
		Boolean isTrue = false;
		if(message == "You filled the bottle with a potion") {
			isTrue = true;
		}
		assertTrue(isTrue);
	}

	@Test
	public void testRemoveItemFromRoom() {
		int roomID = user.getRoom().getRoomID();
		Boolean didRemove = false;
		Item item = user.getRoom().getItems().get(0);
		db.removeItemFromRoom(item, roomID);
		for(int i = 0; i < user.getRoom().getItems().size(); i++) {
			if(!user.getRoom().getItems().get(i).getName().equals(item.getName())) {
				didRemove = true;
			}
		}
		assertTrue(didRemove);
	}

	@Test
	public void testRemoveItemFromInventory() {
		int userID = user.getUserID();
		Boolean didRemove = false;
		Item item = user.getInventory().get(0);
		System.out.println(item.getName());
		assertTrue(db.removeItemFromInventory(item, userID) == true);
	}


	@Test
	public void testSwapItemInInventory() {
		Boolean itemAddSuccess = false;
		Boolean itemRemoveSuccess = false;
		Item itemToRemove = user.getInventory().get(0);
		System.out.println(itemToRemove.getName());
		Item itemToAdd = new Item("box");
		db.swapItemInInventory(itemToRemove, itemToAdd, user);
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(user.getInventory().get(i).getName().equals(itemToAdd.getName())) {
			
				itemAddSuccess = true;
			}
		}
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(!user.getInventory().get(i).getName().equals(itemToRemove.getName())) {
				itemRemoveSuccess = true;
			}
		}
		assertTrue(itemAddSuccess);
		assertTrue(itemRemoveSuccess);

	}

	@Test
	public void testSwapItemInRoom() {
		Boolean itemAddSuccess = false;
		Boolean itemRemoveSuccess = false;
		Item itemToRemove = user.getRoom().getItems().get(0);
		Item itemToAdd = new Item("box");
		db.swapItemInRoom(itemToRemove, itemToAdd, user);
		for(int i = 0; i < user.getRoom().getItems().size(); i++) {
			if(user.getRoom().getItems().get(i).getName().equals(itemToAdd.getName())) {
				itemAddSuccess = true;
			}
		}
		for(int i = 0; i < user.getRoom().getItems().size(); i++) {
			if(!user.getRoom().getItems().get(i).getName().equals(itemToRemove)) {
				itemRemoveSuccess = true;
			}
		}
		assertTrue(itemAddSuccess);
		assertTrue(itemRemoveSuccess);

	}

	@Test
	public void testUseMatches() {
		Item matches = new Item("Matches");
		Item selected = new Item("Unlit Candle");
		Boolean isTrue = false;
		assertTrue(db.useMatches(matches, selected, user) == "You lit the candle");
		
	}

	@Test
	public void testChangeCanBePickedUp() {
		assertTrue(db.changeCanBePickedUp(5, "Jar of Cat Hairs", true) == true);
	}

	@Test
	public void testFindItemsInPositionByID() {
		List<Item> items = new ArrayList<Item>();
		items = db.findItemsInPositionByID(1, 0);
		for(Item item : items) {
			assertTrue(item.getRoomPosition() == 0);
		}
		
		items = db.findItemsInPositionByID(5, 0);
		for(Item item : items) {
			assertTrue(item.getRoomPosition() == 0);
		}
	}

	@Test
	public void testFindItemByNameAndIDInRoom() {
		Item item = db.findItemByNameAndIDInRoom("Shelf", 5);
		assertTrue(item.getName().equals("Shelf"));
	}

	@Test
	public void testFindItemByNameAndIDInInv() {
		Item item = db.findItemByNameAndIDInInv("Matches", 5);
		assertTrue(item.getName().equals("Matches"));
	}
	
	
	@Test
	public void testFindRoomIDByName() {
		assertTrue(db.findRoomIDByUsername("Screamer") ==  5);
		System.out.println("ID = " + db.findRoomIDByUsername("Screamer"));
	}
	@Test
	public void testFindRoomIDByUserID() {
		assertTrue(db.findRoomIDByUserID(5) == 5);
	}
	
	@Test
	public void findItemsInInventory() {
		List<Item> items = db.findItemsInInventory(2);
		assertTrue(items.get(0).getName().equals("Matches"));
		assertTrue(items.get(1).getName().equals("Lit Candle"));
	}
}
