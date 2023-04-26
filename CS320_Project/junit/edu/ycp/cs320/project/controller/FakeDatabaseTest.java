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
		assertTrue(db.addUser(user));
		User newUser = db.findUserByName("Devon");
		assertTrue(newUser.getUsername().equals("Devon"));
		assertTrue(newUser.getPassword().equals("Barrack"));
		assertTrue(newUser.getUserID() == 7);
		assertTrue(newUser.getInventory().size() == 0);
		assertTrue(newUser.getRoom().getRoomID() == 7);
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
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testFindUserIDByName() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testUseEmptyPotion() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testRemoveItemFromRoom() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testRemoveItemFromInventory() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testSwapItemInInventory() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testSwapItemInRoom() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
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
