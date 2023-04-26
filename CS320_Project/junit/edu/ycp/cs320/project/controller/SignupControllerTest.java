package edu.ycp.cs320.project.controller;


import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;

public class SignupControllerTest {
	private SignupController controller;
	
	@Before
	public void setUp() {
		
		controller = new SignupController();
		DatabaseProvider.setInstance(new FakeDatabase());
		IDatabase db = DatabaseProvider.getInstance();
	}
	
	@Test
	public void testValidateSignup() {
		String username = null;
		String password = "1234";
		assertTrue(controller.validateSignup(username, password)== false);
		username = "";
		assertTrue(controller.validateSignup(username, password)== false);
		username = "Username";
		password = "";
		assertTrue(controller.validateSignup(username, password)== false);
		username = "tester1";
		password = "1234";
		assertTrue(controller.validateSignup(username, password)== false);
		username = "'";
		password = "1234";
		assertTrue(controller.validateSignup(username, password)== false);
		username = "user";
		password = "1234";
		assertTrue(controller.validateSignup(username, password));
		}
	
}
