package edu.ycp.cs320.project.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.project.model.Item;
import edu.ycp.cs320.project.model.User;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;

public class DerbyDatabaseTest {
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
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testAddUser() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testTransferItemFromRoomToUser() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testTransferItemFromUserToRoom() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testMoveUser() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testAddItemToRoom() {
		throw new UnsupportedOperationException();
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
