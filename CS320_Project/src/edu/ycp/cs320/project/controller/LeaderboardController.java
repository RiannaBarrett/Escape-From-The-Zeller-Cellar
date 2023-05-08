package edu.ycp.cs320.project.controller;


import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.persist.IDatabase;
import edu.ycp.cs320.project.persist.FakeDatabase;
import edu.ycp.cs320.project.model.Pair;

import java.util.List;
import java.util.regex.Pattern;

import edu.ycp.cs320.project.model.Objective;
import edu.ycp.cs320.project.model.User;

public class LeaderboardController {
	private IDatabase db = null;
	
	public LeaderboardController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		this.db = DatabaseProvider.getInstance();
	}
	
	public List<Pair<String, Integer>> getLeaderboard() {
		return db.getLeaderboard();
	}
}
