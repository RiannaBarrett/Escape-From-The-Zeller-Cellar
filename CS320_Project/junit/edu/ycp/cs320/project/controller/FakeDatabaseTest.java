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
	public IDatabase db;
	public User user;

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
		System.out.println(user.getInventory().size());
	}

	@Test
	public void testAddUser() {
		user = new User("Devon", "Barrack");
		assertTrue(db.addUser(user));
		assertTrue(user.getUsername().equals("Devon"));
		assertTrue(user.getPassword().equals("Barrack"));
		assertTrue(user.getUserID() == 9);
		assertTrue(user.getInventory().size() == 1);
		assertTrue(user.getRoom().getRoomID() == 9);

	}
	
	@Test
	public void testTransferItemFromUserToRoom() {
		assertTrue(db.addItemToInventory(new Item("pen"), user.getUserID()));
		Item userItem = db.findItemByNameAndIDInInv("pen", user.getUserID());
		System.out.println(userItem.getName());
		assertTrue(db.transferItemFromUserToRoom(user, userItem));
		
		boolean itemMoved = false;
		boolean itemRemoved = true;
		
		List<Item> userInv = db.findItemsInInventory(user.getUserID());
		List<Item> roomInv = db.findRoomInventoryByID(user.getRoom().getRoomID());
		
		for(Item item: userInv) {
			if(item.getName().equals(userItem.getName())){
			itemRemoved = false;	
			}
		}

		for(Item item: roomInv) {
			//System.out.println(item.getName() + "|");
			if(item.getName().equals(userItem.getName())){
				itemMoved = true;	
			}
		}
		System.out.println(itemRemoved);
		System.out.println(itemMoved);
		assertTrue(itemMoved);
		assertTrue(itemRemoved);
		
	}

	
	@Test
	public void testTransferItemFromRoomToUser() {
		Item roomItem = user.getRoom().getItems().get(3);
		assertTrue(db.transferItemFromRoomToUser(user, roomItem));
		boolean itemMoved = false;
		boolean itemRemoved = false;
	
		List<Item> userInv = db.findItemsInInventory(user.getUserID());
		List<Item> roomInv = db.findRoomInventoryByID(user.getRoom().getRoomID());

		for(Item item: userInv) {
			if(item.getName().equals(roomItem.getName())){
				itemMoved = true;	
			}
		}
		
		for(Item item: roomInv) {
			if(!item.getName().equals(roomItem.getName())){
				itemRemoved = true;	
			}
		}
		
		assertTrue(itemRemoved);
		assertTrue(itemMoved);
	}

	

	@Test
	public void testMoveUser() {
		assertFalse(user.getRoom().getUserPosition() == 3);
		assertTrue(db.moveUser(user, 3));
		// TODO: Needs a function here to re-get position from db, it doesn't automatically update.
		assertTrue(db.findUserByName(user.getUsername()).getRoom().getUserPosition() == 3);
			
		
	}

	@Test
	public void testAddItemToRoom() {
		Item item = new Item("Matches", true, 240, 235, 0);
		assertTrue(db.addItemToRoom(item, user.getRoom().getRoomID()));
		Boolean isInInventory = false;
		db.addItemToRoom(item, user.getRoom().getRoomID());
		List<Item> inv = db.findRoomInventoryByID(user.getRoom().getRoomID());
		for(int i = 0; i < inv.size(); i++) {
			if(inv.get(i).getName().equals(item.getName())) {
				isInInventory = true;
			}
		}
		assertTrue(isInInventory);
	}

	@Test
	public void testAddItemToInventory() {
		Boolean isInInventory = false;
		Item item = new Item();
		int userID = user.getUserID();
		db.addItemToInventory(item, userID);
		List<Item> inv = db.findItemsInInventory(userID);
		for(int i = 0; i < inv.size(); i++) {
			if(inv.get(i).getName().equals(item.getName())) {
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
		user.getInventory().add(new Item("pencil"));
		Item itemToRemove = user.getInventory().get(1);
		System.out.println("Item to remove from Inv: "+itemToRemove.getName());
		Item itemToAdd = new Item("box");
		
		db.swapItemInInventory(itemToRemove, itemToAdd, user);
		
		List<Item> userInv = db.findItemsInInventory(user.getUserID());
		
		for(Item item: userInv) {
			if(item.getName().equals(itemToAdd.getName())){
				itemAddSuccess = true;	
			}else if(!item.getName().equals(itemToRemove.getName())){
				itemRemoveSuccess = true;
			}

		}
		System.out.println(itemAddSuccess);
		System.out.println(itemRemoveSuccess);
		assertTrue(itemAddSuccess);
		assertTrue(itemRemoveSuccess);

	}

	@Test
	public void testSwapItemInRoom() {
		Boolean itemAddSuccess = false;
		Boolean itemRemoveSuccess = false;
		Item itemToRemove = user.getRoom().getItems().get(2);
		System.out.println("Item to remove : "+itemToRemove.getName());
		Item itemToAdd = new Item("box");
		db.swapItemInRoom(itemToRemove, itemToAdd, user);
		
		List<Item> roomInv = db.findRoomInventoryByID(user.getRoom().getRoomID());
		
		for(Item item: roomInv) {
			if(item.getName().equals(itemToAdd.getName())){
				itemAddSuccess = true;	
			}else if(!item.getName().equals(itemToRemove.getName())){
				itemRemoveSuccess = true;
			}

		}
		System.out.println(itemAddSuccess);
		System.out.println(itemRemoveSuccess);
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
		assertFalse(db.findItemByNameAndIDInRoom("Jar of Cat Hairs", user.getRoom().getRoomID()).getCanBePickedUp());
		db.changeCanBePickedUp(5, "Jar of Cat Hairs", true);
		assertTrue(db.findItemByNameAndIDInRoom("Jar of Cat Hairs", user.getRoom().getRoomID()).getCanBePickedUp());
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
	public void testFindRoomIDByUsername() {
		System.out.println("ID = " + db.findRoomIDByUsername("Screamer"));
		assertTrue(db.findRoomIDByUsername("Screamer") ==  5);
	}
	@Test
	public void testFindRoomIDByUserID() {
		assertTrue(db.findRoomIDByUserID(5) == 5);
	}
	
	@Test
	public void testFindItemsInInventory() {
		List<Item> items = db.findItemsInInventory(2);
		assertTrue(items.get(0).getName().equals("Matches"));
		assertTrue(items.get(1).getName().equals("Lit Candle"));
	}
	
	@Test
	public void testGetCanBePickedUp() {
		assertFalse(db.getCanBePickedUp(5, "Jar of Cat Hairs"));
	}
	
	@Test
	public void testGetUsedItemsByTaskID() {
		List<Item> items = db.getUsedItemsByTaskId(0);
		assertTrue(items.size() == 0);
	}
	
	@Test 
	public void testChangeObjectiveIsStarted() {
		Objective objective = db.getObjectiveByObjectiveID(1);
		assertTrue(objective.getIsStarted());
		db.changeObjectiveIsStarted(1, false);
		assertFalse(db.getObjectiveByObjectiveID(1).getIsStarted());
	}
	
	@Test 
	public void testChangeObjectiveIsComplete() {
		Objective objective = db.getObjectiveByObjectiveID(1);
		assertTrue(objective.getIsComplete());
		db.changeObjectiveIsComplete(1, false);
		assertFalse(db.getObjectiveByObjectiveID(1).getIsComplete());
	}
	
	@Test 
	public void testChangeTaskIsStarted() {
		Task task = db.getTaskByTaskID(1);
		assertTrue(task.getIsStarted());
		db.changeTaskIsStarted(1, false);
		assertFalse(db.getTaskByTaskID(1).getIsStarted());
	}
	
	@Test 
	public void testChangeTaskIsComplete() {
		Task task = db.getTaskByTaskID(1);
		assertTrue(task.getIsComplete());
		db.changeTaskIsComplete(1, false);
		assertFalse(db.getTaskByTaskID(1).getIsComplete());
	}
	
	@Test
	public void testGetTasksByObjID() {
		List<Task> result = db.getTasksByObjID(1);
		assertTrue(result.size() == 2);
		result = db.getTasksByObjID(2);
		assertTrue(result.size()==1);
	}
	
	@Test
	public void testGetObjectivesByRoomID() {
		assertTrue(db.getObjectivesByRoomID(5).size()==2);
	}
	
	@Test
	public void testGetTaskIDByNameAndObjectiveID() {
		assertTrue(db.getTaskIDByNameAndObjectiveID("Cat", 1) == 2);
	}
	
	@Test
	public void testAddItemToTask() {
		Item itemToAdd = new Item("testItem", false, 0, 0, 0);
		db.addItemToTask(itemToAdd, 1);
		List<Item> addedItem = db.getUsedItemsByTaskId(1);
		Boolean found = false;
		for(Item i : addedItem) {
			if(i.getName().equals("testItem")) {
				found = true;
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void testGetObjectiveByObjectiveID() {
		assertTrue(db.getObjectiveByObjectiveID(1).getIsStarted() == true);
		assertTrue(db.getObjectiveByObjectiveID(1).getIsComplete() == true);
	}
	
	@Test
	public void testGetTaskByTaskID() {
		assertTrue(db.getTaskByTaskID(1).getName().equals("PotionMachine"));
		assertTrue(db.getTaskByTaskID(1).getIsStarted() == true);
		assertTrue(db.getTaskByTaskID(1).getIsComplete() == true);
	}
	
	@Test
	public void testAddObjectiveToRoom() {
		Objective newObj = new Objective();
		int objs = db.getObjectivesByRoomID(user.getRoom().getRoomID()).size();
		db.addObjectiveToRoom(newObj, user.getRoom().getRoomID());
		assertTrue(db.getObjectivesByRoomID(user.getRoom().getRoomID()).size() == objs + 1);
	}
	
	@Test
	public void testAddTaskToObjective() {
		Task newTask = new Task();
		newTask.setName("testTask");
		int tasks = db.getTasksByObjID(1).size();
		db.addTaskToObjective(newTask, 1);
		assertTrue(db.getTasksByObjID(1).size() == tasks + 1);
	}
	
	@Test
	public void testFindRoomInventoryByID() {
		List<Item> items = db.findRoomInventoryByID(user.getUserID());
		assertTrue(items.get(0).getName().equals("Shelf"));
	}
	
	@Test
	public void testRemoveItemFromUsedItems() {
		// TODO: Add usedItems to CSV for testing.
	}
}
