package edu.ycp.cs320.project.sqldemos;

import java.util.List;
import java.util.Scanner;

import edu.ycp.cs320.project.controller.InitDatabase;
import edu.ycp.cs320.project.model.*;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class FindItemQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyboard = new Scanner(System.in);

		// Create the default IDatabase instance
		InitDatabase.init(keyboard);
		
		System.out.print("Enter a username: ");
		String username = keyboard.nextLine();
		
		System.out.print("Enter an item name: ");
		String name = keyboard.nextLine();
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		User user = db.findUserByName(username);
		
		int roomID = user.getRoom().getRoomID();
		
		Item foundItem = db.findItemByNameAndIDInRoom(name, roomID);
		
		System.out.println("Found item name: " + foundItem.getName());

	}
}
