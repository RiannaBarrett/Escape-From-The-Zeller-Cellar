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

public class MainPageControllerTest {
	private MainPageController controller;
	
	@Before
	public void setUp() {
		controller = new MainPageController();
		DatabaseProvider.setInstance(new FakeDatabase());
		IDatabase db = DatabaseProvider.getInstance();
	}
	
	@Test
	public void testPopulateModel() throws Exception {
		String username = "Screamer";
		controller.PopulateModel(username);
		assertTrue(controller.getModel() != null);
		assertTrue(controller.getModel().getUser().getUsername().equals("Screamer"));
		assertTrue(controller.getModel().getUser().getPassword().equals("letsGoYCP"));
		assertTrue(controller.getModel().getUser().getInventory().get(0).getName().equals("Matches"));
	}
	
	@Test
	public void testTransferItemFromRoomToUser() throws Exception {
		String username = "Screamer";
		controller.PopulateModel(username);
		// Should be false, doesnt exist.
		assertFalse(controller.transferItemFromRoomToUser("Lit Candle"));
		// Should be false, user is not in the correct room.
		assertFalse(controller.transferItemFromRoomToUser("Unlit Candle"));
		controller.getModel().getUser().getRoom().setUserPosition(0);
		// Should be false, since the item cannot be picked up.
		assertFalse(controller.transferItemFromRoomToUser("Unlit Candle"));
		
		username = "York College";
		controller.PopulateModel(username);
		
		// Should be true now that user is in the correct room position, and an item that can be picked up is chosen.
		assertTrue(controller.transferItemFromRoomToUser("Matches"));
		// Test for item removal and add.
		boolean iAdded = false;
		boolean iRemoved = true;
		for(Item i : controller.getModel().getUser().getRoom().getItems()) {
			if(i.getName().equals("Matches")) {
				iRemoved = false;
			}
		}
		for(Item i : controller.getModel().getUser().getInventory()) {
			if(i.getName().equals("Matches")) {
				iAdded = true;
			}
		}
		assertTrue(iAdded && iRemoved);
	}
	
