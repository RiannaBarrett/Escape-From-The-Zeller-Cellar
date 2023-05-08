package edu.ycp.cs320.project.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ycp.cs320.project.model.*;
import edu.ycp.cs320.project.persist.DBUtil;
import edu.ycp.cs320.project.persist.DerbyDatabase.Transaction;

public class TestDatabase extends DerbyDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			System.out.println(e);
			throw new IllegalStateException("Could not load Derby driver");
		}
	}

	@Override
	public Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");

		// Set autocommit to false to allow execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
	}
	
	public Boolean resetTestDB() {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;
				PreparedStatement stmt7 = null;
				PreparedStatement stmt8 = null;
				ResultSet resultSet = null;

				try {
					stmt8 = conn.prepareStatement("drop table leaderboard ");
					stmt8.execute();
					stmt7 = conn.prepareStatement("drop table usedItems ");
					stmt7.execute();
					stmt6 = conn.prepareStatement("drop table tasks ");
					stmt6.execute();
					stmt5 = conn.prepareStatement("drop table objectives ");
					stmt5.execute();
					stmt4 = conn.prepareStatement("drop table roomInventories ");
					stmt4.execute();
					stmt3 = conn.prepareStatement("drop table userInventories ");
					stmt3.execute();
					stmt2 = conn.prepareStatement("drop table rooms ");
					stmt2.execute();
					stmt = conn.prepareStatement("drop table users ");
					stmt.execute();
					
					//createTables();
					//loadInitialData();
					System.out.println("Test Tables Reset.");
					return true;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt6);
					DBUtil.closeQuietly(stmt7);
					DBUtil.closeQuietly(stmt8);
				}
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		TestDatabase db = new TestDatabase();
		db.createTables();

		System.out.println("Loading initial data...");
		db.loadInitialData();

		System.out.println("Success!");
	}
}