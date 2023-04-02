package edu.ycp.cs320.project.controller;

public class SignupController {

	public boolean validateSignup(String username, String password) {
	    // check if the user name and password meet your criteria for validity
	    if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
	        // return false if the user name or password is null or empty
	        return false;
	    } else {
	        // otherwise, the user name and password are valid
	        return true;
	    }
	}

}