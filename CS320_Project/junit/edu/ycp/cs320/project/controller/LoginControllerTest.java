package edu.ycp.cs320.project.controller;


import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;



import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;

public class LoginControllerTest {
	private LoginController controller;
	
	@Before
	public void setUp() {
		
		controller = new LoginController();
		DatabaseProvider.setInstance(new FakeDatabase());
		IDatabase db = DatabaseProvider.getInstance();
	}
	
	@Test
	public void testValidateLogin() {
		String username = "tester1";
		String password = "1234";
		assertTrue(controller.validateLogin(username, password) == true);
		username = "Screamer";
		password = "letsGoYCP";
		assertTrue(controller.validateLogin(username, password) == false);
		username = "";
		assertTrue(controller.validateLogin(username, password) == false);
		username = "|";
		assertTrue(controller.validateLogin(username, password) == false);
		}
	
}
