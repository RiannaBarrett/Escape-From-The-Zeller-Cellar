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
	}

	@Test
	public void testAddUser() {
		db.findUserByName("Devon");
		User user = new User();
		user.setUsername("Devon");
		user.setPassword("Barrack");
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
		throw new UnsupportedOperationException();
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
		System.out.println(item.getName() +" fdsafds");
		db.removeItemFromInventory(item, userID);
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(user.getInventory().get(i).equals(item)){
				didRemove = true;
			}
			else {
				didRemove = false;
			}
		}
		assertTrue(didRemove);
	}


	@Test
	public void testSwapItemInInventory() {
		Boolean itemAddSuccess = false;
		Boolean itemRemoveSuccess = false;
		Item itemToRemove = user.getInventory().get(0);
		Item itemToAdd = new Item("box");
		db.swapItemInRoom(itemToRemove, itemToAdd, user);
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(user.getRoom().getItems().get(i).getName().equals(itemToAdd.getName())) {
				itemAddSuccess = true;
			}
		}
		for(int i = 0; i < user.getInventory().size(); i++) {
			if(!user.getRoom().getItems().get(i).getName().equals(itemToRemove)) {
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
		throw new UnsupportedOperationException();
	}

	@Test
	public void testChangeCanBePickedUp() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testUsePotionIngredient() {
		Boolean use1 = false;
		Boolean use2 = false; 
		Item selectedItem = null;
		Item useItem1 = new Item("Jar of Cat Hairs");
		Item useItem2 = new Item("Jar with Hibiscus");
		user.getInventory().add(useItem1);
		user.getInventory().add(useItem2);
		for(int i = 0; i < user.getRoom().getItems().size(); i++) {
			if(user.getRoom().getItems().get(i).getName().equals("Empty Cauldron")){
				selectedItem = user.getRoom().getItems().get(i);
				System.out.println(selectedItem.getName());
			}
		}
		String text = db.usePotionIngredient(useItem1, selectedItem, user);
		if(text == "You put the item in the cauldron") {
			use1 = true;
		}
		String text1 = db.usePotionIngredient(useItem2, selectedItem, user);
		if(text1 == "You created a potion") {
			use2 = true;
		}
		assertTrue(use1);
		assertTrue(use2);
	}

	@Test
	public void testFindItemsInPositionByID() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testFindItemByNameAndIDInRoom() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testFindItemByNameAndIDInInv() {
		throw new UnsupportedOperationException();
	}
}
