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

public class TestDatabase extends DerbyDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			System.out.println(e);
			throw new IllegalStateException("Could not load Derby driver");
		}
	}
	private static final int MAX_ATTEMPTS = 10;

	
	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");

		// Set autocommit to false to allow execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
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