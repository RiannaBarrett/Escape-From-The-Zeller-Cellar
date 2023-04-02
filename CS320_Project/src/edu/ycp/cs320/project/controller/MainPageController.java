package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.model.*;

public class MainPageController {

	private static MainPage model;
	
	public MainPageController() {
		model = new MainPage();
	}
	
	public MainPageController(MainPage model) {
		this.model = model;
	}
	
	public void PopulateModel(String username) {
		DatabaseProvider.setInstance(new FakeDatabase());
		IDatabase db = DatabaseProvider.getInstance();
		User user = db.findUserByName(username);
		if(user != null) {
			model.setUser(user);
			model.setRoom(user.getRoom());
		}
	}
}
