package edu.ycp.cs320.project.controller;


import static org.junit.Assert.assertTrue;

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
		assertTrue(controller.getModel().getUser().getPassword().equals("letsGoYCP!"));
		System.out.println(controller.getModel().getUser().getInventory().get(0).getName() + "|");
		assertTrue(controller.getModel().getUser().getInventory().get(0).getName().equals("Matches"));
	}
	
	@Test
	public void testTransferItemFromRoomToUser() throws Exception {
		String username = "Screamer";
		controller.PopulateModel(username);
		//int userInvSize = controller.getModel().getUser().getInventory().size();
		//int roomInvSize = controller.getModel().getUser().getRoom().getItems().size();
		controller.transferItemFromRoomToUser("Lit Candle");
		//assertTrue(controller.getModel().getUser().getRoom().getItems().size() == roomInvSize - 1);
		//assertTrue(controller.getModel().getUser().getInventory().size() == userInvSize + 1);
		boolean iAdded = false;
		boolean iRemoved = true;
		for(Item i : controller.getModel().getUser().getRoom().getItems()) {
			if(i.getName() == "Lit Candle") {
				iRemoved = false;
			}
		}
		for(Item i : controller.getModel().getUser().getInventory()) {
			if(i.getName() == "Lit Candle") {
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
		Item fullCauldron = new Item("Full Cauldron", false, 40, 20, 4);

		Item emptyPotionItem = new Item("Empty Potion Bottle", true, 40, 20, 4);
		
		// Using unlit candle ON matches, should do nothing.
		assertTrue(controller.useItem(unlitCandleItem, matchesItem) == "Nothing Happened");
		// Using matches on unlit candle, should return text and replace unlit with a lit candle.
		assertTrue(controller.useItem(matchesItem, unlitCandleItem) == "You lit the candle");
		boolean iAdded = false;
		boolean iRemoved = true;
		for(Item i : controller.getModel().getUser().getRoom().getItems()) {
			if(i.getName() == "Lit Candle") {
				iAdded = true;
			}
			if(i.getName() == "Unlit Candle") {
				iRemoved = false;
			}
		}
		assertTrue(iAdded && iRemoved);
		
		// Using Empty Potion Bottle ON matches, should do nothing.
		assertTrue(controller.useItem(emptyPotionItem, matchesItem) == "Nothing Happened");
		// Using Empty Potion Bottle ON a Full Cauldron, should replace the empty bottle with a full one.
		assertTrue(controller.useItem(emptyPotionItem, fullCauldron) == "You filled the bottle with a potion");
		iAdded = false;
		iRemoved = true;
		for(Item i : controller.getModel().getUser().getInventory()) {
			if(i.getName() == "Full Potion Bottle") {
				iAdded = true;
			}
			if(i.getName() == "Empty Potion Bottle") {
				iRemoved = false;
			}
		}
		assertTrue(iAdded && iRemoved);
	}
}
