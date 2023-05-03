package edu.ycp.cs320.project.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.project.model.Item;
import edu.ycp.cs320.project.model.User;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.TestDatabase;
import edu.ycp.cs320.project.persist.IDatabase;

public class DerbyDatabaseTest extends FakeDatabaseTest {
	@Before
	@Override
	public void setUp() {
		DatabaseProvider.setInstance(new TestDatabase());
		db = DatabaseProvider.getInstance();
		((TestDatabase) db).resetTestDB();
		String[] args = null;
		try {
			TestDatabase.main(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user = db.findUserByName("Screamer");
	}
}
