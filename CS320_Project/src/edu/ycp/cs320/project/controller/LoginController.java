package edu.ycp.cs320.project.controller;


import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;

import java.util.regex.Pattern;

import edu.ycp.cs320.project.model.User;

public class LoginController {
	private IDatabase db = null;
	
	public LoginController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		this.db = DatabaseProvider.getInstance();
	}
	
	public Boolean validateLogin(String username, String password) {
		// return false if user name or password contains symbols
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
		if(!pattern.matcher(username).matches() || !pattern.matcher(password).matches()) {
			return false;
		}else {
			IDatabase db = DatabaseProvider.getInstance();
			User dbUser = db.findUserByName(username);
			if(dbUser!=null && password.equals(dbUser.getPassword())) {
				return true;
			}else {
				return false;
			}
		}
		
		
	}
}
