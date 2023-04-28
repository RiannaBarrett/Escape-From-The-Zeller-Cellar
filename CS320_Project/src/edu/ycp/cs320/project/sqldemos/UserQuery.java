package edu.ycp.cs320.project.sqldemos;

import java.util.List;
import java.util.Scanner;

import edu.ycp.cs320.project.controller.InitDatabase;
import edu.ycp.cs320.project.model.*;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class UserQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyboard = new Scanner(System.in);

		// Create the default IDatabase instance
		InitDatabase.init(keyboard);
		
		System.out.print("Enter a username: ");
		String username = keyboard.nextLine();
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		User user = db.findUserByName(username);
		
		int id = user.getUserID();
		System.out.println("Room ID by username: " + db.findRoomIDByUsername(username));
		System.out.println(id);
		// check if anything was returned and output the list
		if (user == null) {
			System.out.println("No users found with name: <" + username + ">");
		}
		else {
			System.out.println("User found!");
			System.out.println("ID: \t \t \t" + user.getUserID());
			System.out.println("Username: \t \t" + user.getUsername());
			System.out.println("Password: \t \t" + user.getPassword());
			System.out.println("Inventory: ");
			for (Item item : user.getInventory()) {
				System.out.println("\t Item ID: \t \t" + item.getItemID());
				System.out.println("\t Item Name: \t \t" + item.getName());
				System.out.println("\t Can be picked up?: \t" + item.getCanBePickedUp());
				System.out.println("\t X Position: \t \t" + item.getXPosition());
				System.out.println("\t Y Position: \t \t" + item.getYPosition());
				System.out.println("\t Room Position: \t \t" + item.getRoomPosition());
				System.out.println("");
			}
			
			
			System.out.println("Room ID: \t \t" + user.getRoom().getRoomID());
			System.out.println("User Position: \t \t" + user.getRoom().getUserPosition());
			System.out.println("Room Inventory: ");
			for (Item item : user.getRoom().getItems()) {
				System.out.println("\t Item ID: \t \t" + item.getItemID());
				System.out.println("\t Item Name: \t \t" + item.getName());
				System.out.println("\t Can be picked up?: \t" + item.getCanBePickedUp());
				System.out.println("\t X Position: \t \t" + item.getXPosition());
				System.out.println("\t Y Position: \t \t" + item.getYPosition());
				System.out.println("\t Room Position: \t \t" + item.getRoomPosition());
				System.out.println("");
			}
		}
		
		int roomID = user.getRoom().getRoomID();
		System.out.println("Get items in position 0 test");
		List<Item> items = db.findItemsInPositionByID(roomID, 0);
		
		for (Item item : items) {
			System.out.println("\t Item ID: \t \t" + item.getItemID());
			System.out.println("\t Item Name: \t \t" + item.getName());
			System.out.println("\t Can be picked up?: \t" + item.getCanBePickedUp());
			System.out.println("\t X Position: \t \t" + item.getXPosition());
			System.out.println("\t Y Position: \t \t" + item.getYPosition());
			System.out.println("\t Room Position: \t \t" + item.getRoomPosition());
			System.out.println("");
		}
	}
}
