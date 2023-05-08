package edu.ycp.cs320.project.controller;


import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;

import java.util.regex.Pattern;

import edu.ycp.cs320.project.model.Objective;
import edu.ycp.cs320.project.model.User;

public class WinGameController {
	private IDatabase db = null;
	
	public WinGameController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		this.db = DatabaseProvider.getInstance();
	}
	
	public void resetUser(String username) {
		User user = db.findUserByName(username);
		for(Objective obj : user.getRoom().getObjectives()) {
			obj.setTasks(db.getTasksByObjID(obj.getObjectiveID()));
		}
		db.resetUser(user);
	}
}
