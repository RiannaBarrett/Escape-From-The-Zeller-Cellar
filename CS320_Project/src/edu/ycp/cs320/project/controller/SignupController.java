package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.model.User;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;

public class SignupController {

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
	        if(username.contains("|") || username.contains(";") || username.contains("^") || 
					username.contains("\"") || username.contains("'") || password.contains("|") ||
					password.contains(";") || password.contains("^") || password.contains("\"") || 
					password.contains("'")) {
				return false;
			}
	        User existingUser = db.findUserByName(username);

	        if(existingUser == null) {
	        	success = db.addUser(newUser);
	        }
	        if(success) {
	        	System.out.println(newUser.getUsername() +" & " + newUser.getPassword());
	        }
	        
	        // return true if the sign-up was successful
	        return success;
	    }
	}


}