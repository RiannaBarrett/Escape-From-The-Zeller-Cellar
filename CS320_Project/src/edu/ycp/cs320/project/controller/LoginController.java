package edu.ycp.cs320.project.controller;


import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.model.User;

public class LoginController {

	public Boolean validateLogin(String username, String password) {
		if(username.contains("|") || username.contains(";") || username.contains("^") || 
				username.contains("\"") || username.contains("'") || password.contains("|") ||
				password.contains(";") || password.contains("^") || password.contains("\"") || 
				password.contains("'")) {
			return false;
		}else {
			DatabaseProvider.setInstance(new FakeDatabase());
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
