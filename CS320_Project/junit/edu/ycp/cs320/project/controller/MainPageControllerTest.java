package edu.ycp.cs320.project.controller;


import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;



import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;

public class MainPageControllerTest {
	private MainPageController controller;
	
	@Before
	public void setUp() {
		
		controller = new MainPageController();
		DatabaseProvider.setInstance(new FakeDatabase());
		IDatabase db = DatabaseProvider.getInstance();
	}
	
	@Test
	public void testPopulateModel() {
			String username = "Screamer";
			controller.PopulateModel(username);
			assertTrue(controller.getModel() != null);
			assertTrue(controller.getModel().getUser().getInventory().get(0).getName().equals("Full Potion Bottle"));
			
		}
	
	@Test
	public void testTransferItemFromRoomToUser() {
			String username = "Screamer";
			controller.PopulateModel(username);
			controller.transferItemFromRoomToUser("Lit Candle");
			assertTrue(controller.getModel().getRoom().getItems().size() == 0);
			assertTrue(controller.getModel().getUser().getInventory().size() == 2);
			
		}
	
	@Test
	public void testMoveUserLeft() {
		controller.PopulateModel("Screamer");
		controller.getModel().getRoom().setUserPosition(3);
		controller.moveUserLeft();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 3);
		controller.getModel().getRoom().setUserPosition(0);
		controller.moveUserLeft();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 2);
		controller.getModel().getRoom().setUserPosition(1);
		controller.moveUserLeft();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 0);
		}
	
	@Test
	public void testMoveUserRight() {
		controller.PopulateModel("Screamer");
		controller.getModel().getRoom().setUserPosition(3);
		controller.moveUserRight();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 3);
		controller.getModel().getRoom().setUserPosition(1);
		controller.moveUserRight();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 2);
		controller.getModel().getRoom().setUserPosition(2);
		controller.moveUserRight();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 0);
		}
	
	@Test
	public void testMoveUserUp() {
		controller.PopulateModel("Screamer");
		controller.getModel().getRoom().setUserPosition(3);
		controller.moveUserUp();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 3);
		controller.getModel().getRoom().setUserPosition(1);
		controller.moveUserUp();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 3);
		controller.getModel().getRoom().setUserPosition(2);
		controller.moveUserUp();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 3);
		}
	
	@Test
	public void testMoveUserDown() {
		controller.PopulateModel("Screamer");
		controller.getModel().getRoom().setUserPosition(3);
		controller.moveUserDown();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 0);
		controller.getModel().getRoom().setUserPosition(1);
		controller.moveUserDown();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 1);
		controller.getModel().getRoom().setUserPosition(2);
		controller.moveUserDown();
		assertTrue(controller.getModel().getRoom().getUserPosition() == 2);
		}
	
}