	@Test
	public void testMoveUserLeft() throws Exception {
		controller.PopulateModel("Screamer");
		controller.getModel().getUser().getRoom().setUserPosition(3);
		controller.moveUserLeft();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 3);
		controller.getModel().getUser().getRoom().setUserPosition(0);
		controller.moveUserLeft();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 2);
		controller.getModel().getUser().getRoom().setUserPosition(1);
		controller.moveUserLeft();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 0);
		}
	
	@Test
	public void testMoveUserRight() throws Exception {
		controller.PopulateModel("Screamer");
		controller.getModel().getUser().getRoom().setUserPosition(3);
		controller.moveUserRight();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 3);
		controller.getModel().getUser().getRoom().setUserPosition(1);
		controller.moveUserRight();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 2);
		controller.getModel().getUser().getRoom().setUserPosition(2);
		controller.moveUserRight();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 0);
		}
	
	@Test
	public void testMoveUserUp() throws Exception {
		controller.PopulateModel("Screamer");
		controller.getModel().getUser().getRoom().setUserPosition(3);
		controller.moveUserUp();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 3);
		controller.getModel().getUser().getRoom().setUserPosition(1);
		controller.moveUserUp();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 3);
		controller.getModel().getUser().getRoom().setUserPosition(2);
		controller.moveUserUp();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 3);
		}
	
	@Test
	public void testMoveUserDown() throws Exception {
		controller.PopulateModel("Screamer");
		controller.getModel().getUser().getRoom().setUserPosition(3);
		controller.moveUserDown();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 0);
		controller.getModel().getUser().getRoom().setUserPosition(1);
		controller.moveUserDown();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 1);
		controller.getModel().getUser().getRoom().setUserPosition(2);
		controller.moveUserDown();
		assertTrue(controller.getModel().getUser().getRoom().getUserPosition() == 2);
		}
	
	@Test
	public void testUseItem() throws Exception {
		controller.PopulateModel("Screamer");
		Item matchesItem = new Item("Matches", true, 40, 20, 4);
		Item unlitCandleItem = new Item("Unlit Candle", true, 40, 20, 4);
		Item emptyCauldron = new Item("Empty Cauldron", false, 40, 20, 4);
		Item fullCauldron = new Item("Cauldron with Potion", false, 40, 20, 4);

		Item emptyPotionItem = new Item("Empty Potion Bottle", true, 40, 20, 4);
		
		// Using unlit candle ON matches, should do nothing.
		assertTrue(controller.useItem(unlitCandleItem, matchesItem, 5).equals("Nothing Happened"));
		// Using matches on unlit candle, should return text and replace unlit with a lit candle.
		assertTrue(controller.useItem(matchesItem, unlitCandleItem, 5).equals("You lit the candle"));
		boolean iAdded = false;
		boolean iRemoved = true;
		for(Item i : controller.getModel().getUser().getRoom().getItems()) {
			if(i.getName().equals("Lit Candle")) {
				iAdded = true;
			}
			if(i.getName().equals("Unlit Candle")) {
				iRemoved = false;
			}
		}
		for(Item i : controller.getModel().getUser().getInventory()) {
			if(i.getName().equals("Matches")) {
				iRemoved = false;
			}
		}
		assertTrue(iAdded && iRemoved);
		
		// Using Empty Potion Bottle ON matches, should do nothing.
		assertTrue(controller.useItem(emptyPotionItem, matchesItem, 5).equals("Nothing Happened"));
		// Using Empty Potion Bottle ON a Full Cauldron, should replace the empty bottle with a full one.
		assertTrue(controller.useItem(emptyPotionItem, fullCauldron, 5).equals("You filled the bottle with a potion"));
		iAdded = false;
		iRemoved = true;
		for(Item i : controller.getModel().getUser().getInventory()) {
			if(i.getName().equals("Full Potion Bottle")) {
				iAdded = true;
			}
			if(i.getName().equals("Empty Potion Bottle")) {
				iRemoved = false;
			}
		}
		assertTrue(iAdded && iRemoved);
	}
	
	@Test
	public void testFindItemByName() {
		Item itemA = new Item("ItemA", false, 2, 2, 2);
		Item itemB = new Item("ItemB", true, 2, 2, 2);
		Item itemC = new Item("ItemC", false, 2, 42, 2);
		Item itemD = new Item("ItemD", false, 2, 2, 2);
		List<Item> iList = new ArrayList<Item>();
		iList.add(itemA);
		iList.add(itemB);
		iList.add(itemC);
		iList.add(itemD);
		
		assertTrue(controller.findItemByName(itemA.getName(), iList).equals(itemA));
		assertTrue(controller.findItemByName(itemC.getName(), iList).equals(itemC));
		assertFalse(controller.findItemByName(itemB.getName(), iList).equals(itemC));
	}
	
	@Test
	public void testFindItemsInPosition() {
		String username = "Screamer";
		controller.PopulateModel(username);
		
		List<Item> items = controller.findItemsInPosition(0, username);
		for(Item item : items) {
			assertTrue(item.getRoomPosition() == 0);
		}
	}
	
	@Test
	public void testFindInventoryByName() {
		String username = "tester1";
		controller.PopulateModel(username);
		
		List<Item> items = controller.findInventoryByName(username);
		for(Item item : items) {
			assertTrue(item.getRoomPosition() == 0);
		}
	}
	
	@Test
	public void testUsePotionIngredient() {
		String username = "potionTester";
		controller.PopulateModel(username);
		
		assertTrue(controller.usePotionIngredient("Wishbone", "Empty Cauldron", controller.getUserIDByName(username)).equals("You put the item in the cauldron"));
		assertTrue(controller.usePotionIngredient("Jar of Cat Hairs", "Empty Cauldron", controller.getUserIDByName(username)).equals("You put the item in the cauldron"));
		assertTrue(controller.usePotionIngredient("Clover", "Empty Cauldron", controller.getUserIDByName(username)).equals("You put the item in the cauldron"));
		//should tell the user it didnt work since the ingredients were not in the right order 
		assertTrue(controller.usePotionIngredient("Carton of Lime Juice", "Empty Cauldron", controller.getUserIDByName(username)).equals("The ingredients added did not seem to do anything"));
		

		assertTrue(controller.usePotionIngredient("Jar of Cat Hairs", "Empty Cauldron", controller.getUserIDByName(username)).equals("You put the item in the cauldron"));
		assertTrue(controller.usePotionIngredient("Clover", "Empty Cauldron", controller.getUserIDByName(username)).equals("You put the item in the cauldron"));
		assertTrue(controller.usePotionIngredient("Wishbone", "Empty Cauldron", controller.getUserIDByName(username)).equals("You put the item in the cauldron"));
		//successful
		assertTrue(controller.usePotionIngredient("Carton of Lime Juice", "Empty Cauldron", controller.getUserIDByName(username)).equals("You created a potion"));
		
		/*
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
			*/
	}
	
	
	
	@Test
	public void testGetSelectedMessage() {
		String username = "potionTester2";
		controller.PopulateModel(username);
		assertTrue(controller.getSelectedMessage("Jar with Hibiscus", controller.getModel().getUser().getUserID()).equals("You found a Jar with Hibiscus. It seems to be stuck to the shelf"));
		assertTrue(controller.getSelectedMessage("Jar of Cat Hairs",  controller.getModel().getUser().getUserID()).equals("You found a Jar of Cat Hairs"));
	}
	
	
	//TODO: add tests for useFullPotionBottle and useMeowMix

	@Test
	public void testGetCurrentObjective() {

		List<Objective> objectives = new ArrayList<Objective>();
		objectives.add(new Objective(0,0,true, true, new ArrayList<Task>()));
		objectives.add(new Objective(1,1,true, true, new ArrayList<Task>()));
		objectives.add(new Objective(2,2,false, true, new ArrayList<Task>()));
		objectives.add(new Objective(3,3,false, false, new ArrayList<Task>()));
		objectives.add(new Objective(4,4,true, false, new ArrayList<Task>()));
		objectives.add(new Objective(5,5,false, false, new ArrayList<Task>()));
		assertTrue(controller.getCurrentObjective(objectives).getObjectiveID()==4);
	}
	
	@Test
	public void testStartNextObjective() {
		controller.PopulateModel("Screamer");
		List<Objective> objectives = controller.getModel().getUser().getRoom().getObjectives();
		controller.startNextObjective(objectives);
		Objective currentObj = controller.getCurrentObjective(objectives);
		assertTrue(currentObj.getObjectiveID() == 2);
		for(Task task : objectives.get(1).getTasks()) {
			assertTrue(task.getIsStarted()==true);
		}
		
;	}
	
}
