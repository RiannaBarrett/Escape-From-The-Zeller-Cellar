package edu.ycp.cs320.project.sqldemos;

import java.util.List;
import java.util.Scanner;

import edu.ycp.cs320.project.persist.DerbyDatabase;
import edu.ycp.cs320.project.controller.InitDatabase;
import edu.ycp.cs320.project.model.*;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class InsertUserQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyboard = new Scanner(System.in);

		// Create the default IDatabase instance
		InitDatabase.init(keyboard);
		
		System.out.print("Enter a username: ");
		String username = keyboard.nextLine();
		
		System.out.print("Enter a password: ");
		String password = keyboard.nextLine();
	
		User temp = new User();
		temp.setUsername(username);
		temp.setPassword(password);
		// get the DB instance and execute transaction
		DatabaseProvider.setInstance(new DerbyDatabase());
		IDatabase db = DatabaseProvider.getInstance();
		
		if(!db.addUser(temp)) {
			System.out.print("User added: "+ false);
		}
		else {
			System.out.print("User added: "+ true);
		}

	}
}
