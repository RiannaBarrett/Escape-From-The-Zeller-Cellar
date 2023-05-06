package edu.ycp.cs320.project.controller;

import java.util.regex.Pattern;

import edu.ycp.cs320.project.model.Item;
import edu.ycp.cs320.project.model.User;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;

public class SignupController {
	private IDatabase db = null;

	public SignupController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		this.db = DatabaseProvider.getInstance();
	}

	public boolean validateSignup(String username, String password) {
		// check if the user name and password meet your criteria for validity
		if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
			// return false if the user name or password is null or empty
			return false;
		} else {
			// create a new User object
			User newUser = new User(username, password);
			IDatabase db = DatabaseProvider.getInstance();

			boolean success = false;
			
			// return false if user name or password contains symbols
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9 !@#$%&*()_+.]+$");
			if(!pattern.matcher(username).matches() || !pattern.matcher(password).matches()) {
				return false;
			}
			
			
			User existingUser = db.findUserByName(username);

			if(existingUser == null) {
				success = db.addUser(newUser);

			}
			if(success) {
				User user = db.findUserByName(username);
				System.out.println(user.getUsername() +" & " + user.getPassword());
			}

			// return true if the sign-up was successful
			return success;
		}
	}


}